import java.util.*;
import java.util.regex.*;

public class GameMain {
    private World world;
    private Player player;
    private Room[][] grid;
    private Scanner scanner = new Scanner(System.in);
    private Door lastLockedDoor = null;
    private Set<Room> lockedRooms = new HashSet<>();      // used set as there is no meaning to generate the same room as locked twice
    private long startTime;

    public GameMain() {
        startTime = System.currentTimeMillis();  // for measuring the time spent for completing the game

        world = new World("Labyrinth");     // creates a matrix (6x6) with creating new room obj for each cell
        grid = new Room[6][6];
        int id = 0;
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                grid[i][j] = new Room(id++, i, j, world);
            }
        }

        for (int i = 0; i < 6; i++) {                    // creates new door obj to know which rooms are connected
            for (int j = 0; j < 6; j++) {
                if (i < 5) {
                    new Door(grid[i][j], grid[i + 1][j], false, world);
                }
                if (j < 5) {
                    new Door(grid[i][j], grid[i][j + 1], false, world);
                }
            }
        }

        // place player
        player = new Player("Player", grid[0][0], world);

        Key starterKey = new Key("Key", world);     //for adding key to the inventory of the player by default
        player.getInventory().add(starterKey);
        starterKey.setOwner(player);

        Random rand = new Random();

        // places treasure chest at (5,5)
        Chest treasureChest = new Chest(grid[5][5].getRoomID(), false, world);
        grid[5][5].addObject(treasureChest);
        treasureChest.addObject(new Treasure("Treasure", world));

        // place enemy
        int ei, ej;
        do {
            ei = rand.nextInt(6);
            ej = rand.nextInt(6);
        } while ((ei == 0 && ej == 0) || (ei == 5 && ej == 5));           // if both coordinates are 0 it means the enemy is in (0,0), then the do block repeats
        new NPC("Enemy", grid[ei][ej], world);


        while (lockedRooms.size() < 10) {          // lockes exactly 10 rooms
            int ri = rand.nextInt(6);
            int rj = rand.nextInt(6);
            if ((ri == 0 && rj == 0) || (ri == ei && rj == ej) || (ri == 5 && rj == 5)) continue;
            Room r = grid[ri][rj];
            if (lockedRooms.add(r)) {                     // as lockedRooms is set its add method returns boolean value
                for (Door d : r.getDoors()) {           // if r is not in set - true, otherwise false
                    d.lock();
                }
            }
        }


        // Create 4 other chests with loot
        for (int k = 0; k < 4; k++) {
            int ci, cj;
            do {
                ci = rand.nextInt(6);
                cj = rand.nextInt(6);
            } while (ci == 5 && cj == 5);           // if the coordinate is (5,5), it returns to the loop as (5,5) is a location for treasure.
            Chest c = new Chest(grid[ci][cj].getRoomID(), false, world);
            grid[ci][cj].addObject(c);                     // adds chest as object to the room
            c.addObject(world.generateReward());           // adds random object to the chest
        }


        List<Room> freeRooms = new ArrayList<>();
        for (int r = 0; r < 6; r++) {
            for (int c = 0; c < 6; c++) {
                if ((r == 0 && c == 0)                              // start
                        || (r == ei && c == ej)               // enemy
                        || (r == 5 && c == 5))      // treasure
                    continue;
                freeRooms.add(grid[r][c]);
            }
        }

        Collections.shuffle(freeRooms, rand);

        List<GameObject> loose = new ArrayList<>();
        for (int i = 0; i < 5; i++) loose.add(new Key("Key", world));
        for (int i = 0; i < 3; i++) loose.add(new Crowbar("Crowbar", world));
        for (int i = 0; i < 2; i++) loose.add(new Gun("Gun", 20, world));

        if (loose.size() > freeRooms.size()) {
            throw new IllegalStateException("Not enough rooms to place all items uniquely");
        }
        for (int i = 0; i < loose.size(); i++) {
            freeRooms.get(i).addObject(loose.get(i));
        }


        System.out.println("Welcome to the Labyrinth!");
        start();

    }

    private void displayMap() {
        System.out.print("     ");
        for (int count = 0; count < 6; count++) System.out.print("  " + count + "   ");  // prints headers [0-5]
        for (int r = 0; r < 6; r++) {
            System.out.println("\n    +-----+-----+-----+-----+-----+-----+");
            System.out.print(" " + r + "  |");

            for (int c = 0; c < 6; c++) {
                Room rm = grid[r][c];
                char mark = ' ';

                if (r == 5 && c == 5) {            // mark the treasure room
                    mark = 'T';
                } else {
                    for (LivingBeing lb : rm.getLivingBeings()) {    // iterates through the livingBeings list in Room class to find if there is any creature in the room
                        if (lb instanceof Player) mark = 'P';
                        if (lb instanceof NPC) mark = 'E';
                    }
                    if (mark == ' ' && lockedRooms.contains(rm)) {
                        mark = 'L' ;
                    }
                }
                String output = "  " + mark + "  ";
                if (mark == 'L'){
                    output = ConsoleColors.RED_BACKGROUND + output + ConsoleColors.RESET;
                } else if (mark == 'P'){
                    output = ConsoleColors.BLUE_BACKGROUND + output + ConsoleColors.RESET;
                } else if (mark == 'T'){
                    output = ConsoleColors.GREEN_BACKGROUND + output + ConsoleColors.RESET;
                } else if (mark == 'E'){
                    output = ConsoleColors.YELLOW_BACKGROUND + output + ConsoleColors.RESET;
                }
                System.out.print(output);
                System.out.print("|");
            }
            System.out.print("\t\t");
            if (r == 1){
                System.out.print(ConsoleColors.BLUE_BACKGROUND + "  P  " + ConsoleColors.RESET + " - Player");
            } else if (r == 2){
                System.out.print(ConsoleColors.RED_BACKGROUND + "  L  " + ConsoleColors.RESET + " - Locked Room");
            } else if (r == 3){
                System.out.print(ConsoleColors.YELLOW_BACKGROUND + "  E  " + ConsoleColors.RESET + " - Enemy");
            } else if (r == 4){
                System.out.print(ConsoleColors.GREEN_BACKGROUND + "  T  " + ConsoleColors.RESET + " - Treasure");
            }
        }
        System.out.println("\n    +-----+-----+-----+-----+-----+-----+");
        System.out.println();
    }

    private void displayCommands() {
        System.out.println("Commands: info, move(r,c), open chest, use key, use crowbar, pick (name), drop (name), use (item), inventory, exit");
    }

    public void start() {
        while (true) {
            if (player.getHealth() <= 0) {                            // if the health of the player is below 0 (after the fight) the game finishes
                System.out.println("You have died. Game Over.");
                break;
            }
            displayMap();
            displayCommands();
            System.out.print("> ");
            String cmd = scanner.nextLine().trim().toLowerCase();  // for formatting user input
            handleCommand(cmd);
            world.update();    // it loops through all Executable entities and calls their execute() methods, letting things like enemy attacks or timed events happen after the player’s action.
        }                          // goes like this => (prompt → read player input → execute player action → update world state → repeat)
    }

    private void handleCommand(String cmd) {
        Room from = player.getCurrentRoom();         // the room that you are moving from
        if (cmd.equals("info")) {            // shows user in which room he is, where he can move, which rooms are locked and open
            from.describe();
            return;
        }

        // Unlock door commands
        if (cmd.equals("use key") || cmd.equals("use crowbar")) {
            if (lastLockedDoor == null) {
                System.out.println("No door is awaiting unlock.");
            } else {
                boolean used = false;         // this variable is used for giving a signal that we found the right item or not
                for (GameObject item : new ArrayList<>(player.getInventory())) {
                    if (cmd.equals("use key") && item instanceof Key) {
                        ((Key) item).unlock(lastLockedDoor);
                        used = true;
                    }
                    if (cmd.equals("use crowbar") && item instanceof Crowbar) {
                        ((Crowbar) item).forceOpen(lastLockedDoor);
                        used = true;
                    }
                    if (used) {        // if we used the item that we found, we delete it from the inventory
                        Room unlocked = lastLockedDoor.getOtherRoom(from);  // unlocked room will be the room that is the second room that the specific door connects
                        lockedRooms.remove(unlocked);
                        lastLockedDoor = null;
                        break;              // breaks the loop for only using one item per command
                    }
                }
                if (!used) System.out.println("You have nothing appropriate.");
            }
            return;
        }

        // Move command
        if (cmd.startsWith("move")) {
            Matcher m = Pattern.compile("move\\s*\\(\\s*([0-9])\\s*,\\s*([0-9])\\s*\\)").matcher(cmd);   // regex for handling user input for move
            if (!m.matches()) {      // if user input is not correct, tells user the usage form
                System.out.println("Usage: move(r,c)");
                return;
            }

            int newr = Integer.parseInt(m.group(1));   // first digit row : new row
            int newc = Integer.parseInt(m.group(2));    // second digit row : new column

            if (newr < 0 || newr > 5 || newc < 0 || newc > 5) {
                System.out.println("Out of bounds.");
                return;
            }

            Room to = grid[newr][newc];

            Door door = from.getDoorTo(to);      // finds the door that connects the target room with the current room
            if (door == null) {
                System.out.println("No direct door there.");
                return;
            }
            if (door.isLocked()) {
                System.out.println(ConsoleColors.RED_BOLD + "The door to (" + newr + "," + newc + ") is locked." + ConsoleColors.RESET);
                lastLockedDoor = door;
                return;
            }

            player.move(to);

            // the method below works like this: after moving the player to the new room, we check all the doors that the new room contains.
            // then we unlock the doors that lead to the open rooms. if the door leads to the locked room, we keep the door as locked.
            for (Door d : to.getDoors()) {
                Room neighbor = d.getOtherRoom(to);
                if (lockedRooms.contains(neighbor)) {
                    d.lock();
                } else {
                    d.unlock();
                }
            }

            // enemy fight
            for (LivingBeing lb : new ArrayList<>(to.getLivingBeings())) {
                if (lb instanceof NPC) {
                    NPC enemy = (NPC) lb;
                    System.out.println("You encounter " + enemy.getName() + "!");
                    while (player.getHealth() > 0 && enemy.getHealth() > 0) {
                        String answer;

                        // keep looping until we get “yes” or “no”
                        do {
                            System.out.print("Attack? (yes/no) ");
                            answer = scanner.nextLine().trim().toLowerCase();
                            if (!answer.equals("yes") && !answer.equals("no")) {
                                System.out.println("Please enter 'yes' or 'no'.");
                            }
                        } while (!answer.equals("yes") && !answer.equals("no"));

                        if (answer.equals("yes")) {
                            player.attack(enemy);
                            if (enemy.getHealth() <= 0) break;
                            enemy.attack(player);
                        } else {  // answer.equals("no")
                            System.out.println("You must fight until it's dead!");
                            enemy.attack(player);
                        }
                    }
                    break;
                }
            }


            return;    // serves as break
        }

        if (cmd.equals("open chest")) {                   // checks if there is a chest in the room. if yes opens it.
            for (GameObject o : from.getObjects()) {
                if (o instanceof Chest) {
                    ((Chest) o).open();
                    return;
                }
            }
            System.out.println("No chest here.");
            return;
        }

        if (cmd.startsWith("pick ")) {
            String name = cmd.substring(5);   // name is set to everything after first five characters

            for (GameObject o : new ArrayList<>(from.getObjects())) {   // iterates through the objects in room
                if (o.getName().equalsIgnoreCase(name)) {     // compares two strings ignoring the lower and upper case letters
                    if (o instanceof Treasure) {
                        long total = System.currentTimeMillis() - startTime;
                        long mins = total / 60000;
                        long secs = (total % 60000) / 1000;
                        System.out.println("You found the treasure in " + mins + " minute(s) " + secs + " second(s)! You win!");
                        System.exit(0);
                    }
                    player.pickUpObject(o);
                    return;
                }
            }

            for (GameObject o : from.getObjects()) {
                if (o instanceof Chest) {
                    Chest c = (Chest) o;
                    for (GameObject in : new ArrayList<>(c.getContents())) {   // iterates through the objects in chest
                        if (in.getName().equalsIgnoreCase(name)) {
                            if (in instanceof Treasure) {
                                long total = System.currentTimeMillis() - startTime;
                                long mins = total / 60000;
                                long secs = (total % 60000) / 1000;
                                System.out.println("You found the treasure in " + mins + " minute(s) " + secs + " second(s)! You win!");
                                System.exit(0);
                            }
                            player.getInventory().add(in);
                            in.setOwner(player);
                            c.removeObject(in);
                            System.out.println("Picked up " + in.getName());

                            return;
                        }
                    }
                }
            }

            System.out.println("No such object.");
            return;
        }

        if (cmd.startsWith("drop ")) {
            String name = cmd.substring(5);
            for (GameObject o : new ArrayList<>(player.getInventory())) {   // copy to the new array for confidently deleting the elements
                if (o.getName().equalsIgnoreCase(name)) {
                    player.dropObject(o);
                    return;
                }
            }
            System.out.println("You don't have that.");
            return;
        }

        if (cmd.equals("inventory")) {
            System.out.println("Inventory:");
            for (GameObject o : player.getInventory()) System.out.println(" - " + o.getName());
            return;
        }

        if (cmd.startsWith("use ")) {
            String name = cmd.substring(4);
            for (GameObject o : new ArrayList<>(player.getInventory())) {
                if (o.getName().equalsIgnoreCase(name)) {
                    o.use();
                    return;
                }
            }
            System.out.println("You don't have that.");
            return;
        }

        if (cmd.equals("exit")) {
            System.out.println("Goodbye.");
            System.exit(0);
        }

        System.out.println("Unknown command.");
    }

    public static void main(String[] args) {
        new GameMain();
    }
}



