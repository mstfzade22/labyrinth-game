public class Crowbar extends GameObject {
    private int durability;

    public Crowbar(String name, World world) {
        super(name, world); durability = 2;
    }

    public void forceOpen(Activatable target) {
        if (durability > 0) {
            if (target.isLocked()) {
                if (target instanceof Door) {
                    ((Door)target).unlock();
                } else if (target instanceof Chest) {
                    ((Chest)target).unlock();
                }
                durability--;
                System.out.println("Used crowbar. Durability left: " + durability);
                if (durability == 0) {
                    System.out.println("Crowbar broke.");
                    owner.getInventory().remove(this);
                    world.removeEntity(this);
                }
            } else {
                System.out.println("Target is not locked.");
            }
        } else {
            System.out.println("Crowbar is broken.");
        }
    }

    @Override
    public void use() {
        System.out.println("Specify what to force open.");
    }
}