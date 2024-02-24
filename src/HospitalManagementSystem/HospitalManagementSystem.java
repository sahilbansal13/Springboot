package HospitalManagementSystem;

import java.sql.*;
import java.util.Scanner;

public class HospitalManagementSystem {
    public static Scanner scanner;
    private static final String url="jdbc:mysql://localhost:3306/hospital";

    public static void main(String[] args){
        scanner=new Scanner(System.in);
         final String username="root";
        System.out.println("Enter the password for database");
        final String password=scanner.next();
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }


        try{
            Connection connection = DriverManager.getConnection(url,username,password);
            Patients patient =new Patients(connection,scanner);
            Doctor doctor = new Doctor(connection);
            while (true){
                System.out.println("WELCOME TO HOSPITAL MANAGEMENT SYSTEM");
                System.out.println(" 1. Add Patient");
                System.out.println(" 2. View Patients");
                System.out.println(" 3. View Doctor");
                System.out.println(" 4. Book Appointment");
                System.out.println(" 5. Exit");

                System.out.println(" Enter your Choice");
                int choice =scanner.nextInt();

                switch (choice) {
                    case 1 -> {
                        // add patient
                        patient.addPatient();
                        System.out.println(" Patient added Sucessfully");
                    }
                    case 2 -> {
                        patient.viewPatient();
                        System.out.println(" Patient fetched Sucessfully");
                    }
                    case 3 -> {
                        doctor.viewDoctor();
                        System.out.println(" Doctor fetched Sucessfully");
                    }
                    case 4 -> {
                        bookAppointment(patient,doctor,connection,scanner);
                        System.out.println(" ");
                    }
                    case 5 -> {
                        System.out.println(" Thanks for using the services of our Hospital");
                        return;
                    }
                    default -> System.out.println(" Please Enter a Valid Input");
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

    }
    public static void bookAppointment(Patients patient,Doctor doctor,Connection connection,Scanner scanner){
        System.out.println("Enter Patient ID");
        int p_id =scanner.nextInt();

        System.out.println("Enter Doctor ID");
        int d_id =scanner.nextInt();

        System.out.println("Enter Appointment Date (YYYY-MM-DD) :");
        String appointmentDate = scanner.next();

        if(patient.getPatientById(p_id) && doctor.getDoctorById(d_id)){
            if(checkDoctorAvailable(d_id,appointmentDate,connection)){
                String query = "Insert into appointments(p_id,d_id,appointment_DT)Values(?,?,?)";
                try{
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setInt(1,p_id);
                    preparedStatement.setInt(2,d_id);
                    preparedStatement.setString(3,appointmentDate);
                    int rowaffected =preparedStatement.executeUpdate();
                    if(rowaffected>0){
                        System.out.println(" Appointment Booked Sucessfully");
                    }else{
                        System.out.println("Failed to Book a Appointment");
                    }

                }catch (SQLException e){
                    e.printStackTrace();
                }

            }else {
                System.out.println("Doctor is not available");
            }
        }else{
            System.out.println("Either Patient or Doctor doesn't exist");
        }


    }

    private static boolean checkDoctorAvailable(int dId, String appointmentDate, Connection connection) {
        String query = "Select count(*) from appointments where d_id=? and appointment_dt=?";
        try {
            PreparedStatement preparedStatement =connection.prepareStatement(query);
            preparedStatement.setInt(1,dId);
            preparedStatement.setString(2,appointmentDate);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                int count = resultSet.getInt(1);
                return count == 0;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

}
