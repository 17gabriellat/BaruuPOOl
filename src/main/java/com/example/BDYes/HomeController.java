package com.example.BDYes;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HomeController {
    private Stage stage;
    private Scene scene;
    private Parent root; //Parent -> generic any root node (Group, AnchorPane, Pane ScrollPane, dll)

    public void routeToHome(ActionEvent event) throws IOException { //DARI MENU BAR
        root = FXMLLoader.load(getClass().getResource("home.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);

        scene.getStylesheets().add(this.getClass().getResource("/style.css").toExternalForm());
        stage.setScene(scene);
        stage.show();

    }
    public void routeToAnakMenu(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("anakMenu.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void routeToKelasMenu(ActionEvent event) throws IOException {
         root = FXMLLoader.load(getClass().getResource("kelasMenu.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void routeToKelasPararelMenu(ActionEvent event) throws IOException {
         root = FXMLLoader.load(getClass().getResource("kelasPararelMenu.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void routeToGuruMenu(ActionEvent event) throws IOException {
         root = FXMLLoader.load(getClass().getResource("guruMenu.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void routeToTahunMenu(ActionEvent event) throws IOException {
         root = FXMLLoader.load(getClass().getResource("tahunMenu.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void routeToKebaktianMenu(ActionEvent event) throws IOException {
         root = FXMLLoader.load(getClass().getResource("kebaktianMenu.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    // Intermediate Classes/Tables
    public void routeToKelasDibukaMenu(ActionEvent event) throws IOException {
         root = FXMLLoader.load(getClass().getResource("kelasDibukaMenu.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void routeToKelasAnakMenu(ActionEvent event) throws IOException {
         root = FXMLLoader.load(getClass().getResource("kelasAnakMenu.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void routeToLogMengajarGuruMenu(ActionEvent event) throws IOException {
         root = FXMLLoader.load(getClass().getResource("logMengajarGuruMenu.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void routeToLogKebaktianMenu(ActionEvent event) throws IOException {
         root = FXMLLoader.load(getClass().getResource("logKebaktianMenu.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}

