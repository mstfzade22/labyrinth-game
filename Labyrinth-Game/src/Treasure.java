public class Treasure extends GameObject {
    public Treasure(String name, World world) {
        super(name, world);
    }

    @Override
    public void use() {
        winGame();
    }

    public void winGame() {
        System.out.println("You found the treasure! You win!");
        System.exit(0);
    }
}