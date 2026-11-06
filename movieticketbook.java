
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

class MovieTicketBookingApp {
    private static final ObjectMapper mapper = new ObjectMapper();

    // enum for seat status
    enum SeatStatus {
        AVAILABLE, BOOKED
    }

    // ANSI Colors
    public static final String GREEN = "\u001B[32m";
    public static final String RED = "\u001B[31m";
    public static final String RESET = "\u001B[0m";

    public static void main(String[] args) {
        System.out.println("Jackson is working!");

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

                JsonNode theatreData = mapper.readTree(theatreFile);

                if (!theatreData.isArray() || theatreData.size() != Screens) {
                    throw new IOException("Invalid theatre.json format.");
                }

                int screen = 0;

                while (screen < Screens) {
                    JsonNode item = theatreData.get(screen);

                    screenName[screen] = item.path("screenName").asText();
                    movieName[screen] = item.path("movieName").asText();
                    timing[screen] = item.path("movieTime").asText();
                    rows[screen] = item.path("rows").asInt();
                    seatsPerRow[screen] = item.path("seatsPerRow").asInt();

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
                    for (int newscreen = oldScreens; newscreen < totalScreens; newscreen++) {

                        System.out.println("\nEnter details for Screen "
                                + (newscreen + 1));

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

                        // Movie Timing
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

                        // Seats per row
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
                    seats = new SeatStatus[Screens][][];
                    seatTicketID = new String[Screens][][];
                    bookingDate = new String[Screens][][];
                }

            } catch (Exception e) {

                System.out.println(
                        "Error loading theatre.json.");
                return;
            }

        } else {

            // setup for screens
            for (int newscreen = 0; newscreen < Screens; newscreen++) {

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
            try {
                ArrayNode theatreData = mapper.createArrayNode();

                for (int i = 0; i < Screens; i++) {
                    ObjectNode item = mapper.createObjectNode();

                    item.put("screenName", screenName[i]);
                    item.put("rows", rows[i]);
                    item.put("seatsPerRow", seatsPerRow[i]);
                    item.put("movieName", movieName[i]);
                    item.put("movieTime", timing[i]);

                    theatreData.add(item);
                }

                mapper.writerWithDefaultPrettyPrinter()
                        .writeValue(theatreFile, theatreData);

                System.out.println("Theatre data saved successfully.");

            } catch (IOException e) {
                System.out.println("Error saving theatre data: " + e.getMessage());
            }
        }
        for (int newscreen = 0; newscreen < Screens; newscreen++) {

            seats[newscreen] = new SeatStatus[rows[newscreen]][seatsPerRow[newscreen]];

            seatTicketID[newscreen] = new String[rows[newscreen]][seatsPerRow[newscreen]];

            bookingDate[newscreen] = new String[rows[newscreen]][seatsPerRow[newscreen]];

            for (int i = 0; i < rows[newscreen]; i++) {
                for (int j = 0; j < seatsPerRow[newscreen]; j++) {

                    seats[newscreen][i][j] = SeatStatus.AVAILABLE;
                    seatTicketID[newscreen][i][j] = "";
                    bookingDate[newscreen][i][j] = "";
                }
            }
        }

        try {
            File file = new File("D:\\tasks\\Backup\\tickets.json");

            System.out.println("Reading ticket file: " + file.getAbsolutePath());

            if (file.exists() && file.length() > 0) {
                JsonNode ticketData = mapper.readTree(file);

                if (!ticketData.isArray()) {
                    throw new IOException("Invalid tickets.json format.");
                }

                System.out.println("Tickets found: " + ticketData.size());

                for (JsonNode ticket : ticketData) {
                    String id = ticket.path("ticketID").asText();
                    int screen = ticket.path("screen").asInt() - 1;
                    int r = ticket.path("row").asInt() - 1;
                    int s = ticket.path("seat").asInt() - 1;
                    String date = ticket.path("bookingDate").asText();

                    if (screen >= 0 && screen < Screens
                            && r >= 0 && r < rows[screen]
                            && s >= 0 && s < seatsPerRow[screen]
                            && !id.isEmpty()) {

                        seats[screen][r][s] = SeatStatus.BOOKED;
                        seatTicketID[screen][r][s] = id;
                        bookingDate[screen][r][s] = date;

                        if (id.matches("T\\d+")) {
                            int number = Integer.parseInt(id.substring(1));
                            ticketCounter = Math.max(ticketCounter, number + 1);
                        }
                    }
                }

                System.out.println("Previous booking data loaded successfully.");
            } else {
                System.out.println("No Previous Data Found. Starting a new session.");
            }
        } catch (Exception e) {
            System.out.println("Error loading ticket data: " + e.getMessage());
        }

        while (true) {

            System.out.println("\n");
            System.out.println("MOVIE TICKET BOOKING SYSTEM");
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

                                        File ticketsFile = new File("D:\\tasks\\Backup\\tickets.json");
                                        ArrayNode ticketData = mapper.createArrayNode();

                                        for (int s = 0; s < Screens; s++) {
                                            for (int i = 0; i < rows[s]; i++) {
                                                for (int j = 0; j < seatsPerRow[s]; j++) {

                                                    if (seats[s][i][j] == SeatStatus.BOOKED) {
                                                        ObjectNode ticket = mapper.createObjectNode();

                                                        ticket.put("ticketID", seatTicketID[s][i][j]);
                                                        ticket.put("screen", s + 1);
                                                        ticket.put("row", i + 1);
                                                        ticket.put("seat", j + 1);
                                                        ticket.put("movieName", movieName[s]);
                                                        ticket.put("movieTime", timing[s]);
                                                        ticket.put("bookingDate", bookingDate[s][i][j]);

                                                        ticketData.add(ticket);
                                                    }
                                                }
                                            }
                                        }

                                        mapper.writerWithDefaultPrettyPrinter().writeValue(ticketsFile, ticketData);
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

    private static String readFile(String fileName) throws IOException {

        StringBuilder content = new StringBuilder();

        BufferedReader br = new BufferedReader(new FileReader(fileName));

        String line;

        while ((line = br.readLine()) != null) {
            content.append(line);
        }

        br.close();

        return content.toString();
    }

    private static int getScreenCount() {

        int count = 0;

        try {

            String json = readFile("d:\\tasks\\theatre.json");

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

    private static String getJsonString(String object, String key) {

        Pattern pattern = Pattern.compile("\"" + key + "\"\\s*:\\s*\"(.*?)\"");

        Matcher matcher = pattern.matcher(object);

        if (matcher.find()) {
            return matcher.group(1);
        }

        return "";
    }

    private static int getJsonInt(String object, String key) {

        Pattern pattern = Pattern.compile("\"" + key + "\"\\s*:\\s*(\\d+)");

        Matcher matcher = pattern.matcher(object);

        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }

        return 0;
    }

}
