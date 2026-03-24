import java.io.*;
import java.util.*;

class Reservation implements Serializable {
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

class RoomInventory implements Serializable {
    private HashMap<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
    }

    public void addRoom(String type, int count) {
        inventory.put(type, count);
    }

    public int getAvailability(String type) {
        return inventory.getOrDefault(type, 0);
    }

    public void reduceRoom(String type) {
        inventory.put(type, inventory.get(type) - 1);
    }

    public HashMap<String, Integer> getAll() {
        return inventory;
    }
}

class BookingHistory implements Serializable {
    private List<Reservation> history;

    public BookingHistory() {
        history = new ArrayList<>();
    }

    public void add(Reservation r) {
        history.add(r);
    }

    public List<Reservation> getAll() {
        return history;
    }
}

class SystemState implements Serializable {
    RoomInventory inventory;
    BookingHistory history;

    public SystemState(RoomInventory inventory, BookingHistory history) {
        this.inventory = inventory;
        this.history = history;
    }
}

class PersistenceService {
    private static final String FILE = "system_state.dat";

    public static void save(SystemState state) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE))) {
            out.writeObject(state);
            System.out.println("State saved successfully");
        } catch (Exception e) {
            System.out.println("Error saving state");
        }
    }

    public static SystemState load() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILE))) {
            SystemState state = (SystemState) in.readObject();
            System.out.println("State loaded successfully");
            return state;
        } catch (Exception e) {
            System.out.println("No previous state found, starting fresh");
            return new SystemState(new RoomInventory(), new BookingHistory());
        }
    }
}

public class HotelBookingApp {
    public static void main(String[] args) {

        SystemState state = PersistenceService.load();

        RoomInventory inventory = state.inventory;
        BookingHistory history = state.history;

        if (inventory.getAll().isEmpty()) {
            inventory.addRoom("Single Room", 2);
        }

        String id = "SingleRoom-" + (history.getAll().size() + 1);

        if (inventory.getAvailability("Single Room") > 0) {
            inventory.reduceRoom("Single Room");
            Reservation r = new Reservation("Vishanth", "Single Room", id);
            history.add(r);
            System.out.println("Booked: " + id);
        } else {
            System.out.println("No rooms available");
        }

        System.out.println("Total Bookings: " + history.getAll().size());
        System.out.println("Available Rooms: " + inventory.getAvailability("Single Room"));

        PersistenceService.save(new SystemState(inventory, history));
    }
}