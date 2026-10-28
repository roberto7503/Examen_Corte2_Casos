package com.example.examen2caso1.controller;

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

    @FXML
    private void initialize() {
        restablecerVista();
    }

    @FXML
    private void onIniciarSimulacion() {
        estadoLabel.setText("Pendiente: conectar la simulacion en segundo plano");
    }

    private void restablecerVista() {
        entradasDisponiblesLabel.setText("20");
        reservasConfirmadasLabel.setText("0");
        solicitudesRechazadasLabel.setText("0");
        estadoLabel.setText("Base lista para implementar");
        iniciarButton.setDisable(false);
        progresoIndicator.setVisible(false);
    }
}
