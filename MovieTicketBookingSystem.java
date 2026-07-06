import java.util.Scanner;
abstract class Booking {
    abstract void bookTicket();
}
class Movie {
    private String movieName;
    private int ticketPrice;
    private int availableSeats;
    Movie() {
        this.movieName = "Unknown";
        this.ticketPrice = 0;
        this.availableSeats = 0;
    
}

    Movie(String movieName, int ticketPrice, int availableSeats) {
        this.movieName = movieName;
        this.ticketPrice = ticketPrice;
        this.availableSeats = availableSeats;
    }
    public String getMovieName() {
        return movieName;
    }
    public int getTicketPrice() {
        return ticketPrice;
    }
    public int getAvailableSeats() {
        return availableSeats;
    }
    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }
}

class User{
    protected String userName;

    User(String userName) {
        this.userName = userName;
    }

    void displayUser() {
        System.out.println("Customer Name : " + userName);
    }
}
class MovieBooking extends User{
    MovieBooking(String customerName) {
        super(customerName);
    }
    void showDetails(String movie){
        System.out.println("Movie Selected : " + movie);
    } 
    void showDetails(String movie, int tickets){
        System.out.println("Movie Selected : " + movie);
        System.out.println("Tickets Required : " + tickets);
    }
}
class TicketBooking extends Booking {
    private Movie movie;
    private User user;
    private int tickets;
    TicketBooking(Movie movie, User user, int tickets) {
        this.movie = movie;
        this.user = user;
        this.tickets = tickets;
    }
    @Override
    void bookTicket() {
        if (movie.getAvailableSeats() == 0) {
            System.out.println("\nHOUSE FULL");
            System.out.println(movie.getMovieName() + " tickets are completely sold out.");
            return;
        }
        if (tickets > movie.getAvailableSeats()) {
            System.out.println("\nSEATS NOT AVAILABLE");
            System.out.println("Requested Tckets : " + tickets);
            System.out.println("Available Seats : " + movie.getAvailableSeats());
            System.out.println("Please choose another movie " + "or book fewer tickets.");
            return;
        }
        int totalAmount = tickets * movie.getTicketPrice();
        movie.setAvailableSeats(movie.getAvailableSeats() - tickets);
        System.out.println("\nBOOKING SUCCESSFULL");
        user.displayUser();
        System.out.println("Movie Name : " + movie.getMovieName());
        System.out.println("Tickets Price : Rs. " + movie.getTicketPrice());
        System.out.println("Tickets Booked : " + tickets);
        System.out.println("Total Amount : Rs. " + totalAmount);
        System.out.println("Remaining Seats : " + movie.getAvailableSeats());
        if(movie.getAvailableSeats() == 0) {
            System.out.println("Movie is now HOUSE FULL!");
        }
    }
}
            public class MovieTicketBookingSystem {
                public static void main(String[] args) {
                    Scanner scanner= new Scanner(System.in);
                    Movie m1 = new Movie("RRR", 250, 100);
                    Movie m2 = new Movie("Kantara", 200, 150);
                    Movie m3 = new Movie("KGF", 300, 50);
                    Movie m4 = new Movie("Baahubali", 200, 100);
                    char choice;
                    do {
                        System.out.println("\n Movie Ticket Booking System");
                        System.out.println("1. RRR (Rs.250)");
                        System.out.println("2. Kantara (Rs.200)");
                        System.out.println("3. KGF (Rs.300)");
                        System.out.println("4. Baahubali (Rs.200)");
                        System.out.println("5. Exit");
                        System.out.print("Enter your choice: ");
                        int option = scanner.nextInt();
                        scanner.nextLine(); 
                        if(option == 5){
                            System.out.println("Thank You and Have a Nice Day!");
                            break;
                        }
                        Movie selectedMovie = null;
                        switch (option) {
                            case 1:
                                selectedMovie = m1;
                                break;
                            case 2:
                                selectedMovie = m2;
                                break;
                            case 3:
                                selectedMovie = m3;
                                break;
                            case 4:
                                selectedMovie = m4;
                                break;
                            default:
                                System.out.println("Invalid choice!");
                        }
                        if (selectedMovie != null) {
                            System.out.print("Enter Customer name: ");
                            String name = scanner.nextLine();
                            System.out.print("Enter number of tickets: ");
                            int tickets = scanner.nextInt();
                            scanner.nextLine(); // Consume the newline character

                            MovieBooking mb = new MovieBooking(name);
                           mb.showDetails(selectedMovie.getMovieName());
                           mb.showDetails(selectedMovie.getMovieName(), tickets);
                           Booking booking = new TicketBooking(selectedMovie, mb, tickets);
                            booking.bookTicket();
                        }
                        System.out.print("\nDo you want to continue (Y/N)? ");
                        choice = scanner.next().charAt(0);
                    } while (choice == 'Y' || choice == 'y');
                    System.out.println("Thank You and Have a Nice Day!");
                    scanner.close();
                }
            }