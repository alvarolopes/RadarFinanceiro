package radar.financeiro;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import radar.financeiro.Model.Debito;
import radar.financeiro.Model.DebitoAcumulado;

public class Debitos_Detalhes extends Activity {

    private DebitoAcumulado debitoAcumulado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debitos__detalhes);

        Intent intent = getIntent();
        if (null != intent) {

            debitoAcumulado = (DebitoAcumulado) intent.getSerializableExtra("debitoAcumulado");

        }

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment(debitoAcumulado))
                    .commit();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.

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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        private static DebitoAcumulado debitoAcumulado;
        private static ArrayAdapter ad;
        private static List<Debito> debitos;

        public PlaceholderFragment(DebitoAcumulado _debitoAcumulado) {
            debitoAcumulado = _debitoAcumulado;
        }

        public static void carregarDebitos()
        {
            if (debitos != null)
                debitos.clear();

            debitos = debitoAcumulado.desbitosNoPerido;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_debitos__detalhes, container, false);

            TextView data = (TextView) rootView.findViewById(R.id.txtdatadetalhada);
            TextView valor = (TextView) rootView.findViewById(R.id.txtvalortotal);

            data.setText(debitoAcumulado.getDataView());
            valor.setText(debitoAcumulado.getValorView());

            carregarDebitos();
            ad = new CustomAdapter_debitosDetalhados(rootView.getContext(), R.layout.list_item_detalhe, debitos);
            ListView smsListView = (ListView) rootView.findViewById(R.id.listDebitos);

            smsListView.setAdapter(ad);

            return rootView;
        }
    }

}
