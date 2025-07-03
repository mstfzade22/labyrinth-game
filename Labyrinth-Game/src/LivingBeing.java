import java.util.*;

public abstract class LivingBeing extends Entity implements Executable {
    protected int health;
    protected int strength;
    protected Room currentRoom;
    protected List<GameObject> inventory = new ArrayList<>();

    public LivingBeing(String name, int health, int strength, Room startRoom, World world) {
        super(name, world);
        this.health = health;
        this.strength = strength;
        this.currentRoom = startRoom;
        startRoom.addLivingBeing(this);
    }

    public void move(Room room) {
        Door door = currentRoom.getDoorTo(room);
        if (door == null) {
            System.out.println("You can't move there.");
        } else if (door.isLocked()) {
            System.out.println("The door is locked.");
        } else {
            currentRoom.removeLivingBeing(this);
            currentRoom = room;
            currentRoom.addLivingBeing(this);
            System.out.println("You moved to room " + room.getRoomID());
            currentRoom.describe();
        }
    }

    public void pickUpObject(GameObject obj) {
        if (currentRoom.getObjects().contains(obj)) {
            inventory.add(obj);
            currentRoom.removeObject(obj);
            obj.setOwner(this);
            System.out.println("Picked up " + obj.getName());
        } else {
            System.out.println("No such object here.");
        }
    }

    public void dropObject(GameObject obj) {
        if (inventory.contains(obj)) {
            inventory.remove(obj);
            obj.setOwner(null);
            currentRoom.addObject(obj);
            System.out.println("Dropped " + obj.getName());
        } else {
            System.out.println("You don't have that item.");
        }
    }

    public void attack(LivingBeing other) {
        System.out.println(name + " attacks " + other.getName() + " for " + strength + " damage.");
        other.health -= strength;
        if (other.health <= 0) {
            System.out.println(other.getName() + " has been defeated!");
            currentRoom.removeLivingBeing(other);
            world.removeEntity(other);
            if (other instanceof NPC) {
                GameObject reward = world.generateReward();
                System.out.println("You found " + reward.getName() + " on the dragon.");
                inventory.add(reward);
                reward.setOwner(this);
                System.out.println("Added to inventory.");
            }
        } else {
            System.out.println(other.getName() + " has " + other.health + " health left.");
        }
    }

    @Override
    public void execute() {
    }

    public int getHealth() {
        return health;
    }

    public List<GameObject> getInventory() {
        return inventory;
    }

    public Room getCurrentRoom() {
        return currentRoom;
    }
}
