import java.util.Scanner;

abstract class Room {
    private String roomType;
    private int numberOfBeds;
    private double size;
    private double price;

    public Room(String roomType, int numberOfBeds, double size, double price) {
        this.roomType = roomType;
        this.numberOfBeds = numberOfBeds;
        this.size = size;
        this.price = price;
    }

    public String getRoomType() {
        return roomType;
    }

    public int getNumberOfBeds() {
        return numberOfBeds;
    }

    public double getSize() {
        return size;
    }

    public double getPrice() {
        return price;
    }

    public abstract void displayRoomDetails();
}

class SingleRoom extends Room {
    public SingleRoom() {
        super("Single Room", 1, 200.0, 1500.0);
    }

    public void displayRoomDetails() {
        System.out.println("Room Type: " + getRoomType());
        System.out.println("Beds: " + getNumberOfBeds());
        System.out.println("Size: " + getSize() + " sqft");
        System.out.println("Price: ₹" + getPrice());
    }
}

class DoubleRoom extends Room {
    public DoubleRoom() {
        super("Double Room", 2, 350.0, 2500.0);
    }

    public void displayRoomDetails() {
        System.out.println("Room Type: " + getRoomType());
        System.out.println("Beds: " + getNumberOfBeds());
        System.out.println("Size: " + getSize() + " sqft");
        System.out.println("Price: ₹" + getPrice());
    }
}

class SuiteRoom extends Room {
    public SuiteRoom() {
        super("Suite Room", 3, 600.0, 5000.0);
    }

    public void displayRoomDetails() {
        System.out.println("Room Type: " + getRoomType());
        System.out.println("Beds: " + getNumberOfBeds());
        System.out.println("Size: " + getSize() + " sqft");
        System.out.println("Price: ₹" + getPrice());
    }
}

public class HotelBookingApp {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        Room single = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suite = new SuiteRoom();

        int singleAvailable = 5;
        int doubleAvailable = 3;
        int suiteAvailable = 2;

        System.out.println("===== HOTEL MENU =====");
        System.out.println("1. Single Room");
        System.out.println("2. Double Room");
        System.out.println("3. Suite Room");
        System.out.print("Enter your choice: ");

        int choice = sc.nextInt();

        System.out.println();

        switch (choice) {
            case 1:
                single.displayRoomDetails();
                System.out.println("Available: " + singleAvailable);
                break;

            case 2:
                doubleRoom.displayRoomDetails();
                System.out.println("Available: " + doubleAvailable);
                break;

            case 3:
                suite.displayRoomDetails();
                System.out.println("Available: " + suiteAvailable);
                break;

            default:
                System.out.println("Invalid choice");
        }

        sc.close();
    }
}