public class Door extends Entity implements Activatable {
    private Room room1, room2;
    private boolean locked;

    public Door(Room r1, Room r2, boolean locked, World world) {
        super("Door-" + r1.getRoomID() + "-" + r2.getRoomID(), world);
        this.room1 = r1;
        this.room2 = r2;
        this.locked = locked;
        r1.addDoor(this);
        r2.addDoor(this);
    }

    public Room getOtherRoom(Room from) {        // for returning the room in the other side of the given door of the room
        if (from == room1) return room2;
        if (from == room2) return room1;
        throw new IllegalArgumentException("Room " + from.getName() + " is not connected to this door");
    }

    @Override
    public void open() {
        if (locked) {
            System.out.println("The door is locked.");
        } else {
            System.out.println("You open the door.");
        }
    }

    @Override
    public boolean isLocked() {
        return locked;
    }

    public void unlock() {
        locked = false;
    }

    public void lock() {
        locked = true;
    }
}
