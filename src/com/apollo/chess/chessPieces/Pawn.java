package com.apollo.chess.chessPieces;

import com.apollo.chess.ChessPiece;
import com.apollo.chess.ChessSquare;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class Pawn extends ChessPiece {

    private boolean isFirstMove = true;


    public Pawn(World world, int x, int y, int z, int num, String color) {
        super(world, x, y, z, "Pawn"+num, color);
    }

    //TODO: add en-peasant and promotions

    public boolean isDestinationOk(ChessSquare[][] squareMatrix, ChessSquare destination) {
        int currentColumn = this.getCurrentLocation().getColumn();
        int currentRow = this.getCurrentLocation().getRow();
        int destinationColumn = destination.getColumn();
        int destinationRow = destination.getRow();

        isDestinationObstructed(squareMatrix, destination);

        //move forward open spaces on same column
        if (currentColumn == destinationColumn) {
            //rules for white pawns
            if (getColor() == "white" && !isDestinationObstructed(squareMatrix, destination)) {
                //white to move forward by 1
                if (destinationRow - currentRow == -1) {
                    isFirstMove = false;
                    return true;
                }
                //white to move forward by 2
                else if (destinationRow - currentRow == -2 && isFirstMove) {
                    isFirstMove = false;
                    return true;
                }
                //anything else is invalid
                else {
                    return false;
                }
            }
            else if (getColor() == "black") {
                //black to move forward by 1
                if (destinationRow - currentRow == 1 && !isDestinationObstructed(squareMatrix, destination)) {
                    isFirstMove = false;
                    return true;
                }
                //black to move forward by 2
                else if (destinationRow - currentRow == 2 && isFirstMove) {
                    isFirstMove = false;
                    return true;
                }
                //anything else is invalid
                else {
                    return false;
                }
            }
        }
        //pawn is attacking diagonally forward
        else if (destination.getChessPiece() != null && Math.abs(destinationColumn - currentColumn) == 1) {
            //white pawn attacking
            if (getColor() == "white" && destination.getChessPiece().getColor() == "black" && destinationRow - currentRow == -1) {
                isFirstMove = false;
                return true;
            }
            //black pawn attacking
            else if (getColor() == "black" && destination.getChessPiece().getColor() == "white"  && destinationRow - currentRow == 1) {
                isFirstMove = false;
                return true;
            }
            //anything else is invalid
            else
                return false;
        }
        //anything else is invalid
        return false;
    }

    private boolean isDestinationObstructed(ChessSquare[][] squareMatrix, ChessSquare destination) {
        if (this.getColor() == "white") {
            for (int row = this.getCurrentLocation().getRow()-1; row>= destination.getRow(); row-- ) {
                if (squareMatrix[row][destination.getColumn()].getChessPiece() != null) {
                    return true;
                }
            }
            return false;
        }
        else if (this.getColor() == "black") {
            for (int row = this.getCurrentLocation().getRow()+1; row<= destination.getRow(); row++ ) {
                if (squareMatrix[row][destination.getColumn()].getChessPiece() != null) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }
}
