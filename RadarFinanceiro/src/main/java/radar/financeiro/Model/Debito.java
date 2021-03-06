package radar.financeiro.Model;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Vroou on 08/12/13.
 */
public class Debito implements Serializable {

    public Debito() {
    }

    ;

    public Debito(String data, String valor, String estabelecimento) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yy HH:mm");
        try {
            Date date = format.parse(data);
            this.data = date;
        } catch (Exception e) {

        }
        this.valor = Double.parseDouble(valor.substring(3).trim().replace(",", "."));
        this.estabelecimento = estabelecimento;
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
    private String estabelecimento;

    public String getEstabelecimento() {
        return estabelecimento;
    }

    public void setEstabelecimento(String estabelecimento) {
        this.estabelecimento = estabelecimento;
    }


    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public Date getData() {
        Date data2 = new Date();

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yy");
        try {
            Date date = format.parse(getDataView());
            data2 = date;
        } catch (Exception e) {
        }

        return data2;
    }

    public String getDataView() {

        DateFormat dateFormat;
        dateFormat = new SimpleDateFormat("dd/MM/yy");
        String reportDate = dateFormat.format(data);

        return reportDate;
    }

    public String getHoraView() {

        DateFormat dateFormat;
        dateFormat = new SimpleDateFormat("HH:mm");
        String reportDate = dateFormat.format(data);

        return reportDate;
    }

    public String getValorView() {
        DecimalFormat df = new DecimalFormat("R$ 0.00");
        String reportValue = df.format(valor);

        return reportValue;
    }


    public String getDateTime() {
        DateFormat dateFormat;
        dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String reportDate = dateFormat.format(data);

        return reportDate;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public void setData(String data) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yy HH:mm");
        try {
            Date date = format.parse(data);
            this.data = date;
        } catch (Exception e) {

        }
    }
}
