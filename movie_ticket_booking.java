import java.util.Scanner;

public class MovieTicketBookingApp {

//here enum is used because it has fixed values like seats are available or booked 
enum SeatStatus {
    AVAILABLE, BOOKED
}

public static void main(String[] args) {

    // here we are taking input from user
    Scanner sc = new Scanner(System.in);

    System.out.print("Enter no.of screens: ");
    int Screens = sc.nextInt();
    sc.nextLine();


    String[] screenName = new String[Screens];
    String[] movieName = new String[Screens];
    String[] timing = new String[Screens];

    int[] rows = new int[Screens];
    int[] seatsPerRow = new int[Screens];

    // here 3Darray i had used because there are multiple screens with seating arrangement
    SeatStatus[][][] seats = new SeatStatus[Screens][][];

    for(int screen = 0; screen < Screens; screen++){

    System.out.println("\nEnter details for Screen " + (screen+1));
    System.out.print("Enter Screen Name: ");
    screenName[screen] = sc.nextLine();

    System.out.print("Enter Movie Name: ");
    movieName[screen] = sc.nextLine();

    System.out.print("Enter Movie Timing: ");
    timing[screen] = sc.nextLine();

    //asking no.of rows
    System.out.print("Enter Number of Rows: ");
    rows[screen] = sc.nextInt();

    System.out.print("Enter Seats per Row: ");
    seatsPerRow[screen] = sc.nextInt();

    // here 2Darray i had used because for seatinng arragngement as per row insted od 1d array
 seats[screen] = new SeatStatus[rows[screen]][seatsPerRow[screen]];
    for (int i = 0; i < rows[screen]; i++) {
        for (int j = 0; j < seatsPerRow[screen]; j++) {
            seats[screen][i][j] = SeatStatus.AVAILABLE;
        }
    }
    }

    System.out.println("\nSelect Screen to Book Ticket:");
    for( int i=0;i<Screens;i++){
        System.out.println((i+1) + ". " + screenName[i] + " - " + movieName[i]);
    }

    int selectedScreen = sc.nextInt() - 1;
    System.out.println("\nSeat Layout (A = Available, B = Booked)");
    for (int i = 0; i < rows[selectedScreen]; i++) {
        System.out.print("Row " + (i + 1) + ": ");
        for (int j = 0; j < seatsPerRow[selectedScreen]; j++) {
            System.out.print(seats[selectedScreen][i][j] == SeatStatus.AVAILABLE ? "A " : "B ");
        }
        System.out.println();
    }

    // here we are asking for coustumer to book seats how many they want
    System.out.print("\nHow many seats do you want to book? ");
    int count = sc.nextInt();
    String bookedSeats = "";

    for (int i = 0; i < count; i++) {

        System.out.print("Enter Row Number: ");
        int r = sc.nextInt() - 1;

        System.out.print("Enter Seat Number: ");
        int s = sc.nextInt() - 1;

        // here we are checkinmg either the seats are booked or not?
        if (seats[selectedScreen][r][s] == SeatStatus.BOOKED) {
            System.out.println("Seat already booked! Choose another.");
            i--;
        } else {
            seats[selectedScreen][r][s] = SeatStatus.BOOKED;
            bookedSeats += "Row " + (r + 1) + " Seat " + (s + 1) + ", ";
            System.out.println("Seat booked successfully!");
        }
    }
    
    // Here we are Handling Cancellations
    sc.nextLine();
    System.out.print("\nDo you want to cancel any seat? (yes/no): ");
    String cancelChoice = sc.nextLine();

    if (cancelChoice.equalsIgnoreCase("yes")) {
        System.out.print("Enter Row Number to cancel: ");
        int r = sc.nextInt() - 1;

        System.out.print("Enter Seat Number to cancel: ");
        int s = sc.nextInt() - 1;

        if (seats[selectedScreen][r][s] == SeatStatus.BOOKED) {
            seats[selectedScreen][r][s] = SeatStatus.AVAILABLE;
            System.out.println("Seat cancelled successfully!");
        } else {
            System.out.println("Seat is already available!");
        }
    }

    // here we are printng booked recipts
    System.out.println("\n");
    System.out.println(" BOOKED RECEIPT ");
    System.out.println(" ");
    System.out.println("Screen Name : " + screenName[selectedScreen]);
    System.out.println("Movie Name  : " + movieName[selectedScreen]);
    System.out.println("Timing      : " + timing[selectedScreen]);
    System.out.println("Seats Booked: " + bookedSeats);
    System.out.println(" ");
    System.out.println("Have a good day and Enjoy Your Movie");
}

}