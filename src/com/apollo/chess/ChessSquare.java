package com.apollo.chess;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

public class ChessSquare {
    private Location location;
    private String name = "";
    private int row;
    private int column;
    private static final int SQUARE_SIZE = 10;
    private ChessPiece piece;
    private boolean hasPiece;
    private boolean isHighlighted;

    public ChessSquare () {

    }

    public boolean isHasPiece() {
        return hasPiece;
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
        hasPiece = true;
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
        hasPiece=false;
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
}
