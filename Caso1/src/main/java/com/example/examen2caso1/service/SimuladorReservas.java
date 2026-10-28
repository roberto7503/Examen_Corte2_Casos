package com.example.examen2caso1.service;

import com.example.examen2caso1.model.ResultadoSimulacion;

public interface SimuladorReservas {
    int ENTRADAS_INICIALES = 20;
    int CANTIDAD_AGENTES = 3;
    int INTENTOS_POR_AGENTE = 10;

    ResultadoSimulacion ejecutarSimulacion() throws InterruptedException;

    boolean estaEnEjecucion();
}
