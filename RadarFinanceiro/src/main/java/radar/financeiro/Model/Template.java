package radar.financeiro.Model;

/**
 * Created by Vroou on 20/01/14.
 */
public class Template {
    public String getNumero() {
        return Numero;
    }

    public void setNumero(String numero) {
        Numero = numero;
    }

    public Substring getIdentificador() {
        return Identificador;
    }

    public void setIdentificador(Substring identificador) {
        Identificador = identificador;
    }

    public Substring getData() {
        return Data;
    }

    public void setData(Substring data) {
        Data = data;
    }

    public Substring getValor() {
        return Valor;
    }

    public void setValor(Substring valor) {
        Valor = valor;
    }

    public Substring getEstabelecimento() {
        return Estabelecimento;
    }

    public void setEstabelecimento(Substring estabelecimento) {
        Estabelecimento = estabelecimento;
    }

    public Substring getHora() {
        return Hora;
    }

    public void setHora(Substring hora) {
        Hora = hora;
    }

    private String Numero;
    private Substring Identificador;
    private Substring Data;
    private Substring Valor;
    private Substring Estabelecimento;
    private Substring Hora;

}
