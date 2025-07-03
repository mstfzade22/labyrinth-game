public class AidKit extends GameObject {
    public AidKit(String name, World world) {
        super(name, world);
    }

    @Override
    public void use() {
        if (owner != null) {
            owner.health = Math.min(owner.health + 20, 100);
            System.out.println("Used aid kit. Health is now " + owner.health);
            owner.getInventory().remove(this);
            world.removeEntity(this);
        }
    }
}
