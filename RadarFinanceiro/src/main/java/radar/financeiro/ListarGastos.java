package radar.financeiro;

import radar.financeiro.Model.Debito;
import radar.financeiro.Model.DebitoAcumulado;
import radar.financeiro.Model.Periodicidade;
import radar.financeiro.util.SmsReceiver;
import radar.financeiro.util.StringCryptor;
import radar.financeiro.util.SystemUiHider;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.ContentResolver;
import android.database.Cursor;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;



/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class ListarGastos
{


    static ArrayList<String> smsList = new ArrayList<String>();
    static ArrayList<Debito> debitos = new ArrayList<Debito>();
    static List<DebitoAcumulado> debitosAcumulados = new ArrayList<DebitoAcumulado>();
    ContentResolver contentResolver;

    public ListarGastos() {
        smsList.clear();
        debitos.clear();
    }


    public static ArrayList<String> RecuperarGastosAgrupadosPorPeriodo( Date dataInicio, Date dataFim )
    {
        Iterator<Debito> iter = debitos.iterator();
        Date ultimoDia = null;
        Double valorAcumulado = 0.0;
        smsList.clear();

        do
        {
            Debito debito = iter.next();

            if (debito.getData().after(dataInicio) && debito.getData().before(dataFim))
            {
                    valorAcumulado += debito.getValor();
            }
            else
            {
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
                String reportDate = dateFormat.format(ultimoDia);

                DecimalFormat df=new DecimalFormat("0.00");
                String reportValue = df.format(valorAcumulado);

                smsList.add( reportDate + " - " + reportValue);

                valorAcumulado = debito.getValor();
                ultimoDia = debito.getData();
            }

        }while(iter.hasNext());

        return smsList;
    }

    public static List<DebitoAcumulado> RecuperarGastosAgrupadosPorPeriodicade( Periodicidade periodicidade )
    {
        Iterator<Debito> iter = debitos.iterator();
        DebitoAcumulado debitoAcumulado = new DebitoAcumulado(0.0,periodicidade,null);
        smsList.clear();
        debitosAcumulados.clear();
        int dia = 0 , diaAtual = 0;

        while(iter.hasNext())
        {
            Debito debito = iter.next();
            debitoAcumulado.desbitosNoPerido.add(debito);

            if (debitoAcumulado.getData() == null)
                debitoAcumulado.setData(debito.getData());

            if (periodicidade == Periodicidade.Dia)
            {
                dia = debitoAcumulado.getData().getDay();
                diaAtual = debito.getData().getDay();
            }

            if (periodicidade == periodicidade.Semana)
            {
                Calendar cal = Calendar.getInstance();
                cal.setTime(debitoAcumulado.getData());
                dia = cal.get(Calendar.WEEK_OF_YEAR);

                cal.setTime(debito.getData());
                diaAtual = cal.get(Calendar.WEEK_OF_YEAR);
            }

            if (periodicidade == periodicidade.Mes)
            {
                dia = debitoAcumulado.getData().getMonth();
                diaAtual = debito.getData().getMonth();
            }

            if (dia == diaAtual)
            {
                debitoAcumulado.incValor(debito.getValor());
            }
            else
            {
                debitosAcumulados.add( debitoAcumulado);

                debitoAcumulado = new DebitoAcumulado(debito.getValor(),periodicidade,debito.getData());
            }
        }

        debitosAcumulados.add( debitoAcumulado);

        return  debitosAcumulados;
    }

    public void LerSms(Cursor cursor)
    {
        int indexBody = cursor.getColumnIndex( SmsReceiver.BODY );

        if ( indexBody < 0 || !cursor.moveToFirst() ) return;

        String data =  "";
        String valorString =  "";

        do
        {
            String msg =cursor.getString( indexBody );

            Debito debito = null;
            if (msg.contains("Transacao"))
            {
                data = msg.substring(msg.indexOf(" em ")+4,msg.indexOf(" as "));
                valorString = msg.substring(msg.indexOf("R$"),msg.indexOf("aprovada"));
                debito = new Debito(data, valorString);
                debito.setCodigo(msg.hashCode());
            }
            else
            {
                if (msg.contains("Saque"))
                {
                    data = msg.substring(msg.indexOf(" efetuado em ")+13,msg.indexOf(" as "));
                    valorString = msg.substring(msg.indexOf("R$"),msg.length());
                    debito = new Debito(data, valorString);
                    debito.setCodigo(msg.hashCode());
                }
            }

            if (debito!= null)
                debitos.add(debito);
         }
        while( cursor.moveToNext() );
    }
}
