public class MineEscape {

    // Private variables
    private Map map;// the map of the current mine
    private int numGold;// (the count of how many chunks of gold you are holding)
    private int[] numKeys;// (a count of how many red, green, and blue keys you are holding)

    // Constructor
    public MineEscape(String filename) {
        // Exception Catching in try catch block
        try {
            // Create Map object and pass the file name
            this.map = new Map(filename);

            // Initialize the numGold & numKeys variable
            this.numGold = 0;
            this.numKeys = new int[3];

            // Assign 0 to all elements to numKeys array
            // a count of how many red, green, and blue keys you are holding
            this.numKeys[0] = 0; // Total Red Keys
            this.numKeys[1] = 0; // Total Green Keys
            this.numKeys[2] = 0; // Total Blue Keys

        } catch (Exception e) {
            // If an exception occurs, print an error message from Exception
            System.out.println(e.getMessage());
        }
    }

    // End of constructor code

    // method
    private MapCell findNextCell(MapCell cell) {
        /*
         * Choose the next cell to walk onto from curr such that:
         * the next cell is not a wall and not a lava cell
         * the next cell is not marked (i.e., it is not a cell we have already walked
         * on)
         * the next cell is decided based on the following set of ordered rules (i.e.,
         * follow the first
         * rule that applies)
         * 1. if curr is adjacent to the exit cell, go to the exit cell
         * 2. if curr is adjacent to one or more cells that contain a collectible item
         * (a key or
         * gold), go to the neighbour with the smallest index containing a collectible
         * 3. if curr is adjacent to one or more floor cells, go to the neighbour with
         * the smallest
         * index that is a floor cell
         * 4. if curr is adjacent to one or more locked door cells, go to the neighbour
         * with the
         * smallest index that is a locked door cell for which you have a key of the
         * same
         * colour.
         * 5. if none of these conditions are met, return null to indicate that you
         * cannot
         * proceed and must backtrack
         *
         */

        // Get all 4 adjacent cell
        MapCell[] adjacentCells = new MapCell[4];

        // Collectable
        MapCell Collectable = null;
        MapCell nextFloorCell = null;
        MapCell lockedDorrCellWithKey = null;
        if (cell == null) {
            return null;
        }
        adjacentCells[0] = cell.getNeighbour(0);
        adjacentCells[1] = cell.getNeighbour(1);
        adjacentCells[2] = cell.getNeighbour(2);
        adjacentCells[3] = cell.getNeighbour(3);

        for (MapCell checkCell : adjacentCells) {

            // if there is no floor to walk
            if (checkCell == null) {
                continue;
            }
            // the next cell is not a wall and not a lava cell
            if (checkCell.isLava() || checkCell.isWall()) {
                continue;
            }

            // the next cell is not marked (i.e., it is not a cell we have already walked
            // on)
            if (checkCell.isMarked()) {
                continue;
            }

            // if curr is adjacent to the exit cell, go to the exit cell

            if (checkCell.isExit()) {
                // go to exit

                return checkCell;
            }

            // if curr is adjacent to one or more cells that contain a collectible item (a
            // key or
            // gold), go to the neighbour with the smallest index containing a collectible
            // Loop is running by index so return when you find Key or Gold
            // populate collectable
            if (checkCell.isKeyCell() || checkCell.isGoldCell()) {
                if (Collectable == null) {
                    Collectable = checkCell;
                }
            }

            // if curr is adjacent to one or more floor cells, go to the neighbour with the
            // smallest
            // index that is a floor cell

            if (checkCell.isFloor()) {
                if (nextFloorCell == null) {
                    nextFloorCell = checkCell;
                }
            }

            // if curr is adjacent to one or more locked door cells, go to the neighbour
            // with the
            // smallest index that is a locked door cell for which you have a key of the
            // same
            // colour.
            if (checkCell.isLockCell() && lockedDorrCellWithKey == null) {
                // check Red Cell and Key
                if (checkCell.isRed() && this.numKeys[0] > 0) {
                    lockedDorrCellWithKey = checkCell;
                }
                // Green Cell and Key
                else if (checkCell.isGreen() && this.numKeys[1] > 0) {
                    lockedDorrCellWithKey = checkCell;
                }
                // Blue Cell and Key
                else if (checkCell.isBlue() && this.numKeys[2] > 0) {
                    lockedDorrCellWithKey = checkCell;
                }

            }

        }

        // Check Return after looping all adjacent Cells

        // if we have collectable
        if (Collectable != null) {
            return Collectable;
        }

        // if we have locked with Key

        if (lockedDorrCellWithKey != null) {
            return lockedDorrCellWithKey;
        }
        // if next is floor

        if (nextFloorCell != null) {
            return nextFloorCell;
        }

        // Otherwise Return Null

        return null;

    }

    // Method
    /*
     * Escape Path Algorithm Pseudocode
     * initialize the ArrayStack S to store MapCell objects
     * push the starting cell onto S
     * set a boolean variable running to be true
     * mark the starting cell as in-stack
     * while S is not empty and running is true
     * curr = peek at S
     * if curr is the exit cell, set running = false and end the loop immediately
     * if curr is a key cell, determine its colour and update numKeys accordingly*
     * (see note below)
     * if curr is a gold cell, update numGold accordingly* (see note below)
     * if curr is adjacent to lava, reset numGold to 0
     *
     * next = findNextCell(curr)
     * if next = null, set curr = pop off stack and mark curr as out-of-stack
     * else
     * update path string by adding next
     * push next onto S
     * mark next as in-stack
     * if next is a locked door cell
     * determine colour of locked door
     * unlock the door** and update numKeys accordingly* (see note below)
     * if running is false
     * return path (including gold count)
     * else
     * return "No solution found"
     * When picking up a piece of gold, a key, or unlocking a door, it is important
     * that you call the method
     * changeToFloor() on the cell that was containing the gold, key, or locked
     * door. This method changes it to
     * a floor cell to visually indicate that the item has been removed and it's
     * functionally required to ensure
     * the cell is no longer treated as its original type.
     ** this is based on the assumption that we have the key of the correct colour –
     * this should be checked in
     * the findNextCell() method so it doesn't have to be re-checked here.
     */
    public String findEscapePath() {
        // initialize the ArrayStack S to store MapCell objects
        ArrayStack<MapCell> s = new ArrayStack<MapCell>();
        // push starting map cell to s (stack)
        MapCell startingCell;
        startingCell = this.map.getStart();

        // OutPut String
        String OutPutPathString = "Path: ";

        // Push startingCell to Stack
        s.push(startingCell);

        // Mark the Start
        startingCell.markInStack();

        // Set running to true
        boolean running = true;
        OutPutPathString = OutPutPathString + startingCell.getID() + " ";
        // Loop the Map to build the path
        while (s.isEmpty() == false && running == true) {
            MapCell curr = s.peek(); // it will return Top from stack

            if (curr == null) {
                running = false;
                break; // Exit the loop
            }

            // if curr is the exit cell, set running = false and end the loop immediately
            if (curr != null && curr.isExit()) {
                running = false;
                break; // Exit the loop
            }

            // if curr is a key cell, determine its colour and update numKeys accordingly

            if (curr != null && curr.isKeyCell()) {
                if (curr.isRed()) {
                    this.numKeys[0] = this.numKeys[0] + 1;
                } else if (curr.isGreen()) {
                    this.numKeys[1] = this.numKeys[1] + 1;
                } else if (curr.isBlue()) {
                    this.numKeys[2] = this.numKeys[2] + 1;
                }
                // move
                curr.changeToFloor();
            }

            // if curr is a gold cell, update numGold accordingly
            if (curr != null && curr.isGoldCell()) {
                this.numGold = this.numGold + 1;

                // move
                curr.changeToFloor();
            }

            // if curr is adjacent to lava, reset numGold to 0
            if (curr != null && ((curr.getNeighbour(0) != null && curr.getNeighbour(0).isLava())
                    || (curr.getNeighbour(1) != null && curr.getNeighbour(1).isLava())
                    || (curr.getNeighbour(2) != null && curr.getNeighbour(2).isLava())
                    || (curr.getNeighbour(3) != null && curr.getNeighbour(3).isLava()))) {
                this.numGold = 0; // RESET Gold to 0

            }

            MapCell next = findNextCell(curr);
            // if next = null, set curr = pop off stack and mark curr as out-of-stack

            if (curr != null && next == null) {
                curr = s.pop();
                if (curr != null) {
                    curr.markOutStack();
                }

            } else if (next != null) {
                // System.out.println("NEXT >>" + next);
                OutPutPathString = OutPutPathString + next.getID() + " ";

                // push next onto S
                s.push(next);
                // mark next as in-stack
                next.markInStack();
                // if next is a locked door cell
                // door is opened with key if there is color key
                if (next.isLockCell()) {
                    // update the key count
                    if (curr.isRed()) {
                        this.numKeys[0] = this.numKeys[0] - 1;
                    } else if (curr.isGreen()) {
                        this.numKeys[1] = this.numKeys[1] - 1;
                    } else if (curr.isBlue()) {
                        this.numKeys[2] = this.numKeys[2] - 1;
                    }
                    // move
                    curr.changeToFloor();
                }

            } // else end

        } // loop end

        // if running is false
        if (running == false) {

            return OutPutPathString + this.numGold + "G"; // Return Path with Total # Gold
        }

        return "No solution found";

    } // method end

}
