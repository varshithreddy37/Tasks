
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.Scanner;

class MovieTicketBookingApp {

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

        int Screens;

        while (true) {
            System.out.print("Enter no.of screens: ");

            if (!sc.hasNextInt()) {
                System.out.println("Invalid input! Please enter a number.");
                sc.next();
                continue;
            }

            Screens = sc.nextInt();
            sc.nextLine();

            if (Screens <= 0) {
                System.out.println("Invalid input! Please try again.");
            } else {
                break;
            }
        }

        String[] screenName = new String[Screens];
        String[] movieName = new String[Screens];
        String[] timing = new String[Screens];

        int[] rows = new int[Screens];
        int[] seatsPerRow = new int[Screens];

        // 3D array for screens
        SeatStatus[][][] seats = new SeatStatus[Screens][][];
        String[][][] seatTicketID = new String[Screens][][];
        String[][][] bookingDate = new String[Screens][][];
        // storage for tickets

        int ticketCounter = 1;

        // setup for screens
        for (int screen = 0; screen < Screens; screen++) {

            System.out.println("\nEnter details for Screen " + (screen + 1));

            while (true) {
                System.out.print("Enter Screen Name: ");
                screenName[screen] = sc.nextLine().trim();

                if (!screenName[screen].isEmpty()) {
                    break;
                }

                System.out.println("Invalid Screen Name! Try Again.");
            }

            while (true) {
                System.out.print("Enter Movie Name: ");
                movieName[screen] = sc.nextLine().trim();

                if (!movieName[screen].isEmpty()) {
                    break;
                }

                System.out.println("Invalid Movie Name! Try Again.");
            }
            while (true) {
                System.out.print("Enter Movie Timing (HH:MM): ");
                timing[screen] = sc.nextLine().trim();

                if (timing[screen].matches("([01]\\d|2[0-3]):([0-5]\\d)")) {
                    break;
                }

                System.out.println("Invalid Timing! Try Again.");
            }

            while (true) {
                System.out.print("Enter Number of Rows: ");

                if (!sc.hasNextInt()) {
                    System.out.println("Invalid input!");
                    sc.next();
                    continue;
                }

                rows[screen] = sc.nextInt();

                if (rows[screen] <= 0) {
                    System.out.println("Rows must be greater than 0.");
                } else {
                    break;
                }
            }

            while (true) {
                System.out.print("Enter Seats per Row: ");

                if (!sc.hasNextInt()) {
                    System.out.println("Invalid input!");
                    sc.next();
                    continue;
                }

                seatsPerRow[screen] = sc.nextInt();

                if (seatsPerRow[screen] <= 0) {
                    System.out.println("Seats must be greater than 0.");
                } else {
                    break;
                }
            }
            sc.nextLine();

            seats[screen] = new SeatStatus[rows[screen]][seatsPerRow[screen]];
            seatTicketID[screen] = new String[rows[screen]][seatsPerRow[screen]];
            bookingDate[screen] = new String[rows[screen]][seatsPerRow[screen]];
            for (int i = 0; i < rows[screen]; i++) {
                for (int j = 0; j < seatsPerRow[screen]; j++) {
                    seats[screen][i][j] = SeatStatus.AVAILABLE;
                    seatTicketID[screen][i][j] = "";
                    bookingDate[screen][i][j] = "";
                }
            }
            System.out.println("Screen " + (screen + 1) + " setup complete.");
        }
        // Loading previous data
        try {
            File file = new File("d:\\tasks\\tickets.json");

            if (file.exists()) {

                Scanner fileReader = new Scanner(file);

                while (fileReader.hasNextLine()) {

                    String line = fileReader.nextLine();
                    String[] data = line.split(",");

                    String id = data[0];
                    int screen = Integer.parseInt(data[1]);
                    int r = Integer.parseInt(data[2]);
                    int s = Integer.parseInt(data[3]);

                    seats[screen][r][s] = SeatStatus.BOOKED;
                    seatTicketID[screen][r][s] = id;

                    int number = Integer.parseInt(id.substring(1));
                    if (number >= ticketCounter) {
                        ticketCounter = number + 1;
                    }
                }
                fileReader.close();
                System.out.println("Previous booking data loaded successfully.");

            }

        } catch (Exception e) {
            System.out.println("No Previous Data Found. Starting a new session.");
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

            if (!sc.hasNextInt()) {
                System.out.println("Invalid input! Please try again.");
                sc.next();
                continue;
            }

            int choice = sc.nextInt();
            sc.nextLine();
            switch (choice) {

                case 1:
                    System.out.print("Enter Screen Name: ");
                    String enteredScreen = sc.nextLine();

                    int screen = -1;

                    for (int i = 0; i < Screens; i++) {
                        if (screenName[i].equalsIgnoreCase(enteredScreen)) {
                            screen = i;
                            break;
                        }
                    }

                    if (screen == -1) {
                        System.out.println("Invalid Screen Name!");
                        break;
                    }

                    System.out.print("Row: ");

                    if (!sc.hasNextInt()) {
                        System.out.println("Invalid input! Please try again.");
                        sc.next();
                        break;
                    }

                    int row = sc.nextInt() - 1;

                    if (row < 0 || row >= rows[screen]) {
                        System.out.println("Invalid Row! Please try again.");
                        break;
                    }

                    System.out.print("Seat: ");

                    if (!sc.hasNextInt()) {
                        System.out.println("Invalid input! Please try again.");
                        sc.next();
                        break;
                    }

                    int seat = sc.nextInt() - 1;
                    sc.nextLine();

                    if (seat < 0 || seat >= seatsPerRow[screen]) {
                        System.out.println("Invalid Seat! Please try again.");
                        break;
                    }

                    if (seats[screen][row][seat] == SeatStatus.AVAILABLE) {

                        while (true) {
                            System.out.print("Enter Booking Date (DD/MM/YYYY): ");
                            String bookingDateInput = sc.nextLine();

                            try {
                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/uuuu")
                                        .withResolverStyle(ResolverStyle.STRICT);

                                LocalDate date = LocalDate.parse(bookingDateInput, formatter);

                                if (date.isBefore(LocalDate.now())) {
                                    System.out.println("Past date is not allowed. Please try again.");
                                    continue;
                                }

                                seats[screen][row][seat] = SeatStatus.BOOKED;
                                String ticketID = "T" + ticketCounter++;
                                seatTicketID[screen][row][seat] = ticketID;
                                bookingDate[screen][row][seat] = bookingDateInput;
                                System.out.println("Advance Booking Successful.");
                                System.out.println("Booking Date: " + bookingDateInput);
                                System.out.println("Movie Timing: " + timing[screen]);
                                System.out.println("Ticket ID: " + ticketID);
                                try {

                        FileWriter fw = new FileWriter("d:\\tasks\\tickets.json");
                        

                        for (int s = 0; s < Screens; s++) {

                            for (int i = 0; i < rows[s]; i++) {

                                for (int j = 0; j < seatsPerRow[s]; j++) {

                                    if (seats[s][i][j] == SeatStatus.BOOKED) {
                                        fw.write(
                                                "{\n"
                                                + "  \"ticketID\": \"" + seatTicketID[s][i][j] + "\",\n"
                                                + "  \"screen\": " + s + ",\n"
                                                + "  \"row\": " + i + ",\n"
                                                + "  \"seat\": " + j + ",\n"
                                                + "  \"bookingDate\": \"" + bookingDate[s][i][j] + "\"\n"
                                                + "}\n"
                                        );
                                        System.out.println("Saving: " + seatTicketID[s][i][j]);

                                    }

                                }
                            }
                        }

                        fw.close();
                        System.out.println("Booking Data Saved Successfully.");

                    } catch (Exception e) {
                        System.out.println("Error Saving File");
                    }
                                break;

                            } catch (Exception e) {
                                System.out.println("Invalid input! Please try again.");
                            }
                        }

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
                                    bookingDate[s][i][j] = "";
                                    found = true;
                                }
                            }
                        }
                    }

                    System.out.println(found ? "Cancelled Successfully." : "Invalid Ticket ID.");
                    break;

                case 3:
                    System.out.print("Screen: ");

                    if (!sc.hasNextInt()) {
                        System.out.println("Invalid input! Please try again.");
                        sc.next();
                        break;
                    }

                    int viewScreen = sc.nextInt() - 1;

                    if (viewScreen < 0 || viewScreen >= Screens) {
                        System.out.println("Invalid Screen! Please try again.");
                        break;
                    }

                    for (int i = 0; i < rows[viewScreen]; i++) {
                        for (int j = 0; j < seatsPerRow[viewScreen]; j++) {
                            if (seats[viewScreen][i][j] == SeatStatus.AVAILABLE) {
                                System.out.print("A ");
                            } else {
                                System.out.print("B ");
                            }
                        }
                        System.out.println();
                    }
                    break;

                case 4:
                    // Save Data Before Exit
                    

                    System.out.println("Have a great day!");
                    return;
                default:
                    System.out.println("Invalid Choice.");
            }
        }
    }
}
