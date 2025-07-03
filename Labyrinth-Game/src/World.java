import java.util.*;

public class World {
    private String name;
    private List<Entity> entities = new ArrayList<>();
    private List<Executable> executables = new ArrayList<>();
    private Random random = new Random();

    public World(String name) {
        this.name = name;
    }

    public void addEntity(Entity e) {
        entities.add(e);
    }

    public void removeEntity(Entity e) {
        entities.remove(e);
        if (e instanceof Executable) executables.remove(e);
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public void addExecutable(Executable exe) {
        executables.add(exe);
    }

    public void update() {
        for (Executable exe : new ArrayList<>(executables)) exe.execute();
    }

    public GameObject generateReward() {                  // adds random object to the chest
        switch (random.nextInt(4)) {
            case 0:
                return new Key("Key", this);
            case 1:
                return new AidKit("AidKit", this);
            case 2:
                return new Crowbar("Crowbar", this);
            default:
                return new Gun("Gun", 20, this);
        }
    }
}
