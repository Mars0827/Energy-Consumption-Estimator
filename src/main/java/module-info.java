module com.example.energycalculator {
    requires javafx.controls;
    requires javafx.fxml;
    requires jfreechart;
    requires java.desktop;


    opens com.example.energycalculator to javafx.fxml;
    exports com.example.energycalculator;
}