package com.apollo.chess.chessPieces;

import com.apollo.chess.ChessPiece;
import com.apollo.chess.ChessSquare;
import org.bukkit.World;

public class Knight extends ChessPiece {
    public Knight(World world, int x, int y, int z, int num, String color) {
        super(world, x, y, z, "Knight"+num, color);
    }

    public boolean isDestinationOk(ChessSquare[][] squareMatrix, ChessSquare destination) {
        int rowDistance = destination.getRow() - this.getCurrentLocation().getRow();
        int columnDistance = destination.getColumn() - this.getCurrentLocation().getColumn();

        if (destination.getChessPiece() != null)
            if (isSameColor(destination.getChessPiece().getColor()))
                return false;

        if (((Math.abs(rowDistance)==1) && (Math.abs(columnDistance)==2)) || ((Math.abs(rowDistance)==2) && (Math.abs(columnDistance)==1))) {
            destination.setIsControlledBy(this.getColor());
            return true;
        }
        return false;
    }
}
