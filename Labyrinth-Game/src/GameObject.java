public abstract class GameObject extends Entity {
    protected LivingBeing owner;

    public GameObject(String name, World world) {
        super(name, world);
    }

    public void setOwner(LivingBeing owner) {
        this.owner = owner;
    }

    public abstract void use();
}