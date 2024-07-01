package com.example.BDYes;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class KebaktianController extends HomeController implements Initializable {
    @FXML
    private TableView<String[]> tableView;

    @FXML
    private TableColumn<String[], String> sunday_service_id;
    @FXML
    private TableColumn<String[], String> service_theme;
    @FXML
    private TableColumn<String[], String> service_type;
    @FXML
    private TableColumn<String[], String> service_date;
    @FXML
    private DatePicker service_date_input;
    @FXML
    private TextField service_theme_input;
    @FXML
    private TextField service_type_input;

    @FXML
    private VBox boxEdit;
    @FXML
    private TextField service_theme_edit;
    @FXML
    private TextField service_type_edit;
    @FXML
    private DatePicker service_date_edit;
    private static boolean editShow = false;
    @FXML
    private VBox boxAdd;

    public void editKebaktian(){
        if (tableView.getSelectionModel().getSelectedItem() != null) {
            editShow = !editShow;
            if (editShow) {

                boxEdit.setVisible(true);
                boxEdit.setManaged(true);

                boxAdd.setVisible(false);
                boxAdd.setManaged(false);

                getSelectedRowValues();
            } else {
                boxEdit.setVisible(false);
                boxEdit.setManaged(false);

                boxAdd.setVisible(true);
                boxAdd.setManaged(true);
            }
        }
        else {boxEdit.setVisible(false);
            boxEdit.setManaged(false);

            boxAdd.setVisible(true);
            boxAdd.setManaged(true);}
    }
    private void getSelectedRowValues() {
        String[] selectedRow = tableView.getSelectionModel().getSelectedItem();
        if (selectedRow != null) {
            String date = selectedRow[3];
            String type = selectedRow[2];
            String theme = selectedRow[1];

            service_theme_edit.setText(theme);
            service_type_edit.setText(type);
            service_date_edit.setValue(Date.valueOf(date).toLocalDate());
        }
    }
    public void updateKebaktian(){
        String[] selectedRow = tableView.getSelectionModel().getSelectedItem();
        if (selectedRow != null) {
            int idKebaktian = Integer.parseInt(selectedRow[0]);
            String theme = service_theme_edit.getText();
            String type = service_type_edit.getText();
            Date date = Date.valueOf(service_date_edit.getValue());

            JavaPostgreSQL.updateKebaktian(date,type,theme, idKebaktian);
            refreshTable();
            editKebaktian();
        }
    }
    public void deleteKebaktian(){
        String[] selectedRow = tableView.getSelectionModel().getSelectedItem();
        if (selectedRow != null) {
            int id = Integer.parseInt(selectedRow[0]);
            Date date = Date.valueOf(selectedRow[3]);
            String type = selectedRow[2];
            String theme = selectedRow[1];

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText("Delete Confirmation");
            alert.setContentText("Apakah Sudah Yakin Ingin Menghapus Kebaktian "+type+" Tanggal " + date +" dengan Tema "+theme+ " ?\nAksi Tidak Dapat Dibatalkan");

            // Menampilkan alert dan menunggu respon dari pengguna
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    JavaPostgreSQL.deleteKebaktian(date, type, theme, id);
                } else {
                    // Aksi ketika pengguna menekan Cancel atau menutup alert
                    System.out.println("Delete canceled");
                }
            });
            refreshTable();
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        editKebaktian();
        sunday_service_id.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[0]));
        service_theme.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[1]));
        service_type.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[2]));
        service_date.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[3]));

        ArrayList<String[]> data = JavaPostgreSQL.readKebaktian();

        // Convert ArrayList<String[]> to ObservableList<String[]>
        ObservableList<String[]> observableData = FXCollections.observableArrayList(data);

        tableView.setItems(observableData);
        tableView.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() == 1) { // Single click
                if (editShow) {
                    getSelectedRowValues();
                }
            }
        });

        tableView.setItems(observableData);
    }
    public void addKebaktian(){
        try {
            String theme = service_theme_input.getText();
            String type = service_type_input.getText();

            Date tanggalr = Date.valueOf(service_date_input.getValue()); //localDate to Date

            JavaPostgreSQL.insertIntoKebaktianTable(theme,type,tanggalr);

            refreshTable();
        }catch (Exception e){
            System.out.println("ERROR TAMPIL LABEL");
        }
    }
    private void refreshTable() {
        ArrayList<String[]> data = JavaPostgreSQL.readKebaktian();
        ObservableList<String[]> observableData = FXCollections.observableArrayList(data);
        tableView.setItems(observableData);
    }
}
