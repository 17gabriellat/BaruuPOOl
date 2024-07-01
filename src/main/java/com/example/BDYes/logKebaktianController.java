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

public class logKebaktianController extends HomeController implements Initializable {

    @FXML
    private TableView<String[]> tableView;

    @FXML
    private TableColumn<String[],String> service_log_id;
    @FXML
    private TableColumn<String[],String> sunday_service_id;
    @FXML
    private TableColumn<String[],String> child_id;
    @FXML
    private TableColumn<String[],String> status;
    @FXML
    private ChoiceBox<String> sunday_service_id_input;
    @FXML
    private ChoiceBox<String> child_id_input;
    @FXML
    private ChoiceBox<String> status_input;


    @FXML
    private VBox boxEdit;
    @FXML
    private ChoiceBox<String> sunday_service_id_edit;
    @FXML
    private ChoiceBox<String> child_id_edit;
    @FXML
    private ChoiceBox<String> status_edit;
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
            String kebaktianName = selectedRow[1];
            String anakName = selectedRow[2];
            String periode = selectedRow[3];

            sunday_service_id_edit.setValue(kebaktianName);
            child_id_edit.setValue(anakName);
            status_edit.setValue(periode.equalsIgnoreCase("Hadir") ? "Hadir" : "Tidak Hadir");;
        }
    }

    public void updateKelas() {
        String[] selectedRow = tableView.getSelectionModel().getSelectedItem();
        if (selectedRow != null) {
            int idOpen = Integer.parseInt(selectedRow[0]);

            String kebaktianName = sunday_service_id_edit.getValue();
            String anakName = child_id_edit.getValue();
            String statusName = status_edit.getValue();
            boolean status = statusName.equalsIgnoreCase("Hadir"); // True for Hadir, False for Tidak Hadir

            //cari id dari tiap nama diatas
            // METHOD E GMN
            ArrayList<String[]>kebaktianS=JavaPostgreSQL.readKebaktian();
            ArrayList<String[]>childsS=JavaPostgreSQL.readAnak();


            int masukinKebaktian =0;
            int masukinAnak=0;

            for (int i = 0; i < kebaktianS.size(); i++) {
                if (kebaktianS.get(i)[3].equalsIgnoreCase(kebaktianName)) { //
                    masukinKebaktian = Integer.parseInt(kebaktianS.get(i)[0]);
                }
            }

            for (int i = 0; i < childsS.size(); i++) {
                for (int j = 0; j < childsS.get(i).length; j++) {
                    if (j==0 && Objects.equals(childsS.get(i)[1], anakName)){
                        masukinAnak = Integer.parseInt(childsS.get(i)[0]);
                    }
                }
            }
            JavaPostgreSQL.updateLogKebaktian(masukinKebaktian,masukinAnak,status, idOpen);
            refreshTable();
            editKelas();
        }
    }

    public void deleteKelas() {
        String[] selectedRow = tableView.getSelectionModel().getSelectedItem();
        if (selectedRow != null) {
            int id = Integer.parseInt(selectedRow[0]);

            String kebaktianNAme = sunday_service_id_edit.getValue();
            String anakName = child_id_edit.getValue();
            String statusName = status_edit.getValue();
            boolean status = statusName.equalsIgnoreCase("Hadir"); // True for Hadir, False for Tidak Hadir

            //cari id dari tiap nama diatas
            // METHOD E GMN
            ArrayList<String[]>kebaktianS=JavaPostgreSQL.readKebaktian();
            ArrayList<String[]>childsS=JavaPostgreSQL.readAnak();


            int masukinKebaktian =0;
            int masukinAnak=0;

            for (int i = 0; i < kebaktianS.size(); i++) {
                if (kebaktianS.get(i)[3].equalsIgnoreCase(kebaktianNAme)) { //
                    masukinKebaktian = Integer.parseInt(kebaktianS.get(i)[0]);
                }
            }

            for (int i = 0; i < childsS.size(); i++) {
                for (int j = 0; j < childsS.get(i).length; j++) {
                    if (j==0 && Objects.equals(childsS.get(i)[1], anakName)){
                        masukinAnak = Integer.parseInt(childsS.get(i)[0]);
                    }
                }
            }

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText("Delete Confirmation");
            alert.setContentText("Apakah Sudah Yakin Ingin Menghapus Ke-" + statusName+"an dari "+anakName+" di Kebaktian  "+kebaktianNAme + " ?\nAksi Tidak Dapat Dibatalkan");

            // Menampilkan alert dan menunggu respon dari pengguna
            int finalMasukinClasse = masukinKebaktian;
            int finalMasukinPararele = masukinAnak;
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    JavaPostgreSQL.deleteLogKebaktian(finalMasukinClasse, finalMasukinPararele, status, id);
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
        service_log_id.setCellValueFactory(cellData->new SimpleStringProperty(cellData.getValue()[0]));
        sunday_service_id.setCellValueFactory(cellData->new SimpleStringProperty(cellData.getValue()[1]));
        child_id.setCellValueFactory(cellData->new SimpleStringProperty(cellData.getValue()[2]));
        status.setCellValueFactory(cellData->new SimpleStringProperty(cellData.getValue()[3]));

        ArrayList<String[]>kebaktianS=JavaPostgreSQL.readKebaktian();
        ArrayList<String[]>childs=JavaPostgreSQL.readAnak();

        ArrayList<String[]> data = JavaPostgreSQL.readLogKebaktian();

        for (int i = 0; i < data.size(); i++) {
            for (int j = 0; j < data.get(i).length; j++) { //j<3
                if (j == 1) { //index sunday_service_id
                    for (int k = 0; k < kebaktianS.size(); k++) {
                        for (int l = 0; l < kebaktianS.get(k).length; l++) {
                            if (l == 0) {
                                if (Objects.equals(kebaktianS.get(k)[l], data.get(i)[j])||kebaktianS.get(k)[l].equalsIgnoreCase(data.get(i)[j])) {
                                                                        data.get(i)[j] = kebaktianS.get(k)[3];
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
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i)[3].equalsIgnoreCase("true")){
                data.get(i)[3] = "Hadir";
            }else data.get(i)[3]="Tidak Hadir";
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


        ArrayList<String>tanggalKebaktianPilihan= new ArrayList<>();
        ArrayList<String>childsPilihan= new ArrayList<>();


        for (int i = 0; i < kebaktianS.size(); i++) {
            for (int j = 0; j <kebaktianS.get(i).length ; j++) { //j<3
                if (j==1){ //index class_id
//                    for (int k = 0; k < kelasS.size(); k++) {
//                        for (int l = 0; l < kelasS.get(k).length; l++) {
//                            if (l == 1){//index kelas nama
//                                if (kebaktianS.get(i)[j].equalsIgnoreCase(kelasS.get(k)[0])){//id class sama kaya opened kaga?
                                    tanggalKebaktianPilihan.add(kebaktianS.get(i)[3]); //masukin kelas Name sebagai pilihan
//                                }
//                            }
//                        }
//                    }
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

        sunday_service_id_input.getItems().addAll(tanggalKebaktianPilihan);
        child_id_input.getItems().addAll(childsPilihan);
        status_input.getItems().addAll("Hadir", "Tidak Hadir");

        sunday_service_id_edit.getItems().addAll(tanggalKebaktianPilihan);
        child_id_edit.getItems().addAll(childsPilihan);
        status_edit.getItems().addAll("Hadir", "Tidak Hadir");
    }
    public void addKelasAnak(){
        try {
            String kebaktianId = sunday_service_id_input.getValue(); //ambil date kebaktian
            String childId = child_id_input.getValue();
            Boolean statusId = false;

            if (Objects.equals(status_input.getValue(), "Hadir") || status_input.getValue().equalsIgnoreCase("Hadir")){
                statusId=true;
            }

            if (kebaktianId == null || kebaktianId.isEmpty()) {
                // Handle error, choice box value not selected
                return;
            }
            if (childId == null || childId.isEmpty()) {
                return;
            }

            ArrayList<String[]>kebaktianS=JavaPostgreSQL.readKebaktian();
            ArrayList<String[]>childsS=JavaPostgreSQL.readAnak();


            int masukinKebaktian =0;
            int masukinAnak=0;

            for (int i = 0; i < kebaktianS.size(); i++) {
                if (kebaktianS.get(i)[3].equalsIgnoreCase(kebaktianId)) { //
                        masukinKebaktian = Integer.parseInt(kebaktianS.get(i)[0]);
                }
            }

            for (int i = 0; i < childsS.size(); i++) {
                for (int j = 0; j < childsS.get(i).length; j++) {
                    if (j==0 && Objects.equals(childsS.get(i)[1], childId)){
                        masukinAnak = Integer.parseInt(childsS.get(i)[0]);
                    }
                }
        }


            JavaPostgreSQL.insertIntoLogKebaktianTable(masukinKebaktian, masukinAnak, statusId);

            refreshTable();
        }catch (Exception e){
            System.out.println("ERROR TAMPIL LABEL " + e);
        }
    }
    private void refreshTable() {
        ArrayList<String[]>kebaktianS=JavaPostgreSQL.readKebaktian();
        ArrayList<String[]>childs=JavaPostgreSQL.readAnak();

        ArrayList<String[]> data = JavaPostgreSQL.readLogKebaktian();

        for (int i = 0; i < data.size(); i++) {
            for (int j = 0; j < data.get(i).length; j++) { //j<3
                if (j == 1) { //index sunday_service_id
                    for (int k = 0; k < kebaktianS.size(); k++) {
                        for (int l = 0; l < kebaktianS.get(k).length; l++) {
                            if (l == 0) {
                                if (Objects.equals(kebaktianS.get(k)[l], data.get(i)[j])||kebaktianS.get(k)[l].equalsIgnoreCase(data.get(i)[j])) {
                                    data.get(i)[j] = kebaktianS.get(k)[3];
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
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i)[3].equalsIgnoreCase("true")){
                data.get(i)[3] = "Hadir";
            }else data.get(i)[3]="Tidak Hadir";
        }
        ObservableList<String[]> observableData = FXCollections.observableArrayList(data);
        tableView.setItems(observableData);
    }
}
