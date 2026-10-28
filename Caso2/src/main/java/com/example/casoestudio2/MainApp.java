package com.example.casoestudio2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/com/example/casoestudio2/hello-view.fxml"));
        Scene scene = new Scene(loader.load());

        PedidosController controller = loader.getController();

        stage.setTitle("Procesamiento Concurrente de Pedidos - UAM");
        stage.setScene(scene);
        stage.setResizable(false);

        // Apagado cooperativo y seguro al cerrar la ventana
        stage.setOnCloseRequest(event -> controller.shutdown());

        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}