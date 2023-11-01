package com.cognixia.radiant.connection;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionManager {

    private static Connection connection;

    private static void makeConnection() {

        // Establish the connection if it's not already connected

        try {


            Properties props = new Properties();
            props.load(new FileInputStream("resources/config.properties"));

            String url = props.getProperty("url");
            String username = props.getProperty("username");
            String password = props.getProperty("password");

            // Establish the connection
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, username, password);

        }catch (SQLException e) {
            System.out.println("There was an error connecting to the Database.");
        } catch (FileNotFoundException e) {
            System.out.println("There was an error Fiding Properties File.");
        } catch (IOException e) {
            System.out.println("There was an error in the system.");
        } catch (ClassNotFoundException e) {
            System.out.println("There was an error with the class.");
        }

    }

    public static Connection getConnection() throws FileNotFoundException, IOException, ClassNotFoundException, SQLException {


        // Do not make the connection again if we are already connected
        if(connection == null) {
            makeConnection();
        }

        return connection;
    }
}