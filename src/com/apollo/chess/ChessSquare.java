package com.apollo.chess;

import org.bukkit.Location;
import org.bukkit.Material;

public class ChessSquare {
    private Location location;
    private String color;
    private final int SQUARE_SIZE=10;
    private boolean isBeingControlledByBlack = false;
    private boolean isBeingControlledByWhite = false;

    private String name = "";
    private int row;
    private int column;
    private ChessPiece piece;

    public ChessSquare () {

    }

    public void setIsControlledBy(String color) {
        if (color.equalsIgnoreCase("white") || color.equalsIgnoreCase("black")) {
            if (color.equalsIgnoreCase("white")) {
                isBeingControlledByWhite = true;
            }
            if (color.equalsIgnoreCase("black")) {
                isBeingControlledByBlack = true;
            }
        }
        else {
            isBeingControlledByWhite = false;
            isBeingControlledByBlack = false;
        }
    }

    public boolean isSafe(String color) {
        if (color.equalsIgnoreCase("white"))
            return !isBeingControlledByBlack;
        if (color.equalsIgnoreCase("black"))
            return !isBeingControlledByWhite;

        return true;
    }

    public boolean isBeingControlledByOppositeColor(String currColor) {
        if (currColor.equalsIgnoreCase("white")) {
            return isBeingControlledByBlack;
        }
        else if (currColor.equalsIgnoreCase("black")) {
            System.out.println("is " + name + " controlled by white? " + isBeingControlledByWhite);
            return isBeingControlledByWhite;
        }
        return false;
    }

    public boolean getIsBeingControlledByWhite() {
        return isBeingControlledByWhite;
    }

    public boolean getIsBeingControlledByBlack() {
        return isBeingControlledByBlack;
    }

    public String getColor() {
        return color;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public int getColumn() {
        return column;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getRow() {
        return row;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public ChessPiece getChessPiece() {
        return piece;
    }

    public void movePiece(ChessSquare destination) {
        destination.setPiece(piece);
        piece.setLocation(destination);
        removePiece();
    }
    public void setPiece(ChessPiece piece) {
        this.piece = piece;
        copyFromReference(piece);
        piece.setLocation(this);
    }

    private void copyFromReference(ChessPiece piece) {


        Location reference = new Location(piece.getReferenceLocation().getWorld(), piece.getReferenceLocation().getX(), piece.getReferenceLocation().getY(), piece.getReferenceLocation().getZ());
        reference.setX(piece.getReferenceLocation().getX()-1);
        reference.setY(piece.getReferenceLocation().getY()+1);
        reference.setZ(piece.getReferenceLocation().getZ()-1);

        Location destination = new Location(location.getWorld(), location.getX(), location.getY(), location.getZ());


        for (int y = 0; y < 20; y++) {
            for (int z = 0; z < 10; z++) {
                for (int x = 0; x < 10; x++) {
                    destination.setX(location.getX() + x);
                    destination.setZ(location.getZ() + z);
                    destination.setY(location.getY() + 1 + y);
                    reference.setX(piece.getReferenceLocation().getX() - 1 + x);
                    reference.setZ(piece.getReferenceLocation().getZ() - 1 + z);
                    reference.setY(piece.getReferenceLocation().getY() + y);
                    Material copiedMaterial = reference.getBlock().getType();
                    destination.getBlock().setType(copiedMaterial);
                }
            }
        }
    }

    public void removePiece() {
        this.piece = null;
        Location destination = new Location(location.getWorld(), location.getX(), location.getY(), location.getZ());
        for (int y = 0; y < 20; y++) {
            for (int z = 0; z < 10; z++) {
                for (int x = 0; x < 10; x++) {
                    destination.setX(location.getX() - 1 + x);
                    destination.setZ(location.getZ() - 1 + z);
                    destination.setY(location.getY() + 1 + y);
                    destination.getBlock().setType(Material.AIR);
                }
            }
        }
    }
    public void setWorldLocation(Location worldLocation) {
        this.location = worldLocation;
    }

    public Location getLocation() {
        return this.location;
    }

    public void setSquareColor(String color) {
        Material material = null;

        if (color.equals("yellow"))
            material = Material.YELLOW_CONCRETE;
        else if (color.equals("white")) {
            material = Material.WHITE_CONCRETE;
            this.color = color;
        }
        else if (color.equals("black")) {
            this.color = color;
            material = Material.BLACK_CONCRETE;
        }

        Location initialLocation = new Location(location.getWorld(), location.getX(), location.getY(), location.getZ());
        int xInitial = (int) initialLocation.getX();
        int zInitial = (int) initialLocation.getZ();

        for (int z = 0; z <= SQUARE_SIZE - 1; z++) {
            initialLocation.setZ(zInitial + z);
            for (int x = 0; x <= SQUARE_SIZE - 1; x++) {
                initialLocation.setX(xInitial + x);
                initialLocation.getBlock().setType(material);
            }
        }
        initialLocation.setZ(zInitial);
        initialLocation.setX(xInitial);
    }
}
