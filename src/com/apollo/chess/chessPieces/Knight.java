package com.apollo.chess.chessPieces;

import com.apollo.chess.ChessPiece;
import com.apollo.chess.ChessSquare;
import org.bukkit.Location;
import org.bukkit.World;

public class Knight extends ChessPiece {

    private static boolean isWhite;
    private static boolean isTaken;
    private static boolean isPinned;
    private static boolean isFirstMove;
    private int num;
    private Location referenceLocation;
    private ChessSquare currentLocation;

    public Knight(World world, int x, int y, int z, int num, String color) {
        super(world, x, y, z, "Knight"+num, color);
        referenceLocation = new Location(world, x, y, z);
        this.num=num;
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


    public boolean getIsLegalMove(ChessSquare[][] squareMatrix, ChessSquare destination) {
        return false;
    }
}
