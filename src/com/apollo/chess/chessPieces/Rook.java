package com.apollo.chess.chessPieces;

import com.apollo.chess.ChessPiece;
import com.apollo.chess.ChessSquare;
import org.bukkit.World;

public class Rook extends ChessPiece {
    public Rook(World world, int x, int y, int z, int num, String color) {
        super(world, x, y, z, "Rook" + num, color);
    }
    public boolean isDestinationOk(ChessSquare[][] squareMatrix, ChessSquare destination) {

        int currRow = this.getCurrentLocation().getRow();
        int currColumn = this.getCurrentLocation().getColumn();
        int destRow = destination.getRow();
        int destColumn = destination.getColumn();

        if (destRow == currRow && destColumn == currColumn) //same square
            return false;
        else if(destRow == currRow)
            return !isHorizontalObstructed(squareMatrix, destination, this.getCurrentLocation().getColumn(), destination.getColumn());
        else if (destColumn == currColumn)
            return !isVerticalObstructed(squareMatrix, destination, currRow, destRow);
        return false;

    }
    private boolean isVerticalObstructed(ChessSquare[][] squareMatrix, ChessSquare destination, int initRow, int destRow) {
        int distance = destRow - initRow;
        int asbDistance = Math.abs(distance);
        boolean isMovingDown = distance > 0;
        ChessSquare currSquare;


        for (int currRow = 1; currRow <= asbDistance; currRow++) {
            if (isMovingDown) {
                currSquare = squareMatrix[initRow + currRow][this.getCurrentLocation().getColumn()];
            }
            else {
                currSquare = squareMatrix[initRow-currRow][this.getCurrentLocation().getColumn()];
            }
            if (currSquare.getChessPiece() != null) {
                if (!isSameColor(currSquare.getChessPiece().getColor())) { //if found piece is opp. color then it can be captured
                    currSquare.setIsControlledBy(this.getColor());
                    if (currRow < asbDistance) {
                        return true;
                    }

                }
                else {
                    currSquare.setIsControlledBy(this.getColor());
                    return true;
                }
            }
            else
                currSquare.setIsControlledBy(this.getColor());

            if (isMovingDown) {
                squareMatrix[initRow + currRow][this.getCurrentLocation().getColumn()] = currSquare;
            }
            else {
                squareMatrix[initRow - currRow][this.getCurrentLocation().getColumn()] = currSquare;
            }
        }
        return false;
    }


    private boolean isHorizontalObstructed(ChessSquare[][] squareMatrix, ChessSquare destination, int initColumn, int destColumn) {
        int distance = destColumn - initColumn;
        int asbDistance = Math.abs(distance);
        boolean isMovingRight = distance > 0;


        for (int currColumn = 1; currColumn <= asbDistance; currColumn++) {
            if (isMovingRight) {
                ChessSquare currSquare = squareMatrix[this.getCurrentLocation().getRow()][initColumn+currColumn];
                if (currSquare.getChessPiece() != null) {
                    if (!isSameColor(currSquare.getChessPiece().getColor())) { //if found piece is opp. color then it can be captured
                        currSquare.setIsControlledBy(this.getColor());
                        if (currColumn < asbDistance) {
                            return true;
                        }
                    }
                    else {
                        currSquare.setIsControlledBy(this.getColor());
                        return true;
                    }
                }
                else {
                    currSquare.setIsControlledBy(this.getColor());
                }
                squareMatrix[this.getCurrentLocation().getRow()][initColumn+currColumn] = currSquare;
            }
            else {
                ChessSquare currSquare = squareMatrix[this.getCurrentLocation().getRow()][initColumn-currColumn];
                if (currSquare.getChessPiece() != null) {
                    if (!isSameColor(currSquare.getChessPiece().getColor())) { //if found piece is opp. color then it can be captured
                        currSquare.setIsControlledBy(this.getColor());
                        if (currColumn < asbDistance) {
                            return true;
                        }
                    }
                    else {
                        currSquare.setIsControlledBy(this.getColor());
                        return true;
                    }
                }
                else {
                    currSquare.setIsControlledBy(this.getColor());
                }
                squareMatrix[this.getCurrentLocation().getRow()][initColumn-currColumn] = currSquare;
            }
        }
        return false;
    }
}
