import java.util.HashMap;
import java.util.Map;

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

class RoomInventory {
    private HashMap<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
    }

    public void addRoom(String roomType, int count) {
        inventory.put(roomType, count);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public void updateAvailability(String roomType, int newCount) {
        if (inventory.containsKey(roomType)) {
            inventory.put(roomType, newCount);
        }
    }

    public void displayInventory() {
        System.out.println("===== ROOM INVENTORY =====");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " Available: " + entry.getValue());
        }
    }
}

public class HotelBookingApp {
    public static void main(String[] args) {

        Room single = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suite = new SuiteRoom();

        RoomInventory inventory = new RoomInventory();

        inventory.addRoom(single.getRoomType(), 5);
        inventory.addRoom(doubleRoom.getRoomType(), 3);
        inventory.addRoom(suite.getRoomType(), 2);

        System.out.println("===== ROOM DETAILS =====\n");

        single.displayRoomDetails();
        System.out.println("Available: " + inventory.getAvailability(single.getRoomType()) + "\n");

        doubleRoom.displayRoomDetails();
        System.out.println("Available: " + inventory.getAvailability(doubleRoom.getRoomType()) + "\n");

        suite.displayRoomDetails();
        System.out.println("Available: " + inventory.getAvailability(suite.getRoomType()) + "\n");

        inventory.displayInventory();

        inventory.updateAvailability("Single Room", 4);

        System.out.println("\nAfter Update:\n");
        inventory.displayInventory();
    }
}