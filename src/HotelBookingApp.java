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

class BookingService {
    private RoomInventory inventory;
    private HashMap<String, Set<String>> allocatedRooms;
    private int idCounter = 1;

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
        allocatedRooms = new HashMap<>();
    }

    public List<Reservation> processBookings(BookingQueue queue) {
        List<Reservation> confirmed = new ArrayList<>();

        while (!queue.isEmpty()) {
            Reservation r = queue.getNextRequest();
            String type = r.getRoomType();

            if (inventory.getAvailability(type) > 0) {
                String roomId = type.replace(" ", "") + "-" + idCounter++;

                allocatedRooms.putIfAbsent(type, new HashSet<>());
                Set<String> roomSet = allocatedRooms.get(type);

                if (!roomSet.contains(roomId)) {
                    roomSet.add(roomId);
                    inventory.reduceRoom(type);

                    Reservation confirmedRes = new Reservation(
                            r.getGuestName(),
                            type,
                            roomId
                    );

                    confirmed.add(confirmedRes);

                    System.out.println("Booking Confirmed for " + r.getGuestName());
                    System.out.println("Room ID: " + roomId + "\n");
                }
            } else {
                System.out.println("Booking Failed for " + r.getGuestName() + "\n");
            }
        }

        return confirmed;
    }
}

class AddOnService {
    private String serviceName;
    private double price;

    public AddOnService(String serviceName, double price) {
        this.serviceName = serviceName;
        this.price = price;
    }

    public String getServiceName() {
        return serviceName;
    }

    public double getPrice() {
        return price;
    }
}

class AddOnServiceManager {
    private HashMap<String, List<AddOnService>> serviceMap;

    public AddOnServiceManager() {
        serviceMap = new HashMap<>();
    }

    public void addService(String reservationId, AddOnService service) {
        serviceMap.putIfAbsent(reservationId, new ArrayList<>());
        serviceMap.get(reservationId).add(service);
    }

    public double calculateTotalCost(String reservationId) {
        double total = 0;
        List<AddOnService> services = serviceMap.getOrDefault(reservationId, new ArrayList<>());
        for (AddOnService s : services) {
            total += s.getPrice();
        }
        return total;
    }

    public void displayServices(String reservationId) {
        List<AddOnService> services = serviceMap.getOrDefault(reservationId, new ArrayList<>());
        System.out.println("Services for " + reservationId + ":");
        for (AddOnService s : services) {
            System.out.println(s.getServiceName() + " - ₹" + s.getPrice());
        }
        System.out.println("Total Add-On Cost: ₹" + calculateTotalCost(reservationId) + "\n");
    }
}

public class HotelBookingApp {
    public static void main(String[] args) {

        RoomInventory inventory = new RoomInventory();
        inventory.addRoom("Single Room", 2);

        BookingQueue queue = new BookingQueue();
        queue.addRequest(new Reservation("Vishanth", "Single Room", ""));
        queue.addRequest(new Reservation("Arun", "Single Room", ""));

        BookingService bookingService = new BookingService(inventory);
        List<Reservation> confirmed = bookingService.processBookings(queue);

        AddOnServiceManager manager = new AddOnServiceManager();

        AddOnService breakfast = new AddOnService("Breakfast", 200);
        AddOnService pickup = new AddOnService("Airport Pickup", 500);

        for (Reservation r : confirmed) {
            manager.addService(r.getReservationId(), breakfast);
            manager.addService(r.getReservationId(), pickup);
            manager.displayServices(r.getReservationId());
        }
    }
}