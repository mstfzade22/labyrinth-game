import java.util.*;

public class Chest extends GameObject implements Activatable {
    private boolean locked;
    private List<GameObject> contents;

    public Chest(int locationID, boolean locked, World world) {
        super("Chest-" + locationID, world);
        this.locked  = locked;
        this.contents = new ArrayList<>();
    }

    public void addObject(GameObject obj) {
        contents.add(obj);
    }

    public void removeObject(GameObject obj) {
        contents.remove(obj);
    }

    public List<GameObject> getContents() {
        return new ArrayList<>(contents);
    }

    @Override
    public boolean isLocked() {
        return locked;
    }

    public void unlock() {
        locked = false;
        System.out.println("The chest is now unlocked.");
    }

    @Override
    public void open() {
        if (locked) {
            System.out.println("The chest is locked.");
            return;
        }
        if (contents.isEmpty()) {
            System.out.println("The chest is empty.");
            return;
        }
        System.out.println("Chest contains:");
        for (GameObject obj : contents) {
            System.out.println(" - " + obj.getName());
        }
    }

    @Override
    public void use() {

    }
}
