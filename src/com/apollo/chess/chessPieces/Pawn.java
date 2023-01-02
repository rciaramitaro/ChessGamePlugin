package com.apollo.chess.chessPieces;

import com.apollo.chess.ChessPiece;
import com.apollo.chess.ChessSquare;
import org.bukkit.World;

public class Pawn extends ChessPiece {
    public Pawn(World world, int x, int y, int z, int num, String color) {
        super(world, x, y, z, "Pawn"+num, color);
        isPawn = true;
    }

    //TODO: add en-peasant and promotions

    public boolean isDestinationOk(ChessSquare[][] squareMatrix, ChessSquare destination) {
        int currentColumn = this.getCurrentLocation().getColumn();
        int currentRow = this.getCurrentLocation().getRow();
        int destinationColumn = destination.getColumn();
        int destinationRow = destination.getRow();

        //move forward open spaces on same column
        if (currentColumn == destinationColumn) {
            //rules for white pawns
            if (getColor().equals("white") && !isDestinationObstructed(squareMatrix, destination)) {
                //white to move forward by 1
                //anything else is invalid
                if (destinationRow - currentRow == -1) {
                    return true;
                }
                //white to move forward by 2
                else return destinationRow - currentRow == -2 && isFirstMove;
            }
            else if (getColor().equals("black") && !isDestinationObstructed(squareMatrix, destination)) {
                //black to move forward by 1
                //anything else is invalid
                if (destinationRow - currentRow == 1 && !isDestinationObstructed(squareMatrix, destination)) {
                    return true;
                }
                //black to move forward by 2
                else return destinationRow - currentRow == 2 && isFirstMove;
            }
        }
        //pawn is attacking diagonally forward
        else if (destination.getChessPiece() != null && Math.abs(destinationColumn - currentColumn) == 1) {
            //white pawn attacking
            if (getColor().equals("white") && destination.getChessPiece().getColor().equals("black") && destinationRow - currentRow == -1) {
                return true;
            }
            //black pawn attacking
            else {
                return getColor().equals("black") && destination.getChessPiece().getColor().equals("white") && destinationRow - currentRow == 1;
            }
        }
        //anything else is invalid
        return false;
    }

    public int getAvailableSquares(ChessSquare[][] squareMatrix) {
        int availableSquares = 0;
        for (int row = 0; row < 8; row++) {
            for (int column = 0; column < 8; column++) {
                ChessSquare currSquare = squareMatrix[row][column];
                ChessSquare controlSquare = controlSquareMatrix[row][column];

                if (controlSquare != null) {
                    ChessSquare prevLocation = getCurrentLocation();
                    //make available square is if a piece is on a controlled square
                    //if (this.getColor() == "white" && controlSquare.getIsBeingControlledByWhite() || this.getColor() == "black" && controlSquare.getIsBeingControlledByBlack()) {
                    if (currSquare.getChessPiece() != null) {
                        if (currSquare.getChessPiece().getColor() != this.getColor()) {
                            ChessPiece enemyPiece = currSquare.getChessPiece();
                            //System.out.println("Currently testing square " + currSquare.getName());
                            getCurrentLocation().movePiece(currSquare);
                            getCurrentLocation().getChessPiece().resetControlledSquares();
                            resetBoardControlledSquares(squareMatrix);
                            getCurrentLocation().getChessPiece().setControlledSquares(squareMatrix);

                            if (determineChecks(squareMatrix, getCurrentLocation().getChessPiece().getColor())) {
                                availableSquareMatrix[row][column] = null;
                                getCurrentLocation().setSquareColor(getCurrentLocation().getColor());
                            } else {
                                availableSquareMatrix[row][column] = squareMatrix[row][column];
                                availableSquares++;
                            }

                            ChessSquare enemyLocation = getCurrentLocation();
                            getCurrentLocation().movePiece(prevLocation);
                            enemyLocation.setPiece(enemyPiece);
                            getCurrentLocation().getChessPiece().resetControlledSquares();
                            resetBoardControlledSquares(squareMatrix);
                            getCurrentLocation().getChessPiece().setControlledSquares(squareMatrix);
                            updateAllControlledSquares(squareMatrix);

                        }
                    }
                    //}
                }

                if (isDestinationOk(squareMatrix, currSquare)) {
                    //System.out.println("Currently testing square " + currSquare.getName());
                    if (currSquare.getChessPiece() != null) {
                        if (currSquare.getChessPiece().getColor() != this.getColor()) {
                            ChessPiece enemyPiece = currSquare.getChessPiece();
                            ChessSquare prevLocation = getCurrentLocation();
                            getCurrentLocation().movePiece(currSquare);
                            getCurrentLocation().getChessPiece().resetControlledSquares();
                            resetBoardControlledSquares(squareMatrix);
                            getCurrentLocation().getChessPiece().setControlledSquares(squareMatrix);
                            updateAllControlledSquares(squareMatrix);

                            if (determineChecks(squareMatrix, getCurrentLocation().getChessPiece().getColor())) {
                                availableSquareMatrix[row][column] = null;
                                getCurrentLocation().setSquareColor(getCurrentLocation().getColor());
                            } else {
                                availableSquareMatrix[row][column] = squareMatrix[row][column];
                                availableSquares++;
                            }
                            ChessSquare enemyLocation = getCurrentLocation();
                            getCurrentLocation().movePiece(prevLocation);
                            enemyLocation.setPiece(enemyPiece);
                            getCurrentLocation().getChessPiece().resetControlledSquares();
                            resetBoardControlledSquares(squareMatrix);
                            getCurrentLocation().getChessPiece().setControlledSquares(squareMatrix);
                            updateAllControlledSquares(squareMatrix);
                        }
                    }
                    else {
                        ChessSquare prevLocation = getCurrentLocation();
                        getCurrentLocation().movePiece(currSquare);
                        getCurrentLocation().getChessPiece().resetControlledSquares();
                        resetBoardControlledSquares(squareMatrix);
                        getCurrentLocation().getChessPiece().setControlledSquares(squareMatrix);
                        updateAllControlledSquares(squareMatrix);

                        if (determineChecks(squareMatrix, getCurrentLocation().getChessPiece().getColor())) {
                            availableSquareMatrix[row][column] = null;
                            getCurrentLocation().setSquareColor(getCurrentLocation().getColor());
                        } else {
                            availableSquareMatrix[row][column] = squareMatrix[row][column];
                            availableSquares++;
                        }
                        getCurrentLocation().movePiece(prevLocation);
                        getCurrentLocation().getChessPiece().resetControlledSquares();
                        resetBoardControlledSquares(squareMatrix);
                        getCurrentLocation().getChessPiece().setControlledSquares(squareMatrix);
                        updateAllControlledSquares(squareMatrix);

                    }
                }
            }
        }
        return availableSquares;
    }

    public void setControlledSquares(ChessSquare[][] squareMatrix) {
        int attackingRow = 0;
        int currentColumn = this.getCurrentLocation().getColumn();
        int currentRow = this.getCurrentLocation().getRow();

        if (this.getColor().equalsIgnoreCase("white")) {
            attackingRow = this.getCurrentLocation().getRow() - 1;
        } else if (this.getColor().equalsIgnoreCase("black"))
            attackingRow = this.getCurrentLocation().getRow() + 1;

        if (currentRow > 0 && currentRow < 7) {
            if (currentColumn < 1) { //pawn on the a column
                squareMatrix[attackingRow][currentColumn + 1].setIsControlledBy(this.getColor());
                controlSquareMatrix[attackingRow][currentColumn + 1] = squareMatrix[attackingRow][currentColumn + 1];
            } else if (currentColumn > 6) { //pawn on the h column
                squareMatrix[attackingRow][currentColumn - 1].setIsControlledBy(this.getColor());
                controlSquareMatrix[attackingRow][currentColumn - 1] = squareMatrix[attackingRow][currentColumn - 1];

            } else {
                squareMatrix[attackingRow][currentColumn + 1].setIsControlledBy(this.getColor());
                squareMatrix[attackingRow][currentColumn - 1].setIsControlledBy(this.getColor());
                controlSquareMatrix[attackingRow][currentColumn + 1] = squareMatrix[attackingRow][currentColumn + 1];
                controlSquareMatrix[attackingRow][currentColumn - 1] = squareMatrix[attackingRow][currentColumn - 1];

            }
        }
    }

    private boolean isDestinationObstructed(ChessSquare[][] squareMatrix, ChessSquare destination) {
        if (this.getColor().equals("white")) {
            for (int row = this.getCurrentLocation().getRow()-1; row>= destination.getRow(); row-- ) {
                if (squareMatrix[row][destination.getColumn()].getChessPiece() != null) {
                    return true;
                }
            }
            return false;
        }
        else if (this.getColor().equals("black")) {
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
