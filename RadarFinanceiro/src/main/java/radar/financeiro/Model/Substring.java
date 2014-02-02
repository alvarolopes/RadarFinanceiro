package radar.financeiro.Model;

/**
 * Created by Vroou on 20/01/14.
 */
public class Substring {
    public Substring(int inicio, int fim) {
        this.inicio = inicio;
        this.fim = fim;
    }

    public int getInicio() {
        return inicio;
    }

    public void setInicio(int inicio) {
        this.inicio = inicio;
    }

    private int inicio;

    public int getFim() {
        return fim;
    }

    public void setFim(int fim) {
        this.fim = fim;
    }

    private int fim;
}
