
import java.util.Scanner;

public class TokenRing {

    public static void main(String args[]) {

        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter Number Of Nodes: ");
        int numberOfNode = scanner.nextInt();

        // Display ring structure
        System.out.print("Ring Formed: ");
        for (int i = 0; i < numberOfNode; i++) {
            System.out.print(i + "->");
        }
        System.out.println("0");

        // Initially token at node 0
        int token = 0;

        while (true) {

            System.out.print("\nEnter Sender (-1 to Exit): ");
            int sender = scanner.nextInt();

            if (sender == -1) {
                break;
            }

            System.out.print("Enter Receiver: ");
            int receiver = scanner.nextInt();

            System.out.print("Enter Data To Send: ");
            int data = scanner.nextInt();

            // Token passing
            System.out.print("Token Passing: ");

            while (token != sender) {
                System.out.print(token + "->");
                token = (token + 1) % numberOfNode;
            }

            System.out.println(sender);

            // Sender sends data
            System.out.println("Sender: " + sender +
                    " Sending Data: " + data);

            // Forward data through ring
            int temp = sender;

            while (temp != receiver) {

                temp = (temp + 1) % numberOfNode;

                if (temp != receiver) {
                    System.out.println("Data: " + data +
                            " Forwarded By: " + temp);
                }
            }

            // Receiver gets data
            System.out.println("Receiver: " + receiver +
                    " Received Data: " + data);

            // Token now stays with receiver
            token = receiver;
        }

        scanner.close();
    }
}
