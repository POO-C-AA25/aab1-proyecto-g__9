package Vista;

import java.util.Scanner;

public class VistaConsola {

    public Scanner sc = new Scanner(System.in);

    public int leerEntero(String mensaje) {
        System.out.print(mensaje);
        while (!sc.hasNextInt()) {
            System.out.println("Debe ingresar un número entero. Intente de nuevo.");
            sc.next(); // descarta  ingreso no valido para que no se cierre el programa directo
            System.out.print(mensaje);
        }
        int valor = sc.nextInt();
        sc.nextLine(); // Limpieza de buffer despues de leer el entero
        return valor;
    }

    public String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return sc.nextLine();
    }
}
