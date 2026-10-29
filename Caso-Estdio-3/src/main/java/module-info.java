module org.example.casoestdio3 {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.casoestdio3 to javafx.fxml;
    exports org.example.casoestdio3;
}