package com.example.examen2caso1.service;

import com.example.examen2caso1.model.ResultadoSimulacion;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SimuladorReservasAcceptanceTest {

    @Test
    public void ejecutaCincoSimulacionesCorrectamente()
            throws InterruptedException {

        SimuladorReservas simulador = new SimuladorReservasImpl();

        // Ejecutamos cinco simulaciones usando el mismo objeto
        for (int i = 1; i <= 5; i++) {

            ResultadoSimulacion resultado =
                    simulador.ejecutarSimulacion();

            assertEquals(0, resultado.getEntradasDisponibles());
            assertEquals(20, resultado.getReservasConfirmadas());
            assertEquals(10, resultado.getSolicitudesRechazadas());

            // El inventario nunca puede ser negativo
            assertTrue(resultado.getEntradasDisponibles() >= 0);

            // Disponibles mas confirmadas siempre debe ser 20
            assertEquals(
                    20,
                    resultado.getEntradasDisponibles()
                            + resultado.getReservasConfirmadas()
            );

            // Al finalizar debe permitir una nueva simulacion
            assertFalse(simulador.estaEnEjecucion());
        }
    }
}