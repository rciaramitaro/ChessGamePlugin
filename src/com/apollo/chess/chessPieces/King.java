package com.apollo.chess.chessPieces;

import com.apollo.chess.ChessPiece;
import com.apollo.chess.ChessSquare;
import org.bukkit.World;

public class King extends ChessPiece {

    private boolean isInCheck = false;

    public void setIsInCheck(boolean isInCheck) {
        this.isInCheck = isInCheck;
    }

    public King(World world, int x, int y, int z, String color) {
        super(world, x, y, z, "King", color);
        isKing = true;
    }

    public boolean isDestinationOk(ChessSquare[][] squareMatrix, ChessSquare destination) {
        int currRow = this.getCurrentLocation().getRow();
        int currColumn = this.getCurrentLocation().getColumn();
        int rowDistance = destination.getRow() - currRow;
        int columnDistance = destination.getColumn() - currColumn;
        int absRowDistance = Math.abs(rowDistance);
        int absColumnDistance = Math.abs(columnDistance);

        if (absRowDistance <= 1 && absColumnDistance <= 1) {
            if (absColumnDistance != 0 || absRowDistance != 0) {
                destination.setIsControlledBy(this.getColor());
            }
            if (destination.isSafe(this.getColor())) {
                if (destination.getChessPiece() != null) {
                    if (isSameColor(destination.getChessPiece().getColor())) {
                        return false;
                    }
                }
            }
            return true;
        }
        else if (absRowDistance == 0 && isFirstMove && destination.isSafe(this.getColor())){
            if (columnDistance == -2 && squareMatrix[currRow][currColumn-1].isSafe(this.getColor())) {
                return canCastleLeft(squareMatrix, destination);
            }
            else if (columnDistance == 2 && squareMatrix[currRow][currColumn+1].isSafe(this.getColor())) {
                return canCastleRight(squareMatrix, destination);
            }
        }
        return false;
    }

    private boolean canCastleRight(ChessSquare[][] squareMatrix, ChessSquare destination) {
        if (this.getCurrentLocation().getRow() == 7) { //White king is on bottom row
            if (squareMatrix[7][7].getChessPiece() != null) { //Rook exists in bottom right
                if (squareMatrix[7][6].getChessPiece() == null && squareMatrix[7][5].getChessPiece() == null) { //No obstructions
                    if (squareMatrix[7][7].getChessPiece().isFirstMove)  //The Rook hasnt moved
                        return true; //Can castle to the right
                }
            }
        }
        else if (this.getCurrentLocation().getRow() == 0) { //Black king is on top row
            if (squareMatrix[0][7].getChessPiece() != null) { //Piece exists in top right
                if (squareMatrix[0][6].getChessPiece() == null && squareMatrix[0][5].getChessPiece() == null) { //No obstructions {
                    if (squareMatrix[0][7].getChessPiece().isFirstMove)  //The Rook hasnt move
                        return true; //Can castle to the right
                }
            }
        }
        return false;
    }

    private boolean canCastleLeft(ChessSquare[][] squareMatrix, ChessSquare destination) {

            if (this.getCurrentLocation().getRow() == 7) { //White king is on bottom row
                if (squareMatrix[7][0].getChessPiece() != null) {//Piece exists in bottom left
                    if (squareMatrix[7][1].getChessPiece() == null && squareMatrix[7][2].getChessPiece() == null && squareMatrix[7][3].getChessPiece() == null) { //No obstructions
                        if (squareMatrix[7][0].getChessPiece().isFirstMove)  //The Rook hasnt move
                            return true; //Can castle to the left
                    }
                }
            }
            else if (this.getCurrentLocation().getRow() == 0) { //Black king is on top row
                if (squareMatrix[0][0].getChessPiece() != null ) //Piece exists in top left
                    if (squareMatrix[0][1].getChessPiece() == null && squareMatrix[0][2].getChessPiece() == null && squareMatrix[0][3].getChessPiece() == null) { //No obstructions
                        if (squareMatrix[0][0].getChessPiece().isFirstMove) { //The Rook hasnt move
                            return true; //Can castle to the left
                        }
                    }
            }
        //if rook is firstMove
        //if castle destination is obstructed either by piece OR control
        return false;
    }
}
