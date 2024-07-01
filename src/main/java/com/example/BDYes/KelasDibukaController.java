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
import java.util.Objects;
import java.util.ResourceBundle;

public class KelasDibukaController extends HomeController implements Initializable {

    @FXML
    private TableView<String[]> tableView;

    @FXML
    private TableColumn<String[], String> opened_id;
    @FXML
    private TableColumn<String[], String> class_id;
    @FXML
    private TableColumn<String[], String> parallel_id;
    @FXML
    private TableColumn<String[], String> year_id;
    @FXML
    private ChoiceBox<String> class_id_input;
    @FXML
    private ChoiceBox<String> parallel_id_input;
    @FXML
    private ChoiceBox<String> year_id_input;
    @FXML
    private VBox boxEdit;
    @FXML
    private ChoiceBox<String> class_id_edit;
    @FXML
    private ChoiceBox<String> parallel_id_edit;
    @FXML
    private ChoiceBox<String> year_id_edit;
    private static boolean editShow = false;
    @FXML
    private VBox boxAdd;

    public void editKelas() {
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
        } else {
            boxEdit.setVisible(false);
            boxEdit.setManaged(false);

            boxAdd.setVisible(true);
            boxAdd.setManaged(true);
        }
    }

    private void getSelectedRowValues() {
        String[] selectedRow = tableView.getSelectionModel().getSelectedItem();
        if (selectedRow != null) {
            String namaKelas = selectedRow[1];
            String namaPararel = selectedRow[2];
            String periode = selectedRow[3];

            class_id_edit.setValue(namaKelas);
            parallel_id_edit.setValue(namaPararel);
            year_id_edit.setValue(periode);
        }
    }

    public void updateKelas() {
        String[] selectedRow = tableView.getSelectionModel().getSelectedItem();
        if (selectedRow != null) {
            int idOpen = Integer.parseInt(selectedRow[0]);

            String kelasName = class_id_edit.getValue();
            String pararelName = parallel_id_edit.getValue();
            String periodeName = year_id_edit.getValue();

            //cari id dari tiap nama diatas
            // METHOD E GMN
            ArrayList<String[]> classes = JavaPostgreSQL.readKelas();
            ArrayList<String[]> pararels = JavaPostgreSQL.readKelasPararel();
            ArrayList<String[]> tahuns = JavaPostgreSQL.readTahun();

            int masukinClass = 0;
            int masukinPararel = 0;
            int masukinTahun = 0;

            for (int i = 0; i < classes.size(); i++) {
                for (int j = 0; j < classes.get(i).length; j++) {
                    if (j == 0 && classes.get(i)[1].equalsIgnoreCase(kelasName)) {
                        masukinClass = Integer.parseInt(classes.get(i)[0]);
                    }
                }
            }

            for (int i = 0; i < pararels.size(); i++) {
                for (int j = 0; j < pararels.get(i).length; j++) {
                    if (j == 0 && pararels.get(i)[1].equalsIgnoreCase(pararelName)) {
                        masukinPararel = Integer.parseInt(pararels.get(i)[0]);
                    }
                }
            }

            for (int i = 0; i < tahuns.size(); i++) {
                for (int j = 0; j < tahuns.get(i).length; j++) {
                    if (j == 0 && tahuns.get(i)[1].equalsIgnoreCase(periodeName)) {
                        masukinTahun = Integer.parseInt(tahuns.get(i)[0]);
                    }
                }
            }
            JavaPostgreSQL.updateKelasDibuka(masukinClass,masukinPararel,masukinTahun, idOpen);
            refreshTable();
            editKelas();
        }
    }

    public void deleteKelas() {
        String[] selectedRow = tableView.getSelectionModel().getSelectedItem();
        if (selectedRow != null) {
            int id = Integer.parseInt(selectedRow[0]);
            String kelasName = selectedRow[1];
            String pararelName = selectedRow[2];
            String periodeName = selectedRow[3];

            ArrayList<String[]> classes = JavaPostgreSQL.readKelas();
            ArrayList<String[]> pararels = JavaPostgreSQL.readKelasPararel();
            ArrayList<String[]> tahuns = JavaPostgreSQL.readTahun();

            int masukinClasse = 0;
            int masukinPararele = 0;
            int masukinTahune = 0;

            for (int i = 0; i < classes.size(); i++) {
                for (int j = 0; j < classes.get(i).length; j++) {
                    if (j == 0 && classes.get(i)[1].equalsIgnoreCase(kelasName)) {
                        masukinClasse = Integer.parseInt(classes.get(i)[0]);
                    }
                }
            }

            for (int i = 0; i < pararels.size(); i++) {
                for (int j = 0; j < pararels.get(i).length; j++) {
                    if (j == 0 && pararels.get(i)[1].equalsIgnoreCase(pararelName)) {
                        masukinPararele = Integer.parseInt(pararels.get(i)[0]);
                    }
                }
            }

            for (int i = 0; i < tahuns.size(); i++) {
                for (int j = 0; j < tahuns.get(i).length; j++) {
                    if (j == 0 && tahuns.get(i)[1].equalsIgnoreCase(periodeName)) {
                        masukinTahune = Integer.parseInt(tahuns.get(i)[0]);
                    }
                }
            }

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText("Delete Confirmation");
            alert.setContentText("Apakah Sudah Yakin Ingin Menghapus Kelas " + kelasName+"-"+pararelName+" periode "+periodeName + " ?\nAksi Tidak Dapat Dibatalkan");

            // Menampilkan alert dan menunggu respon dari pengguna
            int finalMasukinClasse = masukinClasse;
            int finalMasukinPararele = masukinPararele;
            int finalMasukinTahune = masukinTahune;
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    JavaPostgreSQL.deleteKelasDibuka(finalMasukinClasse, finalMasukinPararele, finalMasukinTahune, id);
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
        opened_id.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[0]));
        class_id.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[1]));
        parallel_id.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[2]));
        year_id.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[3]));

        ArrayList<String[]> classes = JavaPostgreSQL.readKelas();
        ArrayList<String[]> pararels = JavaPostgreSQL.readKelasPararel();
        ArrayList<String[]> tahuns = JavaPostgreSQL.readTahun();

        ArrayList<String[]> data = JavaPostgreSQL.readKelasDibuka();

        for (int i = 0; i < data.size(); i++) {
            for (int j = 0; j < data.get(i).length; j++) { //j<3
                if (j == 1) { //index class_id
                    for (int k = 0; k < classes.size(); k++) {
                        for (int l = 0; l < classes.get(k).length; l++) {
                            if (l == 0) {
                                if (Objects.equals(classes.get(k)[l], data.get(i)[j]) || classes.get(k)[l].equalsIgnoreCase(data.get(i)[j])) {
                                    data.get(i)[j] = classes.get(k)[1];
                                }
                            }
                        }
                    }
                }
            }
        }

        for (int i = 0; i < data.size(); i++) {
            for (int j = 0; j < data.get(i).length; j++) { //j<3
                if (j == 2) { //index pararel_id
                    for (int k = 0; k < pararels.size(); k++) {
                        for (int l = 0; l < pararels.get(k).length; l++) {
                            if (l == 0) {
                                if (Objects.equals(pararels.get(k)[l], data.get(i)[j]) || pararels.get(k)[l].equalsIgnoreCase(data.get(i)[j])) {
                                    data.get(i)[j] = pararels.get(k)[1];
                                }
                            }
                        }
                    }
                }
            }
        }

        for (int i = 0; i < data.size(); i++) {
            for (int j = 0; j < data.get(i).length; j++) { //j<3
                if (j == 3) { //index year_id
                    for (int k = 0; k < tahuns.size(); k++) {
                        for (int l = 0; l < tahuns.get(k).length; l++) {
                            if (l == 0) {
                                if (Objects.equals(tahuns.get(k)[l], data.get(i)[j]) || tahuns.get(k)[l].equalsIgnoreCase(data.get(i)[j])) {
                                    data.get(i)[j] = tahuns.get(k)[1];
                                }
                            }
                        }
                    }
                }
            }
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


        ArrayList<String> classesPilihan = new ArrayList<>();
        ArrayList<String> pararelsPilihan = new ArrayList<>();
        ArrayList<String> tahunsPilihan = new ArrayList<>();

        for (int i = 0; i < classes.size(); i++) {
            for (int j = 0; j < classes.get(i).length; j++) { //j<3
                if (j == 1) { //index class_name
                    classesPilihan.add(classes.get(i)[j]);
                }
            }
        }
        for (int i = 0; i < pararels.size(); i++) {
            for (int j = 0; j < pararels.get(i).length; j++) { //j<3
                if (j == 1) { //index pararels_name
                    pararelsPilihan.add(pararels.get(i)[j]);
                }
            }
        }
        for (int i = 0; i < tahuns.size(); i++) {
            for (int j = 0; j < tahuns.get(i).length; j++) { //j<3
                if (j == 1) { //index periode tahun ajaran
                    tahunsPilihan.add(tahuns.get(i)[j]);
                }
            }
        }
        class_id_input.getItems().addAll(classesPilihan);
        parallel_id_input.getItems().addAll(pararelsPilihan);
        year_id_input.getItems().addAll(tahunsPilihan);

        class_id_edit.getItems().addAll(classesPilihan);
        parallel_id_edit.getItems().addAll(pararelsPilihan);
        year_id_edit.getItems().addAll(tahunsPilihan);
    }

    public void addKelasDibuka() {
        try {
            String classId = class_id_input.getValue();
            String pararelId = parallel_id_input.getValue();
            String tahunId = year_id_input.getValue();

            if (classId == null || classId.isEmpty()) {
                // Handle error, choice box value not selected
                return;
            }
            if (pararelId == null || pararelId.isEmpty()) {
                return;
            }
            if (tahunId == null || tahunId.isEmpty()) {
                return;
            }
            ArrayList<String[]> classes = JavaPostgreSQL.readKelas();
            ArrayList<String[]> pararels = JavaPostgreSQL.readKelasPararel();
            ArrayList<String[]> tahuns = JavaPostgreSQL.readTahun();

            int masukinClass = 0;
            int masukinPararel = 0;
            int masukinTahun = 0;

            for (int i = 0; i < classes.size(); i++) {
                for (int j = 0; j < classes.get(i).length; j++) {
                    if (j == 0 && Objects.equals(classes.get(i)[1], classId)) {
                        masukinClass = Integer.parseInt(classes.get(i)[0]);
                    }
                }
            }

            for (int i = 0; i < pararels.size(); i++) {
                for (int j = 0; j < pararels.get(i).length; j++) {
                    if (j == 0 && Objects.equals(pararels.get(i)[1], pararelId)) {
                        masukinPararel = Integer.parseInt(pararels.get(i)[0]);
                    }
                }
            }

            for (int i = 0; i < tahuns.size(); i++) {
                for (int j = 0; j < tahuns.get(i).length; j++) {
                    if (j == 0 && Objects.equals(tahuns.get(i)[1], tahunId)) {
                        masukinTahun = Integer.parseInt(tahuns.get(i)[0]);
                    }
                }
            }


            JavaPostgreSQL.insertIntoKelasDibukaTable(masukinClass, masukinPararel, masukinTahun);

            refreshTable();
        } catch (Exception e) {
            System.out.println("ERROR TAMPIL LABEL " + e);
        }
    }

    private void refreshTable() {
        ArrayList<String[]> data = JavaPostgreSQL.readKelasDibuka();

        ArrayList<String[]> classes = JavaPostgreSQL.readKelas();
        ArrayList<String[]> pararels = JavaPostgreSQL.readKelasPararel();
        ArrayList<String[]> tahuns = JavaPostgreSQL.readTahun();

        for (int i = 0; i < data.size(); i++) {
            for (int j = 0; j < data.get(i).length; j++) { //j<3
                if (j == 1) { //index class_id
                    for (int k = 0; k < classes.size(); k++) {
                        for (int l = 0; l < classes.get(k).length; l++) {
                            if (l == 0) {
                                if (Objects.equals(classes.get(k)[l], data.get(i)[j]) || classes.get(k)[l].equalsIgnoreCase(data.get(i)[j])) {
                                    data.get(i)[j] = classes.get(k)[1];
                                }
                            }
                        }
                    }
                }
            }
        }

        for (int i = 0; i < data.size(); i++) {
            for (int j = 0; j < data.get(i).length; j++) { //j<3
                if (j == 2) { //index pararel_id
                    for (int k = 0; k < pararels.size(); k++) {
                        for (int l = 0; l < pararels.get(k).length; l++) {
                            if (l == 0) {
                                if (Objects.equals(pararels.get(k)[l], data.get(i)[j]) || pararels.get(k)[l].equalsIgnoreCase(data.get(i)[j])) {
                                    data.get(i)[j] = pararels.get(k)[1];
                                }
                            }
                        }
                    }
                }
            }
        }

        for (int i = 0; i < data.size(); i++) {
            for (int j = 0; j < data.get(i).length; j++) { //j<3
                if (j == 3) { //index year_id
                    for (int k = 0; k < tahuns.size(); k++) {
                        for (int l = 0; l < tahuns.get(k).length; l++) {
                            if (l == 0) {
                                if (Objects.equals(tahuns.get(k)[l], data.get(i)[j]) || tahuns.get(k)[l].equalsIgnoreCase(data.get(i)[j])) {
                                    data.get(i)[j] = tahuns.get(k)[1];
                                }
                            }
                        }
                    }
                }
            }
        }

        ObservableList<String[]> observableData = FXCollections.observableArrayList(data);
        tableView.setItems(observableData);
    }
}
