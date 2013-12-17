package radar.financeiro;

import radar.financeiro.Model.Debito;
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
    ContentResolver contentResolver;

    public ListarGastos() {

    }

    public static ArrayList<String> RecuperarGastosAgrupadosPorPeriodicade( Periodicidade periodicidade )
    {
        Iterator<Debito> iter = debitos.iterator();
        Date ultimoDia = null;
        Double valorAcumulado = 0.0;
        smsList.clear();

        do
        {
            Debito debito = iter.next();


            if (periodicidade == Periodicidade.Dia)
            {

                if (ultimoDia == null)
                    ultimoDia = debito.getData();

                if (ultimoDia.getDay() == debito.getData().getDay())
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
            }

            if (periodicidade == Periodicidade.Semana)
            {

                if (ultimoDia == null)
                    ultimoDia = debito.getData();

                Calendar cal = Calendar.getInstance();
                cal.setTime(ultimoDia);
                int ultimaSemana = cal.get(Calendar.WEEK_OF_YEAR);

                cal.setTime(debito.getData());
                int estaSemana = cal.get(Calendar.WEEK_OF_YEAR);

                if (ultimaSemana == estaSemana)
                {
                    valorAcumulado += debito.getValor();
                }
                else
                {
                    DateFormat dateFormat = new SimpleDateFormat("ww - MM/yy");
                    String reportDate = dateFormat.format(ultimoDia);

                    DecimalFormat df=new DecimalFormat("0.00");
                    String reportValue = df.format(valorAcumulado);

                    smsList.add( reportDate + " - " + reportValue);

                    valorAcumulado = debito.getValor();
                    ultimoDia = debito.getData();
                }
            }

            if (periodicidade == Periodicidade.Mes)
            {

                if (ultimoDia == null)
                    ultimoDia = debito.getData();

                if (ultimoDia.getMonth() == debito.getData().getMonth())
                {
                    valorAcumulado += debito.getValor();
                }
                else
                {
                    DateFormat dateFormat = new SimpleDateFormat("MM/yy");
                    String reportDate = dateFormat.format(ultimoDia);

                    DecimalFormat df=new DecimalFormat("0.00");
                    String reportValue = df.format(valorAcumulado);

                    smsList.add( reportDate + " - " + reportValue);

                    valorAcumulado = debito.getValor();
                    ultimoDia = debito.getData();
                }
            }

        }while(iter.hasNext());

        return  smsList;
    }

    public void CarregarSms(Cursor cursor)
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
                debito = RecuperarRegistroDeDebito(data, valorString);
                debito.setCodigo(msg.hashCode());
            }
            else
            {
                if (msg.contains("Saque"))
                {
                    data = msg.substring(msg.indexOf(" efetuado em ")+13,msg.indexOf(" as "));
                    valorString = msg.substring(msg.indexOf("R$"),msg.length());
                    debito = RecuperarRegistroDeDebito(data, valorString);
                    debito.setCodigo(msg.hashCode());
                }
            }

            if (debito!= null)
                debitos.add(debito);
         }
        while( cursor.moveToNext() );
    }

    private Debito RecuperarRegistroDeDebito(String data, String valor)
    {
        Debito debito = new Debito();
        SimpleDateFormat  format = new SimpleDateFormat("dd/MM/yy");
        try {
            Date date = format.parse(data);
            debito.setData(date);
        } catch (Exception e) {

        }
        debito.setValor(Double.parseDouble(valor.substring(3).trim().replace(",", ".")));

        return debito;
    }
}
