import java.util.*;

class RoomInventory {
    private HashMap<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
    }

    public synchronized void addRoom(String roomType, int count) {
        inventory.put(roomType, count);
    }

    public synchronized boolean bookRoom(String roomType) {
        if (!inventory.containsKey(roomType)) return false;
        int count = inventory.get(roomType);
        if (count <= 0) return false;
        inventory.put(roomType, count - 1);
        return true;
    }

    public synchronized int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
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

    public synchronized void addRequest(Reservation r) {
        queue.add(r);
    }

    public synchronized Reservation getRequest() {
        return queue.poll();
    }
}

class BookingProcessor extends Thread {
    private BookingQueue queue;
    private RoomInventory inventory;
    private static int idCounter = 1;

    public BookingProcessor(BookingQueue queue, RoomInventory inventory) {
        this.queue = queue;
        this.inventory = inventory;
    }

    public void run() {
        while (true) {
            Reservation r;
            synchronized (queue) {
                r = queue.getRequest();
            }

            if (r == null) break;

            boolean success;
            synchronized (inventory) {
                success = inventory.bookRoom(r.getRoomType());
            }

            if (success) {
                String id = r.getRoomType().replace(" ", "") + "-" + getNextId();
                System.out.println(Thread.currentThread().getName() + " booked " + id + " for " + r.getGuestName());
            } else {
                System.out.println(Thread.currentThread().getName() + " failed booking for " + r.getGuestName());
            }
        }
    }

    private static synchronized int getNextId() {
        return idCounter++;
    }
}

public class HotelBookingApp {
    public static void main(String[] args) {

        RoomInventory inventory = new RoomInventory();
        inventory.addRoom("Single Room", 2);

        BookingQueue queue = new BookingQueue();

        queue.addRequest(new Reservation("Vishanth", "Single Room"));
        queue.addRequest(new Reservation("Arun", "Single Room"));
        queue.addRequest(new Reservation("Priya", "Single Room"));
        queue.addRequest(new Reservation("Kiran", "Single Room"));

        BookingProcessor t1 = new BookingProcessor(queue, inventory);
        BookingProcessor t2 = new BookingProcessor(queue, inventory);
        BookingProcessor t3 = new BookingProcessor(queue, inventory);

        t1.setName("Thread-1");
        t2.setName("Thread-2");
        t3.setName("Thread-3");

        t1.start();
        t2.start();
        t3.start();
    }
}