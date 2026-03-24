import java.util.*;

class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

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

    public abstract void displayRoomDetails();
}

class SingleRoom extends Room {
    public SingleRoom() {
        super("Single Room", 1, 200.0, 1500.0);
    }

    public void displayRoomDetails() {
        System.out.println(getRoomType());
    }
}

class DoubleRoom extends Room {
    public DoubleRoom() {
        super("Double Room", 2, 350.0, 2500.0);
    }

    public void displayRoomDetails() {
        System.out.println(getRoomType());
    }
}

class SuiteRoom extends Room {
    public SuiteRoom() {
        super("Suite Room", 3, 600.0, 5000.0);
    }

    public void displayRoomDetails() {
        System.out.println(getRoomType());
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
        return inventory.getOrDefault(roomType, -1);
    }

    public void reduceRoom(String roomType) throws InvalidBookingException {
        if (!inventory.containsKey(roomType)) {
            throw new InvalidBookingException("Invalid Room Type");
        }
        int count = inventory.get(roomType);
        if (count <= 0) {
            throw new InvalidBookingException("No rooms available for " + roomType);
        }
        inventory.put(roomType, count - 1);
    }
}

class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}

class BookingQueue {
    private Queue<Reservation> queue;

    public BookingQueue() {
        queue = new LinkedList<>();
    }

    public void addRequest(Reservation r) {
        queue.add(r);
    }

    public Reservation getNextRequest() {
        return queue.poll();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}

class BookingService {
    private RoomInventory inventory;
    private Set<String> allocatedRooms;
    private int idCounter = 1;

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
        allocatedRooms = new HashSet<>();
    }

    public void processBookings(BookingQueue queue) {
        while (!queue.isEmpty()) {
            Reservation r = queue.getNextRequest();

            try {
                validate(r);

                String roomId = r.getRoomType().replace(" ", "") + "-" + idCounter++;

                if (allocatedRooms.contains(roomId)) {
                    throw new InvalidBookingException("Duplicate Room Allocation");
                }

                inventory.reduceRoom(r.getRoomType());
                allocatedRooms.add(roomId);

                System.out.println("Booking Confirmed for " + r.getGuestName());
                System.out.println("Room ID: " + roomId + "\n");

            } catch (InvalidBookingException e) {
                System.out.println("Booking Failed for " + r.getGuestName() + ": " + e.getMessage() + "\n");
            }
        }
    }

    private void validate(Reservation r) throws InvalidBookingException {
        if (r.getGuestName() == null || r.getGuestName().isEmpty()) {
            throw new InvalidBookingException("Guest name cannot be empty");
        }

        if (r.getRoomType() == null || r.getRoomType().isEmpty()) {
            throw new InvalidBookingException("Room type cannot be empty");
        }

        if (inventory.getAvailability(r.getRoomType()) == -1) {
            throw new InvalidBookingException("Room type does not exist");
        }
    }
}

public class HotelBookingApp {
    public static void main(String[] args) {

        RoomInventory inventory = new RoomInventory();
        inventory.addRoom("Single Room", 1);
        inventory.addRoom("Suite Room", 1);

        BookingQueue queue = new BookingQueue();

        queue.addRequest(new Reservation("Vishanth", "Single Room"));
        queue.addRequest(new Reservation("", "Suite Room"));
        queue.addRequest(new Reservation("Arun", "Double Room"));
        queue.addRequest(new Reservation("Priya", "Single Room"));

        BookingService service = new BookingService(inventory);
        service.processBookings(queue);
    }
}