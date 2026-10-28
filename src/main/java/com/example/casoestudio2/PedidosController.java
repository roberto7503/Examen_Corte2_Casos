package com.example.casoestudio2;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class PedidosController {

    @FXML private Button btnIniciar;
    @FXML private Button btnCancelar;
    @FXML private ProgressBar pbProgreso;
    @FXML private Label lblContador;
    @FXML private Label lblEstado;

    private final ExecutorService executor = Executors.newSingleThreadExecutor(r -> {
        Thread t = new Thread(r, "hilo-pedidos-worker");
        t.setDaemon(true);
        return t;
    });

    private ProcesamientoPedidosTask tareaActual;

    @FXML
    public void initialize() {
        setEstado(EstadoApp.LISTO);
        pbProgreso.setProgress(0.0);
        lblContador.setText("0 / " + ProcesamientoPedidosTask.TOTAL_PEDIDOS);
        btnCancelar.setDisable(true);
    }

    @FXML
    private void handleIniciar() {
        // Prevención de ejecuciones duplicadas
        if (tareaActual != null && tareaActual.isRunning()) {
            return;
        }

        // Restablecimiento de propiedades y valores
        pbProgreso.progressProperty().unbind();
        lblContador.textProperty().unbind();
        pbProgreso.setProgress(0.0);
        lblContador.setText("0 / " + ProcesamientoPedidosTask.TOTAL_PEDIDOS);

        // Se crea siempre una nueva instancia en cada ejecución
        tareaActual = new ProcesamientoPedidosTask();

        // Enlace reactivo seguro hacia la interfaz
        pbProgreso.progressProperty().bind(tareaActual.progressProperty());
        lblContador.textProperty().bind(
                Bindings.when(tareaActual.messageProperty().isEmpty())
                        .then("0 / " + ProcesamientoPedidosTask.TOTAL_PEDIDOS)
                        .otherwise(tareaActual.messageProperty())
        );

        tareaActual.setOnRunning(e -> {
            setEstado(EstadoApp.EN_EJECUCION);
            btnIniciar.setDisable(true);
            btnCancelar.setDisable(false);
        });

        tareaActual.setOnSucceeded(e -> {
            setEstado(EstadoApp.COMPLETADO);
            btnIniciar.setDisable(false);
            btnCancelar.setDisable(true);
        });

        tareaActual.setOnCancelled(e -> {
            // Confirmación asíncrona de la terminación efectiva del trabajador
            setEstado(EstadoApp.CANCELADO);
            btnIniciar.setDisable(false);
            btnCancelar.setDisable(true);
        });

        tareaActual.setOnFailed(e -> {
            setEstado(EstadoApp.ERROR);
            btnIniciar.setDisable(false);
            btnCancelar.setDisable(true);
        });

        executor.submit(tareaActual);
    }

    @FXML
    private void handleCancelar() {
        if (tareaActual != null && tareaActual.isRunning()) {
            // Transición a Cancelando mientras el trabajador responde
            setEstado(EstadoApp.CANCELANDO);
            btnCancelar.setDisable(true);
            btnIniciar.setDisable(true);

            tareaActual.cancel(true);
        }
    }

    private void setEstado(EstadoApp estado) {
        lblEstado.setText(estado.getEtiqueta());
    }

    public void shutdown() {
        if (tareaActual != null && tareaActual.isRunning()) {
            tareaActual.cancel(true);
        }

        new Thread(() -> {
            executor.shutdown();
            try {
                if (!executor.awaitTermination(800, TimeUnit.MILLISECONDS)) {
                    executor.shutdownNow();
                }
            } catch (InterruptedException ex) {
                executor.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }, "hilo-shutdown-cleanup").start();
    }
}