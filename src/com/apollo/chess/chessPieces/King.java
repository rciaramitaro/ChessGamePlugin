package com.apollo.chess.chessPieces;

import com.apollo.chess.ChessPiece;
import com.apollo.chess.ChessSquare;
import org.bukkit.World;

public class King extends ChessPiece {

    public void setIsInCheck(boolean isInCheck) {
    }

    public King(World world, int x, int y, int z, String color) {
        super(world, x, y, z, "King", color);
        isKing = true;
    }

    public void setControlledSquares(ChessSquare[][] squareMatrix) {
        for (int row=0; row < 8; row++) {
            for (int column=0; column < 8; column++) {
                ChessSquare destination = squareMatrix[row][column];
                int currRow = this.getCurrentLocation().getRow();
                int currColumn = this.getCurrentLocation().getColumn();
                int rowDistance = destination.getRow() - currRow;
                int columnDistance = destination.getColumn() - currColumn;
                int absRowDistance = Math.abs(rowDistance);
                int absColumnDistance = Math.abs(columnDistance);

                if (absRowDistance <= 1 && absColumnDistance <= 1) {
                    if (absColumnDistance != 0 || absRowDistance != 0) {
                        destination.setIsControlledBy(this.getColor());
                        controlSquareMatrix[row][column] = destination;
                    }
                }
                else {
                    if (absRowDistance == 0 && isFirstMove) {
                        if (columnDistance == -2 && squareMatrix[currRow][currColumn - 1].isSafe(this.getColor())) {
                            if (canCastleLeft(squareMatrix)) {
                                controlSquareMatrix[row][column] = squareMatrix[row][column];
                            }
                        }
                        else if (columnDistance == 2 && squareMatrix[currRow][currColumn + 1].isSafe(this.getColor())) {
                            if (canCastleRight(squareMatrix)) {
                                controlSquareMatrix[row][column] = squareMatrix[row][column];
                            }
                        }
                    }
                }
            }
        }
    }

    public boolean canCastleRight(ChessSquare[][] squareMatrix) {
        if (this.getCurrentLocation().getRow() == 7) { //White king is on bottom row
            if (squareMatrix[7][7].getChessPiece() != null) { //Rook exists in bottom right
                if (squareMatrix[7][6].getChessPiece() == null && squareMatrix[7][5].getChessPiece() == null) { //No obstructions
                    //The Rook hasn't moved
                    return squareMatrix[7][7].getChessPiece().isFirstMove; //Can castle to the right
                }
            }
        }
        else if (this.getCurrentLocation().getRow() == 0) { //Black king is on top row
            if (squareMatrix[0][7].getChessPiece() != null) { //Piece exists in top right
                if (squareMatrix[0][6].getChessPiece() == null && squareMatrix[0][5].getChessPiece() == null) { //No obstructions {
                    //The Rook hasn't move
                    return squareMatrix[0][7].getChessPiece().isFirstMove; //Can castle to the right
                }
            }
        }
        return false;
    }

    public boolean canCastleLeft(ChessSquare[][] squareMatrix) {

            if (this.getCurrentLocation().getRow() == 7) { //White king is on bottom row
                if (squareMatrix[7][0].getChessPiece() != null) {//Piece exists in bottom left
                    if (squareMatrix[7][1].getChessPiece() == null && squareMatrix[7][2].getChessPiece() == null && squareMatrix[7][3].getChessPiece() == null) { //No obstructions
                        //The Rook hasn't move
                        return squareMatrix[7][0].getChessPiece().isFirstMove; //Can castle to the left
                    }
                }
            }
            else if (this.getCurrentLocation().getRow() == 0) { //Black king is on top row
                if (squareMatrix[0][0].getChessPiece() != null ) //Piece exists in top left
                    if (squareMatrix[0][1].getChessPiece() == null && squareMatrix[0][2].getChessPiece() == null && squareMatrix[0][3].getChessPiece() == null) { //No obstructions
                        //The Rook hasn't move
                        return squareMatrix[0][0].getChessPiece().isFirstMove; //Can castle to the left
                    }
            }
        //if rook is firstMove
        //if castle destination is obstructed either by piece OR control
        return false;
    }
}
