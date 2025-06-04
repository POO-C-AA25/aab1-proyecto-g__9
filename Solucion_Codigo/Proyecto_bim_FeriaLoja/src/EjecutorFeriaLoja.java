
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class EjecutorFeriaLoja {
//controlador

    Scanner entrada = new Scanner(System.in);
    ArrayList<Cliente> listaCompradores = new ArrayList<>();
    //lo creamos al array mediante la clase Cliente para asi poder rescatar los datos necesarios

    double valorE = 8;
    double valorN = 3.5;
    //valores quemados para precio de las entradas

    public static void main(String[] args) {
        new EjecutorFeriaLoja().menu();
    }

    public void menu() {
        int opcion;
        do {
            System.out.println("\nMenu boleteria FERIA DE LOJA 30 de"
                    + " Agosto - 8 De Septiembre De 2024:");
            System.out.println("[1] Registrar comprador");
            System.out.println("[2] Comprar boletos");
            System.out.println("[3] Ver total y descuento");
            System.out.println("[4] Asistencia por evento");
            System.out.println("[5] Ver ganancias del dia");
            System.out.println("[6] Salir");
            opcion = entrada.nextInt();
            entrada.nextLine();

            switch (opcion) {
                case 1:
                    registroCliente();
                    break;
                case 2:
                    comprarBoletos();
                    break;
                case 3:
                    mostrarFactura();
                    break;
                case 4:
                    calcularAsistenciaPorEvento();
                    break;
                case 5:
                    calcularGananciasGeneradas();
                    break;
                case 6:
                    mostrarReporteFinal();
                    break;
                default:
                    System.out.println("Esa opcion no existe.");
            }

        } while (opcion != 6);
    }

    public void registroCliente() {
        System.out.print("Nombre del comprador: ");
        String nombre = entrada.nextLine();

        System.out.print("Número de cédula: ");
        String cedula = entrada.nextLine();

        listaCompradores.add(new Cliente(nombre, cedula));
        System.out.println("Cliente registrado correctamente.");
        /*
        le enviamos a nuestro array los datos ingresados por 
        teclado para asi poder ir almacenandolos ahi 
         */
    }

    public void comprarBoletos() {

        if (listaCompradores.isEmpty()) {
            System.out.println("Debe registrar un cliente antes de comprar boletos.");
            return;
        } //manejo de excepciones

        int opcion;
        int cantN = 0;
        int cantE = 0;
        String evento = "";

        //ocupamos el for each para poder presentar el nombre de los productos
        for (String even : EventosEspeciales.listaEventosE) {
            System.out.println("Lista de eventos especiales " + even);
        }
        System.out.println("Seleccione 1 si desea comprar una entrada normal\n "
                + "2 Si necesita una o más entradas especiales para eventos"
                + "a partir de de las 5pm a 2 am.");
        opcion = entrada.nextInt();
        entrada.nextLine();

        if (opcion == 1) {
            evento = "Entrada normal";
            System.out.print("Cantidad de entradas normales: ");
            cantN = entrada.nextInt();
            entrada.nextLine();
        } else if (opcion == 2) {
            System.out.print("Nombre del evento tal como se presento la lista: ");
            evento = entrada.nextLine();

            System.out.print("Cantidad de entradas normales: ");
            cantN = entrada.nextInt();

            System.out.print("Cantidad de entradas especiales (para conciertos): ");
            cantE = entrada.nextInt();
            entrada.nextLine();
        } else {
            System.out.println("Opción inválida.");
            return;
            //manejo de excepciones por si el cliente nos pone un num dif de 1 o 2, retorno eso  
        }

        Entrada entradas = new Entrada(evento, cantN, cantE, valorE, valorN);
        //esto siempre fuera si no una de las opciones no nos crearia nada 

        Cliente ultimo = listaCompradores.get(listaCompradores.size() - 1);
        Factura factura = new Factura(ultimo.nombre, entradas);
        ultimo.setFactura(factura);

        System.out.println("Boletos comprados.");
    }

    public void mostrarFactura() {

        if (listaCompradores.isEmpty()) {
            System.out.println("No hay clientes registrados.");
            return;
        } //manejo de excepciones

        Cliente ultimo = listaCompradores.get(listaCompradores.size() - 1);
        Factura fac = ultimo.getFactura();

        String factura = String.format(
                "FACTURA BOLETERIA FERIA DE LOJA\n"
                + "Cliente: %s\n"
                + "Cédula: %s\n"
                + "Evento: %s\n"
                + "Entradas normales compradas: %d\n"
                + "Entradas especiales compradas: %d\n"
                + "Total sin descuento: %.2f\n"
                + "Descuento aplicado del : %.2f porciento\n "
                + "Total a pagar: $%.2f\n",
                ultimo.nombre,
                ultimo.cedula,
                fac.entrada.nombreEvento,
                fac.entrada.numEntradasN,
                fac.entrada.numEntradasE,
                fac.calcularTotal(),
                fac.calcularDescuento(),
                fac.calcularTotalConDescuento());

        System.out.println(factura);
    }

    public void calcularAsistenciaPorEvento() {
        // Arreglo que lo usamos para asistentes por evento mediante listaEventosE
        int[] asistenciaTotal = new int[EventosEspeciales.listaEventosE.length];

        // Recorremos todos los compradores con el for mejorado
        for (Cliente cliente : listaCompradores) {
            if (cliente.getFactura() != null) { //si encuentra algo dentro seguimos
                String nombreEvento = cliente.getFactura().entrada.nombreEvento;
                int totalEntradas = cliente.getFactura().entrada.numEntradasN
                        + cliente.getFactura().entrada.numEntradasE;

                // Buscamos a qué evento corresponde
                for (int i = 0; i < EventosEspeciales.listaEventosE.length; i++) {
                    if (EventosEspeciales.listaEventosE[i].equals(nombreEvento)) {
                        asistenciaTotal[i] += totalEntradas;
                        break; // Utilizamos para salir del for si ya se encuentra el evento
                    }
                }
            }
        }

        System.out.println("\nASISTENCIA POR EVENTOS EN LA FERIA DE LOJA:");
        for (int i = 0; i < EventosEspeciales.listaEventosE.length; i++) {
            System.out.printf(" %s %d asistentes\n",
                    EventosEspeciales.listaEventosE[i], asistenciaTotal[i]);
        }
    }

    public boolean validacionEventos(String nombreEvento) {
        for (String evento : EventosEspeciales.listaEventosE) {
            /*
            for mejorado para recorrer listaEventos, es decir cada uno de los eventos
            que quemamos uno por uno y si el equals lo encuentra validar con true; caso contrario false. 
             */

            if (evento.equals(nombreEvento)) {
                //Compara el evento de la lista con el nombre del evento que ingresaron
                return true;
            }
        }
        return false;

        /*
        Lo utilizamos a este metodo para poder saber si el evento es de tipo especial
         */
    }

    public double calcularGananciasGeneradas() {
        double totalGanancias = 0;

        for (Cliente cliente : listaCompradores) { //recorremos otra vez lista compradores
            if (cliente.getFactura() != null) { //revisamos si es que ese cliente tiene factura
                totalGanancias += cliente.getFactura().calcularTotalConDescuento();
                //lamamos a totalconDesc por lo que tiene el valor "real"
            }
        }

        return totalGanancias;
    }

    public void mostrarReporteFinal() {
        int totalClientes = listaCompradores.size();
        int totalNormales = 0;
        int totalEspeciales = 0;
        int totalPersonas = 0;
        double totalConDescuento = 0;

        for (Cliente cliente : listaCompradores) {
            if (cliente.getFactura() != null) {
                Factura f = cliente.getFactura();
                totalNormales += f.entrada.numEntradasN;
                totalEspeciales += f.entrada.numEntradasE;
                totalPersonas += f.calcularAfluencia();
                totalConDescuento += f.calcularTotalConDescuento();
            }
        }

        String reporteFinal = String.format(
                "\nREPORTE FINAL DEL DÍA\n"
                + "Total de clientes: %d\n"
                + "Entradas normales vendidas: %d\n"
                + "Entradas especiales vendidas: %d\n"
                + "Total de personas: %d\n"
                + "Recaudación final: %.2f\n",
                totalClientes,
                totalNormales,
                totalEspeciales,
                totalPersonas,
                totalConDescuento
        );

        System.out.println(reporteFinal);

        // Guardar el mismo texto en un archivo .txt
        try {
            FileWriter archivo = new FileWriter("reporte_final.txt");
            PrintWriter escritor = new PrintWriter(archivo);
            escritor.println(reporteFinal); // escribe en el archivo
            escritor.close(); // siempre cerrar el archivo
            System.out.println("El reporte también ha sido guardado en 'reporte_final.txt'");
        } catch (IOException e) {
            System.out.println("Ocurrió un error al guardar el archivo: " + e.getMessage());
        }
    }

    //modelo
    class Cliente {

        String nombre;
        String cedula;
        Factura factura;

        public Cliente(String nombre, String cedula) {
            this.nombre = nombre;
            this.cedula = cedula;
        }

        public void setFactura(Factura factura) {
            this.factura = factura;
        }

        public Factura getFactura() {
            return factura;
        }
    }

    class Factura {

        public String nombreCliente;
        public Entrada entrada;

        public Factura(String nombreCliente, Entrada entrada) {
            this.nombreCliente = nombreCliente;
            this.entrada = entrada;
        }

        public double calcularMontoN() {
            return entrada.numEntradasN * entrada.valorEntradaN;
        }

        public double calcularMontoE() {
            return entrada.numEntradasE * entrada.valorEntradaE;
        }

        public double calcularTotal() {
            return calcularMontoN() + calcularMontoE();
        }

        public double calcularDescuento() {
            double total = calcularTotal();
            return total >= 25 ? total * 0.15 : 0;// si es > aplica el 15% de
            //descuento Si no el descuento es 0. lo mismo que un ternario
        }

        public double calcularTotalConDescuento() {
            return calcularTotal() - calcularDescuento();
        }

        public int calcularAfluencia() {
            return entrada.numEntradasN + entrada.numEntradasE;
        }
    }

    class Entrada {

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
    }

    class EventosEspeciales {

        public static String[] listaEventosE = {
            "Don Merardo y Sus Players", "Gustavo Cerati",
            "Binomio de Oro de America", "Tierra Canela", "Hombres G"};
    }
}
