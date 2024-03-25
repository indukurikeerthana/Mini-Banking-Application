package banking;



import java.io.BufferedReader;

import java.io.InputStreamReader;

import java.sql.Connection;

import java.sql.PreparedStatement;

import java.sql.ResultSet;

import java.sql.SQLException;

import java.sql.SQLIntegrityConstraintViolationException;

import java.sql.Statement;



public class bankManagement { 



    private static final int NULL = 0;

    static Connection con = connection.getConnection();

    static String sql = "";

    ResultSet rs;



    public static int createAccount(String name, int passCode) 

    {

        try {

            if (name.equals("") || passCode == NULL) {

                System.out.println("All Field Required!");

                return -1;

            }

      

            Statement st = con.createStatement();

            sql = "INSERT INTO customer(cname,balance,pass_code) values('" + name + "',1000," + passCode + ")";



           

            if (st.executeUpdate(sql) == 1) {

                System.out.println(name + ", Now You Login!");

                return 1;

            }

      

        } catch (SQLIntegrityConstraintViolationException e) {

            System.out.println("Username Not Available!");

        } catch (Exception e) {

            e.printStackTrace();

        }

        return -1;

    }



    public static int loginAccount(String name, int passCode, BufferedReader sc) {

        try {

            if (name.equals("") || passCode == NULL) {

                System.out.println("All Field Required!");

                return -1;

            }

        

            sql = "select * from customer where cname='" + name + "' and pass_code=" + passCode;

            PreparedStatement st = con.prepareStatement(sql);

            ResultSet rs = st.executeQuery();

           

            if (rs.next()) {

                int acNo = rs.getInt("ac_no");

                

                return acNo;

            } else {

                System.out.println("ERR : Login Failed!");

                return -1;

            }

         

        } catch (SQLIntegrityConstraintViolationException e) {

            System.out.println("Username Not Available!");

        } catch (Exception e) {

            e.printStackTrace();

        }

        return -1;

    }



    public static void showLoggedInOptions(int acNo, BufferedReader sc, String userName) {

        int choice = 0;

        while (choice != 3) {

            try {

                System.out.println("Hello, " + userName + "!");

                System.out.println("1) View Balance");

                System.out.println("2) Transfer Money");

                System.out.println("3) Logout");

                System.out.print("Enter choice: ");

                choice = Integer.parseInt(sc.readLine());

                switch (choice) {

                    case 1:

                        getBalance(acNo);

                        break;

                    case 2:

                        transferMoney(acNo);

                        break;

                    case 3:

                        System.out.println("Logout Successful!");

                        break;

                    default:

                        System.out.println("Invalid choice!");

                }

            } catch (NumberFormatException e) {

                System.out.println("Invalid input! Please enter a number.");

            } catch (Exception e) {

                e.printStackTrace();

            }

        }

    }





    public static void getBalance(int acNo) {

        try {

            sql = "select * from customer where ac_no=" + acNo;

            PreparedStatement st = con.prepareStatement(sql);

            ResultSet rs = st.executeQuery(sql);

            System.out.println("-----------------------------------------------------------");

            System.out.printf("%12s %10s %10s\n", new Object[]{"Account No", "Name", "Balance"});



            while (rs.next()) {

                Object[] args = {

                        Integer.valueOf(rs.getInt("ac_no")),

                        rs.getString("cname"),

                        Integer.valueOf(rs.getInt("balance") / 1), 

                        Integer.valueOf(rs.getInt("balance") % 1) 

                };

                System.out.printf("%12d %10s %10d.%02d\n", args);

            }

            System.out.println("-----------------------------------------------------------\n");

        } catch (Exception e) {

            e.printStackTrace();

        }

    }



    public static void transferMoney(int sender_ac) {

        try {

            BufferedReader sc = new BufferedReader(new InputStreamReader(System.in));

            System.out.print("Enter Receiver A/c No:");

            int receiver_ac = Integer.parseInt(sc.readLine());

            System.out.print("Enter Amount:");

            int amount = Integer.parseInt(sc.readLine());

    

            if (transferMoney(sender_ac, receiver_ac, amount)) {

                System.out.println(amount+"/-Money Sent Successfully!");

            } else {

                System.out.println("Transfer Failed!");

            }

        } catch (Exception e) {

            e.printStackTrace();

        }

    }



    public static boolean transferMoney(int sender_ac, int receiver_ac, int amount)

    		throws SQLException {

        if (receiver_ac == NULL || amount == NULL) {

            System.out.println("All Field Required!");

            return false;

        }

        try {

            con.setAutoCommit(false);



            String checkReceiverExistenceQuery = "SELECT * FROM customer WHERE ac_no=?";

            PreparedStatement receiverExistenceStmt = con.prepareStatement(checkReceiverExistenceQuery);

            receiverExistenceStmt.setInt(1, receiver_ac);

            ResultSet receiverExistenceResult = receiverExistenceStmt.executeQuery();



            if (!receiverExistenceResult.next()) {

                System.out.println("Receiver Account Not Found!");

                return false;

            }



            String checkSenderBalanceQuery = "SELECT balance FROM customer WHERE ac_no=?";

            PreparedStatement senderBalanceStmt = con.prepareStatement(checkSenderBalanceQuery);

            senderBalanceStmt.setInt(1, sender_ac);

            ResultSet senderBalanceResult = senderBalanceStmt.executeQuery();



            if (senderBalanceResult.next()) {

                int senderBalance = senderBalanceResult.getInt("balance");

                if (senderBalance < amount) {

                    System.out.println("Insufficient Balance!");

                    return false;

                }

            }



            String debitQuery = "UPDATE customer SET balance=balance-? WHERE ac_no=?";

            PreparedStatement debitStmt = con.prepareStatement(debitQuery);

            debitStmt.setInt(1, amount);

            debitStmt.setInt(2, sender_ac);

            debitStmt.executeUpdate();



            String creditQuery = "UPDATE customer SET balance=balance+? WHERE ac_no=?";

            PreparedStatement creditStmt = con.prepareStatement(creditQuery);

            creditStmt.setInt(1, amount);

            creditStmt.setInt(2, receiver_ac);

            creditStmt.executeUpdate();



            con.commit();

            return true;

        } catch (SQLException e) {

            e.printStackTrace();

            con.rollback();

        } finally {

            con.setAutoCommit(true);

        }



        return false;

    }

}