import java.util.*;

class InvalidCancellationException extends Exception {
    public InvalidCancellationException(String message) {
        super(message);
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
        inventory.put(roomType, inventory.get(roomType) - 1);
    }

    public void increaseRoom(String roomType) {
        inventory.put(roomType, inventory.get(roomType) + 1);
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

class BookingHistory {
    private List<Reservation> history;

    public BookingHistory() {
        history = new ArrayList<>();
    }

    public void addReservation(Reservation r) {
        history.add(r);
    }

    public boolean removeReservation(String reservationId) {
        Iterator<Reservation> it = history.iterator();
        while (it.hasNext()) {
            Reservation r = it.next();
            if (r.getReservationId().equals(reservationId)) {
                it.remove();
                return true;
            }
        }
        return false;
    }

    public Reservation findReservation(String reservationId) {
        for (Reservation r : history) {
            if (r.getReservationId().equals(reservationId)) {
                return r;
            }
        }
        return null;
    }

    public List<Reservation> getAll() {
        return history;
    }
}

class BookingService {
    private RoomInventory inventory;
    private Set<String> allocated;
    private int idCounter = 1;
    private BookingHistory history;

    public BookingService(RoomInventory inventory, BookingHistory history) {
        this.inventory = inventory;
        this.history = history;
        allocated = new HashSet<>();
    }

    public void book(String guest, String type) {
        if (inventory.getAvailability(type) > 0) {
            String id = type.replace(" ", "") + "-" + idCounter++;
            allocated.add(id);
            inventory.reduceRoom(type);
            Reservation r = new Reservation(guest, type, id);
            history.addReservation(r);
            System.out.println("Booked: " + id);
        } else {
            System.out.println("Booking failed for " + guest);
        }
    }
}

class CancellationService {
    private RoomInventory inventory;
    private BookingHistory history;
    private Stack<String> rollbackStack;

    public CancellationService(RoomInventory inventory, BookingHistory history) {
        this.inventory = inventory;
        this.history = history;
        rollbackStack = new Stack<>();
    }

    public void cancel(String reservationId) {
        try {
            Reservation r = history.findReservation(reservationId);

            if (r == null) {
                throw new InvalidCancellationException("Reservation not found");
            }

            rollbackStack.push(reservationId);

            inventory.increaseRoom(r.getRoomType());

            boolean removed = history.removeReservation(reservationId);

            if (!removed) {
                throw new InvalidCancellationException("Cancellation failed");
            }

            System.out.println("Cancelled: " + reservationId);

        } catch (InvalidCancellationException e) {
            System.out.println("Cancellation Error: " + e.getMessage());
        }
    }

    public void showRollbackStack() {
        System.out.println("\nRollback Stack:");
        for (String id : rollbackStack) {
            System.out.println(id);
        }
    }
}

public class HotelBookingApp {
    public static void main(String[] args) {

        RoomInventory inventory = new RoomInventory();
        inventory.addRoom("Single Room", 2);

        BookingHistory history = new BookingHistory();

        BookingService booking = new BookingService(inventory, history);

        booking.book("Vishanth", "Single Room");
        booking.book("Arun", "Single Room");

        CancellationService cancelService = new CancellationService(inventory, history);

        cancelService.cancel("SingleRoom-2");
        cancelService.cancel("Invalid-999");

        cancelService.showRollbackStack();

        System.out.println("\nAvailable Rooms: " + inventory.getAvailability("Single Room"));
    }
}