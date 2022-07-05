package com.apollo.chess;

import org.bukkit.Location;
import org.bukkit.World;

public class ChessPiece {

    public ChessSquare[][] squareMatrix;
    public boolean isFirstMove = true;
    public boolean isPinned;
    public boolean isControllingSquare = false;
    public boolean isKing = false;
    public boolean isAttacking;
    private final String name;
    private final String color;
    private final Location referenceLocation;
    private ChessSquare currentLocation;

    public ChessPiece(World world, int x, int y, int z, String name, String color) {
        referenceLocation = new Location(world, x, y, z);
        this.name=name;
        this.color=color;
    }

    public String getColor() {
        return color;
    }

    public void setLocation(ChessSquare square) {
        currentLocation = square;
    }

    public ChessSquare getCurrentLocation() {
        return currentLocation;
    }

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

    public void updateControlledSquares(ChessSquare[][] squareMatrix, boolean isHighlight) {
        setControlSquares(squareMatrix);
        for (int row=0; row < 8; row++) {
            for (int column=0; column < 8; column++) {
                if (isDestinationOk(squareMatrix, squareMatrix[row][column])) {
                    if (isHighlight)
                        squareMatrix[row][column].setSquareColor("yellow");
                }
                else {
                    squareMatrix[row][column].setSquareColor(squareMatrix[row][column].getColor());
                }
            }
        }
    }

    public void setControlSquares(ChessSquare[][] squareMatrix) {
    }

    public void setMoved() {
        this.isFirstMove = false;
    }

    public void setIsInCheck(boolean b) {
    }
}
