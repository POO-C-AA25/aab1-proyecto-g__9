package controlador;

import Vista.VistaConsola;
import modelo.*;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/*
 ControladorFeriaLoja gestiona la interaccion con el usuario para
 registrar clientes, procesar compras de boletos, generar facturas y reportes
 de la Feria de Loja, Utilizamos una vista de consola para entrada y salida de datos
 */

public class ControladorFeriaLoja {

    VistaConsola vista = new VistaConsola();
    ArrayList<Cliente> listaCompradores = new ArrayList<>();
    double valorE = 8;
    double valorN = 3.5;

    public void menu() {
        int opcion;
        do {
            System.out.println("\nMenu boleteria FERIA DE LOJA 30 de Agosto - 8 De Septiembre De 2024:");
            System.out.println("[1] Registrar comprador");
            System.out.println("[2] Comprar boletos");
            System.out.println("[3] Ver total y descuento");
            System.out.println("[4] Asistencia por evento");
            System.out.println("[5] Ver ganancias del dia");
            System.out.println("[6] Salir");
            opcion = vista.leerEntero("Seleccione una opción: ");

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

                case 5: {
                    double ganancias = calcularGananciasGeneradas();
                    System.out.printf("Las ganancias totales del día son: $%.2f\n", ganancias);
                    //lo hacemos para imprimir directamente con el metodo
                    break;
                }
                case 6:
                    mostrarReporteFinal();
                    break;
                default:
                    System.out.println("Esa opcion no existe.");
            }
        } while (opcion != 6);
    }

    public void registroCliente() {
        String nombre = vista.leerTexto("Nombre del comprador: ");
        String cedula = vista.leerTexto("Número de cédula: ");
        listaCompradores.add(new Cliente(nombre, cedula)); //agregamos los datos ingresados a la lista
        System.out.println("Cliente registrado correctamente.");
        //ocupamos vista.leertexto para asi poder respetar el mvc y no ocupar los sout
    }

    public void comprarBoletos() {
        if (listaCompradores.isEmpty()) {
            System.out.println("Debe registrar un cliente antes de comprar boletos.");
            return; //manejo de excepciones
        }

        int cantN = 0;
        int cantE = 0;
        String evento = "";

        for (String even : EventosEspeciales.listaEventosE) {
            System.out.println("Lista de eventos especiales: " + even);
            //recorremos todos los eventos que tenemos quemados
        }

        int opcion = vista.leerEntero("Seleccione 1 para entrada normal o 2 para especial: ");

        if (opcion == 1) {
            evento = "Entrada normal";
            cantN = vista.leerEntero("Cantidad de entradas normales: ");
        } else if (opcion == 2) {
            evento = vista.leerTexto("Nombre del evento: ");

            if (!validacionEventos(evento)) {
                System.out.println("Nombre de evento inválido. Asegúrese de escribirlo correctamente.");
                return; //manejo de excepciones
            }

            cantN = vista.leerEntero("Cantidad de entradas normales: ");
            cantE = vista.leerEntero("Cantidad de entradas especiales (conciertos): ");
            //leemos los datos mediante vista con el metodo leerEntero

        } else {
            System.out.println("Opción inválida.");
            return;
        }

        Entrada entradas = new Entrada(evento, cantN, cantE, valorE, valorN);
        // Se crea una entrada con los datos del evento seleccionado y cantidades ingresadas
        Cliente ultimo = listaCompradores.get(listaCompradores.size() - 1);
        //rescatamos el ultimo cliente para poder crear la factura
        Factura factura = new Factura(ultimo.getNombre(), entradas);
        /*creamos factura que es un arreglo el cual recibe
        el nombre y entradas del ultimo cliente
         */
        ultimo.setFactura(factura); //le seteamos factura a ultimo cliente

        System.out.println("Boletos comprados.");
    }

    public void mostrarFactura() {
        if (listaCompradores.isEmpty()) {
            System.out.println("No hay clientes registrados.");
            return;
        }

        Cliente ultimo = listaCompradores.get(listaCompradores.size() - 1);
        // Obtiene la factura asociada a ese cliente por eso utilizamos el get
        Factura fac = ultimo.getFactura();

        System.out.printf("""
                FACTURA BOLETERIA FERIA DE LOJA
                Cliente: %s
                Cédula: %s
                Evento: %s
                Entradas normales compradas: %d
                Entradas especiales compradas: %d
                Total sin descuento: %.2f
                Descuento aplicado del: %.2f porciento
                Total a pagar: $%.2f
                """,
                ultimo.getNombre(),
                ultimo.getCedula(),
                fac.getEntrada().getNombreEvento(),
                fac.getEntrada().getNumEntradasN(),
                fac.getEntrada().getNumEntradasE(),
                fac.calcularTotal(),
                (fac.calcularDescuento() > 0 ? 15.0 : 0.0),
                //aplicamos15% de descuento en caso de cumplir caso contrario ninguno
                fac.calcularTotalConDescuento());
    }

    public void calcularAsistenciaPorEvento() {
        int[] asistenciaTotal = new int[EventosEspeciales.listaEventosE.length];
        //creado para contar la asistencia por eventocon el  tamaño de listae...
        
        for (Cliente cliente : listaCompradores) {
             // Recorremos todos los compradores registrados

            if (cliente.getFactura() != null) {
                //manejo de excepciones 
                String nombreEvento = cliente.getFactura().getEntrada().getNombreEvento();
                // Obtenemos el nombre del evento de la entrada comprada

                int totalEntradas = cliente.getFactura().getEntrada().getNumEntradasN()
                        + cliente.getFactura().getEntrada().getNumEntradasE();
                //calculo del total de entradas
                for (int i = 0; i < EventosEspeciales.listaEventosE.length; i++) {
                    if (EventosEspeciales.listaEventosE[i].equalsIgnoreCase(nombreEvento)) {
                        //manejo de excepciones mediante la recepcion de la lectura del 
                        //nombre del evento ignorando las mayus y minus
                        asistenciaTotal[i] += totalEntradas;//llenamos arreglo asistenciaTotal para reporte
                        break;
                    }
                }
            }
        }

        System.out.println("\nASISTENCIA POR EVENTOS EN LA FERIA DE LOJA:");
        for (int i = 0; i < EventosEspeciales.listaEventosE.length; i++) {
            System.out.printf(" %s: %d asistentes\n", EventosEspeciales.listaEventosE[i], asistenciaTotal[i]);
            //presentamos asistencia del evento
        }
    }

    public boolean validacionEventos(String nombreEvento) {
        for (String evento : EventosEspeciales.listaEventosE) {
            //recorremos los string quemados
            if (evento.equalsIgnoreCase(nombreEvento)) {
    // Compara el nombre ingresado con cada evento, ignorando mayúsculas y minúsculas
                return true;
                //si existe mandamos verdadero caso contrio f

            }
        }
        return false;
    }   //buscamos el evento ingresado por el usuario y
    //si lo encontramos en la lista devolvemos un true caso contrario el false

    public double calcularGananciasGeneradas() {
        double totalGanancias = 0;
        for (Cliente cliente : listaCompradores) {
            if (cliente.getFactura() != null) {
                totalGanancias += cliente.getFactura().calcularTotalConDescuento();
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
                /*creamos f de factura igual referenciada hacia la clase factura
                //para poder imprimiry crear el txt mediante las 
                variables, utilizadas en el string format de igual manera
                 */

                totalNormales += f.getEntrada().getNumEntradasN();
                totalEspeciales += f.getEntrada().getNumEntradasE();
                totalPersonas += f.calcularAfluencia();
                totalConDescuento += f.calcularTotalConDescuento();
            }
        }

        String reporteFinal = String.format("""
                REPORTE FINAL DEL DÍA
                Total de clientes: %d
                Entradas normales vendidas: %d
                Entradas especiales vendidas: %d
                Total de personas: %d
                Recaudación final: %.2f
                """, totalClientes, totalNormales, totalEspeciales, totalPersonas, totalConDescuento);

        System.out.println(reporteFinal);

        try {
            FileWriter archivo = new FileWriter("reporte_final.txt");
            PrintWriter escritor = new PrintWriter(archivo);
            escritor.println(reporteFinal);
            //enviamos a escritor variable declarada para el printwriter
            //utilizado para la creacion de nuestro archivo txt
            escritor.close();
            System.out.println("El reporte también ha sido guardado en 'reporte_final.txt'");
        } catch (IOException e) {
            System.out.println("Ocurrió un error al guardar el archivo: " + e.getMessage());
        }
    }
}
