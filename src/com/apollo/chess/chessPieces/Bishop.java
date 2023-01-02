package com.apollo.chess.chessPieces;

import com.apollo.chess.ChessPiece;
import com.apollo.chess.ChessSquare;
import org.bukkit.World;

public class Bishop extends ChessPiece {

    public Bishop(World world, int x, int y, int z, int num, String color) {
        super(world, x, y, z, "Bishop"+num, color);
    }



    public void setControlledSquares(ChessSquare[][] squareMatrix) {

        for (int row=0; row < 8; row++) {
            for (int column=0; column < 8; column++) {
                ChessSquare destination = squareMatrix[row][column];
                int rowDistance = destination.getRow() - this.getCurrentLocation().getRow();
                int columnDistance = destination.getColumn() - this.getCurrentLocation().getColumn();

                if (Math.abs(rowDistance) == Math.abs(columnDistance)) {
                    setSquareControl(squareMatrix, rowDistance, columnDistance);
                }

            }
        }
    }

    private void setSquareControl(ChessSquare[][] squareMatrix, int rowDistance, int columnDistance) {
        boolean isMovingDown = rowDistance > 0;
        boolean isMovingRight = columnDistance > 0;
        int absDistance = Math.abs(rowDistance);
        int initialRow = this.getCurrentLocation().getRow();
        int initialColumn = this.getCurrentLocation().getColumn();

        //same square
        if(absDistance == 0) {
            return;
        }

        for (int currDistance = 1; currDistance <= absDistance; currDistance++) {
            //Bishop is trying to move down and right
            if (isMovingDown && isMovingRight) {
                ChessSquare currSquare = squareMatrix[initialRow+currDistance][initialColumn+currDistance];
                if (currSquare.getChessPiece() != null) {
                    if (!isSameColor(currSquare.getChessPiece().getColor())) { //if found piece is opp. color then it can be captured
                        currSquare.setIsControlledBy(this.getColor());
                        if (currDistance < absDistance) {
                            return;
                        }
                    }
                    else {
                        currSquare.setIsControlledBy(this.getColor());
                        controlSquareMatrix[initialRow+currDistance][initialColumn+currDistance] = currSquare;
                        return;
                    }
                }
                else {
                    currSquare.setIsControlledBy(this.getColor());
                }
                controlSquareMatrix[initialRow+currDistance][initialColumn+currDistance] = currSquare;
                squareMatrix[initialRow+currDistance][initialColumn+currDistance] = currSquare;


            }
            //Bishop is trying to move down and left
            else if (isMovingDown && !isMovingRight) {
                ChessSquare currSquare = squareMatrix[initialRow+currDistance][initialColumn-currDistance];

                if (currSquare.getChessPiece() != null) {
                    if (!isSameColor(currSquare.getChessPiece().getColor())) { //if found piece is opp. color then it can be captured
                        currSquare.setIsControlledBy(this.getColor());
                        if (currDistance < absDistance) {
                            return;
                        }
                    }
                    else {
                        currSquare.setIsControlledBy(this.getColor());
                        controlSquareMatrix[initialRow+currDistance][initialColumn-currDistance] = currSquare;
                        return;
                    }
                }
                else {
                    currSquare.setIsControlledBy(this.getColor());
                }
                controlSquareMatrix[initialRow+currDistance][initialColumn-currDistance] = currSquare;
                squareMatrix[initialRow+currDistance][initialColumn-currDistance] = currSquare;


            }
            //Bishop is trying to move up and right
            else if (!isMovingDown && isMovingRight) {
                ChessSquare currSquare = squareMatrix[initialRow - currDistance][initialColumn + currDistance];

                if (currSquare.getChessPiece() != null) {
                    if (!isSameColor(currSquare.getChessPiece().getColor())) { //if found piece is opp. color then it can be captured
                        currSquare.setIsControlledBy(this.getColor());
                        if (currDistance < absDistance) {
                            return;
                        }
                    }
                    else {
                        currSquare.setIsControlledBy(this.getColor());
                        controlSquareMatrix[initialRow - currDistance][initialColumn + currDistance] = currSquare;
                        return;
                    }
                }
                else {
                    currSquare.setIsControlledBy(this.getColor());
                }
                controlSquareMatrix[initialRow - currDistance][initialColumn + currDistance] = currSquare;
                squareMatrix[initialRow - currDistance][initialColumn + currDistance] = currSquare;

            }
            //Bishop is trying to move up and left
            else {
                ChessSquare currSquare = squareMatrix[initialRow-currDistance][initialColumn-currDistance];
                if (currSquare.getChessPiece() != null) {
                    if (!isSameColor(currSquare.getChessPiece().getColor())) { //if found piece is opp. color then it can be captured
                        currSquare.setIsControlledBy(this.getColor());
                        if (currDistance < absDistance) {
                            return;
                        }
                    }
                    else {
                        currSquare.setIsControlledBy(this.getColor());
                        controlSquareMatrix[initialRow-currDistance][initialColumn-currDistance] = currSquare;
                        return;
                    }
                }
                else {
                    currSquare.setIsControlledBy(this.getColor());
                }
                controlSquareMatrix[initialRow-currDistance][initialColumn-currDistance] = currSquare;
                squareMatrix[initialRow-currDistance][initialColumn-currDistance] = currSquare;


            }
        }
    }
}
