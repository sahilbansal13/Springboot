
import java.io.*;
import java.sql.*;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws Exception {
        Console console = System.console() ;

        char [] password = console.readPassword("Enter password: ");
        System.out.println("Password was: " + Arrays.toString(password));
       Arrays.fill(password,' ');
    }

}