// This class is used to find the escape path out of the mines.
public class MineEscape {

    //Initializing 3 private instance variables.
    private Map map; // The map of the current mine.
    private int numGold; // The count of how many chunks of gold we're holding.
    private int[] numKeys; // A count of how many red, green and blue keys we're holding.

    // Constructor class that is enclosed within a try catch statement to handle exceptions.
    public MineEscape(String filename) {
        try {
            // Initializing the map variable using the provided filename.
            map = new Map(filename);
            // Setting number of gold chunks to 0.
            numGold = 0;
            // Creating an array of 3 cells.
            // Let's assume throughout the program that index 0 stays for Red keys, index 1 for Green keys and index 2 for Blue keys.
            numKeys = new int[3];
        }
        catch (IllegalArgumentException e) { System.out.println(filename); }  // Having an illegal argument exception that runs if the filename entered is incorrent, i.e. doesn't exist.
        catch (Exception e) { System.out.println(e.getMessage()); } // General exception class.
    }

    // Determines the next cell to walk onto from the current cell. We follow the rules provided in the Movement rules section.
    private MapCell findNextCell(MapCell cell) {
        // Initializing neighbor of type Mapcell.
        MapCell neighbor;
        // Looping through each neighbor (top, right, bottom, left) of the current cell.
        for (int i = 0; i < 4; i++) {
            try { neighbor = cell.getNeighbour(i); }
            // If an invalid index is accessed we continue with the next iteration.
            catch (InvalidNeighbourIndexException e) { continue; }
            // Rule 1: If cell is adjacent to the exit cell we go to the exit cell.
            if (neighbor != null && neighbor.isExit()) { return neighbor; }
        }

        // Looping through each neighbor (top, right, bottom, left) of the current cell.
        for (int i = 0; i < 4; i++) {
            try { neighbor = cell.getNeighbour(i); }
            // If an invalid index is accessed we continue with the next iteration.
            catch (InvalidNeighbourIndexException e) { continue; }
            // Rule 2: If cell is adjacent to one or more cells that contain a collectible item (a key or gold)
            // then we go to the neighbour with the smallest index containing a collectible.
            if (neighbor != null && !neighbor.isMarked() && (neighbor.isKeyCell() || neighbor.isGoldCell())) {
                return neighbor;
            }
        }

        // Looping through each neighbor (top, right, bottom, left) of the current cell.
        for (int i = 0; i < 4; i++) {
            try { neighbor = cell.getNeighbour(i); }
            // If an invalid index is accessed we continue with the next iteration.
            catch (InvalidNeighbourIndexException e) { continue; }
            // Rule 3: if curr is adjacent to one or more floor cells, go to the neighbour with the smallest
            // index that is a floor cell
            if (neighbor != null && !neighbor.isMarked() && neighbor.isFloor()) { return neighbor; }
        }

        // Looping through each neighbor (top, right, bottom, left) of the current cell.
        for (int i = 0; i < 4; i++) {
            try { neighbor = cell.getNeighbour(i); }
            // If an invalid index is accessed we continue with the next iteration.
            catch (InvalidNeighbourIndexException e) { continue; }
            // Rule 4: if cell is adjacent to one or more locked door cells, we go to the neighbour with the
            // smallest index that is a locked door cell for which we have a key of the same colour.
            if (neighbor != null && !neighbor.isMarked() && neighbor.isLockCell()) {
                if (neighbor.isRed() && numKeys[0] > 0) { return neighbor; } // The neighbor is a red lock and there is at least one red key.
                else if (neighbor.isGreen() && numKeys[1] > 0) { return neighbor; } // The neighbor is a green lock and there is at least one green key.
                else if (neighbor.isBlue() && numKeys[2] > 0) { return neighbor; } // The neighbor is a blue lock and there is atleast one blue key.
            }
        }
        // Rule 5: if none of these conditions are met, return null to indicate that you cannot proceed
        // and must backtrack
        return null;
    }

    // Determining the path from the starting point to the exit cell,
    public String findEscapePath() {
        // Initializing the ArrayStack S to store MapCell objects.
        ArrayStack<MapCell> S = new ArrayStack<>();
        // Building a path.
        String path = "Path: ";
        // Setting a boolean value to be true.
        boolean running = true;
        // Initializing startingCell as the start of the stack.
        MapCell startingCell = map.getStart();
        // Pushing it into the stack.
        S.push(startingCell);
        // Marking the start curr as in-stack.
        startingCell.markInStack();
        path += startingCell.getID() + " ";
        // While the stack isn't empty and running is true.
        while (!S.isEmpty() && running) {
            // Initializing the variable curr to peek at S.
            MapCell curr = S.peek();
            // If curr is the exit curr then setting running equal to false and ending the loop immediately.
            if (curr.isExit()) {
                running = false;
                break;
            }
            // If curr is a key curr then we determine it's color and update numKeys accordingly.
            if (curr.isKeyCell()) {
                if (curr.isRed()) numKeys[0]++; // Adding number of red keys as we pick them up.
                else if (curr.isGreen()) numKeys[1]++; // Adding number of green keys as we pick them up.
                else if (curr.isBlue()) numKeys[2]++; // Adding number of blue keys as we pick them up.
                curr.changeToFloor();
            }
            // If curr is a gold cell then we update numGold accordingly.
            if (curr.isGoldCell()) {
                numGold++; // Incrementing number of gold chunks we possess.
                curr.changeToFloor();
            }
            // To check if a tile is adjacent to lava we create a condition.
            // We create a for loop that checks for the neighbors of the cell and if curr is adjacent to lava we reset numGold to 0.
            boolean adjacentToLava = false;
            // Iterating through neighboring cells.
            for (int i = 0; i < 4; i++) {
                MapCell neighbor = curr.getNeighbour(i);
                if (neighbor != null && neighbor.isLava()) {
                    adjacentToLava = true;
                    break;
                }
            }
            if (adjacentToLava) { numGold = 0; }

            // Initializing next to findNextCell(curr) as asked for in the pseudocode.
            MapCell next = findNextCell(curr);
            // Checking if next is null to set certain conditions afterwards.
            if (next == null) {
                // Setting curr as S.pop and then marking it out of the stack.
                S.pop();
                curr.markOutStack();
            } else {
                // Updating the path string by adding next and p
                path += next.getID() + " ";
                // Pushing next onto S.
                S.push(next);
                // Then marking next as in stack.
                next.markInStack();
                if (next.isLockCell()) {
                    // We check for conditions if the key matches, since this uses up a key we keep removing those keys
                    // from their respective indexes from the array.
                    if (next.isRed()) { numKeys[0]--; } // Since we use a red key, we decrement it until we're done using it.
                    else if (next.isGreen()) { numKeys[1]--; } // Since we use a green key, we decrement it until we're done using it.
                    else if (next.isBlue()) { numKeys[2]--; } // Since we use a blue key, we decrement it until we're done using it.
                    next.changeToFloor(); // Change the locked door curr to a floor curr
                }
            }
        }
        if (!running) {
            return path + numGold + "G";
        } else {
            return "No solution found";
        }
    }



}
