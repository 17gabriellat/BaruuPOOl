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

public class KelasAnakController extends HomeController implements Initializable {

    @FXML
    private TableView<String[]> tableView;

    @FXML
    private TableColumn<String[],String> opened_id;
    @FXML
    private TableColumn<String[],String> child_class_id;
    @FXML
    private TableColumn<String[],String> child_id;

    @FXML
    private ChoiceBox<String> opened_id_input;
    @FXML
    private ChoiceBox<String> child_id_input;


    @FXML
    private VBox boxEdit;
    @FXML
    private ChoiceBox<String> opened_id_edit;
    @FXML
    private ChoiceBox<String> child_id_edit;
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
            String openedNama = selectedRow[1];
            String anakNama = selectedRow[2];

            opened_id_edit.setValue(openedNama);
            child_id_edit.setValue(anakNama);
        }
    }

    public void updateKelas() {
        String[] selectedRow = tableView.getSelectionModel().getSelectedItem();
        if (selectedRow != null) {
            int idClassAnak = Integer.parseInt(selectedRow[0]);

            String openName = opened_id_edit.getValue();
            String anakName = child_id_edit.getValue();

            //cari id dari tiap nama diatas
            // METHOD E GMN
            ArrayList<String[]>openedsS=JavaPostgreSQL.readKelasDibuka();
            ArrayList<String[]>childsS=JavaPostgreSQL.readAnak();
            ArrayList<String[]>kelasS = JavaPostgreSQL.readKelas();

            int masukinOpened =0;
            int masukinAnak=0;

            for (int i = 0; i < openedsS.size(); i++) {
                for (int j = 0; j < openedsS.get(i).length; j++) {
                    if (j == 0) {
                        for (int k = 0; k < kelasS.size(); k++) {
                            for (int l = 0; l < kelasS.get(k).length; l++) {
                                if (l == 1) { //index kelas_nama
                                    if (kelasS.get(k)[l].equalsIgnoreCase(openName)) {
                                        if (openedsS.get(i)[1].equalsIgnoreCase(kelasS.get(k)[0])) { //ambil id kelas yang ketemu, cek dengan id class di opened
                                            masukinOpened = Integer.parseInt(openedsS.get(i)[0]); //ambil kelas_id dari nama_kelas yang dipilih
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            for (int i = 0; i < childsS.size(); i++) {
                for (int j = 0; j < childsS.get(i).length; j++) {
                    if (j==0 && Objects.equals(childsS.get(i)[1], anakName)){
                        masukinAnak = Integer.parseInt(childsS.get(i)[0]);
                    }
                }
            }
            JavaPostgreSQL.updateKelasAnak(masukinOpened,masukinAnak, idClassAnak);
            refreshTable();
            editKelas();
        }
    }

    public void deleteKelas() {
        String[] selectedRow = tableView.getSelectionModel().getSelectedItem();
        if (selectedRow != null) {
            int id = Integer.parseInt(selectedRow[0]);
            String openName = selectedRow[1];
            String anakNam = selectedRow[2];

            ArrayList<String[]>openedsS=JavaPostgreSQL.readKelasDibuka();
            ArrayList<String[]>childsS=JavaPostgreSQL.readAnak();
            ArrayList<String[]>kelasS = JavaPostgreSQL.readKelas();

            int masukinOpened =0;
            int masukinAnak=0;

            for (int i = 0; i < openedsS.size(); i++) {
                for (int j = 0; j < openedsS.get(i).length; j++) {
                    if (j == 0) {
                        for (int k = 0; k < kelasS.size(); k++) {
                            for (int l = 0; l < kelasS.get(k).length; l++) {
                                if (l == 1) { //index kelas_nama
                                    if (kelasS.get(k)[l].equalsIgnoreCase(openName)) {
                                        if (openedsS.get(i)[1].equalsIgnoreCase(kelasS.get(k)[0])) { //ambil id kelas yang ketemu, cek dengan id class di opened
                                            masukinOpened = Integer.parseInt(openedsS.get(i)[0]); //ambil kelas_id dari nama_kelas yang dipilih
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            for (int i = 0; i < childsS.size(); i++) {
                for (int j = 0; j < childsS.get(i).length; j++) {
                    if (j==0 && Objects.equals(childsS.get(i)[1], anakNam)){
                        masukinAnak = Integer.parseInt(childsS.get(i)[0]);
                    }
                }
            }

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText("Delete Confirmation");
            alert.setContentText("Apakah Sudah Yakin Ingin Menghapus Anak " + anakNam+" dari Kelas "+openName + " ?\nAksi Tidak Dapat Dibatalkan");

            // Menampilkan alert dan menunggu respon dari pengguna
            int finalMasukinClasse = masukinOpened;
            int finalMasukinPararele = masukinAnak;
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    JavaPostgreSQL.deleteKelasAnak(finalMasukinClasse, finalMasukinPararele, id);
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
        child_class_id.setCellValueFactory(cellData->new SimpleStringProperty(cellData.getValue()[0]));
        opened_id.setCellValueFactory(cellData->new SimpleStringProperty(cellData.getValue()[1]));
        child_id.setCellValueFactory(cellData->new SimpleStringProperty(cellData.getValue()[2]));

        ArrayList<String[]>openeds=JavaPostgreSQL.readKelasDibuka();
        ArrayList<String[]>childs=JavaPostgreSQL.readAnak();
        ArrayList<String[]> kelasS = JavaPostgreSQL.readKelas();

        ArrayList<String[]> data = JavaPostgreSQL.readKelasAnak();

        for (int i = 0; i < data.size(); i++) {
            for (int j = 0; j < data.get(i).length; j++) { //j<3
                if (j == 1) { //index opened_id
                    for (int k = 0; k < openeds.size(); k++) {
                        for (int l = 0; l < openeds.get(k).length; l++) {
                            if (l == 0) {
                                if (Objects.equals(openeds.get(k)[l], data.get(i)[j])||openeds.get(k)[l].equalsIgnoreCase(data.get(i)[j])) {
//                                    data.get(i)[j] = openeds.get(k)[1];
                                    for (int m = 0; m < kelasS.size(); m++) {
                                        for (int n = 0; n < kelasS.get(m).length; n++) {
                                            if (n == 0) { //kelas_id apakah sama dengan open -> class_id
                                                if (Objects.equals(kelasS.get(m)[n], openeds.get(k)[1])||kelasS.get(m)[n].equalsIgnoreCase(openeds.get(k)[1])) {
                                                    data.get(i)[j] = kelasS.get(m)[1];
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
        }

        for (int i = 0; i < data.size(); i++) {
            for (int j = 0; j < data.get(i).length; j++) { //j<3
                if (j == 2) { //index child_id
                    for (int k = 0; k < childs.size(); k++) {
                        for (int l = 0; l < childs.get(k).length; l++) {
                            if (l == 0) {
                                if (Objects.equals(childs.get(k)[l], data.get(i)[j]) || childs.get(k)[l].equalsIgnoreCase(data.get(i)[j])) {
                                    data.get(i)[j] = childs.get(k)[1];
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


        ArrayList<String>openedsPilihan= new ArrayList<>();
        ArrayList<String>childsPilihan= new ArrayList<>();


        for (int i = 0; i < openeds.size(); i++) {
            for (int j = 0; j <openeds.get(i).length ; j++) { //j<3
                if (j==1){ //index class_id
                    for (int k = 0; k < kelasS.size(); k++) {
                        for (int l = 0; l < kelasS.get(k).length; l++) {
                            if (l == 1){//index kelas nama
                                if (openeds.get(i)[j].equalsIgnoreCase(kelasS.get(k)[0])){//id class sama kaya opened kaga?
                                    openedsPilihan.add(kelasS.get(k)[1]); //masukin kelas Name sebagai pilihan
                                }
                            }
                        }
                    }
                }
                if (j==2){
                    // Pararel Name e GMN ?
                }
            }
        }
        for (int i = 0; i < childs.size(); i++) {
            for (int j = 0; j <childs.get(i).length ; j++) { //j<3
                if (j==1){ //index childs_name
                    childsPilihan.add(childs.get(i)[j]);
                }
            }
        }

        opened_id_input.getItems().addAll(openedsPilihan);
        child_id_input.getItems().addAll(childsPilihan);
        
        opened_id_edit.getItems().addAll(openedsPilihan);
        child_id_edit.getItems().addAll(childsPilihan);
    }
    public void addKelasAnak(){
        try {
            String openedId = opened_id_input.getValue();
            String childId = child_id_input.getValue();

            if (openedId == null || openedId.isEmpty()) {
                // Handle error, choice box value not selected
                return;
            }
            if (childId == null || childId.isEmpty()) {
                return;
            }

            ArrayList<String[]>openedsS=JavaPostgreSQL.readKelasDibuka();
            ArrayList<String[]>childsS=JavaPostgreSQL.readAnak();
            ArrayList<String[]>kelasS = JavaPostgreSQL.readKelas();

            int masukinOpened =0;
            int masukinAnak=0;

            for (int i = 0; i < openedsS.size(); i++) {
                for (int j = 0; j < openedsS.get(i).length; j++) {
                    if (j == 0) {
                        for (int k = 0; k < kelasS.size(); k++) {
                            for (int l = 0; l < kelasS.get(k).length; l++) {
                                if (l == 1) { //index kelas_nama
                                    if (kelasS.get(k)[l].equalsIgnoreCase(openedId)) {
                                        if (openedsS.get(i)[1].equalsIgnoreCase(kelasS.get(k)[0])) { //ambil id kelas yang ketemu, cek dengan id class di opened
                                            masukinOpened = Integer.parseInt(openedsS.get(i)[0]); //ambil kelas_id dari nama_kelas yang dipilih
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            for (int i = 0; i < childsS.size(); i++) {
                for (int j = 0; j < childsS.get(i).length; j++) {
                    if (j==0 && Objects.equals(childsS.get(i)[1], childId)){
                        masukinAnak = Integer.parseInt(childsS.get(i)[0]);
                    }
                }
        }


            JavaPostgreSQL.insertIntoKelasAnakTable(masukinOpened, masukinAnak);

            refreshTable();
        }catch (Exception e){
            System.out.println("ERRO1R TAMPIL LABEL " + e);
        }
    }
    private void refreshTable() {
        ArrayList<String[]>openeds=JavaPostgreSQL.readKelasDibuka();
        ArrayList<String[]>childs=JavaPostgreSQL.readAnak();
        ArrayList<String[]> kelasS = JavaPostgreSQL.readKelas();

        ArrayList<String[]> data = JavaPostgreSQL.readKelasAnak();

        for (int i = 0; i < data.size(); i++) {
            for (int j = 0; j < data.get(i).length; j++) { //j<3
                if (j == 1) { //index opened_id
                    for (int k = 0; k < openeds.size(); k++) {
                        for (int l = 0; l < openeds.get(k).length; l++) {
                            if (l == 0) {
                                if (Objects.equals(openeds.get(k)[l], data.get(i)[j])||openeds.get(k)[l].equalsIgnoreCase(data.get(i)[j])) {
//                                    data.get(i)[j] = openeds.get(k)[1];
                                    for (int m = 0; m < kelasS.size(); m++) {
                                        for (int n = 0; n < kelasS.get(m).length; n++) {
                                            if (n == 0) { //kelas_id apakah sama dengan open -> class_id
                                                if (Objects.equals(kelasS.get(m)[n], openeds.get(k)[1])||kelasS.get(m)[n].equalsIgnoreCase(openeds.get(k)[1])) {
                                                    data.get(i)[j] = kelasS.get(m)[1];
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
        }

        for (int i = 0; i < data.size(); i++) {
            for (int j = 0; j < data.get(i).length; j++) { //j<3
                if (j == 2) { //index child_id
                    for (int k = 0; k < childs.size(); k++) {
                        for (int l = 0; l < childs.get(k).length; l++) {
                            if (l == 0) {
                                if (Objects.equals(childs.get(k)[l], data.get(i)[j]) || childs.get(k)[l].equalsIgnoreCase(data.get(i)[j])) {
                                    data.get(i)[j] = childs.get(k)[1];
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
