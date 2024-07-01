package com.example.BDYes;

import java.sql.*;
import java.util.ArrayList;

public class JavaPostgreSQL {
    // Constants for database connection
    private static final String URL = "jdbc:postgresql://localhost:5432/proyekBD";
    private static final String USER = "postgres";
    private static final String PASSWORD = "Tanaka5768";

    // Method to get a database connection
    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void insertIntoKelasTable(String class_name, boolean class_status) {
        String query = "INSERT INTO classes(class_name, class_status) VALUES (?, ?)";

        try (Connection conn = getConnection();

             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, class_name);
            pstmt.setBoolean(2, class_status);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("SQL exception occurred");
            e.printStackTrace();
        }
    }

    public static void insertIntoAnakTable(String child_name, boolean gender, Date birth_date, String guardian_name, String guardian_telephone_number, String address) {
        String query = "INSERT INTO childs(child_name, gender, birth_date, guardian_name, guardian_telephone_number, address) VALUES(?, ?, ?, ?, ?, ?)";
        try
                (
                        Connection con = getConnection();
                        PreparedStatement pstmt = con.prepareStatement(query)
                ) {
            pstmt.setString(1, child_name);
            pstmt.setBoolean(2, gender);
            pstmt.setDate(3, birth_date);
            pstmt.setString(4, guardian_name);
            pstmt.setString(5, guardian_telephone_number);
            pstmt.setString(6, address);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("SQL exception occured");
            e.printStackTrace();
        }
    }

    public static void insertIntoKelasPararelTable(String parallel_name) {
        String query = "INSERT INTO parallels(parallel_name) VALUES (?)";

        try (Connection conn = getConnection();

             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, parallel_name);
            pstmt.executeUpdate();
            System.out.println("Insertion successful!");
        } catch (SQLException e) {
            System.out.println("SQL exception occurred");
            e.printStackTrace();
        }
    }

    public static void insertIntoTahunTable(String year_period, boolean semester) {
        String query = "INSERT INTO years(year_period, semester) VALUES (?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, year_period);
            pstmt.setBoolean(2, semester);
            pstmt.executeUpdate();
            System.out.println("Insertion successful!");
        } catch (SQLException e) {
            System.out.println("SQL exception occurred");
            e.printStackTrace();
        }
    }

    public static void insertIntoGuruTable(String teacher_name, boolean gender, String teacher_telephone_number, Date started_at) {
        String query = "INSERT INTO teachers(teacher_name, gender, teacher_telephone_number, started_at) VALUES(?, ?, ?, ?)";
        try
                (
                        Connection con = getConnection();
                        PreparedStatement pstmt = con.prepareStatement(query)
                ) {
            pstmt.setString(1, teacher_name);
            pstmt.setBoolean(2, gender);
            pstmt.setString(3, teacher_telephone_number);
            pstmt.setDate(4, started_at);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("SQL exception occured");
            e.printStackTrace();
        }
    }

    public static void insertIntoKebaktianTable(String service_theme, String service_type, Date service_date) {
        String query = "INSERT INTO sunday_services(service_date, service_type, service_theme) VALUES(?, ?, ?)";
        try
                (
                        Connection con = getConnection();
                        PreparedStatement pstmt = con.prepareStatement(query)
                ) {
            pstmt.setDate(1, service_date);
            pstmt.setString(2, service_type);
            pstmt.setString(3, service_theme);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("SQL exception occurred");
            e.printStackTrace();
        }
    }

    public static ArrayList<String[]> readAnak() {
        ArrayList<String[]> anaks = new ArrayList<>();
        ResultSet rs = null;
        String query = "SELECT * FROM childs ORDER BY child_id";
        try (Connection con = getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            rs = pstmt.executeQuery();
            while (rs.next()) {
                String[] temp = new String[7];
                temp[0] = String.valueOf(rs.getInt("child_id"));
                temp[1] = rs.getString("child_name");
                temp[2] = String.valueOf(rs.getBoolean("gender"));
                temp[3] = rs.getDate("birth_date").toString();
                temp[4] = rs.getString("guardian_name");
                temp[5] = rs.getString("guardian_telephone_number");
                temp[6] = rs.getString("address");
                anaks.add(temp);
            }
        } catch (SQLException e) {
            System.out.println("SQL exception occurred: " + e.getMessage());
        }
        return anaks;
    }

    public static ArrayList<String[]> readGuru() {
        ArrayList<String[]> gurus = new ArrayList<>();
        ResultSet rs = null;
        String query = "SELECT * FROM teachers ORDER BY teacher_id";
        try (Connection con = getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            rs = pstmt.executeQuery();
            while (rs.next()) {
                String[] temp = new String[5];
                temp[0] = String.valueOf(rs.getInt("teacher_id"));
                temp[1] = rs.getString("teacher_name");
                temp[2] = String.valueOf(rs.getBoolean("gender"));
                temp[3] = rs.getString("teacher_telephone_number");
                temp[4] = rs.getDate("started_at").toString();
                gurus.add(temp);

            }
        } catch (SQLException e) {
            System.out.println("SQL exception occurred: " + e.getMessage());
        }
        return gurus;
    }

    public static ArrayList<String[]> readKebaktian() {
        ArrayList<String[]> kebaktians = new ArrayList<>();
        ResultSet rs = null;
        String query = "SELECT * FROM sunday_services ORDER BY sunday_service_id";
        try (Connection con = getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            rs = pstmt.executeQuery();
            while (rs.next()) {
                String[] temp = new String[4];
                temp[0] = String.valueOf(rs.getInt("sunday_service_id"));
                temp[1] = rs.getString("service_theme");
                temp[2] = rs.getString("service_type");
                temp[3] = rs.getDate("service_date").toString();
                kebaktians.add(temp);

            }
        } catch (SQLException e) {
            System.out.println("SQL exception occurred: " + e.getMessage());
        }
        return kebaktians;
    }

    public static ArrayList<String[]> readKelas() {
        ArrayList<String[]> kelasS = new ArrayList<>();
        ResultSet rs = null;
        String query = "SELECT * FROM classes ORDER BY class_id";
        try (Connection con = getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            rs = pstmt.executeQuery();
            while (rs.next()) {
                String[] temp = new String[3];
                temp[0] = String.valueOf(rs.getInt("class_id"));
                temp[1] = rs.getString("class_name");
                temp[2] = String.valueOf(rs.getBoolean("class_status"));
                kelasS.add(temp);
            }

        } catch (SQLException e) {
            System.out.println("SQL excemption occured: " + e.getMessage());
        }
        return kelasS;
    }

    public static ArrayList<String[]> readKelasPararel() {
        ArrayList<String[]> kelasPararelS = new ArrayList<>();
        ResultSet rs = null;
        String query = "SELECT * FROM parallels ORDER BY parallel_id";
        try (Connection con = getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            rs = pstmt.executeQuery();
            while (rs.next()) {
                String[] temp = new String[2];
                temp[0] = String.valueOf(rs.getInt("parallel_id"));
                temp[1] = rs.getString("parallel_name");
                kelasPararelS.add(temp);
            }

        } catch (SQLException e) {
            System.out.println("SQL excemption occured: " + e.getMessage());
        }
        return kelasPararelS;
    }

    public static ArrayList<String[]> readTahun() {
        ArrayList<String[]> tahuns = new ArrayList<>();
        ResultSet rs = null;
        String query = "SELECT * FROM years ORDER BY year_id";
        try (Connection con = getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            rs = pstmt.executeQuery();
            while (rs.next()) {
                String[] temp = new String[3];
                temp[0] = String.valueOf(rs.getInt("year_id"));
                temp[1] = rs.getString("year_period");
                temp[2] = String.valueOf(rs.getBoolean("semester"));
                tahuns.add(temp);
            }

        } catch (SQLException e) {
            System.out.println("SQL excemption occured: " + e.getMessage());
        }
        return tahuns;
    }

    // Intermediate Classes/Tables
    public static void insertIntoKelasDibukaTable(int class_id, int parallel_id, int year_id) {
        String query = "INSERT INTO class_opened(class_id, parallel_id, year_id) VALUES(?, ?, ?)";
        try (
                Connection con = getConnection();
                PreparedStatement pstmt = con.prepareStatement(query);
        ){
            pstmt.setInt(1,class_id);
            pstmt.setInt(2,parallel_id);
            pstmt.setInt(3,year_id);
            pstmt.executeUpdate();
            System.out.println("Insertion successful!");
        } catch (SQLException e) {
            System.out.println("SQL ERROR: " + e);
            e.printStackTrace();
        }
    }

    public static ArrayList<String[]> readKelasDibuka() {
        ArrayList<String[]> kelasDibukas = new ArrayList<>();
        ResultSet rs = null;
        String query = "SELECT * FROM class_opened ORDER BY opened_id";
        try (Connection con = getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {

            rs = pstmt.executeQuery();
            while (rs.next()) {
                String[] temp = new String[4];
                temp[0] = String.valueOf(rs.getInt("opened_id"));
                temp[1] = String.valueOf(rs.getInt("class_id"));
                temp[2] = String.valueOf(rs.getInt("parallel_id"));
                temp[3] = String.valueOf(rs.getInt("year_id"));
                kelasDibukas.add(temp);
            }

        } catch (SQLException e) {
            System.out.println("SQL excemption occured: " + e.getMessage());
        }
        return kelasDibukas;
    }
    public static void insertIntoKelasAnakTable(int opened_id, int child_id) {
        String query = "INSERT INTO child_classes(opened_id, child_id) VALUES(?, ?)";
        try (
                Connection con = getConnection();
                PreparedStatement pstmt = con.prepareStatement(query);
        ){
            pstmt.setInt(1,opened_id);
            pstmt.setInt(2,child_id);
            pstmt.executeUpdate();
            System.out.println("Insertion successful!");
        } catch (SQLException e) {
            System.out.println("SQL ERROR: " + e);
            e.printStackTrace();
        }
    }

    public static ArrayList<String[]> readKelasAnak() {
        ArrayList<String[]> kelasDibukas = new ArrayList<>();
        ResultSet rs = null;
        String query = "SELECT * FROM child_classes ORDER BY child_class_id";
        try (Connection con = getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {

            rs = pstmt.executeQuery();
            while (rs.next()) {
                String[] temp = new String[3];
                temp[0] = String.valueOf(rs.getInt("child_class_id"));
                temp[1] = String.valueOf(rs.getInt("opened_id"));
                temp[2] = String.valueOf(rs.getInt("child_id"));
                kelasDibukas.add(temp);
            }

        } catch (SQLException e) {
            System.out.println("SQL excemption occured: " + e.getMessage());
        }
        return kelasDibukas;
    }
    public static void insertIntoLogMengajarGuruTable(int opened_id, int teacher_id) {
        String query = "INSERT INTO teach_log(opened_id, teacher_id) VALUES(?, ?)";
        try (
                Connection con = getConnection();
                PreparedStatement pstmt = con.prepareStatement(query);
        ){
            pstmt.setInt(1,opened_id);
            pstmt.setInt(2,teacher_id);
            pstmt.executeUpdate();
            System.out.println("Insertion successful!");
        } catch (SQLException e) {
            System.out.println("SQL ERROR: " + e);
            e.printStackTrace();
        }
    }

    public static ArrayList<String[]> readLogMengajarGuru() {
        ArrayList<String[]> logMengajars = new ArrayList<>();
        ResultSet rs = null;
        String query = "SELECT * FROM teach_log ORDER BY teach_log_id";
        try (Connection con = getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {

            rs = pstmt.executeQuery();
            while (rs.next()) {
                String[] temp = new String[3];
                temp[0] = String.valueOf(rs.getInt("teach_log_id"));
                temp[1] = String.valueOf(rs.getInt("opened_id"));
                temp[2] = String.valueOf(rs.getInt("teacher_id"));
                logMengajars.add(temp);
            }

        } catch (SQLException e) {
            System.out.println("SQL excemption occured: " + e.getMessage());
        }
        return logMengajars;
    }
    public static void insertIntoLogKebaktianTable(int sunday_service_id, int child_id, boolean status) {
        String query = "INSERT INTO service_log(sunday_service_id, child_id, status) VALUES(?, ?, ?)";
        try (
                Connection con = getConnection();
                PreparedStatement pstmt = con.prepareStatement(query);
        ){
            pstmt.setInt(1,sunday_service_id);
            pstmt.setInt(2,child_id);
            pstmt.setBoolean(3,status);

            pstmt.executeUpdate();
            System.out.println("Insertion successful!");
        } catch (SQLException e) {
            System.out.println("SQL ERROR: " + e);
            e.printStackTrace();
        }
    }

    public static ArrayList<String[]> readLogKebaktian() {
        ArrayList<String[]> logMengajars = new ArrayList<>();
        ResultSet rs = null;
        String query = "SELECT * FROM service_log ORDER BY service_log_id";
        try (Connection con = getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {

            rs = pstmt.executeQuery();
            while (rs.next()) {
                String[] temp = new String[4];
                temp[0] = String.valueOf(rs.getInt("service_log_id"));
                temp[1] = String.valueOf(rs.getInt("sunday_service_id"));
                temp[2] = String.valueOf(rs.getInt("child_id"));
                temp[3] = String.valueOf(rs.getBoolean("status"));
                logMengajars.add(temp);
            }

        } catch (SQLException e) {
            System.out.println("SQL excemption occured: " + e.getMessage());
        }
        return logMengajars;
    }
    public static void updateAnak(String child_name, boolean gender, Date birth_date, String guardian_name, String guardian_telephone_number, String address, int id){
        String query = "UPDATE childs SET child_name = ?, gender = ?, birth_date = ?, guardian_name = ?, guardian_telephone_number = ?, address = ? WHERE child_id = ?";
        try (Connection con = getConnection(); PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, child_name);
            pstmt.setBoolean(2, gender);
            pstmt.setDate(3, birth_date);
            pstmt.setString(4, guardian_name);
            pstmt.setString(5, guardian_telephone_number);
            pstmt.setString(6, address);
            pstmt.setInt(7, id);

            pstmt.executeUpdate();

            System.out.println("Updated");
        } catch (SQLException e) {
            System.out.println("error : " + e);
            e.printStackTrace();
        }
    }
    public static void deleteAnak(String child_name, boolean gender, Date birth_date, String guardian_name, String guardian_telephone_number, String address, int id){
        String query = "DELETE FROM childs WHERE child_name = ? AND gender = ? AND birth_date = ? AND guardian_name = ? AND guardian_telephone_number = ? AND address = ? AND child_id = ?";
        try (Connection con = getConnection(); PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, child_name);
            pstmt.setBoolean(2, gender);
            pstmt.setDate(3, birth_date);
            pstmt.setString(4, guardian_name);
            pstmt.setString(5, guardian_telephone_number);
            pstmt.setString(6, address);
            pstmt.setInt(7, id);

            pstmt.executeUpdate();

            System.out.println("Deleted");
        } catch (SQLException e) {
            System.out.println("error : " + e);
            e.printStackTrace();
        }
    }
    public static void updateGuru(String teacher_name, boolean gender, Date started_at, String teacher_telephone_number, int teacher_id){
        String query = "UPDATE teachers SET teacher_name = ?, gender = ?, started_at = ?, teacher_telephone_number = ? WHERE teacher_id = ?";
        try (Connection con = getConnection(); PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, teacher_name);
            pstmt.setBoolean(2, gender);
            pstmt.setDate(3, started_at);
            pstmt.setString(4, teacher_telephone_number);
            pstmt.setInt(5, teacher_id);

            pstmt.executeUpdate();

            System.out.println("Updated");
        } catch (SQLException e) {
            System.out.println("error : " + e);
            e.printStackTrace();
        }
    }
    public static void deleteGuru(String teacher_name, boolean gender, Date started_at, String teacher_telephone_number, int id){
        String query = "DELETE FROM teachers WHERE teacher_name = ? AND gender = ? AND started_at = ? AND teacher_telephone_number = ? AND teacher_id = ?";
        try (Connection con = getConnection(); PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, teacher_name);
            pstmt.setBoolean(2, gender);
            pstmt.setDate(3, started_at);
            pstmt.setString(4, teacher_telephone_number);
            pstmt.setInt(5, id);

            pstmt.executeUpdate();

            System.out.println("Deleted");
        } catch (SQLException e) {
            System.out.println("error : " + e);
            e.printStackTrace();
        }
    }
    public static void updateKelas(String class_name, boolean class_status, int class_id){
        String query = "UPDATE classes SET class_name = ?, class_status = ? WHERE class_id = ?";
        try (Connection con = getConnection(); PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, class_name);
            pstmt.setBoolean(2, class_status);
            pstmt.setInt(3, class_id);

            pstmt.executeUpdate();

            System.out.println("Updated");
        } catch (SQLException e) {
            System.out.println("error : " + e);
            e.printStackTrace();
        }
    }
    public static void deleteKelas(String class_name, boolean class_status, int class_id){
        String query = "DELETE FROM classes WHERE class_name = ? AND class_status = ? AND class_id = ?";
        try (Connection con = getConnection(); PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, class_name);
            pstmt.setBoolean(2, class_status);
            pstmt.setInt(3, class_id);

            pstmt.executeUpdate();

            System.out.println("Deleted");
        } catch (SQLException e) {
            System.out.println("error : " + e);
            e.printStackTrace();
        }
    }
    public static void updateTahun(String year_period, boolean semester, int year_id){
        String query = "UPDATE years SET year_period = ?, semester = ? WHERE year_id = ?";
        try (Connection con = getConnection(); PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, year_period);
            pstmt.setBoolean(2, semester);
            pstmt.setInt(3, year_id);

            pstmt.executeUpdate();

            System.out.println("Updated");
        } catch (SQLException e) {
            System.out.println("error : " + e);
            e.printStackTrace();
        }
    }
    public static void deleteTahun(String year_period, boolean semester, int year_id){
        String query = "DELETE FROM years WHERE year_period = ? AND semester = ? AND year_id = ?";
        try (Connection con = getConnection(); PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, year_period);
            pstmt.setBoolean(2, semester);
            pstmt.setInt(3, year_id);

            pstmt.executeUpdate();

//            System.out.println("Deleted");
            System.out.println(year_period);
            System.out.println(semester);
            System.out.println(year_id);
        } catch (SQLException e) {
            System.out.println("error : " + e);
            e.printStackTrace();
        }
    }
    public static void updatePararel(String parallel_name, int class_id){
        String query = "UPDATE parallels SET parallel_name = ? WHERE parallel_id = ?";
        try (Connection con = getConnection(); PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, parallel_name);
            pstmt.setInt(2, class_id);

            pstmt.executeUpdate();

            System.out.println("Updated");
        } catch (SQLException e) {
            System.out.println("error : " + e);
            e.printStackTrace();
        }
    }
    public static void deletePararel(String parallel_name, int class_id){
        String query = "DELETE FROM parallels WHERE parallel_name = ? AND parallel_id = ?";
        try (Connection con = getConnection(); PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, parallel_name);
            pstmt.setInt(2, class_id);

            pstmt.executeUpdate();

            System.out.println("Deleted");
        } catch (SQLException e) {
            System.out.println("error : " + e);
            e.printStackTrace();
        }
    }
    public static void updateKebaktian(Date date, String service_theme,String service_type, int sunday_service_id){
        String query = "UPDATE sunday_services SET service_date = ?, service_type = ?, service_theme = ? WHERE sunday_service_id = ?";
        try (Connection con = getConnection(); PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setDate(1, date);
            pstmt.setString(2, service_type);
            pstmt.setString(3, service_theme);
            pstmt.setInt(4, sunday_service_id);

            pstmt.executeUpdate();

            System.out.println("Updated");
        } catch (SQLException e) {
            System.out.println("error : " + e);
            e.printStackTrace();
        }
    }
    public static void deleteKebaktian(Date date,String service_type,String service_theme, int sunday_service_id){
        String query = "DELETE FROM sunday_services WHERE service_date = ? AND service_type = ? AND service_theme = ? AND sunday_service_id = ?";
        try (Connection con = getConnection(); PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setDate(1, date);
            pstmt.setString(2, service_type);
            pstmt.setString(3, service_theme);
            pstmt.setInt(4, sunday_service_id);

            pstmt.executeUpdate();

            System.out.println("Deleted");
        } catch (SQLException e) {
            System.out.println("error : " + e);
            e.printStackTrace();
        }
    }
    public static void updateKelasDibuka(int class_id, int parallel_id, int year_id, int opened_id){
        String query = "UPDATE class_opened SET class_id = ?, parallel_id = ?, year_id = ? WHERE opened_id = ?";
        try (Connection con = getConnection(); PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setInt(1, class_id);
            pstmt.setInt(2, parallel_id);
            pstmt.setInt(3, year_id);
            pstmt.setInt(4, opened_id);

            pstmt.executeUpdate();

            System.out.println("Updated");
        } catch (SQLException e) {
            System.out.println("error : " + e);
            e.printStackTrace();
        }
    }
    public static void deleteKelasDibuka(int class_id, int parallel_id, int year_id, int opened_id){
        String query = "DELETE FROM class_opened WHERE class_id = ? AND parallel_id = ? AND year_id = ? AND opened_id = ?";
        try (Connection con = getConnection(); PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setInt(1, class_id);
            pstmt.setInt(2, parallel_id);
            pstmt.setInt(3, year_id);
            pstmt.setInt(4, opened_id);

            pstmt.executeUpdate();

            System.out.println("Deleted");
        } catch (SQLException e) {
            System.out.println("error : " + e);
            e.printStackTrace();
        }
    }
    public static void updateLogKebaktian(int sunday_service_id, int child_id, boolean status, int service_log_id){
        String query = "UPDATE service_log SET sunday_service_id = ?, child_id = ?, status = ? WHERE service_log_id = ?";
        try (Connection con = getConnection(); PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setInt(1, sunday_service_id);
            pstmt.setInt(2, child_id);
            pstmt.setBoolean(3, status);
            pstmt.setInt(4, service_log_id);

            pstmt.executeUpdate();

            System.out.println("Updated");
        } catch (SQLException e) {
            System.out.println("error : " + e);
            e.printStackTrace();
        }
    }
    public static void deleteLogKebaktian(int sunday_service_id, int child_id, boolean status, int service_log_id){
        String query = "DELETE FROM service_log WHERE sunday_service_id = ? AND child_id = ? AND status = ? AND service_log_id = ?";
        try (Connection con = getConnection(); PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setInt(1, sunday_service_id);
            pstmt.setInt(2, child_id);
            pstmt.setBoolean(3, status);
            pstmt.setInt(4, service_log_id);

            pstmt.executeUpdate();

            System.out.println("Deleted");
        } catch (SQLException e) {
            System.out.println("error : " + e);
            e.printStackTrace();
        }
    }
    public static void updateLogMengajarGuru(int opened_id, int teacher_id, int teach_log_id){
        String query = "UPDATE teach_log SET opened_id = ?, teacher_id = ? WHERE teach_log_id = ?";
        try (Connection con = getConnection(); PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setInt(1, opened_id);
            pstmt.setInt(2, teacher_id);
            pstmt.setInt(3, teach_log_id);

            pstmt.executeUpdate();

            System.out.println("Updated");
        } catch (SQLException e) {
            System.out.println("error : " + e);
            e.printStackTrace();
        }
    }
    public static void deleteLogMengajarGuru(int opened_id, int teacher_id, int teach_log_id){
        String query = "DELETE FROM teach_log WHERE opened_id = ? AND teacher_id = ? AND teach_log_id = ?";
        try (Connection con = getConnection(); PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setInt(1, opened_id);
            pstmt.setInt(2, teacher_id);
            pstmt.setInt(3, teach_log_id);

            pstmt.executeUpdate();

            System.out.println("Deleted");
        } catch (SQLException e) {
            System.out.println("error : " + e);
            e.printStackTrace();
        }
    }
    public static void updateKelasAnak(int opened_id, int child_id, int child_class_id){
        String query = "UPDATE child_classes SET opened_id = ?, child_id = ?  WHERE child_class_id = ?";
        try (Connection con = getConnection(); PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setInt(1, opened_id);
            pstmt.setInt(2, child_id);
            pstmt.setInt(3, child_class_id);

            pstmt.executeUpdate();

            System.out.println("Updated");
        } catch (SQLException e) {
            System.out.println("error : " + e);
            e.printStackTrace();
        }
    }
    public static void deleteKelasAnak(int opened_id, int child_id, int child_class_id){
        String query = "DELETE FROM child_classes WHERE opened_id = ? AND child_id = ? AND child_class_id = ?";
        try (Connection con = getConnection(); PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setInt(1, opened_id);
            pstmt.setInt(2, child_id);
            pstmt.setInt(3, child_class_id);

            pstmt.executeUpdate();

            System.out.println("Deleted");
        } catch (SQLException e) {
            System.out.println("error : " + e);
            e.printStackTrace();
        }
    }
}
