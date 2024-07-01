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

public class AnakController extends HomeController implements Initializable {

    @FXML
    private TableView<String[]> tableView;

    @FXML
    private TableColumn<String[], String> id_Anak;

    @FXML
    private TableColumn<String[], String> nama_Anak;

    @FXML
    private TableColumn<String[], String> jenis_Kelamin;

    @FXML
    private TableColumn<String[], String> tanggal_Lahir;

    @FXML
    private TableColumn<String[], String> nama_Orang_Tua;

    @FXML
    private TableColumn<String[], String> nomor_Telephone_Orang_Tua;
    @FXML
    private TableColumn<String[], String> alamat;
    @FXML
    private ChoiceBox<String> jenis_Kelamin_input;
    @FXML
    private DatePicker tanggal_Lahir_input;
    @FXML
    private TextField nama_Anak_input;
    @FXML
    private TextField nama_Orang_Tua_input;
    @FXML
    private TextField nomor_Telephone_Orang_Tua_input;
    @FXML
    private TextField alamat_input;

    @FXML
    private VBox munculEditAnak;

    @FXML
    private TextField nama_Anak_edit;
    @FXML
    private ChoiceBox<String> jenis_Kelamin_edit;
    @FXML
    private DatePicker tanggal_Lahir_edit;
    @FXML
    private TextField nama_Orang_Tua_edit;
    @FXML
    private TextField nomor_Telephone_Orang_Tua_edit;
    @FXML
    private TextField alamat_edit;

    private static boolean editShow = false;
    @FXML
    private VBox munculAddAnak;

    public void munculUpdateAnak(){
        if (tableView.getSelectionModel().getSelectedItem() != null) {
            editShow = !editShow;
            if (editShow) {
                //munculkan munculEditAnak
                munculEditAnak.setVisible(true);
                munculEditAnak.setManaged(true);

                munculAddAnak.setVisible(false);
                munculAddAnak.setManaged(false);

                getSelectedRowValues();
            } else {
                munculEditAnak.setVisible(false);
                munculEditAnak.setManaged(false);

                munculAddAnak.setVisible(true);
                munculAddAnak.setManaged(true);
            }
        }
        else {munculEditAnak.setVisible(false);
            munculEditAnak.setManaged(false);

            munculAddAnak.setVisible(true);
            munculAddAnak.setManaged(true);}
    }
    private void getSelectedRowValues() {
        String[] selectedRow = tableView.getSelectionModel().getSelectedItem();
        if (selectedRow != null) {
            String idAnak = selectedRow[0];
            String namaAnak = selectedRow[1];
            String jenisKelamin = selectedRow[2];
            String tanggalLahir = selectedRow[3];
            String namaOrangTua = selectedRow[4];
            String nomorTeleponOrangTua = selectedRow[5];
            String alamat = selectedRow[6];

            nama_Anak_edit.setText(namaAnak);
            jenis_Kelamin_edit.setValue(jenisKelamin.equals("Laki-laki") ? "Laki-laki" : "Perempuan");
            tanggal_Lahir_edit.setValue(Date.valueOf(tanggalLahir).toLocalDate());
            nama_Orang_Tua_edit.setText(namaOrangTua);
            nomor_Telephone_Orang_Tua_edit.setText(nomorTeleponOrangTua);
            alamat_edit.setText(alamat);
        }
    }
    public void updateAnak(){
        String[] selectedRow = tableView.getSelectionModel().getSelectedItem();
        if (selectedRow != null) {
            int idAnak = Integer.parseInt(selectedRow[0]);
            String namaAnak = nama_Anak_edit.getText();
            String jenisKelaminValue = jenis_Kelamin_edit.getValue();

            boolean jenisKelamin = jenisKelaminValue.equals("Laki-laki"); // True for Laki-laki, False for Perempuan

            Date tanggalLahir = Date.valueOf(tanggal_Lahir_edit.getValue()); //localDate to Date
            String namaOrangTua = nama_Orang_Tua_edit.getText();
            String nomorTeleponOrangTua = nomor_Telephone_Orang_Tua_edit.getText();
            String alamat = alamat_edit.getText();

            JavaPostgreSQL.updateAnak(namaAnak, jenisKelamin, tanggalLahir, namaOrangTua, nomorTeleponOrangTua, alamat, idAnak);
            refreshTable();
            munculUpdateAnak();
        }
    }
    public void munculDeleteAnak(){
        String[] selectedRow = tableView.getSelectionModel().getSelectedItem();
        if (selectedRow != null) {
            int id = Integer.parseInt(selectedRow[0]);
            String namaAnak = selectedRow[1];
            String jenisKelaminValue = selectedRow[2];
            boolean jenisKelamin = jenisKelaminValue.equals("Laki-laki"); // True for Laki-laki, False for Perempuan

            Date tanggalLahir = Date.valueOf(selectedRow[3]); //localDate to Date
            String namaOrangTua = selectedRow[4];
            String nomorTeleponOrangTua = selectedRow[5];
            String alamat = selectedRow[6];

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText("Delete Confirmation");
            alert.setContentText("Apakah Sudah Yakin Ingin Menghapus " + namaAnak + " ?\nAksi Tidak Dapat Dibatalkan");

            // Menampilkan alert dan menunggu respon dari pengguna
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    JavaPostgreSQL.deleteAnak(namaAnak, jenisKelamin, tanggalLahir, namaOrangTua, nomorTeleponOrangTua, alamat, id);
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
        munculUpdateAnak();

        id_Anak.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[0]));
        nama_Anak.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[1]));
        jenis_Kelamin.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[2]));
        tanggal_Lahir.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[3]));
        nama_Orang_Tua.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[4]));
        nomor_Telephone_Orang_Tua.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[5]));
        alamat.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[6]));

        ArrayList<String[]> data = JavaPostgreSQL.readAnak();

        for (int i = 0; i < data.size(); i++) {
            if (data.get(i)[2].equalsIgnoreCase("true") || data.get(i)[2].equalsIgnoreCase("Laki-laki")){
                data.get(i)[2] = "Laki-laki";
            }
            else data.get(i)[2] = "Perempuan";
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

        //ChoiceBox Items
        jenis_Kelamin_input.getItems().addAll("Laki-laki","Perempuan");
        jenis_Kelamin_edit.getItems().addAll("Laki-laki","Perempuan");
    }

    public void addAnak(){
        try {
            String childName = nama_Anak_input.getText();
            String guardianName = nama_Orang_Tua_input.getText();
            String guardianPhone = nomor_Telephone_Orang_Tua_input.getText();
            String address = alamat_input.getText();

            String jenisKelaminValue = jenis_Kelamin_input.getValue();
            if (jenisKelaminValue == null || jenisKelaminValue.isEmpty()) {
                // Handle error, choice box value not selected
                return;
            }
            Boolean jenisKelamin = jenisKelaminValue.equals("Laki-laki"); // True for Laki-laki, False for Perempuan

            Date tanggalLahir = Date.valueOf(tanggal_Lahir_input.getValue()); //localDate to Date

            JavaPostgreSQL.insertIntoAnakTable(childName, jenisKelamin, tanggalLahir, guardianName, guardianPhone, address);

            refreshTable();
        }catch (Exception e){
            System.out.println("ERROR TAMPIL LABEL");
        }
    }
    private void refreshTable() {
        ArrayList<String[]> data = JavaPostgreSQL.readAnak();
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i)[2].equalsIgnoreCase("true") || data.get(i)[2].equalsIgnoreCase("Laki-laki")){
                data.get(i)[2] = "Laki-laki";
            }
            else data.get(i)[2] = "Perempuan";
        }
        ObservableList<String[]> observableData = FXCollections.observableArrayList(data);
        tableView.setItems(observableData);
    }
}
