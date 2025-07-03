public class Gun extends GameObject {
    private int damage;

    public Gun(String name, int damage, World world) {
        super(name, world);
        this.damage = damage;
    }

    @Override
    public void use() {
        if (owner != null) {
            owner.strength *= 2;
            System.out.println("Equipped gun. Strength is now " + owner.strength);

            owner.getInventory().remove(this);
            world.removeEntity(this);
            this.owner = null;
        }
    }
}
