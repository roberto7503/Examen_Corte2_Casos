module com.example.examen2caso1 {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.example.examen2caso1.controller to javafx.fxml;
    exports com.example.examen2caso1;
    exports com.example.examen2caso1.model;
    exports com.example.examen2caso1.service;
}
