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

public class KelasController extends HomeController implements Initializable {

    @FXML
    private TableView<String[]> tableView;

    @FXML
    private TableColumn<String[],String> id_Kelas;

    @FXML
    private TableColumn<String[],String> nama_Kelas;

    @FXML
    private TableColumn<String[],String> status_Kelas;

    @FXML
    private ChoiceBox<String> statusKelas_input;
    @FXML
    private TextField namaKelas_input;

    @FXML
    private VBox boxEdit;
    @FXML
    private TextField namaKelas_edit;
    @FXML
    private ChoiceBox<String> statusKelas_edit;
    private static boolean editShow = false;
    @FXML
    private VBox boxAdd;

    public void editKelas(){
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
            String namaKelas = selectedRow[1];
            String statusKelas = selectedRow[2];

            namaKelas_edit.setText(namaKelas);
            statusKelas_edit.setValue(statusKelas.equals("Buka") ? "Buka" : "Tutup");
        }
    }
    public void updateKelas(){
        String[] selectedRow = tableView.getSelectionModel().getSelectedItem();
        if (selectedRow != null) {
            int idKelas = Integer.parseInt(selectedRow[0]);
            String namaKelas = namaKelas_edit.getText();
            String bukaTutupValue = statusKelas_edit.getValue();

            boolean statusKelas = bukaTutupValue.equals("Buka"); // True for Buka, False for Tutup

            JavaPostgreSQL.updateKelas(namaKelas, statusKelas, idKelas);
            refreshTable();
            editKelas();
        }
    }
    public void deleteKelas(){
        String[] selectedRow = tableView.getSelectionModel().getSelectedItem();
        if (selectedRow != null) {
            int id = Integer.parseInt(selectedRow[0]);
            String namaKelas = selectedRow[1];
            String statusValue = selectedRow[2];
            boolean statusKelas = statusValue.equals("Buka");

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText("Delete Confirmation");
            alert.setContentText("Apakah Sudah Yakin Ingin Menghapus Kelas " + namaKelas + " ?\nAksi Tidak Dapat Dibatalkan");

            // Menampilkan alert dan menunggu respon dari pengguna
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    JavaPostgreSQL.deleteKelas(namaKelas, statusKelas, id);
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
        editKelas();
        id_Kelas.setCellValueFactory(cellData->new SimpleStringProperty(cellData.getValue()[0]));
        nama_Kelas.setCellValueFactory(cellData->new SimpleStringProperty(cellData.getValue()[1]));
        status_Kelas.setCellValueFactory(cellData->new SimpleStringProperty(cellData.getValue()[2]));

        ArrayList<String[]> data = JavaPostgreSQL.readKelas();

        for (int i = 0; i < data.size(); i++) {
            if (data.get(i)[2].equalsIgnoreCase("true")){
                data.get(i)[2] = "Buka";
            }else data.get(i)[2] = "Tutup";
        }
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
        statusKelas_input.getItems().addAll("Buka","Tutup");
        statusKelas_edit.getItems().addAll("Buka","Tutup");
    }
    public void addKelas(){
        try {
            String namaKelas = namaKelas_input.getText();

            String tempstatusKelas = statusKelas_input.getValue();
            if (tempstatusKelas == null || tempstatusKelas.isEmpty()) {
                // Handle error, choice box value not selected
                return;
            }
            Boolean statusKelas = tempstatusKelas.equals("Buka"); // True for Buka, False for Tutup

            JavaPostgreSQL.insertIntoKelasTable(namaKelas, statusKelas);

            refreshTable();
        }catch (Exception e){
            System.out.println("ERROR TAMPIL LABEL");
        }
    }
    private void refreshTable() {
        ArrayList<String[]> data = JavaPostgreSQL.readKelas();
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i)[2].equalsIgnoreCase("true")){
                data.get(i)[2] = "Buka";
            }else data.get(i)[2] = "Tutup";
        }
        ObservableList<String[]> observableData = FXCollections.observableArrayList(data);
        tableView.setItems(observableData);
    }
}
