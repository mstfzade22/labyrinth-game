# Labyrinth Game

A text-based adventure game in Java where a player explores a 6×6 labyrinth, collects items, avoids enemies, and attempts to retrieve a treasure.

## Requirements

- Java JDK 11 or later  
- An IDE or development environment (e.g., IntelliJ IDEA)


## How to Run

1. **Open the Project**  
   - Launch your IDE.  
   - Click **Open** and select the root folder of the Labyrinth project.

2. **Configure Project SDK**  
   - Go to **File → Project Structure → Project**.  
   - Ensure the **Project SDK** is set to **Java 11** or above.  
     - If not, click **New → JDK** and select the path to your JDK.

3. **Build and Run**  
   - In the Project panel, locate `GameMain.java` under `src`.  
   - Right‑click on `GameMain.java` and select **Run 'GameMain.main()'**.

## How to Play

Once the game starts, you'll see a 6×6 grid:

- `P` is the Player  
- `E` is an Enemy  
- `L` is a Locked room  
- `T` is the Treasure room  

### Available Commands

| Command         | Description                         |
| --------------- | ----------------------------------- |
| `info`          | Show current room details           |
| `move(r,c)`     | Move to room at row `r`, column `c` |
| `open chest`    | Open a chest in the current room    |
| `use key`       | Use a key to unlock a door          |
| `use crowbar`   | Use a crowbar to force open a door  |
| `pick (name)`   | Pick up an item                     |
| `drop (name)`   | Drop an item                        |
| `use (item)`    | Use a specific item (e.g., a gun)   |
| `inventory`     | View your inventory                 |
| `exit`          | Quit the game                       |

## Notes

- The player starts with **one key**.  
- **Ten** rooms are randomly locked.  
- There is **one enemy** placed randomly.  
- You **win** by reaching the treasure room at `(5,5)`.

## Troubleshooting

- **No run option for `GameMain.java`:**  
  Ensure it’s inside a correctly configured Java package.

- **Cannot find classes:**  
  Rebuild the project via **Build → Rebuild Project**.
