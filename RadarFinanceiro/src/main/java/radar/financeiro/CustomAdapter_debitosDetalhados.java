package radar.financeiro;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import radar.financeiro.Model.Debito;

public class CustomAdapter_debitosDetalhados extends ArrayAdapter<Debito> {

    /*
     * Used to instantiate layout XML file into its corresponding View objects
     */
    private final LayoutInflater inflater;

    /*
     * each list item layout ID
     */
    private final int resourceId;

    public CustomAdapter_debitosDetalhados(Context context, int resource, List<Debito> objects) {
        super(context, resource, objects);
        this.inflater = LayoutInflater.from(context);
        this.resourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //get the person from position
        Debito debito = getItem(position);

        // get a new View no matter recycling or ViewHolder FIXME
        convertView = inflater.inflate(resourceId, parent, false);

        //get all object from view
        TextView data = (TextView) convertView.findViewById(R.id.txtdata);
        TextView valor = (TextView) convertView.findViewById(R.id.txtvalor);
        TextView estabelecimento = (TextView) convertView.findViewById(R.id.txtestabelecimento);

        //fill the view objects according values from person object
        data.setText(debito.getHoraView());
        valor.setText(debito.getValorView());
        estabelecimento.setText(debito.getEstabelecimento());

        return convertView;
    }
}