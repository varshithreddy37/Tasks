import java.util.Scanner;
import java.io.*;
public class MovieTicketBookingApp {

    // enum for seat status
    enum SeatStatus {
        AVAILABLE, BOOKED
    }

    // ANSI Colors
    public static final String GREEN = "\u001B[32m";
    public static final String RED = "\u001B[31m";
    public static final String RESET = "\u001B[0m";

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter no.of screens: ");
        int Screens = sc.nextInt();
        sc.nextLine();

        String[] screenName = new String[Screens];
        String[] movieName = new String[Screens];
        String[] timing = new String[Screens];

        int[] rows = new int[Screens];
        int[] seatsPerRow = new int[Screens];

        // 3D array for screens
        SeatStatus[][][] seats = new SeatStatus[Screens][][];
        String[][][] seatTicketID = new String[Screens][][];
        // storage for tickets
    
        int ticketCounter = 1;

        // setup for screens
        for (int screen = 0; screen < Screens; screen++) {

            System.out.println("\nEnter details for Screen " + (screen + 1));

            System.out.print("Enter Screen Name: ");
            screenName[screen] = sc.nextLine();

            System.out.print("Enter Movie Name: ");
            movieName[screen] = sc.nextLine();

            System.out.print("Enter Movie Timing: ");
            timing[screen] = sc.nextLine();

            System.out.print("Enter Number of Rows: ");
            rows[screen] = sc.nextInt();

            System.out.print("Enter Seats per Row: ");
            seatsPerRow[screen] = sc.nextInt();

            seats[screen] = new SeatStatus[rows[screen]][seatsPerRow[screen]];
            seatTicketID[screen] = new String[rows[screen]][seatsPerRow[screen]];

            for (int i = 0; i < rows[screen]; i++) {
                for (int j = 0; j < seatsPerRow[screen]; j++) {
                    seats[screen][i][j] = SeatStatus.AVAILABLE;
                    seatTicketID[screen][i][j] = "";                }
            }
        }
        // Loading previous data
try {
            File file = new File("tickets.txt");

            if (file.exists()) {

                Scanner fileReader = new Scanner(file);

                while (fileReader.hasNextLine()) {

                    String line = fileReader.nextLine();
                    String[] data = line.split(",");

                    int screen = Integer.parseInt(data[0]);
                    int r = Integer.parseInt(data[1]);
                    int s = Integer.parseInt(data[2]);
                    String id = data[3];

                    seats[screen][r][s] = SeatStatus.BOOKED;
                    seatTicketID[screen][r][s] = id;
                    int number = Integer.parseInt(id.substring(1));
                    if (number >= ticketCounter)
                        ticketCounter = number + 1;
                }
                fileReader.close();
                
            }

        } catch (Exception e) {
            System.out.println("No Previous Data Found.");
        }
        while (true) {

            System.out.println("\n");
            System.out.println("MOVIE TICKET BOOKING SYSTEM");
            System.out.println(",");
            System.out.println("1. Book Ticket");
            System.out.println("2. Cancel Ticket");
            System.out.println("3. View Seats");
            System.out.println("4. Exit");
            System.out.print("Enter Choice: ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1:
                       System.out.print("Screen: ");
int screen = sc.nextInt() - 1;

System.out.print("Row: ");
int row = sc.nextInt() - 1;

System.out.print("Seat: ");
int seat = sc.nextInt() - 1;

if (seats[screen][row][seat] == SeatStatus.AVAILABLE) {
    seats[screen][row][seat] = SeatStatus.BOOKED;
    String ticketID = "T" + ticketCounter++;
    seatTicketID[screen][row][seat] = ticketID;
    System.out.println("Booked Successfully. Ticket ID: " + ticketID);
} else {
    System.out.println("Seat Already Booked.");
}
                    break;

                case 2:
                    System.out.print("Enter Ticket ID: ");
String id = sc.nextLine();

boolean found = false;

for (int s = 0; s < Screens; s++) {
    for (int i = 0; i < rows[s]; i++) {
        for (int j = 0; j < seatsPerRow[s]; j++) {
            if (seatTicketID[s][i][j].equals(id)) {
                seats[s][i][j] = SeatStatus.AVAILABLE;
                seatTicketID[s][i][j] = "";
                found = true;
            }
        }
    }
}

System.out.println(found ? "Cancelled Successfully." : "Invalid Ticket ID.");
                    break;

                case 3:
                   System.out.print("Screen: ");
int viewScreen = sc.nextInt() - 1;

for (int i = 0; i < rows[viewScreen]; i++) {
    for (int j = 0; j < seatsPerRow[viewScreen]; j++) {
        if (seats[viewScreen][i][j] == SeatStatus.AVAILABLE)
            System.out.print("A ");
        else
            System.out.print("B ");
    }
    System.out.println();
}
                    break;

                case 4:
                    // Save Data Before Exit
                    try {

                        FileWriter fw = new FileWriter("tickets.txt");

                        for (int s = 0; s < Screens; s++) {

                            for (int i = 0; i < rows[s]; i++) {

                                for (int j = 0; j < seatsPerRow[s]; j++) {

                                    if (seats[s][i][j] == SeatStatus.BOOKED) {

                                        fw.write(s + "," + i + "," + j + "," + seatTicketID[s][i][j] + "\n");

                                    }
                                }
                            }
                        }

                        fw.close();

                    } catch (Exception e) {
                        System.out.println("Error Saving File");
                    }

                    System.out.println("Have a great day!");
                    System.exit(0);

                default:
                    System.out.println("Invalid Choice.");
            }
        }
    }
}
