package com.apollo.chess;

import org.bukkit.Location;
import org.bukkit.World;

public class ChessPiece {

    public ChessSquare[][] controlSquareMatrix = new ChessSquare[8][8];
    public ChessSquare[][] availableSquareMatrix = new ChessSquare[8][8];
    public boolean isFirstMove = true;
    public boolean isKing = false;
    public boolean isPawn = false;

    private String pieceLetter;
    private final String name;
    private final String color;
    private final Location referenceLocation;
    private ChessSquare currentLocation;
    private ChessSquare prevSquare, currSquare;

    public ChessPiece(World world, int x, int y, int z, String name, String color) {
        referenceLocation = new Location(world, x, y, z);
        this.name=name;
        this.color=color;
        this.pieceLetter=determinePieceLetter();
    }

    private String determinePieceLetter() {

        String pieceLetter = Character.toString(name.charAt(0));

        if (pieceLetter.equals("K")) {
            if (name.charAt(1) == 'i')
                pieceLetter = "K";
            else
                pieceLetter = "N";
        }

        if (color == "white")
            pieceLetter = pieceLetter.toUpperCase();
        else
            pieceLetter = pieceLetter.toLowerCase();

        return pieceLetter;
    }

    public String getPieceLetter() { return pieceLetter; }

    public String getColor() {
        return color;
    }

    public void setLocation(ChessSquare square) {
        currentLocation = square;
    }

    public ChessSquare getCurrentLocation() {
        return currentLocation;
    }

    public ChessSquare getPrevLocation() { return prevSquare; }

    public Location getReferenceLocation() {
        return this.referenceLocation;
    }

    public String getName() {
        return name;
    }

    public boolean isDestinationOk(ChessSquare[][] squareMatrix, ChessSquare destination) {
        return false;
    }

    public boolean isSameColor (String color) {
        return this.getColor().equals(color);
    }

    public static boolean determineChecks(ChessSquare[][] squareMatrix, String color) {
        for (int row=0; row < 8; row++) {
            for (int column = 0; column < 8; column++) {
                ChessSquare currSquare = squareMatrix[row][column];
                if (currSquare.getChessPiece() != null) {
                    ChessPiece currPiece = currSquare.getChessPiece();
                    currPiece.setControlledSquares(squareMatrix);
                    if (currPiece.getName().equalsIgnoreCase("King") && currPiece.getColor() == color) {
                        if (currPiece.getCurrentLocation().isBeingControlledByOppositeColor(currPiece.getColor())) {
                            currPiece.setIsInCheck(true);
                            System.out.println(color + " is in CHECK");
                            return true;
                        }
                        else
                            currPiece.setIsInCheck(false);
                    }
                }
            }
        }
        return false;
    }

    public void printControlledSquares() {
        for (int row=0; row < 8; row++) {
            System.out.println();
            for (int column = 0; column < 8; column++) {
                if (controlSquareMatrix[row][column] == null)
                    System.out.print(row + "" + column + "" + " # ");
                else {
                    if (controlSquareMatrix[row][column].getIsBeingControlledByBlack())
                        System.out.print(row + "" + column + "" + " b ");
                    if (controlSquareMatrix[row][column].getIsBeingControlledByWhite())
                        System.out.print(row + "" + column + "" + " w ");
                }
            }
        }
        System.out.println();
    }
    public void printAvailableSquares() {
        for (int row=0; row < 8; row++) {
            System.out.println();
            for (int column = 0; column < 8; column++) {
                if (availableSquareMatrix[row][column] == null)
                    System.out.print(row + "" + column + "" + " # ");
                else {
                    if (availableSquareMatrix[row][column].getIsBeingControlledByBlack())
                        System.out.print(row + "" + column + "" + " b ");
                    if (availableSquareMatrix[row][column].getIsBeingControlledByWhite())
                        System.out.print(row + "" + column + "" + " w ");
                }
            }
        }
        System.out.println();
    }

    public void resetAvailableSquares() {
        for (int row=0; row < 8; row++) {
            for (int column = 0; column < 8; column++) {
                availableSquareMatrix[row][column] = null;
            }
        }
    }
    public int getAvailableSquares(ChessSquare[][] squareMatrix) {
        int availableSquares = 0;
        for (int row=0; row < 8; row++) {
            for (int column = 0; column < 8; column++) {
                ChessSquare currSquare = controlSquareMatrix[row][column];
                if (currSquare != null) { //being controlled by piece
                    ChessSquare prevLocation = currentLocation;
                    if (currSquare.getChessPiece() != null) {
                        if (currSquare.getChessPiece().getColor() != this.getColor()) {
                            ChessPiece enemyPiece = currSquare.getChessPiece();
                            //System.out.println("Currently testing square " + currSquare.getName());
                            currentLocation.movePiece(currSquare);
                            currentLocation.getChessPiece().resetControlledSquares();
                            resetBoardControlledSquares(squareMatrix);
                            currentLocation.getChessPiece().setControlledSquares(squareMatrix);
                            updateAllControlledSquares(squareMatrix);

                            if (determineChecks(squareMatrix, currentLocation.getChessPiece().getColor())) {
                                availableSquareMatrix[row][column] = null;
                                currentLocation.setSquareColor(currentLocation.getColor());
                            }
                            else {
                                availableSquareMatrix[row][column] = squareMatrix[row][column];
                                availableSquares++;
                            }

                            ChessSquare enemyLocation = currentLocation;
                            currentLocation.movePiece(prevLocation);
                            enemyLocation.setPiece(enemyPiece);
                            currentLocation.getChessPiece().resetControlledSquares();
                            resetBoardControlledSquares(squareMatrix);
                            currentLocation.getChessPiece().setControlledSquares(squareMatrix);
                            updateAllControlledSquares(squareMatrix);

                        }
                    }
                    else {
                        //System.out.println("Currently testing square " + currSquare.getName());
                        currentLocation.movePiece(currSquare);
                        currentLocation.getChessPiece().resetControlledSquares();
                        resetBoardControlledSquares(squareMatrix);
                        currentLocation.getChessPiece().setControlledSquares(squareMatrix);
                        updateAllControlledSquares(squareMatrix);

                        if (determineChecks(squareMatrix, currentLocation.getChessPiece().getColor())) {
                            availableSquareMatrix[row][column] = null;
                            currentLocation.setSquareColor(currentLocation.getColor());
                        }
                        else {
                            availableSquareMatrix[row][column] = squareMatrix[row][column];
                            availableSquares++;
                        }
                        currentLocation.movePiece(prevLocation);
                        currentLocation.getChessPiece().resetControlledSquares();
                        resetBoardControlledSquares(squareMatrix);
                        currentLocation.getChessPiece().setControlledSquares(squareMatrix);
                        updateAllControlledSquares(squareMatrix);
                    }
                }
            }
        }
        return availableSquares;
    }

    public void showAvailableSquares() {
        for (int row=0; row < 8; row++) {
            for (int column = 0; column < 8; column++) {
                if (availableSquareMatrix[row][column] != null)
                    availableSquareMatrix[row][column].setSquareColor("yellow");
            }
        }
    }

    public static void updateAllControlledSquares(ChessSquare[][] squareMatrix) {
        for (int row=0; row < 8; row++) {
            for (int column=0; column < 8; column++) {
                ChessSquare currSquare = squareMatrix[row][column];
                if (currSquare.getChessPiece() != null) {
                    currSquare.getChessPiece().setControlledSquares(squareMatrix);
                }
            }
        }
    }

    public static void resetBoardControlledSquares(ChessSquare[][] squareMatrix) {
        for (int row=0; row < 8; row++) {
            for (int column=0; column < 8; column++) {
                squareMatrix[row][column].setIsControlledBy("");
            }
        }
    }

    public void resetControlledSquares() {
        for (int row=0; row < 8; row++) {
            for (int column=0; column < 8; column++) {
                ChessSquare currSquare = controlSquareMatrix[row][column];
                if (currSquare != null)
                    controlSquareMatrix[row][column].setIsControlledBy("");
                controlSquareMatrix[row][column] = null;
            }
        }
    }

    public void setControlledSquares(ChessSquare[][] squareMatrix) {
    }

    public void setMoved() {
        this.isFirstMove = false;
    }

    public void setIsInCheck(boolean b) {
    }

    public void setRecentSquares(ChessSquare prev, ChessSquare dest) {
        this.prevSquare = prev;
        this.currSquare = dest;
    }
}
