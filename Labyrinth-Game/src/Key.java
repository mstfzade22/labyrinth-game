public class Key extends GameObject {
    public Key(String name, World world) {
        super(name, world);
    }

    public void unlock(Activatable target) {
        if (target.isLocked()) {
            if (target instanceof Door) {
                ((Door)target).unlock();
            } else if (target instanceof Chest) {
                ((Chest)target).unlock();
            }
            System.out.println("Used key to unlock " + ((Entity)target).getName());
            owner.getInventory().remove(this);
            world.removeEntity(this);
        } else {
            System.out.println("Target is not locked.");
        }
    }

    @Override
    public void use() {
        System.out.println("Specify what to unlock.");
    }
}