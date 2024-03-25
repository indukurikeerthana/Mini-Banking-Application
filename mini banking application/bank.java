import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class bank {
    public static void main(String args[]) throws IOException {
        BufferedReader sc = new BufferedReader(new InputStreamReader(System.in));
        String name = "";
        int pass_code;
        int ch;
        int accountId = -1;
        while (true) {
            System.out.println("\n ->|| Welcome to BankIT ||<- \n");
            System.out.println("1)Create Account");
            System.out.println("2)Login Account");
            System.out.println("3)Exit");

            try {
                System.out.print("\n Enter Input:");
                ch = Integer.parseInt(sc.readLine());

                switch (ch) {
                    case 1:
                        try {
                            System.out.print("Enter Unique UserName:");
                            name = sc.readLine();
                            System.out.print("Enter New Password:");
                            pass_code = Integer.parseInt(sc.readLine());
                            int ac_id = bankManagement.createAccount(name, pass_code);
                            if (ac_id != -1) {
                                System.out.println("MSG : Account Created Successfully!\n");
                            } else {
                                System.out.println("ERR : Account Creation Failed!\n");
                            }
                        } catch (Exception e) {
                            System.out.println(" ERR : Enter Valid Data::Insertion Failed!\n");
                        }
                        break;

                    case 2:
                        try {
                            System.out.print("Enter UserName:");
                             name = sc.readLine();
                            System.out.print("Enter Password:");
                            pass_code = Integer.parseInt(sc.readLine());

                            accountId = bankManagement.loginAccount(name, pass_code, sc);
                            if (accountId != -1) {
                                System.out.println("MSG : Login Successfully!\n");
                                bankManagement.showLoggedInOptions(accountId, sc, name);
                            }

                            else {
                                System.out.println("ERR : Login Failed!\n");
                            }
                        } catch (Exception e) {
                            System.out.println(" ERR : Enter Valid Data::Login Failed!\n");
                        }
                        break;

                    case 3:
                        System.out.println("Exited Successfully!\n\n Thank You :)");
                        sc.close();
                        System.exit(0);
                        break;

                    default:
                        System.out.println("Invalid Entry!\n");
                        break;
                }

           
                if (accountId != -1 && ch == 3) {
                    break;
                }
            } catch (Exception e) {
                System.out.println("Enter Valid Entry!");
            }
        }
    }
}