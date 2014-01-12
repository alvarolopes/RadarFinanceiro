package radar.financeiro.Model;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import radar.financeiro.Model.Debito;

/**
 * Created by Vroou on 08/12/13.
 */
public class DebitoAcumulado {

    public ArrayList<Debito> desbitosNoPerido;
    public Periodicidade periodicidade;
    private Double valor;
    private Date data;

    public DebitoAcumulado(double valorInicial, Periodicidade periodicidade, Date data) {
        valor = valorInicial;
        desbitosNoPerido = new ArrayList<Debito>();
        this.periodicidade = periodicidade;
        this.data = data;
    }


    public String getDataView() {

        DateFormat dateFormat;
        switch (periodicidade.ordinal()){
            case 0:
                dateFormat = new SimpleDateFormat("dd/MM/yy");
                break;
            case 1:
                dateFormat = new SimpleDateFormat("ww - MM/yy");
                break;
            case 2:
                dateFormat = new SimpleDateFormat("MMM / yyyy");
                break;
            case 3:
                dateFormat = new SimpleDateFormat("MM/yy");
                break;
            default:
                dateFormat = new SimpleDateFormat("dd/MM/yy");
        };

        String reportDate = dateFormat.format(data);

        return reportDate;
    }

    public String getValorView() {
        DecimalFormat df=new DecimalFormat("R$ 0.00");
        String reportValue = df.format(valor);

        return reportValue;
    }


    public Double getValor() {
        return valor;
    }

    public void incValor(Double valor) {
        this.valor += valor;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }
}
