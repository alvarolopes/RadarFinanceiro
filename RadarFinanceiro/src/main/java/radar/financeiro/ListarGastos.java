package radar.financeiro;

import radar.financeiro.util.SystemUiHider;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;



/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class ListarGastos extends Activity implements OnClickListener, OnItemClickListener {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * If set, will toggle the system UI visibility upon interaction. Otherwise,
     * will show the system UI visibility upon interaction.
     */
    private static final boolean TOGGLE_ON_CLICK = false;

    /**
     * The flags to pass to {@link SystemUiHider#getInstance}.
     */
    private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

    /**
     * The instance of the {@link SystemUiHider} for this activity.
     */
    private SystemUiHider mSystemUiHider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_listar_gastos);

        final View controlsView = findViewById(R.id.fullscreen_content_controls);
        final View contentView = findViewById(R.id.fullscreen_content_controls);

        // Set up an instance of SystemUiHider to control the system UI for
        // this activity.
        mSystemUiHider = SystemUiHider.getInstance(this, contentView, HIDER_FLAGS);
        mSystemUiHider.setup();
        mSystemUiHider
                .setOnVisibilityChangeListener(new SystemUiHider.OnVisibilityChangeListener() {
                    // Cached values.
                    int mControlsHeight;
                    int mShortAnimTime;

                    @Override
                    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
                    public void onVisibilityChange(boolean visible) {

                    }
                });

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        //findViewById(R.id.dummy_button).setOnTouchListener(mDelayHideTouchListener);
    }


    ArrayList<String> smsList = new ArrayList<String>();

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }


    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    Handler mHideHandler = new Handler();
    Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            mSystemUiHider.hide();
        }
    };

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }


    public void onItemClick( AdapterView<?> parent, View view, int pos, long id )
    {
        try
        {
            String[] splitted = smsList.get( pos ).split("\n");
            String sender = splitted[0];
            String encryptedData = "";
            for ( int i = 1; i < splitted.length; ++i )
            {
                encryptedData += splitted[i];
            }
            String data = sender + "\n" + StringCryptor.decrypt( new String(SmsReceiver.PASSWORD), encryptedData );
            Toast.makeText(this, data, Toast.LENGTH_SHORT).show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void onClick( View v )
    {
        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query( Uri.parse("content://sms/inbox"), null,"ADDRESS == 27181 " , null, null);

        int indexBody = cursor.getColumnIndex( SmsReceiver.BODY );
        //int indexAddr = cursor.getColumnIndex( SmsReceiver.ADDRESS );

        if ( indexBody < 0 || !cursor.moveToFirst() ) return;

        smsList.clear();
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

            //Salvar no banco de dados

         }
        while( cursor.moveToNext() );


        ListView smsListView = (ListView) findViewById( R.id.SMSList );
        smsListView.setAdapter( new ArrayAdapter<String>( this, android.R.layout.simple_list_item_1, smsList) );
        smsListView.setOnItemClickListener( this );
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