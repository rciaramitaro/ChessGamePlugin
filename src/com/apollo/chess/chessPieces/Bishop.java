package com.apollo.chess.chessPieces;

import com.apollo.chess.ChessPiece;
import com.apollo.chess.ChessSquare;
import org.bukkit.World;

public class Bishop extends ChessPiece {

    private static boolean isPinned;

    public Bishop(World world, int x, int y, int z, int num, String color) {
        super(world, x, y, z, "Bishop"+num, color);
    }

    public boolean isDestinationOk(ChessSquare[][] squareMatrix, ChessSquare destination) {
        int rowDistance = destination.getRow() - this.getCurrentLocation().getRow();
        int columnDistance = destination.getColumn() - this.getCurrentLocation().getColumn();

        //dont capture same color pieces
        if (destination.getChessPiece() != null)
            if (isSameColor(destination.getChessPiece()))
                return false;

        if (Math.abs(rowDistance) == Math.abs(columnDistance)) {
            return !isDestinationObstructed(squareMatrix, rowDistance, columnDistance);
        }
        return false;
    }

    private boolean isDestinationObstructed(ChessSquare[][] squareMatrix, int rowDistance, int columnDistance) {
        boolean isMovingDown = rowDistance > 0;
        boolean isMovingRight = columnDistance > 0;
        int absDistance = Math.abs(rowDistance);
        int initialRow = this.getCurrentLocation().getRow();
        int initialColumn = this.getCurrentLocation().getColumn();

        //same square
        if(absDistance == 0) {
            System.out.println("Problem trying to move Bishop to same square");
            return true;
        }

        for (int currDistance = 1; currDistance < absDistance; currDistance++) {
            //Bishop is trying to move down and right
            if (isMovingDown && isMovingRight) {
                if(squareMatrix[initialRow+currDistance][initialColumn+currDistance].getChessPiece() != null)
                    return true;
            }
            //Bishop is trying to move down and left
            else if (isMovingDown && !isMovingRight) {
                if(squareMatrix[initialRow+currDistance][initialColumn-currDistance].getChessPiece() != null)
                    return true;

            }
            //Bishop is trying to move up and right
            else if (!isMovingDown && isMovingRight) {
                if(squareMatrix[initialRow-currDistance][initialColumn+currDistance].getChessPiece() != null)
                    return true;
            }
            //Bishop is trying to move up and left
            else {
                if(squareMatrix[initialRow-currDistance][initialColumn-currDistance].getChessPiece() != null)
                    return true;
            }
        }
        return false;
    }
}
