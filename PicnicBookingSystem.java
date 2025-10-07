package picnic_booking;
import java.util.*;

// ===== Abstraction =====
abstract class Booking {
    protected Customer customer;
    protected PicnicSpot spot;
    protected int people;

    public Booking(Customer customer, PicnicSpot spot, int people) {
        this.customer = customer;
        this.spot = spot;
        this.people = people;
    }

    public abstract double calculatePrice();
    public abstract void showBookingDetails();
}

// ===== Inheritance (Base Class) =====
class Place {
    private String id;
    private String name;
    private String location;
    private double price;
    private boolean isBooked;

    public Place(String id, String name, String location, double price) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.price = price;
        this.isBooked = false;
    }

    // Encapsulation
    public String getId() { return id; }
    public String getName() { return name; }
    public String getLocation() { return location; }
    public double getPrice() { return price; }
    public boolean isBooked() { return isBooked; }
    public void book() { isBooked = true; }
    public void cancelBooking() { isBooked = false; }
}

// ===== PicnicSpot Child =====
class PicnicSpot extends Place {
    public PicnicSpot(String id, String name, String location, double entryFeePerPerson) {
        super(id, name, location, entryFeePerPerson);
    }
}

// ===== Customer =====
class Customer {
    private String customerId;
    private String customerName;

    public Customer(String customerId, String customerName) {
        this.customerId = customerId;
        this.customerName = customerName;
    }

    public String getCustomerId() { return customerId; }
    public String getCustomerName() { return customerName; }
}

// ===== Polymorphism: Different Booking Behaviors =====
class SpotBooking extends Booking {
    public SpotBooking(Customer customer, PicnicSpot spot, int people) {
        super(customer, spot, people);
    }
    public double calculatePrice() {                   //Method Overriding
        return spot.getPrice() * people;               // entryFee × people
    }
    public void showBookingDetails() {                 //Method Overriding
        System.out.println("\n---Booking Details---\n" +"\nCustomer Name: "+customer.getCustomerName() +
            "\nSpot Name: " + spot.getName() + "\nPeople: " + people + "\nTotal Price = " + calculatePrice());
    }
}

// ===== Main System =====
public class PicnicBookingSystem {
    private List<PicnicSpot> spots;
    private List<Booking> bookings;
    private List<Customer> customers;

    public PicnicBookingSystem() {
        spots = new ArrayList<>();
        bookings = new ArrayList<>();
        customers = new ArrayList<>();
    }

    public void addSpot(PicnicSpot spot) { spots.add(spot); }
    public void addCustomer(Customer customer) { customers.add(customer); }

    // Booking method
    public void makeBooking(Customer customer, PicnicSpot spot, int people) {
        if (!spot.isBooked()) {
            Booking booking = new SpotBooking(customer, spot, people);
            spot.book();
            bookings.add(booking);
            booking.showBookingDetails();
        } else {
            System.out.println("Sorry, " + spot.getName() + " is already booked.\n");
        }
    }

    // Cancel booking
    public void cancelBooking(String spotId) {
        Booking bookingToRemove = null;
        for (Booking booking : bookings) {
            if (booking.spot.getId().equals(spotId)) {
                booking.spot.cancelBooking();
                bookingToRemove = booking;
                break;
            }
        }
        if (bookingToRemove != null) {
            bookings.remove(bookingToRemove);
            System.out.println("\nBooking for " + bookingToRemove.spot.getName() + " cancelled.\n");
        } else {
            System.out.println("\nNo booking found with that ID.\n");
        }
    }

    // Show available spots
    public void showAvailableSpots() {
        System.out.println("\n--- Available Picnic Spots ---\n");
        for (PicnicSpot spot : spots) {
            if (!spot.isBooked()) {
                System.out.println(spot.getId() + " - " + spot.getName() + " (" + spot.getLocation() + ") EntryFee: " + spot.getPrice());
            }
        }
    }

    // Menu
    public void menu() {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n<===== Picnic Spot Booking System =====>\n");
            System.out.println("1. Show Available Spots");
            System.out.println("2. Book a Spot");
            System.out.println("3. Cancel Booking");
            System.out.println("4. Exit");
            System.out.print("Enter choice: ");
            int choice = sc.nextInt();

            if (choice == 1) {
                showAvailableSpots();
            } 
            else if (choice == 2) {
                sc.nextLine();
                System.out.print("Enter your name: ");
                String name = sc.nextLine();
                Customer c = new Customer("CUS" + (customers.size() + 1), name);
                addCustomer(c);

                showAvailableSpots();
                System.out.print("Enter Spot ID to book: ");
                String pid = sc.nextLine();
                PicnicSpot selected = null;
                for (PicnicSpot s : spots) {
                    if (s.getId().equals(pid) && !s.isBooked()) {
                        selected = s;
                        break;
                    }
                }

                if (selected != null) {
                    System.out.print("Enter number of people: ");
                    int people = sc.nextInt();
                    makeBooking(c, selected, people);
                    System.out.print("\nConfirm Booking (Y/N): ");
                    
                    char confirm = sc.next().charAt(0);
                    
        			if(confirm == 'Y') {
        				System.out.println("\nBooking Confirmed Successfully\n");
        			}
        			else {
        				cancelBooking(pid);
        			}
                } else {
                    System.out.println("Invalid selection or already booked.\n");
                }
            } 
            else if (choice == 3) {
                sc.nextLine();
                System.out.print("Enter Spot ID to cancel booking: ");
                String pid = sc.nextLine();
                cancelBooking(pid);
            } 
            else if (choice == 4) {
                System.out.println("\nThank you for using the Picnic Spot Booking System!");
                break;
            } 
            else {
                System.out.println("\nInvalid choice.\n");
            }
        }
        sc.close();
    }

    public static void main(String[] args) {
        PicnicBookingSystem system = new PicnicBookingSystem();

        // Add picnic spots
        system.addSpot(new PicnicSpot("S001", "Green Valley", "Sylhet", 200));
        system.addSpot(new PicnicSpot("S002", "Dreamland Park", "Sylhet", 150));
        system.addSpot(new PicnicSpot("S003", "Regent Park Resort", "Sylhet", 250));


        system.menu();
    }
}
