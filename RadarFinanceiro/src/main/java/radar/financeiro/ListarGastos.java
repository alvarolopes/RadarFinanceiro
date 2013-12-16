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
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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


    ArrayList<String> smsList = new ArrayList<String>();
    ContentResolver contentResolver;

    public ListarGastos(ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
    }

    public ArrayList<String> RecuperarGastosAgrupadosPorPeriodicade( Periodicidade periodicidade )
    {
        Cursor cursor = contentResolver.query( Uri.parse("content://sms/inbox"), null,"ADDRESS == 27181 " , null, null);


        //smsList.add("teste");
        CarregarSms(cursor);

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


            smsList.add(debito.getData() + " - "+ debito.getValor().toString());
            //Salvar no banco de dados

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



/*if (!data.equals(ultimadata))
        {
        DecimalFormat df=new DecimalFormat("0.00");
        String formate = df.format(valorAcumulado);

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String reportDate = dateFormat.format(debito.getData());

        smsList.add( reportDate + " - " + formate);

        valorAcumulado = 0.0;
        ultimadata = data;
        }*/