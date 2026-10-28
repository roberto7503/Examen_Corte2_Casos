package com.example.examen2caso1.service;

import com.example.examen2caso1.model.ResultadoSimulacion;

public class SimuladorReservasImpl implements SimuladorReservas {

    // Datos compartidos por los tres agentes
    private int entradasDisponibles = ENTRADAS_INICIALES;
    private int reservasConfirmadas = 0;
    private int solicitudesRechazadas = 0;

    // Indica si actualmente hay una simulacion trabajando
    private boolean enEjecucion = false;

    private synchronized void prepararSimulacion() {

        // Evita iniciar dos simulaciones al mismo tiempo
        if (enEjecucion) {
            throw new IllegalStateException(
                    "Ya existe una simulacion en ejecucion"
            );
        }

        enEjecucion = true;

        // Restablecemos los datos antes de comenzar
        entradasDisponibles = ENTRADAS_INICIALES;
        reservasConfirmadas = 0;
        solicitudesRechazadas = 0;
    }

    private synchronized void intentarReserva() {

        // La comprobacion y el descuento ocurren juntos
        if (entradasDisponibles > 0) {
            entradasDisponibles--;
            reservasConfirmadas++;
        } else {
            solicitudesRechazadas++;
        }
    }

    private void procesarReservas() {

        // Cada agente realiza exactamente 10 intentos
        for (int i = 0; i < INTENTOS_POR_AGENTE; i++) {
            intentarReserva();
        }
    }

    private synchronized void finalizarSimulacion() {
        enEjecucion = false;
    }

    @Override
    public ResultadoSimulacion ejecutarSimulacion()
            throws InterruptedException {

        prepararSimulacion();

        try {
            // Cada Runnable representa el trabajo de un agente
            Runnable agente1 = () -> procesarReservas();
            Runnable agente2 = () -> procesarReservas();
            Runnable agente3 = () -> procesarReservas();

            Thread hilo1 = new Thread(agente1);
            Thread hilo2 = new Thread(agente2);
            Thread hilo3 = new Thread(agente3);

            // Iniciamos los tres agentes
            hilo1.start();
            hilo2.start();
            hilo3.start();

            // Esperamos que los tres terminen
            hilo1.join();
            hilo2.join();
            hilo3.join();

            return new ResultadoSimulacion(
                    entradasDisponibles,
                    reservasConfirmadas,
                    solicitudesRechazadas
            );
        } finally {

            // Siempre permitimos una nueva simulacion al terminar
            finalizarSimulacion();
        }
    }

    @Override
    public synchronized boolean estaEnEjecucion() {
        return enEjecucion;
    }
}