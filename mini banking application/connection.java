package banking;

 

import java.sql.Connection;

import java.sql.DriverManager;



public class connection {

    static Connection con; 

    public static Connection getConnection()

    {

        try {

            

            

            String mysqlJDBCDriver

                = "com.mysql.cj.jdbc.Driver"; 

            String url

                = "jdbc:mysql://localhost:3306/bank"; 

            String user = "root";        

            String pass = "reddy@065"; 

             Class.forName(mysqlJDBCDriver);

            con = DriverManager.getConnection(url, user,

                                              pass);

        }

        catch (Exception e) {

            System.out.println("Connection Failed!");

        }

 

        return con;

    }

}