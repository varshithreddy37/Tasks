import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        File theatreFile = new File("d:\\tasks\\theatre.json");

        if (theatreFile.exists()) {

            Screens = getScreenCount();

            if (Screens <= 0) {
                System.out.println("Invalid theatre.json file.");
                return;
            }

        } else {

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
        if (theatreFile.exists()) {

            try {

                String json = readFile("d:\\tasks\\theatre.json");

                Pattern objectPattern
                        = Pattern.compile("\\{(.*?)\\}", Pattern.DOTALL);

                Matcher matcher = objectPattern.matcher(json);

                int screen = 0;

                while (matcher.find() && screen < Screens) {

                    String object = matcher.group(1);

                    screenName[screen]
                            = getJsonString(object, "screenName");

                    movieName[screen]
                            = getJsonString(object, "movieName");

                    timing[screen]
                            = getJsonString(object, "movieTime");

                    rows[screen]
                            = getJsonInt(object, "rows");

                    seatsPerRow[screen]
                            = getJsonInt(object, "seatsPerRow");

                    screen++;
                }

                System.out.println(
                        "Previous theatre details loaded successfully.");

                // DISPLAY PREVIOUS THEATRE DETAILS
                System.out.println("\nPREVIOUS THEATRE DETAILS");

                for (int i = 0; i < Screens; i++) {

                    System.out.println("\nScreen " + (i + 1));
                    System.out.println("Screen Name  : " + screenName[i]);
                    System.out.println("Movie Name   : " + movieName[i]);
                    System.out.println("Movie Timing : " + timing[i]);
                    System.out.println("Rows         : " + rows[i]);
                    System.out.println("Seats/Row    : " + seatsPerRow[i]);
                }

                System.out.print("\nDo you want to add new movies? (Y/N): ");

                String answer = sc.nextLine();

                if (answer.equalsIgnoreCase("Y")) {

                    int newScreens;

                    while (true) {

                        System.out.print("Enter number of new movies/screens: ");

                        if (!sc.hasNextInt()) {

                            System.out.println("Invalid input! Please enter a number.");

                            sc.next();
                            continue;
                        }

                        newScreens = sc.nextInt();
                        sc.nextLine();

                        if (newScreens <= 0) {

                            System.out.println("Enter at least 1.");

                        } else {

                            break;
                        }
                    }

                    int oldScreens = Screens;
                    int totalScreens = oldScreens + newScreens;

                    // Old arrays save
                    String[] oldScreenName = screenName;
                    String[] oldMovieName = movieName;
                    String[] oldTiming = timing;

                    int[] oldRows = rows;
                    int[] oldSeatsPerRow = seatsPerRow;

                    // New arrays
                    screenName = new String[totalScreens];
                    movieName = new String[totalScreens];
                    timing = new String[totalScreens];

                    rows = new int[totalScreens];
                    seatsPerRow = new int[totalScreens];

                    // Copying the previous data
                    for (int i = 0; i < oldScreens; i++) {

                        screenName[i] = oldScreenName[i];
                        movieName[i] = oldMovieName[i];
                        timing[i] = oldTiming[i];

                        rows[i] = oldRows[i];
                        seatsPerRow[i] = oldSeatsPerRow[i];
                    }

                    // Enter the new movie details
                    // Enter the new movie details
                    for (int newscreen = oldScreens;
                            newscreen < totalScreens;
                            newscreen++) {

                        System.out.println("\nEnter details for Screen " + (newscreen + 1));

                        while (true) {
                            System.out.print("Enter Screen Name: ");
                            screenName[newscreen] = sc.nextLine().trim();

                            if (!screenName[newscreen].isEmpty()) {
                                break;
                            }

                            System.out.println("Invalid Screen Name! Try Again.");
                        }

                        while (true) {
                            System.out.print("Enter Movie Name: ");
                            movieName[newscreen] = sc.nextLine().trim();

                            if (!movieName[newscreen].isEmpty()) {
                                break;
                            }

                            System.out.println("Invalid Movie Name! Try Again.");
                        }

                        while (true) {
                            System.out.print("Enter Movie Timing (HH:MM): ");
                            timing[newscreen] = sc.nextLine().trim();

                            if (timing[newscreen].matches("([01]\\d|2[0-3]):([0-5]\\d)")) {
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

                            rows[newscreen] = sc.nextInt();

                            if (rows[newscreen] <= 0) {
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

                            seatsPerRow[newscreen] = sc.nextInt();

                            if (seatsPerRow[newscreen] <= 0) {
                                System.out.println("Seats must be greater than 0.");
                            } else {
                                break;
                            }
                        }

                        sc.nextLine();

                        System.out.println("Screen " + (newscreen + 1) + " setup complete.");
                    }

                    Screens = totalScreens;

                    try {

                        saveTheatre(Screens, screenName, movieName,
                                timing, rows, seatsPerRow);

                        System.out.println("Theatre Details Saved Successfully.");

                    } catch (IOException e) {

                        System.out.println("Error saving theatre details: " + e.getMessage());

                    }

                }

            } catch (Exception e) {

                System.out.println(
                        "Error loading theatre.json.");
                return;
            }

        } else {

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

                System.out.println("Screen " + (screen + 1) + " setup complete.");
            }
            try {
                FileWriter theatre = new FileWriter("d:\\tasks\\theatre.json");

                theatre.write("[\n");

                for (int i = 0; i < Screens; i++) {

                    theatre.write(
                            "{\n"
                            + "  \"screenName\": \"" + screenName[i] + "\",\n"
                            + "  \"rows\": " + rows[i] + ",\n"
                            + "  \"seatsPerRow\": " + seatsPerRow[i] + ",\n"
                            + "  \"movieName\": \"" + movieName[i] + "\",\n"
                            + "  \"movieTime\": \"" + timing[i] + "\"\n"
                            + "}"
                    );

                    if (i != Screens - 1) {
                        theatre.write(",");
                    }
                }

                theatre.write("]");
                theatre.close();

            } catch (Exception e) {
                System.out.println("Error saving theatre data.");
            }
        }
        for (int screen = 0; screen < Screens; screen++) {

            seats[screen]
                    = new SeatStatus[rows[screen]][seatsPerRow[screen]];

            seatTicketID[screen]
                    = new String[rows[screen]][seatsPerRow[screen]];

            bookingDate[screen]
                    = new String[rows[screen]][seatsPerRow[screen]];

            for (int i = 0; i < rows[screen]; i++) {
                for (int j = 0; j < seatsPerRow[screen]; j++) {

                    seats[screen][i][j] = SeatStatus.AVAILABLE;
                    seatTicketID[screen][i][j] = "";
                    bookingDate[screen][i][j] = "";
                }
            }
        }

// Loading previous booking data
        try {
            File file = new File("d:\\tasks\\tickets.json");

            if (file.exists()) {

                String json = readFile(file.getPath());

                Pattern objectPattern
                        = Pattern.compile("\\{(.*?)\\}", Pattern.DOTALL);

                Matcher matcher = objectPattern.matcher(json);

                while (matcher.find()) {

                    String object = matcher.group(1);

                    String id = getJsonString(object, "ticketID");
                    int screen = getJsonInt(object, "screen") - 1;
                    int r = getJsonInt(object, "row") - 1;
                    int s = getJsonInt(object, "seat") - 1;

                    String date = getJsonString(object, "bookingDate");

                    if (id.isEmpty() || screen < 0 || screen >= Screens
                            || r < 0 || r >= rows[screen]
                            || s < 0 || s >= seatsPerRow[screen]) {

                        System.out.println("Invalid booking data found.");
                        continue;
                    }

                    seats[screen][r][s] = SeatStatus.BOOKED;
                    seatTicketID[screen][r][s] = id;
                    bookingDate[screen][r][s] = date;

                    int number = Integer.parseInt(id.substring(1));

                    if (number >= ticketCounter) {
                        ticketCounter = number + 1;
                    }

                    System.out.println("Restored booking: " + id);
                }

                System.out.println("Previous booking data loaded successfully.");
            }

        } catch (Exception e) {
            System.out.println("Error loading booking data: " + e.getMessage());
            e.printStackTrace();
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
                    int noOfTickets;

                    while (true) {
                        System.out.print("Enter Number of Tickets: ");

                        if (!sc.hasNextInt()) {
                            System.out.println("Invalid input! Please try again.");
                            sc.next();
                            continue;
                        }

                        noOfTickets = sc.nextInt();
                        sc.nextLine();

                        if (noOfTickets <= 0) {
                            System.out.println("Please enter at least 1 ticket.");
                        } else {
                            break;
                        }
                    }
                    for (int t = 1; t <= noOfTickets; t++) {

                        System.out.println("\nBooking Ticket " + t);

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

                                        saveBookings(Screens, rows, seatsPerRow,
                                                seats, seatTicketID, bookingDate,
                                                movieName, timing);

                                        System.out.println("Booking Data Saved Successfully.");

                                    } catch (IOException e) {
                                        seats[screen][row][seat] = SeatStatus.AVAILABLE;
                                        seatTicketID[screen][row][seat] = "";
                                        bookingDate[screen][row][seat] = "";

                                        ticketCounter--;
                                        System.out.println("Error Saving File: " + e.getMessage());
                                        System.out.println("Booking was not saved. Seat is available again.");
                                    }

                                    break;

                                } catch (Exception e) {
                                    System.out.println("Invalid input! Please try again.");
                                }
                            }

                        } else {
                            System.out.println("Seat Already Booked.");
                        }
                    }
                    break;

                case 2:

                    System.out.print("Enter Ticket ID: ");
                    String id = sc.nextLine();

                    boolean found = false;

                    int foundScreen = -1;
                    int foundRow = -1;
                    int foundSeat = -1;
                    String oldBookingDate = "";
                    String oldTicketID = "";

                    for (int s = 0; s < Screens && !found; s++) {
                        for (int i = 0; i < rows[s] && !found; i++) {
                            for (int j = 0; j < seatsPerRow[s]; j++) {

                                if (seatTicketID[s][i][j].equalsIgnoreCase(id)) {

                                    found = true;

                                    foundScreen = s;
                                    foundRow = i;
                                    foundSeat = j;

                                    oldTicketID = seatTicketID[s][i][j];
                                    oldBookingDate = bookingDate[s][i][j];

                                    seats[s][i][j] = SeatStatus.AVAILABLE;
                                    seatTicketID[s][i][j] = "";
                                    bookingDate[s][i][j] = "";

                                    break;
                                }
                            }
                        }
                    }

                    if (found) {

                        try {

                            saveBookings(Screens, rows, seatsPerRow,
                                    seats, seatTicketID, bookingDate,
                                    movieName, timing);

                            System.out.println("Cancelled Successfully.");
                            System.out.println("Booking Data Updated Successfully.");

                        } catch (IOException e) {

                            seats[foundScreen][foundRow][foundSeat] = SeatStatus.BOOKED;
                            seatTicketID[foundScreen][foundRow][foundSeat] = oldTicketID;
                            bookingDate[foundScreen][foundRow][foundSeat] = oldBookingDate;

                            System.out.println("Error updating booking data.");
                            System.out.println("Cancellation failed. Booking restored.");
                        }

                    } else {
                        System.out.println("Invalid Ticket ID.");
                    }

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

    static String readFile(String fileName) //File lo unna complete data ni read chesi string ga return chestundi.
            throws IOException {

        StringBuilder content = new StringBuilder(); //edhi file lo unna content ni store cheyadaniki use chestunnam

        BufferedReader br = new BufferedReader(new FileReader(fileName));

        String line;

        while ((line = br.readLine()) != null) {
            content.append(line);
        }

        br.close();

        return content.toString();
    }

    static int getScreenCount() {

        int count = 0;

        try {

            String json
                    = readFile("d:\\tasks\\theatre.json");

            Pattern pattern = Pattern.compile("\"screenName\"");

            Matcher matcher = pattern.matcher(json);

            while (matcher.find()) {
                count++;
            }

        } catch (Exception e) {

            return 0;
        }

        return count;
    }

    static String getJsonString(String object, String key) {

        Pattern pattern
                = Pattern.compile("\"" + key + "\"\\s*:\\s*\"(.*?)\"");

        Matcher matcher = pattern.matcher(object);

        if (matcher.find()) {
            return matcher.group(1);
        }

        return "";
    }

    static int getJsonInt(String object, String key) {

        Pattern pattern
                = Pattern.compile("\"" + key + "\"\\s*:\\s*(\\d+)");

        Matcher matcher
                = pattern.matcher(object);

        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }

        return 0;
    }

    static void saveBookings(
            int Screens,
            int[] rows,
            int[] seatsPerRow,
            SeatStatus[][][] seats,
            String[][][] seatTicketID,
            String[][][] bookingDate,
            String[] movieName,
            String[] timing) throws IOException {

        try (FileWriter fw = new FileWriter("d:\\tasks\\tickets.json")) {

            fw.write("[\n");
            boolean first = true;

            for (int s = 0; s < Screens; s++) {
                for (int i = 0; i < rows[s]; i++) {
                    for (int j = 0; j < seatsPerRow[s]; j++) {

                        if (seats[s][i][j] == SeatStatus.BOOKED) {

                            if (!first) {
                                fw.write(",\n");
                            }

                            fw.write(
                                    "{\n"
                                    + "\"ticketID\": \"" + escapeJson(seatTicketID[s][i][j]) + "\",\n"
                                    + "\"screen\": " + (s + 1) + ",\n"
                                    + "\"row\": " + (i + 1) + ",\n"
                                    + "\"seat\": " + (j + 1) + ",\n"
                                    + "\"movieName\": \"" + escapeJson(movieName[s]) + "\",\n"
                                    + "\"movieTime\": \"" + escapeJson(timing[s]) + "\",\n"
                                    + "\"bookingDate\": \"" + escapeJson(bookingDate[s][i][j]) + "\"\n"
                                    + "}"
                            );

                            first = false;
                        }
                    }
                }
            }

            fw.write("\n]");
        }
    }

    static void saveTheatre(
            int Screens,
            String[] screenName,
            String[] movieName,
            String[] timing,
            int[] rows,
            int[] seatsPerRow) throws IOException {

        try (FileWriter fw = new FileWriter("d:\\tasks\\theatre.json")) {

            fw.write("[\n");

            for (int i = 0; i < Screens; i++) {

                fw.write(
                        "{\n"
                        + "\"screenName\": \"" + escapeJson(screenName[i]) + "\",\n"
                        + "\"rows\": " + rows[i] + ",\n"
                        + "\"seatsPerRow\": " + seatsPerRow[i] + ",\n"
                        + "\"movieName\": \"" + escapeJson(movieName[i]) + "\",\n"
                        + "\"movieTime\": \"" + escapeJson(timing[i]) + "\"\n"
                        + "}"
                );

                if (i != Screens - 1) {
                    fw.write(",");
                }

                fw.write("\n");
            }

            fw.write("]");
        }
    }

    static String escapeJson(String value) {

        if (value == null) {
            return "";
        }

        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}
