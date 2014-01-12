package radar.financeiro;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import radar.financeiro.Model.Debito;
import radar.financeiro.Model.DebitoAcumulado;
import radar.financeiro.Model.Periodicidade;


public class MainActivity extends Activity implements ActionBar.OnNavigationListener {


    /**
     * The serialization (saved instance state) Bundle key representing the
     * current dropdown position.
     */
    private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";
    private ContentResolver contentResolver;
    private ListarGastos listarGastos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // Set up the action bar to show a dropdown list.
        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);


        // Set up the dropdown list navigation in the action bar.
        actionBar.setListNavigationCallbacks(
                // Specify a SpinnerAdapter to populate the dropdown list.
                new ArrayAdapter<String>(
                        actionBar.getThemedContext(),
                        android.R.layout.simple_list_item_1,
                        android.R.id.text1,
                        new String[] {
                                getString(R.string.title_section1),
                                getString(R.string.title_section2),
                                getString(R.string.title_section3),
                                getString(R.string.title_section4),
                        }),
                this);

        contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query( Uri.parse("content://sms/inbox"), null,"ADDRESS == 27181 " , null, null);


        if (listarGastos == null)
        {
            listarGastos = new ListarGastos();
            listarGastos.LerSms(cursor);
        }

    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // Restore the previously serialized current dropdown position.
        if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
            getActionBar().setSelectedNavigationItem(
                    savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // Serialize the current dropdown position.
        outState.putInt(STATE_SELECTED_NAVIGATION_ITEM,
                getActionBar().getSelectedNavigationIndex());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(int position, long id) {
        // When the given dropdown item is selected, show its contents in the
        // container view.
        getFragmentManager().beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
        return true;
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            Periodicidade periodicidade = Periodicidade.values()[getArguments().getInt(ARG_SECTION_NUMBER)-1];

            if (Periodicidade.Periodo == periodicidade )
            {
                Date dataInicio = new Date(2013,9,1);
                Date dataFim = new Date(2013,10,15);

                ArrayList<String> smsList = ListarGastos.RecuperarGastosAgrupadosPorPeriodo(dataInicio, dataFim);

                ListView smsListView = (ListView) rootView.findViewById(R.id.SMSList);
                smsListView.setAdapter( new ArrayAdapter<String>(rootView.getContext(), android.R.layout.simple_list_item_1, smsList) );
            }
            else
            {
                List<DebitoAcumulado> debitos =  ListarGastos.RecuperarGastosAgrupadosPorPeriodicade(periodicidade);
                ArrayAdapter ad = new CustomAdapter_debitos(rootView.getContext(), R.layout.list_item, debitos);
                ListView smsListView = (ListView) rootView.findViewById(R.id.SMSList);
                smsListView.setAdapter(ad);
                smsListView.setOnItemClickListener(new DrawerItemClickListener());
            }

            return rootView;
        }

        private class DrawerItemClickListener implements ListView.OnItemClickListener {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {

                ListView listView = (ListView) view.getParent();
                ArrayAdapter ad = (ArrayAdapter)listView.getAdapter();
                Intent i=new Intent(view.getContext(), Debitos_Detalhes.class);
                i.putExtra("debitoAcumulado", (DebitoAcumulado) ad.getItem(position));
                startActivity(i);
            }
        }


    }

}
