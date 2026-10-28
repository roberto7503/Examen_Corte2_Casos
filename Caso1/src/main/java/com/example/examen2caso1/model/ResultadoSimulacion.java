package com.example.examen2caso1.model;

public final class ResultadoSimulacion {
    private final int entradasDisponibles;
    private final int reservasConfirmadas;
    private final int solicitudesRechazadas;

    public ResultadoSimulacion(
            int entradasDisponibles,
            int reservasConfirmadas,
            int solicitudesRechazadas
    ) {
        this.entradasDisponibles = entradasDisponibles;
        this.reservasConfirmadas = reservasConfirmadas;
        this.solicitudesRechazadas = solicitudesRechazadas;
    }

    public int getEntradasDisponibles() {
        return entradasDisponibles;
    }

    public int getReservasConfirmadas() {
        return reservasConfirmadas;
    }

    public int getSolicitudesRechazadas() {
        return solicitudesRechazadas;
    }
}
