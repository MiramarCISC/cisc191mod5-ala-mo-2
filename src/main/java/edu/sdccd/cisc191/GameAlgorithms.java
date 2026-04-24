package edu.sdccd.cisc191;

import java.util.*;

// importing java.util.ArrayDeque and java.util.Deque;
// also importing java.util.ArrayList;

/**
 * Module 5 Lab: Recursion + Algorithms
 *
 * Reflection Questions:
 * 1. What is the base case for your recursive binary search?
 * 2. Why is recursion natural for the bracket tree?
 * 3. Why might the iterative tile-counting method be safer on a very large map?
 * 4. Which problems in this lab felt more natural with loops, and which felt more natural with recursion?
 */
public class GameAlgorithms {

    /**
     * Searches a sorted array of match IDs recursively.
     *
     * @param sortedMatchIds sorted in ascending order
     * @param target the match ID to find
     * @return index of target, or -1 if not found
     */

    public static int findMatchRecursive(int[] sortedMatchIds, int target) {
        // replacing this stub by calling recursive helper method findMatchRecursiveHelper()
        // basically delegating back to findMatchRecursiveHelper
        return findMatchRecursiveHelper(sortedMatchIds, target, 0, sortedMatchIds.length - 1);
    }

    /**
     * Helper method for recursive binary search.
     *
     * @param sortedMatchIds sorted in ascending order
     * @param target the match ID to find
     * @param low starting index of the current search range
     * @param high ending index of the current search range
     * @return index of target, or -1 if not found
     */
    private static int findMatchRecursiveHelper(int[] sortedMatchIds, int target, int low, int high) {
        // implementing recursive binary search.
        if (low > high) {
            return -1;
        }

        int mid = (low + high) / 2;

        if(sortedMatchIds[mid] == target) {
            return mid;
        }

        if (target < sortedMatchIds[mid]) {
            return findMatchRecursiveHelper(sortedMatchIds, target, low, mid - 1);
        }
        else if (target > sortedMatchIds[mid]){  //Doesn't need to be an else-if statement
            return findMatchRecursiveHelper(sortedMatchIds, target, mid + 1, high);
        }
        //This is not needed, as it can never be reached
        return -1;
    }
    /**
     * Searches a sorted array of match IDs iteratively.
     *
     * @param sortedMatchIds sorted in ascending order
     * @param target the match ID to find
     * @return index of target, or -1 if not found
     */public static int findMatchIterative(int[] sortedMatchIds, int target) {
         // implementing an iterative (looping through the array) binary search
        //This is not a binary search, this is a linear search
        for (int i = 0; i < (sortedMatchIds.length - 1); i++) {
            if (sortedMatchIds[i] == target) {
                return i;
            }
        }
        return -1;
        //This would be a binary search
        /*
        int low = 0;
        int high = sortedMatchIds.length - 1;

        while (low <= high) { //while not empty
            int middle = (low + high)/2;

            if (target == sortedMatchIds[middle]) {
                return middle;
            } else if (target < sortedMatchIds[middle]) {
                high = middle - 1;
            } else {
                low = middle + 1;
            }
        }
        return -1;
        */
     }

     /**
      * Counts connected walkable tiles recursively.
      * Walkable tiles are represented by '.'.
      * Blocked tiles can be any other character.
      *
      * This method should count the size of the connected region starting at (startRow, startCol).
      * Count only vertical and horizontal neighbors, not diagonals.
      *
      * @param map mutable map of tiles
      * @param startRow starting row
      * @param startCol starting column
      * @return number of connected walkable tiles
      */
     public static int countConnectedTilesRecursive(char[][] map, int startRow, int startCol) {
         // implementing recursive flood-fill / connected tile counting.
         int totalRows = map.length;
         int totalColumns = map[0].length;

         // validation
         if (startRow < 0
                 || startRow >= totalRows //Could use isOutOfBounds
                 || startCol < 0
                 || startCol >= totalColumns
                 || map[startRow][startCol] != '.') {
             return 0;
         }
         /*
         if(isOutOfBounds(map, startRow, startCol) || map[startRow][startCol] != '.'){
            return 0;
         }
         */

         // represents the flood-fill part
         map[startRow][startCol] = '#';

         // represents the counted tile counting part
         return 1
                 + countConnectedTilesRecursive(map, startRow + 1, startCol)
                 + countConnectedTilesRecursive(map, startRow - 1, startCol)
                 + countConnectedTilesRecursive(map, startRow, startCol + 1)
                 + countConnectedTilesRecursive(map, startRow, startCol - 1);
     }

     /**
      * Counts connected walkable tiles iteratively using an explicit stack.
      *
      * @param map mutable map of tiles
      * @param startRow starting row
      * @param startCol starting column
      * @return number of connected walkable tiles
      */
     public static int countConnectedTilesIterative(char[][] map, int startRow, int startCol) {
         // implementing an iterative connected tile counting
         int totalRows = map.length;
         int totalColumns = map[0].length;

         // check starting position
         if (map[startRow][startCol] != '.') {
             return 0;
         }

         // adding map coords to an ArrayList to prevent any duplication
         ArrayList<int[]> tileCoordsOnMap = new ArrayList<>();
         tileCoordsOnMap.add(new int[]{startRow, startCol});

         // adding a validator for what tiles were checked to prevent duplication
         boolean[][] visitedTiles = new boolean[totalRows][totalColumns];
         visitedTiles[startRow][startCol] = true;

         // a count for the connected tiles
         int walkableTiles = 0;

         while (!tileCoordsOnMap.isEmpty()) {
             // removing the last element to prevent added coordinates from being processed infinitely
             int[] current = tileCoordsOnMap.remove(tileCoordsOnMap.size() - 1);
             int row = current[0];
             int column = current[1];

             walkableTiles++;

             int[][] directions = {
                     {1, 0}, // down
                     {-1, 0}, // up
                     {0, -1}, // left
                     {0, 1} // right
             };

             // checking directions of the current tile
             for (int[] d : directions) {
                 int currentRow = row + d[0];
                 int currentColumn = column + d[1];

                 if (currentRow >= 0
                         && currentRow < totalRows //Could use isOutOfBonds
                         && currentColumn >= 0
                         && currentColumn < totalColumns
                         && map[currentRow][currentColumn] == '.'
                         && !visitedTiles[currentRow][currentColumn]) {
                     visitedTiles[currentRow][currentColumn] = true;
                     tileCoordsOnMap.add(new int[]{currentRow, currentColumn});
                 }
             }
         }
         return walkableTiles;
     }

     /**
      * Returns true if the tournament bracket contains a match with the given target name.
      * This public method should call a recursive helper.
      *
      * @param root root of the bracket tree
      * @param target match name to search for
      * @return true if found, false otherwise
      */
     public static boolean containsMatch(BracketNode root, String target) {
         // replacing this stub by calling helper method containsMatchHelper()
         return containsMatchHelper(root, target);
     }

     /**
      * Helper method for recursive bracket tree search.
      *
      * @param node current node
      * @param target match name to search for
      * @return true if found, false otherwise
      */
     private static boolean containsMatchHelper(BracketNode node, String target) {
         // implementing a recursive tree search

         // validating
         if (node == null) {
             return false;
         }

         // checking the node itself for the target
         if (node.getMatchName().equals(target)) {
             return true;
         }

         // checking the branches of the node for the target
         return containsMatchHelper(node.getLeft(), target)
                 || containsMatchHelper(node.getRight(), target);
     }

     /**
      * Optional utility students may use if they want to avoid repeating bounds checks.
      */
     public static boolean isOutOfBounds(char[][] map, int row, int col) {
         return row < 0 || row >= map.length || col < 0 || col >= map[row].length;
     }

     /**
      * Optional utility students may use in the iterative flood-fill.
      */
     public static void pushNeighbor(Deque<CellPosition> stack, int row, int col) {
         stack.push(new CellPosition(row, col));
     }
}