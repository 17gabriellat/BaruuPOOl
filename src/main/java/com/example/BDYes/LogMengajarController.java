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

public class LogMengajarController extends HomeController implements Initializable {

    @FXML
    private TableView<String[]> tableView;

    @FXML
    private TableColumn<String[],String> teach_log_id;
    @FXML
    private TableColumn<String[],String> opened_id;
    @FXML
    private TableColumn<String[],String> teacher_id;
    @FXML
    private ChoiceBox<String> opened_id_input;
    @FXML
    private ChoiceBox<String> teacher_id_input;

    @FXML
    private VBox boxEdit;
    @FXML
    private ChoiceBox<String> opened_id_edit;
    @FXML
    private ChoiceBox<String> teacher_id_edit;
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
            String namaGuru = selectedRow[2];

            opened_id_edit.setValue(namaKelas);
            teacher_id_edit.setValue(namaGuru);
        }
    }

    public void updateLogMengajarGuru() {
        String[] selectedRow = tableView.getSelectionModel().getSelectedItem();
        if (selectedRow != null) {
            int idOpen = Integer.parseInt(selectedRow[0]);

            String kelasName = opened_id_edit.getValue();
            String guruName = teacher_id_edit.getValue();

            //cari id dari tiap nama diatas
            // METHOD E GMN
            ArrayList<String[]>openedS=JavaPostgreSQL.readKelasDibuka();
            ArrayList<String[]>teacherS=JavaPostgreSQL.readGuru();
            ArrayList<String[]>kelasS=JavaPostgreSQL.readKelas();

            int masukinOpen =-1;
            int masukinTeacher=-1;


            for (int i = 0; i < kelasS.size(); i++) {
                if (Objects.equals(kelasS.get(i)[1], kelasName)){ //nama_kelas apakah sama dengan input yang diterima
                    for (int j = 0; j < openedS.size(); j++) {
                        if (kelasS.get(i)[0].equalsIgnoreCase(openedS.get(j)[1])){
                            masukinOpen = Integer.parseInt(openedS.get(j)[0]);
                        }
                    }
                }

            }

            for (int i = 0; i < teacherS.size(); i++) {
                for (int j = 0; j < teacherS.get(i).length; j++) {
                    if (Objects.equals(teacherS.get(i)[1], guruName)){
                        masukinTeacher = Integer.parseInt(teacherS.get(i)[0]);
                    }
                }
            }

            JavaPostgreSQL.updateLogMengajarGuru(masukinOpen,masukinTeacher, idOpen);
            refreshTable();
            editKelas();
        }
    }

    public void deleteLogMengajarGuru() {
        String[] selectedRow = tableView.getSelectionModel().getSelectedItem();
        if (selectedRow != null) {
            int id = Integer.parseInt(selectedRow[0]);
            String kelasName = selectedRow[1];
            String guruName = selectedRow[2];

            ArrayList<String[]>openedS=JavaPostgreSQL.readKelasDibuka();
            ArrayList<String[]>teacherS=JavaPostgreSQL.readGuru();
            ArrayList<String[]>kelasS=JavaPostgreSQL.readKelas();

            int masukinOpen =-1;
            int masukinTeacher=-1;


            for (int i = 0; i < kelasS.size(); i++) {
                if (Objects.equals(kelasS.get(i)[1], kelasName)){ //nama_kelas apakah sama dengan input yang diterima
                    for (int j = 0; j < openedS.size(); j++) {
                        if (kelasS.get(i)[0].equalsIgnoreCase(openedS.get(j)[1])){
                            masukinOpen = Integer.parseInt(openedS.get(j)[0]);
                        }
                    }
                }

            }

            for (int i = 0; i < teacherS.size(); i++) {
                for (int j = 0; j < teacherS.get(i).length; j++) {
                    if (Objects.equals(teacherS.get(i)[1], guruName)){
                        masukinTeacher = Integer.parseInt(teacherS.get(i)[0]);
                    }
                }
            }


            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText("Delete Confirmation");
            alert.setContentText("Apakah Sudah Yakin Ingin Menghapus Log " + guruName+" saat Mengajar di "+kelasName+" ?\nAksi Tidak Dapat Dibatalkan");

            // Menampilkan alert dan menunggu respon dari pengguna
            int finalMasukinClasse = masukinOpen;
            int finalMasukinPararele = masukinTeacher;
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    JavaPostgreSQL.deleteLogMengajarGuru(finalMasukinClasse, finalMasukinPararele, id);
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
        teach_log_id.setCellValueFactory(cellData->new SimpleStringProperty(cellData.getValue()[0]));
        opened_id.setCellValueFactory(cellData->new SimpleStringProperty(cellData.getValue()[1]));
        teacher_id.setCellValueFactory(cellData->new SimpleStringProperty(cellData.getValue()[2]));

        ArrayList<String[]>openedS=JavaPostgreSQL.readKelasDibuka();
        ArrayList<String[]>teacherS=JavaPostgreSQL.readGuru();
        ArrayList<String[]>kelasS=JavaPostgreSQL.readKelas();

        ArrayList<String[]> data = JavaPostgreSQL.readLogMengajarGuru();

        for (int i = 0; i < data.size(); i++) {
            for (int j = 0; j < data.get(i).length; j++) { //j<3
                if (j == 1) { //index opened_id
                    for (int k = 0; k < openedS.size(); k++) {
                        for (int l = 0; l < openedS.get(k).length; l++) {
                            if (l == 0) {
                                if (Objects.equals(openedS.get(k)[l], data.get(i)[j]) || openedS.get(k)[l].equalsIgnoreCase(data.get(i)[j])) {
                                    for (int m = 0; m < kelasS.size(); m++) {
                                        for (int n = 0; n < kelasS.get(m).length; n++) {
                                            if (openedS.get(k)[1].equalsIgnoreCase(kelasS.get(m)[0])){//jika class_id ada di kelasBuka
                                                data.get(i)[j] = kelasS.get(m)[1]; //ambil nama kelas
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        for (int i = 0; i < data.size(); i++) {
            for (int j = 0; j < data.get(i).length; j++) { //j<3
                if (j == 2) { //index teacher_id
                    for (int k = 0; k < teacherS.size(); k++) {
                        for (int l = 0; l < teacherS.get(k).length; l++) {
                            if (l == 0) {//index teacher_id di Teachers
                                if (Objects.equals(teacherS.get(k)[l], data.get(i)[j]) || teacherS.get(k)[l].equalsIgnoreCase(data.get(i)[j])) {
                                    data.get(i)[j] = teacherS.get(k)[1];
                                }
                            }
                        }
                    }
                }
            }
        }

        // Convert ArrayList<String[]> to ObservableList<String[]>
        ObservableList<String[]> observableData = FXCollections.observableArrayList(data);
        tableView.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() == 1) { // Single click
                if (editShow) {
                    getSelectedRowValues();
                }
            }
        });
        tableView.setItems(observableData);


        ArrayList<String>openedPilihan= new ArrayList<>();
        ArrayList<String>teacherPilihan= new ArrayList<>();

        for (int i = 0; i < openedS.size(); i++) {
            for (int j = 0; j <openedS.get(i).length ; j++) { //j<3
                if (j==1){ //index class_id
                    for (int k = 0; k < kelasS.size(); k++) {
                        for (int l = 0; l < kelasS.get(k).length; l++) {
                            if (l == 0) { //index kelas_id
                                if (openedS.get(i)[j].equalsIgnoreCase(kelasS.get(k)[l] )){
                                    openedPilihan.add(kelasS.get(k)[1]); //ambil kelas_name
                                }
                            }
                        }
                    }
                }
            }
        }
        for (int i = 0; i < teacherS.size(); i++) {
            for (int j = 0; j <teacherS.get(i).length ; j++) { //j<3
                if (j==1){ //index teacher_name
                    teacherPilihan.add(teacherS.get(i)[j]);
                }
            }
        }

        opened_id_input.getItems().addAll(openedPilihan);
        teacher_id_input.getItems().addAll(teacherPilihan);

        opened_id_edit.getItems().addAll(openedPilihan);
        teacher_id_edit.getItems().addAll(teacherPilihan);
    }
    public void addLogMengajar(){
        try {
            String openId = opened_id_input.getValue(); //nama Kelas yang opened
            String teacherId = teacher_id_input.getValue(); // teacher_name

            if (openId == null || openId.isEmpty()) {
                // Handle error, choice box value not selected
                return;
            }
            if (teacherId == null || teacherId.isEmpty()) {
                return;
            }

            ArrayList<String[]>openedS=JavaPostgreSQL.readKelasDibuka();
            ArrayList<String[]>teacherS=JavaPostgreSQL.readGuru();
            ArrayList<String[]>kelasS=JavaPostgreSQL.readKelas();

            int masukinOpen =-1;
            int masukinTeacher=-1;


            for (int i = 0; i < kelasS.size(); i++) {
                    if (Objects.equals(kelasS.get(i)[1], openId)){ //nama_kelas apakah sama dengan input yang diterima
                        for (int j = 0; j < openedS.size(); j++) {
                            if (kelasS.get(i)[0].equalsIgnoreCase(openedS.get(j)[1])){
                                masukinOpen = Integer.parseInt(openedS.get(j)[0]);
                            }
                        }
                    }

            }

            for (int i = 0; i < teacherS.size(); i++) {
                for (int j = 0; j < teacherS.get(i).length; j++) {
                    if (Objects.equals(teacherS.get(i)[1], teacherId)){
                        masukinTeacher = Integer.parseInt(teacherS.get(i)[0]);
                    }
                }
        }

            JavaPostgreSQL.insertIntoLogMengajarGuruTable(masukinOpen, masukinTeacher);

            refreshTable();
        }catch (Exception e){
            System.out.println("ERROR TAMPIL LABEL " + e);
        }
    }
    private void refreshTable() {
        ArrayList<String[]>openedS=JavaPostgreSQL.readKelasDibuka();
        ArrayList<String[]>teacherS=JavaPostgreSQL.readGuru();
        ArrayList<String[]>kelasS=JavaPostgreSQL.readKelas();

        ArrayList<String[]> data = JavaPostgreSQL.readLogMengajarGuru();

        for (int i = 0; i < data.size(); i++) {
            for (int j = 0; j < data.get(i).length; j++) { //j<3
                if (j == 1) { //index opened_id
                    for (int k = 0; k < openedS.size(); k++) {
                        for (int l = 0; l < openedS.get(k).length; l++) {
                            if (l == 0) {
                                if (Objects.equals(openedS.get(k)[l], data.get(i)[j]) || openedS.get(k)[l].equalsIgnoreCase(data.get(i)[j])) {
                                    for (int m = 0; m < kelasS.size(); m++) {
                                        for (int n = 0; n < kelasS.get(m).length; n++) {
                                            if (openedS.get(k)[1].equalsIgnoreCase(kelasS.get(m)[0])){//jika class_id ada di kelasBuka
                                                data.get(i)[j] = kelasS.get(m)[1]; //ambil nama kelas
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        for (int i = 0; i < data.size(); i++) {
            for (int j = 0; j < data.get(i).length; j++) { //j<3
                if (j == 2) { //index teacher_id
                    for (int k = 0; k < teacherS.size(); k++) {
                        for (int l = 0; l < teacherS.get(k).length; l++) {
                            if (l == 0) {//index teacher_id di Teachers
                                if (Objects.equals(teacherS.get(k)[l], data.get(i)[j]) || teacherS.get(k)[l].equalsIgnoreCase(data.get(i)[j])) {
                                    data.get(i)[j] = teacherS.get(k)[1];
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
