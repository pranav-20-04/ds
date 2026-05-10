import java.util.*;

public class Ring {
    int max_processes;
    int coordinator;
    boolean processes[];
    ArrayList<Integer> pid;

    public Ring(int max) {
        max_processes = max;
        processes = new boolean[max];
        pid = new ArrayList<>();

        System.out.println("Creating processes...");
        for (int i = 0; i < max; i++) {
            processes[i] = true;
            System.out.println("P" + (i + 1) + " created.");
        }

        coordinator = max; // highest initially
        System.out.println("P" + coordinator + " is the coordinator");
    }

    void displayProcesses() {
        for (int i = 0; i < max_processes; i++) {
            if (processes[i])
                System.out.println("P" + (i + 1) + " is up.");
            else
                System.out.println("P" + (i + 1) + " is down.");
        }

        if (coordinator != -1 && processes[coordinator - 1]) {
            System.out.println("P" + coordinator + " is the coordinator");
        } else {
            System.out.println("No valid coordinator (run election)");
        }
    }

    void upProcess(int process_id) {
        if (!processes[process_id - 1]) {
            processes[process_id - 1] = true;
            System.out.println("Process P" + process_id + " is now up.");
        } else {
            System.out.println("Process P" + process_id + " is already up.");
        }
    }

    void downProcess(int process_id) {
        if (!processes[process_id - 1]) {
            System.out.println("Process P" + process_id + " is already down.");
        } else {
            processes[process_id - 1] = false;
            System.out.println("Process P" + process_id + " is down.");

            if (coordinator == process_id) {
                coordinator = -1;
                System.out.println("Coordinator failed! Run election.");
            }
        }
    }

    void displayArrayList(ArrayList<Integer> pid) {
        System.out.print("[ ");
        for (int x : pid) {
            System.out.print(x + " ");
        }
        System.out.println("]");
    }

    void initElection(int process_id) {

        // ❗ Check if initiator is alive
        if (!processes[process_id - 1]) {
            System.out.println("Process P" + process_id + " is down. Cannot start election.");
            return;
        }

        pid.clear();
        int current = process_id - 1;

        System.out.println("Election started by P" + process_id);

        // Traverse full ring
        do {
            if (processes[current]) {
                pid.add(current + 1);
                System.out.print("Process P" + (current + 1) + " passing list: ");
                displayArrayList(pid);
            }

            current = (current + 1) % max_processes;

        } while (current != (process_id - 1));

        // Select highest alive process
        coordinator = Collections.max(pid);

        System.out.println("New Coordinator is P" + coordinator);
    }

    public static void main(String args[]) {
        Ring ring = null;
        int max_processes = 0, process_id = 0;
        int choice = 0;
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\nRing Algorithm");
            System.out.println("1. Create processes");
            System.out.println("2. Display processes");
            System.out.println("3. Up a process");
            System.out.println("4. Down a process");
            System.out.println("5. Run election algorithm");
            System.out.println("6. Exit Program");
            System.out.print("Enter your choice:- ");
            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Enter total number of processes:- ");
                    max_processes = sc.nextInt();
                    ring = new Ring(max_processes);
                    break;

                case 2:
                    ring.displayProcesses();
                    break;

                case 3:
                    System.out.print("Enter process to up:- ");
                    process_id = sc.nextInt();
                    ring.upProcess(process_id);
                    break;

                case 4:
                    System.out.print("Enter process to down:- ");
                    process_id = sc.nextInt();
                    ring.downProcess(process_id);
                    break;

                case 5:
                    System.out.print("Enter process to start election:- ");
                    process_id = sc.nextInt();
                    ring.initElection(process_id);
                    break;

                case 6:
                    System.exit(0);

                default:
                    System.out.println("Invalid choice!");
            }
        }
    }
}
