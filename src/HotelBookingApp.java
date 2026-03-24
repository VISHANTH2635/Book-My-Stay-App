import java.util.*;

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

    public void reduceRoom(String roomType) {
        if (inventory.containsKey(roomType) && inventory.get(roomType) > 0) {
            inventory.put(roomType, inventory.get(roomType) - 1);
        }
    }
}

class Reservation {
    private String guestName;
    private String roomType;
    private String reservationId;

    public Reservation(String guestName, String roomType, String reservationId) {
        this.guestName = guestName;
        this.roomType = roomType;
        this.reservationId = reservationId;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public String getReservationId() {
        return reservationId;
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

class BookingHistory {
    private List<Reservation> history;

    public BookingHistory() {
        history = new ArrayList<>();
    }

    public void addReservation(Reservation r) {
        history.add(r);
    }

    public List<Reservation> getAllReservations() {
        return history;
    }
}

class BookingService {
    private RoomInventory inventory;
    private HashMap<String, Set<String>> allocatedRooms;
    private int idCounter = 1;
    private BookingHistory history;

    public BookingService(RoomInventory inventory, BookingHistory history) {
        this.inventory = inventory;
        this.history = history;
        allocatedRooms = new HashMap<>();
    }

    public void processBookings(BookingQueue queue) {
        while (!queue.isEmpty()) {
            Reservation r = queue.getNextRequest();
            String type = r.getRoomType();

            if (inventory.getAvailability(type) > 0) {
                String roomId = type.replace(" ", "") + "-" + idCounter++;

                allocatedRooms.putIfAbsent(type, new HashSet<>());
                Set<String> set = allocatedRooms.get(type);

                if (!set.contains(roomId)) {
                    set.add(roomId);
                    inventory.reduceRoom(type);

                    Reservation confirmed = new Reservation(
                            r.getGuestName(),
                            type,
                            roomId
                    );

                    history.addReservation(confirmed);

                    System.out.println("Booking Confirmed: " + confirmed.getReservationId());
                }
            } else {
                System.out.println("Booking Failed for " + r.getGuestName());
            }
        }
    }
}

class BookingReportService {
    public void generateReport(List<Reservation> history) {
        System.out.println("\n===== BOOKING REPORT =====");
        for (Reservation r : history) {
            System.out.println("Guest: " + r.getGuestName() +
                    " | Room: " + r.getRoomType() +
                    " | ID: " + r.getReservationId());
        }
        System.out.println("Total Bookings: " + history.size());
    }
}

public class HotelBookingApp {
    public static void main(String[] args) {

        RoomInventory inventory = new RoomInventory();
        inventory.addRoom("Single Room", 2);
        inventory.addRoom("Suite Room", 1);

        BookingHistory history = new BookingHistory();

        BookingQueue queue = new BookingQueue();
        queue.addRequest(new Reservation("Vishanth", "Single Room", ""));
        queue.addRequest(new Reservation("Arun", "Single Room", ""));
        queue.addRequest(new Reservation("Priya", "Suite Room", ""));

        BookingService service = new BookingService(inventory, history);
        service.processBookings(queue);

        BookingReportService reportService = new BookingReportService();
        reportService.generateReport(history.getAllReservations());
    }
}