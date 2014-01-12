package radar.financeiro.Model;

import java.util.ArrayList;
import java.util.Date;

import radar.financeiro.Model.Debito;

/**
 * Created by Vroou on 08/12/13.
 */
public class DebitoAcumulado {

    public ArrayList<Debito> gastosNoDia;

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    private Double valor;

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    private Date data;
}
