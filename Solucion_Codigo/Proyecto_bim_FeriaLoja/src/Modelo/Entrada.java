package modelo;

public class Entrada {

    public String nombreEvento;
    public int numEntradasN;
    public int numEntradasE;
    public double valorEntradaE;
    public double valorEntradaN;

    public Entrada(String nombreEvento, int numEntradasN, int numEntradasE,
            double valorEntradaE, double valorEntradaN) {
        this.nombreEvento = nombreEvento;
        this.numEntradasN = numEntradasN;
        this.numEntradasE = numEntradasE;
        this.valorEntradaE = valorEntradaE;
        this.valorEntradaN = valorEntradaN;
    }

    public String getNombreEvento() {
        return nombreEvento;
    }

    public int getNumEntradasN() {
        return numEntradasN;
    }

    public int getNumEntradasE() {
        return numEntradasE;
    }

    public double getValorEntradaE() {
        return valorEntradaE;
    }

    public double getValorEntradaN() {
        return valorEntradaN;
    }
}
