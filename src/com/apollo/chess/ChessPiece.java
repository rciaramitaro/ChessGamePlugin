package com.apollo.chess;

import org.bukkit.Location;
import org.bukkit.World;

public class ChessPiece {

    public boolean isFirstMove = true;

    private static boolean isPinned;
    private String name;
    private String color;
    private Location referenceLocation;
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

    public boolean isSameColor (ChessPiece piece) {
        return this.getColor() == piece.getColor();
    }
}
