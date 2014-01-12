package radar.financeiro;

import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import radar.financeiro.Model.DebitoAcumulado;

public class CustomAdapter_debitosDetalhados extends ArrayAdapter<DebitoAcumulado>
{

    /*
     * Used to instantiate layout XML file into its corresponding View objects
     */
    private final LayoutInflater inflater;

    /*
     * each list item layout ID
     */
    private final int resourceId;

    public CustomAdapter_debitosDetalhados(Context context, int resource, List<DebitoAcumulado> objects) {
        super(context, resource, objects);
        this.inflater = LayoutInflater.from(context);
        this.resourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //get the person from position
        DebitoAcumulado debitoAcumulado = getItem(position);

        // get a new View no matter recycling or ViewHolder FIXME
        convertView = inflater.inflate(resourceId, parent, false);

        //get all object from view
        TextView data = (TextView) convertView.findViewById(R.id.txtdata);
        TextView valor = (TextView) convertView.findViewById(R.id.txtvalor);

        //fill the view objects according values from person object
        data.setText(debitoAcumulado.getDataView());
        valor.setText(debitoAcumulado.getValorView());

        return convertView;
    }
}