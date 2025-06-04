package modelo;

public class Factura {

    public String nombreCliente;
    public Entrada entrada;

    public Factura(String nombreCliente, Entrada entrada) {
        this.nombreCliente = nombreCliente;
        this.entrada = entrada;
    }

    public Entrada getEntrada() {
        return entrada;
    }

    public double calcularMontoN() {
        return entrada.getNumEntradasN() * entrada.getValorEntradaN();
    }

    public double calcularMontoE() {
        return entrada.getNumEntradasE() * entrada.getValorEntradaE();
    }

    public double calcularTotal() {
        return calcularMontoN() + calcularMontoE();
    }

    public double calcularDescuento() {
        double total = calcularTotal();
        return total >= 25 ? total * 0.15 : 0;
    }

    public double calcularTotalConDescuento() {
        return calcularTotal() - calcularDescuento();
    }

    public int calcularAfluencia() {
        return entrada.getNumEntradasN() + entrada.getNumEntradasE();
    }
}
