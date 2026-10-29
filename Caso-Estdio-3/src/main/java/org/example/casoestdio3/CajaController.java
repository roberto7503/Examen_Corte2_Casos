package org.example.casoestdio3;

import java.util.Objects;
import java.util.Scanner;

// Menu, simulacion y ciclo de vida de los hilos.
public class CajaController {
    private final ModeloCaja modelo;
    private Thread[] vendedores = new Thread[0];
    private Thread supervisor;
    private boolean cerrado;

    public CajaController(ModeloCaja modelo) {
        this.modelo = Objects.requireNonNull(modelo, "El modelo es obligatorio.");
    }

    public synchronized void iniciar() {
        if (cerrado) {
            System.out.println("El programa ya esta cerrado.");
            return;
        }
        if (supervisor != null) {
            if (supervisor.isAlive()) {
                System.out.println("Ya hay una simulacion activa. Espera su terminacion.");
                return;
            }
            esperarTerminacion(supervisor);
        }
        modelo.preparar();
        System.out.println("Nueva simulacion: tres vendedores, cinco ventas de C$100 cada uno.");
        vendedores = new Thread[ModeloCaja.VENDEDORES];
        for (int i = 0; i < vendedores.length; i++) {
            final int vendedor = i;
            vendedores[i] = new Thread(() -> procesarVentas(vendedor), "vendedor-" + (i + 1));
        }
        // El supervisor conserva los hilos de esta simulacion.
        final Thread[] grupo = vendedores;
        supervisor = new Thread(() -> {
            for (Thread hilo : grupo) {
                esperarTerminacion(hilo);
            }
            modelo.finalizar();
            System.out.println("\nTodos los vendedores terminaron.\n" + modelo.getResumen());
        }, "supervisor-caja");
        for (Thread hilo : vendedores) {
            hilo.start();
        }
        supervisor.start();
    }

    private void procesarVentas(int vendedor) {
        try {
            for (int venta = 1; venta <= ModeloCaja.VENTAS_POR_VENDEDOR; venta++) {
                if (!modelo.puedeContinuar() || Thread.currentThread().isInterrupted()) {
                    return;
                }
                System.out.printf("Vendedor %d inicia venta %d.%n", vendedor + 1, venta);
                // Simulacion de una venta; interrupt() despierta esta espera.
                Thread.sleep(700 + vendedor * 150L);
                String registro = modelo.registrarVenta(vendedor);
                if (registro == null) {
                    return;
                }
                System.out.println(registro);
                if (venta < ModeloCaja.VENTAS_POR_VENDEDOR && modelo.puedeContinuar()) {
                    Thread.sleep(200);
                }
            }
        } catch (InterruptedException ex) {
            // La venta interrumpida no se suma ni agrega dinero.
            modelo.solicitarCancelacion();
            Thread.currentThread().interrupt();
        } catch (RuntimeException ex) {
            modelo.registrarError("Vendedor " + (vendedor + 1) + ": " + ex);
        } finally {
            System.out.printf("Vendedor %d termino.%n", vendedor + 1);
        }
    }

    public synchronized void cancelar() {
        if (supervisor == null || !supervisor.isAlive()) {
            System.out.println("No hay una simulacion activa.");
            return;
        }
        if (modelo.solicitarCancelacion()) {
            for (Thread hilo : vendedores) {
                hilo.interrupt();
            }
            System.out.println("Cancelacion solicitada a todos los vendedores.");
        } else {
            System.out.println("La simulacion ya esta finalizando.");
        }
    }

    public void mostrarEstado() {
        System.out.println(modelo.getResumen());
    }

    public synchronized void cerrar() {
        if (cerrado) {
            return;
        }
        cerrado = true;
        if (supervisor != null) {
            if (supervisor.isAlive()) {
                modelo.solicitarCancelacion();
                for (Thread hilo : vendedores) {
                    hilo.interrupt();
                }
            }
            // El supervisor no necesita este bloqueo: puede terminar sin interbloqueos.
            esperarTerminacion(supervisor);
        }
        System.out.println("Programa cerrado. No quedan vendedores activos.");
    }

    private static void esperarTerminacion(Thread hilo) {
        boolean interrumpido = false;
        while (true) {
            try {
                hilo.join();
                break;
            } catch (InterruptedException ex) {
                interrumpido = true;
            }
        }
        if (interrumpido) {
            Thread.currentThread().interrupt();
        }
    }

    public void ejecutarMenu() {
        try (Scanner entrada = new Scanner(System.in)) {
            boolean salir = false;
            while (!salir) {
                System.out.println("\n1. Iniciar o reiniciar\n2. Cancelar\n3. Consultar estado\n4. Salir");
                System.out.print("Escribe una opcion y pulsa Enter: ");
                if (!entrada.hasNextLine()) {
                    break;
                }
                switch (entrada.nextLine().trim()) {
                    case "1": iniciar(); break;
                    case "2": cancelar(); break;
                    case "3": mostrarEstado(); break;
                    case "4": salir = true; break;
                    default: System.out.println("Opcion invalida.");
                }
            }
        } finally {
            cerrar();
        }
    }

    public static void main(String[] args) {
        new CajaController(new ModeloCaja()).ejecutarMenu();
    }
}
