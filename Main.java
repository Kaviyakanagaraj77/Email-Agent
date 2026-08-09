import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while(true){
            System.out.println("Email received (type 'exit' to quit):");
            String email = sc.nextLine();

            if(email.equalsIgnoreCase("exit")) break;

            String reply = AIReply.generateReply(email);
            System.out.println("\nAI Reply:");
            System.out.println(reply);
            System.out.println("----------------------------");
        }

        sc.close();
    }
}
