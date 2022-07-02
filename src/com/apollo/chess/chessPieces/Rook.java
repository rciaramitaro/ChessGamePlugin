package com.apollo.chess.chessPieces;

import com.apollo.chess.ChessPiece;
import com.apollo.chess.ChessSquare;
import org.bukkit.World;

public class Rook extends ChessPiece {

    public Rook(World world, int x, int y, int z, int num, String color) {
        super(world, x, y, z, "Rook" + num, color);
    }
    public boolean isDestinationOk(ChessSquare[][] squareMatrix, ChessSquare destination) {

        //dont capture same color pieces
        if (destination.getChessPiece() != null)
            if (isSameColor(destination.getChessPiece()))
                return false;

        //move Rook along same column
        if (this.getCurrentLocation().getColumn() == destination.getColumn()) {
            return !isDestinationObstructed(squareMatrix, destination.getColumn(), this.getCurrentLocation().getRow(), destination.getRow());
        }
        //move Rook along same row
        else if (this.getCurrentLocation().getRow() == destination.getRow()) {
            return !isDestinationObstructed(squareMatrix, destination.getRow(), this.getCurrentLocation().getColumn(), destination.getColumn());
        }
        return false;
    }
    private boolean isDestinationObstructed(ChessSquare[][] squareMatrix, int sameLine, int currLine, int destLine) {
            int distance = destLine - currLine;
            boolean isMovingDownOrRight = distance > 0;
            int absDistance = Math.abs(distance);

            //same square
            if(absDistance == 0) {
                System.out.println("Problem trying to move Rook to same square");
                return true;
            }

            for (int currDistance = 1; currDistance < absDistance; currDistance++) {
                if (sameLine == this.getCurrentLocation().getColumn()) { //Rook is trying to move up or down
                    if (isMovingDownOrRight) { //Rook is trying to move down
                        if (squareMatrix[currLine+currDistance][sameLine].getChessPiece() != null) {
                            System.out.println("Problem trying to move rook down");
                            return true;
                        }
                    }
                    else { //Rook is trying to move up
                        if (squareMatrix[currLine-currDistance][sameLine].getChessPiece() != null) {
                            System.out.println("Problem trying to move rook up");
                            return true;
                        }
                    }
                }
                else { //Rook is trying to move left or right
                    if (isMovingDownOrRight) { //Rook is trying to move right
                        if (squareMatrix[sameLine][currLine+currDistance].getChessPiece() != null) {
                            System.out.println("Problem trying to move rook right");
                            return true;
                        }
                    }
                    else { //Rook is trying to move left
                        if (squareMatrix[sameLine][currLine-currDistance].getChessPiece() != null) {
                            System.out.println("Problem trying to move rook left");
                            return true;
                        }
                    }
                }
            }
            isFirstMove = false;
            return false;
    }
}
