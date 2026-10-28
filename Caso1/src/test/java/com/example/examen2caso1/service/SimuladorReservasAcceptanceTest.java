package com.example.examen2caso1.service;

import com.example.examen2caso1.model.ResultadoSimulacion;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.RepeatedTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Disabled("Activar cuando exista la implementacion de SimuladorReservas")
class SimuladorReservasAcceptanceTest {
    private SimuladorReservas simulador;

    @RepeatedTest(5)
    void cadaEjecucionCumpleLosResultadosEsperados() throws InterruptedException {
        ResultadoSimulacion resultado = simulador.ejecutarSimulacion();

        assertEquals(0, resultado.getEntradasDisponibles());
        assertEquals(20, resultado.getReservasConfirmadas());
        assertEquals(10, resultado.getSolicitudesRechazadas());
        assertTrue(resultado.getEntradasDisponibles() >= 0);
        assertEquals(
                20,
                resultado.getEntradasDisponibles() + resultado.getReservasConfirmadas()
        );
    }
}
