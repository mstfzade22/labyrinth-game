public abstract class Entity {
    protected String name;
    protected World world;

    public Entity(String name, World world) {
        this.name = name;
        this.world = world;
        world.addEntity(this);
        if (this instanceof Executable) {
            world.addExecutable((Executable)this);
        }
    }

    public String getName() {
        return name;
    }

    public World getWorld() {
        return world;
    }
}