package com.example.examen2caso1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ReservaApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                ReservaApplication.class.getResource("reserva-view.fxml")
        );
        Scene scene = new Scene(loader.load(), 720, 460);
        scene.getStylesheets().add(
                ReservaApplication.class.getResource("styles.css").toExternalForm()
        );

        stage.setTitle("Reserva concurrente de entradas");
        stage.setMinWidth(680);
        stage.setMinHeight(440);
        stage.setScene(scene);
        stage.show();
    }
}
