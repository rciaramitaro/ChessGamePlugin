package com.apollo.chess.chessPieces;

import com.apollo.chess.ChessPiece;
import com.apollo.chess.ChessSquare;
import org.bukkit.World;

public class Queen extends ChessPiece {

    public Queen(World world, int x, int y, int z, String color) {
        super(world, x, y, z, "Queen", color);
    }

    public void setControlledSquares(ChessSquare[][] squareMatrix) {

        ChessPiece rookRef = new Rook(this.getReferenceLocation().getWorld(), 0,0,0, 0, this.getColor());
        ChessPiece bishopRef = new Bishop(this.getReferenceLocation().getWorld(), 0,0,0, 0, this.getColor());

        rookRef.setLocation(this.getCurrentLocation());
        bishopRef.setLocation(this.getCurrentLocation());

        rookRef.setControlledSquares(squareMatrix);
        bishopRef.setControlledSquares(squareMatrix);

        copyControlSquareMatrix(rookRef.controlSquareMatrix, bishopRef.controlSquareMatrix);
    }

    private void copyControlSquareMatrix(ChessSquare[][] rookControlSquareMatrix, ChessSquare[][] bishopControlSquareMatrix1) {
        for (int row=0; row < 8; row++) {
            for (int column = 0; column < 8; column++) {
                ChessSquare rookCurrSquare = rookControlSquareMatrix[row][column];
                ChessSquare bishopCurrSquare = bishopControlSquareMatrix1[row][column];

                if (rookCurrSquare != null) {
                    controlSquareMatrix[row][column] = rookCurrSquare;
                }
                if (bishopCurrSquare != null) {
                    controlSquareMatrix[row][column] = bishopCurrSquare;
                }
            }
        }
    }
}
