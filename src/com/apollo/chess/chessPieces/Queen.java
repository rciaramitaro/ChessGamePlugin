package com.apollo.chess.chessPieces;

import com.apollo.chess.ChessPiece;
import com.apollo.chess.ChessSquare;
import org.bukkit.World;

public class Queen extends ChessPiece {

    private static boolean isPinned;
    private ChessSquare currentLocation;

    public Queen(World world, int x, int y, int z, String color) {
        super(world, x, y, z, "Queen", color);
    }

    public void setLocation(ChessSquare square) {
        currentLocation = square;
    }

    public ChessSquare getCurrentLocation() {
        return currentLocation;
    }

    public boolean isDestinationOk(ChessSquare[][] squareMatrix, ChessSquare destination) {
        int rowDistance = destination.getRow() - this.getCurrentLocation().getRow();
        int columnDistance = destination.getColumn() - this.getCurrentLocation().getColumn();

        ChessPiece rookRef = new Rook(this.getReferenceLocation().getWorld(), 0,0,0, 0, this.getColor());
        ChessPiece bishopRef = new Bishop(this.getReferenceLocation().getWorld(), 0,0,0, 0, this.getColor());

        rookRef.setLocation(this.currentLocation);
        bishopRef.setLocation(this.currentLocation);

        if (rookRef.isDestinationOk(squareMatrix, destination) || bishopRef.isDestinationOk(squareMatrix, destination))
            return true;
        return false;

    }
}
