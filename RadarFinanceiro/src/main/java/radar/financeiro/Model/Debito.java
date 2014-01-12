package radar.financeiro.Model;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import radar.financeiro.R;

/**
 * Created by Vroou on 08/12/13.
 */
public class Debito implements Serializable
{

    public Debito(){};

    public Debito(String data, String valor) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yy");
        try {
            Date date = format.parse(data);
            this.data = date;
        } catch (Exception e) {

        }
        this.valor = Double.parseDouble(valor.substring(3).trim().replace(",", "."));
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    private int codigo;
    private Date data;
    private Double valor;

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public Date getData() {
        return data;
    }

    public String getDataView() {

        DateFormat dateFormat;
        dateFormat = new SimpleDateFormat("dd/MM/yy");
        String reportDate = dateFormat.format(data);

        return reportDate;
    }

    public String getValorView() {
        DecimalFormat df=new DecimalFormat("R$ 0.00");
        String reportValue = df.format(valor);

        return reportValue;
    }

    public void setData(Date data) {
        this.data = data;
    }
}
