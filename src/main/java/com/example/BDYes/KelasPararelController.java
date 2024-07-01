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
import java.util.ArrayList;
import java.util.ResourceBundle;

public class KelasPararelController extends HomeController implements Initializable {

    @FXML
    private TableView<String[]> tableView;

    @FXML
    private TableColumn<String[],String> id_Pararel;

    @FXML
    private TableColumn<String[],String> nama_Pararel;
    @FXML
    private TextField nama_Pararel_input;

    @FXML
    private VBox boxEdit;
    @FXML
    private TextField nama_Pararel_edit;
    private static boolean editShow = false;
    @FXML
    private VBox boxAdd;

    public void editPararel(){
        if (tableView.getSelectionModel().getSelectedItem() != null) {
            editShow = !editShow;
            if (editShow) {
                //munculkan munculEditAnak
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
            String namaKelas = selectedRow[1];

            nama_Pararel_edit.setText(namaKelas);
        }
    }
    public void updatePararel(){
        String[] selectedRow = tableView.getSelectionModel().getSelectedItem();
        if (selectedRow != null) {
            int idKelas = Integer.parseInt(selectedRow[0]);
            String namaKelas = nama_Pararel_edit.getText();

            JavaPostgreSQL.updatePararel(namaKelas, idKelas);
            refreshTable();
            editPararel();
        }
    }
    public void deletePararel(){
        String[] selectedRow = tableView.getSelectionModel().getSelectedItem();
        if (selectedRow != null) {
            int id = Integer.parseInt(selectedRow[0]);
            String namaKelas = selectedRow[1];

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText("Delete Confirmation");
            alert.setContentText("Apakah Sudah Yakin Ingin Menghapus Pararel " + namaKelas + " ?\nAksi Tidak Dapat Dibatalkan");

            // Menampilkan alert dan menunggu respon dari pengguna
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    JavaPostgreSQL.deletePararel(namaKelas, id);
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
        editPararel();
        id_Pararel.setCellValueFactory(cellData->new SimpleStringProperty(cellData.getValue()[0]));
        nama_Pararel.setCellValueFactory(cellData->new SimpleStringProperty(cellData.getValue()[1]));

        ArrayList<String[]> data = JavaPostgreSQL.readKelasPararel();

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
    public void addKelasPararel(){
        try {
            String namaKelasPararel = nama_Pararel_input.getText();

            JavaPostgreSQL.insertIntoKelasPararelTable(namaKelasPararel);

            refreshTable();
        }catch (Exception e){
            System.out.println("ERROR TAMPIL LABEL");
        }
    }
    private void refreshTable() {
        ArrayList<String[]> data = JavaPostgreSQL.readKelasPararel();
        ObservableList<String[]> observableData = FXCollections.observableArrayList(data);
        tableView.setItems(observableData);
    }
}
