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

public class TahunController extends HomeController implements Initializable {

    @FXML
    private TableView<String[]> tableView;

    @FXML
    private TableColumn<String[],String> id_Tahun;
    @FXML
    private TableColumn<String[],String> periode_Tahun;
    @FXML
    private TableColumn<String[],String> semester;
    @FXML
    private ChoiceBox<String> semester_input;
    @FXML
    private TextField periode_Tahun_input;
    @FXML
    private VBox boxEdit;
    @FXML
    private TextField periode_Tahun_edit;
    @FXML
    private ChoiceBox<String> semester_edit;
    private static boolean editShow = false;
    @FXML
    private VBox boxAdd;

    public void editTahun(){
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

            periode_Tahun_edit.setText(namaKelas);
            semester_edit.setValue(statusKelas.equals("Gasal") ? "Gasal" : "Genap");
        }
    }
    public void updateTahun(){
        String[] selectedRow = tableView.getSelectionModel().getSelectedItem();
        if (selectedRow != null) {
            int idTahun = Integer.parseInt(selectedRow[0]);
            String periodTahun = periode_Tahun_edit.getText();
            String bukaTutupValue = semester_edit.getValue();

            boolean semester = bukaTutupValue.equals("Gasal"); // True for Gasal, False for Genap

            JavaPostgreSQL.updateTahun(periodTahun, semester, idTahun);
            refreshTable();
            editTahun();
        }
    }
    public void deleteTahun(){
        String[] selectedRow = tableView.getSelectionModel().getSelectedItem();
        if (selectedRow != null) {
            int id = Integer.parseInt(selectedRow[0]);
            String periode_Tahun = selectedRow[1];
            String statusValue = selectedRow[2];
            boolean semester = statusValue.equals("Gasal");

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText("Delete Confirmation");
            alert.setContentText("Apakah Sudah Yakin Ingin Menghapus Periode " + periode_Tahun +" Semester " +statusValue+" ?\nAksi Tidak Dapat Dibatalkan");

            // Menampilkan alert dan menunggu respon dari pengguna
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    JavaPostgreSQL.deleteTahun(periode_Tahun, semester, id);
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
        editTahun();
        id_Tahun.setCellValueFactory(cellData->new SimpleStringProperty(cellData.getValue()[0]));
        periode_Tahun.setCellValueFactory(cellData->new SimpleStringProperty(cellData.getValue()[1]));
        semester.setCellValueFactory(cellData->new SimpleStringProperty(cellData.getValue()[2]));

        ArrayList<String[]> data = JavaPostgreSQL.readTahun();

        for (int i = 0; i < data.size(); i++) {
            if (data.get(i)[2].equalsIgnoreCase("true")){
                data.get(i)[2] = "Gasal";
            }else data.get(i)[2] = "Genap";
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
        semester_input.getItems().addAll("Gasal","Genap");
        semester_edit.getItems().addAll("Gasal","Genap");
    }
    public void addTahun(){
        try {
            String periode = periode_Tahun_input.getText();

            String semesterPilih = semester_input.getValue();
            if (semesterPilih == null || semesterPilih.isEmpty()) {
                // Handle error, choice box value not selected
                return;
            }
            Boolean sems = semesterPilih.equals("Gasal"); // True for Buka, False for Tutup

            JavaPostgreSQL.insertIntoTahunTable(periode, sems);

            refreshTable();
        }catch (Exception e){
            System.out.println("ERROR TAMPIL LABEL");
        }
    }
    private void refreshTable() {
        ArrayList<String[]> data = JavaPostgreSQL.readTahun();
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i)[2].equalsIgnoreCase("true")){
                data.get(i)[2] = "Gasal";
            }else data.get(i)[2] = "Genap";
        }
        ObservableList<String[]> observableData = FXCollections.observableArrayList(data);
        tableView.setItems(observableData);
    }
}
