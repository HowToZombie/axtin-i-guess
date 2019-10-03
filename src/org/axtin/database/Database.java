package org.axtin.database;


import java.sql.*;

public class Database {

    private static Connection source = null;
    private String host = "db2.voodooservers.com";

    public Database() {
       load();
    }

    public void load(){
        try {
            Class.forName("com.mysql.jdbc.Driver");

            source = DriverManager
                    .getConnection("jdbc:mysql://" + host + "/user3232?"
                            + "user=user3232" + "&password=9ffd2b939f");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public Connection getConnection() {
        close();
        load();
        return source;
    }

    public void close() {
        try {
            if (source != null && !source.isClosed()) {
                source.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
