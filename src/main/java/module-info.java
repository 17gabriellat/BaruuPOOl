module com.example.bdprojectyes {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.BDYes to javafx.fxml;
    exports com.example.BDYes;
}