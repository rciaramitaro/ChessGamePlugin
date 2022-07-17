package com.apollo.chess.chessPieces;

import com.apollo.chess.ChessPiece;
import com.apollo.chess.ChessSquare;
import org.bukkit.World;

public class Rook extends ChessPiece {
    public Rook(World world, int x, int y, int z, int num, String color) {
        super(world, x, y, z, "Rook" + num, color);
    }

    public void setControlledSquares(ChessSquare[][] squareMatrix) {

        for (int row=0; row < 8; row++) {
            for (int column = 0; column < 8; column++) {
                ChessSquare destination = squareMatrix[row][column];
                int currRow = this.getCurrentLocation().getRow();
                int currColumn = this.getCurrentLocation().getColumn();
                int destRow = destination.getRow();
                int destColumn = destination.getColumn();

                if (destRow == currRow)
                    setHorizontalControl(squareMatrix, this.getCurrentLocation().getColumn(), destination.getColumn());
                else if (destColumn == currColumn)
                    setVerticalControl(squareMatrix, currRow, destRow);
            }
        }
    }
    private void setVerticalControl(ChessSquare[][] squareMatrix, int initRow, int destRow) {
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

            currSquare.setIsControlledBy(this.getColor());

            if (currSquare.getChessPiece() != null) {
                if (isMovingDown) {
                    squareMatrix[initRow + currRow][this.getCurrentLocation().getColumn()] = currSquare;
                    controlSquareMatrix[initRow + currRow][this.getCurrentLocation().getColumn()] = currSquare;
                    //controlSquareMatrix[initRow + currRow][this.getCurrentLocation().getColumn()].setIsControlledBy("");
                    //controlSquareMatrix[initRow + currRow][this.getCurrentLocation().getColumn()].setIsControlledBy(this.getColor());
                }
                else {
                    squareMatrix[initRow - currRow][this.getCurrentLocation().getColumn()] = currSquare;
                    controlSquareMatrix[initRow - currRow][this.getCurrentLocation().getColumn()] = currSquare;
                    //controlSquareMatrix[initRow - currRow][this.getCurrentLocation().getColumn()].setIsControlledBy("");
                    //controlSquareMatrix[initRow - currRow][this.getCurrentLocation().getColumn()].setIsControlledBy(this.getColor());
                }
                return;
            }

            if (isMovingDown) {
                squareMatrix[initRow + currRow][this.getCurrentLocation().getColumn()] = currSquare;
                controlSquareMatrix[initRow + currRow][this.getCurrentLocation().getColumn()] = currSquare;
                //controlSquareMatrix[initRow + currRow][this.getCurrentLocation().getColumn()].setIsControlledBy("");
                //controlSquareMatrix[initRow + currRow][this.getCurrentLocation().getColumn()].setIsControlledBy(this.getColor());
            }
            else {
                squareMatrix[initRow - currRow][this.getCurrentLocation().getColumn()] = currSquare;
                controlSquareMatrix[initRow - currRow][this.getCurrentLocation().getColumn()] = currSquare;
                //controlSquareMatrix[initRow - currRow][this.getCurrentLocation().getColumn()].setIsControlledBy("");
                //controlSquareMatrix[initRow - currRow][this.getCurrentLocation().getColumn()].setIsControlledBy(this.getColor());
            }
        }
    }


    private void setHorizontalControl(ChessSquare[][] squareMatrix, int initColumn, int destColumn) {
        int distance = destColumn - initColumn;
        int asbDistance = Math.abs(distance);
        boolean isMovingRight = distance > 0;
        ChessSquare currSquare;

        for (int currColumn = 1; currColumn <= asbDistance; currColumn++) {
            if (isMovingRight) {
                currSquare = squareMatrix[this.getCurrentLocation().getRow()][initColumn+currColumn];
            }
            else {
                currSquare = squareMatrix[this.getCurrentLocation().getRow()][initColumn-currColumn];
            }

            currSquare.setIsControlledBy(this.getColor());

            if (currSquare.getChessPiece() != null) {
                if (isMovingRight) {
                    squareMatrix[this.getCurrentLocation().getRow()][initColumn+currColumn] = currSquare;
                    controlSquareMatrix[this.getCurrentLocation().getRow()][initColumn+currColumn] = currSquare;
                    //controlSquareMatrix[this.getCurrentLocation().getRow()][initColumn+currColumn].setIsControlledBy("");
                    //controlSquareMatrix[this.getCurrentLocation().getRow()][initColumn+currColumn].setIsControlledBy(this.getColor());

                }
                else {
                    squareMatrix[this.getCurrentLocation().getRow()][initColumn-currColumn] = currSquare;
                    controlSquareMatrix[this.getCurrentLocation().getRow()][initColumn-currColumn] = currSquare;
                    //controlSquareMatrix[this.getCurrentLocation().getRow()][initColumn-currColumn].setIsControlledBy("");
                    //controlSquareMatrix[this.getCurrentLocation().getRow()][initColumn-currColumn].setIsControlledBy(this.getColor());
                }
                return;
            }

            if (isMovingRight) {
                squareMatrix[this.getCurrentLocation().getRow()][initColumn+currColumn] = currSquare;
                controlSquareMatrix[this.getCurrentLocation().getRow()][initColumn+currColumn] = currSquare;
                //controlSquareMatrix[this.getCurrentLocation().getRow()][initColumn+currColumn].setIsControlledBy("");
                //controlSquareMatrix[this.getCurrentLocation().getRow()][initColumn+currColumn].setIsControlledBy(this.getColor());
            }
            else {
                squareMatrix[this.getCurrentLocation().getRow()][initColumn-currColumn] = currSquare;
                controlSquareMatrix[this.getCurrentLocation().getRow()][initColumn-currColumn] = currSquare;
                //controlSquareMatrix[this.getCurrentLocation().getRow()][initColumn-currColumn].setIsControlledBy("");
                //controlSquareMatrix[this.getCurrentLocation().getRow()][initColumn-currColumn].setIsControlledBy(this.getColor());

            }
        }
    }
}
