module com.example.casoestudio2 {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.example.casoestudio2 to javafx.fxml;
    exports com.example.casoestudio2;
}