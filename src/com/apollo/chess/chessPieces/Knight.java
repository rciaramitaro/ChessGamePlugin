package com.apollo.chess.chessPieces;

import com.apollo.chess.ChessPiece;
import com.apollo.chess.ChessSquare;
import org.bukkit.World;

public class Knight extends ChessPiece {
    public Knight(World world, int x, int y, int z, int num, String color) {
        super(world, x, y, z, "Knight"+num, color);
    }

    public void setControlledSquares(ChessSquare[][] squareMatrix) {

        for (int row=0; row < 8; row++) {
            for (int column = 0; column < 8; column++) {
                ChessSquare destination = squareMatrix[row][column];
                int rowDistance = destination.getRow() - this.getCurrentLocation().getRow();
                int columnDistance = destination.getColumn() - this.getCurrentLocation().getColumn();

                if (((Math.abs(rowDistance)==1) && (Math.abs(columnDistance)==2)) || ((Math.abs(rowDistance)==2) && (Math.abs(columnDistance)==1))) {
                    destination.setIsControlledBy(this.getColor());
                    controlSquareMatrix[row][column] = destination;
                }
            }
        }
    }
}
