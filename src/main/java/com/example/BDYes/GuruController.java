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

public class GuruController extends HomeController implements Initializable {

    @FXML
    private TableView<String[]> tableView;

    @FXML
    private TableColumn<String[], String> id_Guru;
    @FXML
    private TableColumn<String[], String> nama_Guru;
    @FXML
    private TableColumn<String[], String> jenis_Kelamin;
    @FXML
    private TableColumn<String[], String> tanggal_Mulai_Mengajar;
    @FXML
    private TableColumn<String[], String> nomor_Telephone_Guru;

    @FXML
    private ChoiceBox<String> jenis_Kelamin_input;
    @FXML
    private DatePicker tanggal_Mulai_Mengajar_input;
    @FXML
    private TextField nama_Guru_input;
    @FXML
    private TextField nomor_Telephone_Guru_input;

    @FXML
    private VBox boxEdit;
    @FXML
    private TextField nama_Guru_edit;
    @FXML
    private ChoiceBox<String> jenis_Kelamin_edit;
    @FXML
    private DatePicker tanggal_Mulai_Mengajar_edit;
    @FXML
    private TextField nomor_Telephone_Guru_edit;

    private static boolean editShow = false;
    @FXML
    private VBox boxAdd;

    public void munculEdit(){
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
            String namaGuru = selectedRow[1];
            String jenisKelamin = selectedRow[2];
            String nomorTeleponGuru = selectedRow[3];
            String tanggalMulaiAjar = selectedRow[4];

            nama_Guru_edit.setText(namaGuru);
            jenis_Kelamin_edit.setValue(jenisKelamin.equals("Laki-laki") ? "Laki-laki" : "Perempuan");
            tanggal_Mulai_Mengajar_edit.setValue(Date.valueOf(tanggalMulaiAjar).toLocalDate());
            nomor_Telephone_Guru_edit.setText(nomorTeleponGuru);
        }
    }
    public void updateGuru(){
        String[] selectedRow = tableView.getSelectionModel().getSelectedItem();
        if (selectedRow != null) {
            int idGuru = Integer.parseInt(selectedRow[0]);
            String namaGuru = nama_Guru_edit.getText();
            String jenisKelaminValue = jenis_Kelamin_edit.getValue();

            boolean jenisKelamin = jenisKelaminValue.equals("Laki-laki"); // True for Laki-laki, False for Perempuan

            Date tanggalMulaiAjar = Date.valueOf(tanggal_Mulai_Mengajar_edit.getValue()); //localDate to Date
            String nomorGuru = nomor_Telephone_Guru_edit.getText();

            JavaPostgreSQL.updateGuru(namaGuru, jenisKelamin, tanggalMulaiAjar, nomorGuru, idGuru);
            refreshTable();
            munculEdit();
        }
    }
    public void munculDelete(){
        String[] selectedRow = tableView.getSelectionModel().getSelectedItem();
        if (selectedRow != null) {
            int id = Integer.parseInt(selectedRow[0]);
            String namaGuru = selectedRow[1];
            String jenisKelaminValue = selectedRow[2];
            boolean jenisKelamin = jenisKelaminValue.equals("Laki-laki"); // True for Laki-laki, False for Perempuan

            String noGuru = selectedRow[3];
            Date tanggalMulai = Date.valueOf(selectedRow[4]); //localDate to Date

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText("Delete Confirmation");
            alert.setContentText("Apakah Sudah Yakin Ingin Menghapus " + namaGuru + " ?\nAksi Tidak Dapat Dibatalkan");

            // Menampilkan alert dan menunggu respon dari pengguna
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    JavaPostgreSQL.deleteGuru(namaGuru, jenisKelamin, tanggalMulai, noGuru, id);
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
        munculEdit();
        id_Guru.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[0]));
        nama_Guru.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[1]));
        jenis_Kelamin.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[2]));
        nomor_Telephone_Guru.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[3]));
        tanggal_Mulai_Mengajar.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[4]));

        ArrayList<String[]> data = JavaPostgreSQL.readGuru();
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

        tableView.setItems(observableData);

        //ChoiceBox Items
        jenis_Kelamin_input.getItems().addAll("Laki-laki","Perempuan");
        jenis_Kelamin_edit.getItems().addAll("Laki-laki","Perempuan");
    }

    @FXML
    private TableColumn<String[], String> alamat;
    public void addGuru(){
        try {
            String teacherName = nama_Guru_input.getText();
            String teacherPhone = nomor_Telephone_Guru_input.getText();

            String jenisKelaminValue = jenis_Kelamin_input.getValue();
            if (jenisKelaminValue == null || jenisKelaminValue.isEmpty()) {
                // Handle error, choice box value not selected
                return;
            }
            Boolean jenisKelamin = jenisKelaminValue.equals("Laki-laki"); // True for Laki-laki, False for Perempuan

            Date tanggalAjar = Date.valueOf(tanggal_Mulai_Mengajar_input.getValue()); //localDate to Date

            JavaPostgreSQL.insertIntoGuruTable(teacherName,jenisKelamin,teacherPhone,tanggalAjar);

            refreshTable();
        }catch (Exception e){
            System.out.println("ERROR TAMPIL LABEL");
        }
    }
    private void refreshTable() {
        ArrayList<String[]> data = JavaPostgreSQL.readGuru();
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
