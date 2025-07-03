import java.util.*;

public class Room extends Entity {
    private final int roomID;
    private final int row, col;
    private List<Door> connectedDoors = new ArrayList<>();
    private List<LivingBeing> livingBeings = new ArrayList<>();
    private List<GameObject> objects = new ArrayList<>();

    public Room(int id, int row, int col, World world) {
        super("Room-" + id, world);
        this.roomID = id;
        this.row = row;
        this.col = col;
    }

    public int getRoomID() {
        return roomID;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public void addDoor(Door door) {
        connectedDoors.add(door);
    }

    public List<Door> getDoors() {
        return connectedDoors;
    }

    public void addLivingBeing(LivingBeing lb) {
        livingBeings.add(lb);
    }

    public void removeLivingBeing(LivingBeing lb) {
        livingBeings.remove(lb);
    }

    public List<LivingBeing> getLivingBeings() {
        return livingBeings;
    }

    public void addObject(GameObject obj) {
        objects.add(obj);
        obj.setOwner(null);
    }

    public void removeObject(GameObject obj) {
        objects.remove(obj);
    }

    public List<GameObject> getObjects() {
        return objects;
    }

    public Door getDoorTo(Room target) {          // to find which door in this room’s connectedDoors list leads directly to the specified target room.
        for (Door d : connectedDoors) {
            if (d.getOtherRoom(this) == target) return d;
        }
        return null;
    }

    public void describe() {           // describes the room that you are in. shows available rooms (locked or not), objects and livingbeings in it
        System.out.println("You are in room (" + row + "," + col + ")");
        if (!connectedDoors.isEmpty()) {
            System.out.print("Doors lead to: ");
            for (Door d : connectedDoors) {
                Room other = d.getOtherRoom(this);
                System.out.print(
                        "(" + other.getRow() + "," + other.getCol() + ")" +
                                (d.isLocked() ? "(locked) " : " ")
                );
            }
            System.out.println();
        }
        if (!objects.isEmpty()) {
            System.out.print("You see: ");
            for (GameObject obj : objects) {
                System.out.print(obj.getName() + " ");
            }
            System.out.println();
        }
        for (LivingBeing lb : livingBeings) {
            if (!(lb instanceof Player)) {
                System.out.println("Also here: " + lb.getName());
            }
        }
    }
}
