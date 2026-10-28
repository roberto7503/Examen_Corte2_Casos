package com.example.examen2caso1.service;

import com.example.examen2caso1.model.ResultadoSimulacion;

//iniciamos con la creacion de la clase


public class SimuladorReservasImpl implements SimuladorReservas {

    // Datos compartidos por los tres agentes
    private int entradasDisponibles = ENTRADAS_INICIALES;
    private int reservasConfirmadas = 0;
    private int solicitudesRechazadas = 0;

    // Indica si actualmente hay una simulacion trabajando
    private boolean enEjecucion = false;

    private synchronized void intentarReserva() {

        // La comprobacion y el descuento ocurren juntos (mas practico, para mi)
        if (entradasDisponibles > 0) {
            entradasDisponibles--;
            reservasConfirmadas++;
        } else {
            solicitudesRechazadas++;
        }
    }

    @Override
    public ResultadoSimulacion ejecutarSimulacion() throws InterruptedException {

        // Los agentes se agregaran en el siguiente paso
        return new ResultadoSimulacion(
                entradasDisponibles,
                reservasConfirmadas,
                solicitudesRechazadas
        );
    }

    @Override
    public boolean estaEnEjecucion() {
        return enEjecucion;
    }
}