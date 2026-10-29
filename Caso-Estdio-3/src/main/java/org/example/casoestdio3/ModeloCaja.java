package org.example.casoestdio3;

import java.util.Arrays;

public class ModeloCaja {
    public static final int VENDEDORES = 3;
    public static final int VENTAS_POR_VENDEDOR = 5;
    public static final int PRECIO = 100;

    public enum Estado { LISTO, EN_EJECUCION, CANCELANDO, CANCELADO, COMPLETADO, ERROR }

    private final int[] ventasPorVendedor = new int[VENDEDORES];
    private int totalVentas;
    private int recaudacion;
    private Estado estado = Estado.LISTO;
    private String error = "";

    // Se llama solamente despues de confirmar que terminaron los hilos anteriores.
    public synchronized void preparar() {
        Arrays.fill(ventasPorVendedor, 0);
        totalVentas = 0;
        recaudacion = 0;
        error = "";
        estado = Estado.EN_EJECUCION;
    }

    public synchronized boolean puedeContinuar() {
        return estado == Estado.EN_EJECUCION;
    }

    public synchronized boolean solicitarCancelacion() {
        if (estado != Estado.EN_EJECUCION
                || totalVentas == VENDEDORES * VENTAS_POR_VENDEDOR) {
            return false;
        }
        estado = Estado.CANCELANDO;
        return true;
    }

    // Confirma la venta completa o la descarta si se cancelo antes del registro.
    public synchronized String registrarVenta(int vendedor) {
        if (vendedor < 0 || vendedor >= VENDEDORES) {
            throw new IllegalArgumentException("Vendedor invalido.");
        }
        if (!puedeContinuar() || ventasPorVendedor[vendedor] == VENTAS_POR_VENDEDOR) {
            return null;
        }
        // Contador, dinero y avance se actualizan como una sola operacion.
        ventasPorVendedor[vendedor]++;
        totalVentas++;
        recaudacion += PRECIO;
        return String.format("Vendedor %d confirma venta %d/%d de C$%d | Total: %d | Caja: C$%d",
                vendedor + 1, ventasPorVendedor[vendedor], VENTAS_POR_VENDEDOR,
                PRECIO, totalVentas, recaudacion);
    }

    public synchronized void registrarError(String mensaje) {
        error = mensaje;
        estado = Estado.ERROR;
    }

    // El supervisor lo llama tras hacer join() a los tres vendedores.
    public synchronized void finalizar() {
        if (estado == Estado.ERROR) {
            return;
        }
        if (totalVentas == VENDEDORES * VENTAS_POR_VENDEDOR) {
            estado = Estado.COMPLETADO;
        } else if (estado == Estado.CANCELANDO) {
            estado = Estado.CANCELADO;
        } else {
            registrarError("Los vendedores terminaron sin completar todas las ventas.");
        }
    }

    public synchronized String getResumen() {
        StringBuilder texto = new StringBuilder("Estado: " + estado);
        for (int i = 0; i < VENDEDORES; i++) {
            texto.append(String.format("%nVendedor %d: %d/%d (%.0f%%)", i + 1,
                    ventasPorVendedor[i], VENTAS_POR_VENDEDOR,
                    ventasPorVendedor[i] * 100.0 / VENTAS_POR_VENDEDOR));
        }
        texto.append(String.format("%nTotal de ventas: %d/%d | Recaudacion: C$%d",
                totalVentas, VENDEDORES * VENTAS_POR_VENDEDOR, recaudacion));
        if (!error.isEmpty()) {
            texto.append(" | Error: ").append(error);
        }
        return texto.toString();
    }
}
