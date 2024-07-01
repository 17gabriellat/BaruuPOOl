package com.example.BDYes;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.ResourceBundle;

public class LaporanController extends HomeController implements Initializable {
    @FXML
    private TableView<String[]> tableView;

    @FXML
    private TableColumn<String[], String> no_col;

    @FXML
    private TableColumn<String[], String> nama_anak_col;

    @FXML
    private TableColumn<String[], String> status_col;
    @FXML
    private RadioButton mingguan;
    @FXML
    private RadioButton tahunan;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        no_col.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[0]));
        nama_anak_col.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[1]));
        status_col.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[2]));
    }
}
