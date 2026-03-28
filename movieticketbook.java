import java.util.Scanner;

public class MovieTicketBookingApp {

    // enum for seat status
    enum SeatStatus {
        AVAILABLE, BOOKED
    }

    // ANSI Colors
    public static final String GREEN = "\u001B[32m";
    public static final String RED = "\u001B[31m";

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

        // storage for tickets
        String[] ticketIDs = new String[100];
        int[] ticketScreen = new int[100];
        int[] ticketRow = new int[100];
        int[] ticketSeat = new int[100];
        int ticketCount = 0;

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

            for (int i = 0; i < rows[screen]; i++) {
                for (int j = 0; j < seatsPerRow[screen]; j++) {
                    seats[screen][i][j] = SeatStatus.AVAILABLE;
                }
            }

            sc.nextLine();
        }

        // Selecting the screens here
        System.out.println("\nSelect Screen to Book Ticket:");
        for (int i = 0; i < Screens; i++) {
            System.out.println((i + 1) + ". " + screenName[i] + " - " + movieName[i]);
        }

        int selectedScreen = sc.nextInt() - 1;

// here is the layout for the seats
System.out.println("\nSeat Layout (Green = Available, Red = Booked)");
        for (int i = 0; i < rows[selectedScreen]; i++) {
            System.out.print("Row " + (i + 1) + ": ");
            for (int j = 0; j < seatsPerRow[selectedScreen]; j++) {

                if (seats[selectedScreen][i][j] == SeatStatus.AVAILABLE) {
                    System.out.print(GREEN + "A ");
                } else {
                    System.out.print(RED + "B ");
                }
            }
            System.out.println();
        }

        //  Booking seats here
        System.out.print("\nHow many seats do you want to book? ");
        int count = sc.nextInt();

        String bookedSeats = "";

        for (int i = 0; i < count; i++) {

            System.out.print("Enter Row Number: ");
            int r = sc.nextInt() - 1;

            System.out.print("Enter Seat Number: ");
            int s = sc.nextInt() - 1;

            if (seats[selectedScreen][r][s] == SeatStatus.BOOKED) {
                System.out.println(RED + "Seat already booked! Choose another.");
                i--;
            } else {

                seats[selectedScreen][r][s] = SeatStatus.BOOKED;

                // creating a ticket 
                String ticketId = "T" + (ticketCount + 1);

                ticketIDs[ticketCount] = ticketId;
                ticketScreen[ticketCount] = selectedScreen;
                ticketRow[ticketCount] = r;
                ticketSeat[ticketCount] = s;

                ticketCount++;

                bookedSeats += "Row " + (r + 1) + " Seat " + (s + 1) + ", ";

                System.out.println(GREEN + "Seat booked successfully!");
                System.out.println("Your Ticket ID: " + ticketId);
            }
        }

        sc.nextLine();

// for cancellation
System.out.print("\nDo you want to cancel any ticket? (yes/no): ");
        String cancelChoice = sc.nextLine();

        if (cancelChoice.equalsIgnoreCase("yes")) {

            System.out.print("Enter Ticket ID: ");
            String inputId = sc.nextLine();

            System.out.print("Enter Row Number: ");
            int r = sc.nextInt() - 1;

            System.out.print("Enter Seat Number: ");
            int s = sc.nextInt() - 1;
            
// i have used loop through all the stored tickets will be here

            boolean found = false;

            for (int i = 0; i < ticketCount; i++) {

                if (ticketIDs[i].equals(inputId) &&
                    ticketScreen[i] == selectedScreen &&
                    ticketRow[i] == r &&
                    ticketSeat[i] == s) {

                    if (seats[selectedScreen][r][s] == SeatStatus.BOOKED) {
                        seats[selectedScreen][r][s] = SeatStatus.AVAILABLE;
                        System.out.println(GREEN + "Ticket cancelled successfully!");
                    } else {
                        System.out.println(RED + "Seat already available!");
                    }

                    found = true;
                    
                    // here break is for exit the loop immediatley if not waste of time
                    
                    break;
                }
            }

            if (!found) {
                System.out.println(RED + "Invalid Ticket ID or Seat details!");
            }
        }

        // Receipt
        System.out.println("\n");
        System.out.println("Screen Name : " + screenName[selectedScreen]);
        System.out.println("Movie Name  : " + movieName[selectedScreen]);
        System.out.println("Timing      : " + timing[selectedScreen]);
        System.out.println("Seats Booked: " + bookedSeats);
        System.out.println("");

        System.out.println("Have a good day and Enjoy Your Movie");
    }
}