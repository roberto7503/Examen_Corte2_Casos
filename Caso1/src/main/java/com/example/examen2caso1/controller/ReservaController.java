package com.example.examen2caso1.controller;

import com.example.examen2caso1.model.ResultadoSimulacion;
import com.example.examen2caso1.service.SimuladorReservas;
import com.example.examen2caso1.service.SimuladorReservasImpl;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;

public class ReservaController {

    @FXML
    private Label entradasDisponiblesLabel;

    @FXML
    private Label reservasConfirmadasLabel;

    @FXML
    private Label solicitudesRechazadasLabel;

    @FXML
    private Label estadoLabel;

    @FXML
    private Button iniciarButton;

    @FXML
    private ProgressIndicator progresoIndicator;

    private final SimuladorReservas simulador =
            new SimuladorReservasImpl();

    private boolean simulacionEnCurso;

    @FXML
    private void initialize() {
        restablecerVista();
    }

    @FXML
    private void onIniciarSimulacion() {
        if (simulacionEnCurso) {
            return;
        }

        restablecerVista();
        cambiarEstadoEjecucion(true);
        estadoLabel.setText("Simulación en curso...");

        Task<ResultadoSimulacion> tarea = new Task<>() {
            @Override
            protected ResultadoSimulacion call() throws Exception {
                try {
                    return simulador.ejecutarSimulacion();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw e;
                }
            }
        };

        tarea.setOnSucceeded(event -> {
            ResultadoSimulacion resultado = tarea.getValue();

            entradasDisponiblesLabel.setText(
                    String.valueOf(resultado.getEntradasDisponibles())
            );

            reservasConfirmadasLabel.setText(
                    String.valueOf(resultado.getReservasConfirmadas())
            );

            solicitudesRechazadasLabel.setText(
                    String.valueOf(resultado.getSolicitudesRechazadas())
            );

            cambiarEstadoEjecucion(false);
            estadoLabel.setText("Simulación finalizada correctamente.");
        });

        tarea.setOnFailed(event -> {
            cambiarEstadoEjecucion(false);

            Throwable error = tarea.getException();

            if (error instanceof InterruptedException) {
                estadoLabel.setText(
                        "La simulación se interrumpió. Intentá nuevamente."
                );
            } else if (error instanceof IllegalStateException) {
                estadoLabel.setText(
                        "Ya hay una simulación en ejecución. Esperá a que termine."
                );
            } else {
                estadoLabel.setText(
                        "No se pudo completar la simulación. Intentá nuevamente."
                );
            }
        });

        Thread hilo = new Thread(tarea, "simulacion-reservas");
        hilo.setDaemon(true);
        hilo.start();
    }

    private void restablecerVista() {
        entradasDisponiblesLabel.setText(
                String.valueOf(SimuladorReservas.ENTRADAS_INICIALES)
        );

        reservasConfirmadasLabel.setText("0");
        solicitudesRechazadasLabel.setText("0");
        estadoLabel.setText("Lista para iniciar la simulación.");

        progresoIndicator.setProgress(
                ProgressIndicator.INDETERMINATE_PROGRESS
        );

        cambiarEstadoEjecucion(false);
    }

    private void cambiarEstadoEjecucion(boolean enCurso) {
        simulacionEnCurso = enCurso;
        iniciarButton.setDisable(enCurso);
        progresoIndicator.setVisible(enCurso);
        progresoIndicator.setManaged(enCurso);
    }
}