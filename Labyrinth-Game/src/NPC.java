public class NPC extends LivingBeing {
    public NPC(String name, Room startRoom, World world) {
        super(name, 100, 10, startRoom, world);
    }

    @Override
    public void execute() {
        for (Entity e : world.getEntities()) {
            if (e instanceof Player) {
                Player player = (Player)e;
                if (player.getCurrentRoom() == this.currentRoom && health>0) {
                    System.out.println(name + " attacks you!");
                    player.health -= strength;
                    System.out.println("You have " + player.health + " health left.");
                }
            }
        }
    }
}