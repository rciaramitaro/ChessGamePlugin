package com.apollo.chess;

import com.apollo.chess.chessPieces.*;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;


public class Chess {

    private static final int SQUARE_SIZE = 10;
    private static final int BORDER_SIZE = 2;

    private static ChessSquare[][] squareMatrix = new ChessSquare[8][8];
    private static ChessSquare selectedSquare;
    private static Player player1;

    public static void init(Player player) {
        player1 = player;
        Location chessStartLocation = new Location(player.getWorld(), -1235, 81, -333);

        createChessBorder(chessStartLocation);

        resetChessBoard(chessStartLocation);
        setupChessBoard(chessStartLocation);
    }

    public static void onSelectSquare(Player player, String selected) {

        ChessSquare tempSquare = findLocation(selected);
        if (tempSquare != null) {
            if (tempSquare.getChessPiece() != null) {
                selectedSquare = tempSquare;
                updateControlledSquares();
                selectedSquare.getChessPiece().updateControlledSquares(squareMatrix, true);
                printControlledSquares();
            }
            else {
                player.sendMessage("This square doesn't have a piece on it");
            }
        }
        else
            player.sendMessage("Square doesn't exist");
    }

    private static void printControlledSquares() {
        for (int row=0; row < 8; row++) {
            System.out.println();
            for (int column = 0; column < 8; column++) {
                if (squareMatrix[row][column].getIsBeingControlledByBlack())
                    System.out.print(row + "" + column + "" + " b ");
                if (squareMatrix[row][column].getIsBeingControlledByWhite())
                    System.out.print(row + "" + column + "" + " w ");
                if (!squareMatrix[row][column].getIsBeingControlledByWhite() && !squareMatrix[row][column].getIsBeingControlledByBlack())
                    System.out.print(row + "" + column + "" + " # ");
            }
        }
        System.out.println();
    }

    private static void resetChessBoard(Location initialLocation) {
        int a1_x = BORDER_SIZE + (int) initialLocation.getX();
        int a1_y = (int) initialLocation.getY();
        int a1_z = BORDER_SIZE + (int) initialLocation.getZ();

        Location chessBoardStartLocation = new Location(initialLocation.getWorld(), a1_x, a1_y, a1_z);

        for (int y = 0; y < 20; y++) {
            for (int z = 0; z < SQUARE_SIZE*8; z++) {
                for (int x = 0; x < SQUARE_SIZE*8; x++) {
                    chessBoardStartLocation.setX(a1_x + x);
                    chessBoardStartLocation.setY(a1_y + y);
                    chessBoardStartLocation.setZ(a1_z + z);
                    chessBoardStartLocation.getBlock().setType(Material.AIR);
                }
            }
        }
    }

    public static void onMovePiece(Player player, String destination) {
        ChessSquare prevLocation = selectedSquare;
        ChessSquare destinationLocation = findLocation(destination);


        if (prevLocation != null && destinationLocation != null) {
            if (prevLocation.getChessPiece() != null) {
                if (prevLocation.getChessPiece().isDestinationOk(squareMatrix, destinationLocation)) {
                    int currColumn = prevLocation.getColumn();
                    int destColumn = destinationLocation.getColumn();
                    int distance = destColumn - currColumn;

                    prevLocation.getChessPiece().setMoved();
                    player.sendMessage("Moving " + prevLocation.getChessPiece().getColor() + " " + prevLocation.getChessPiece().getName());

                    //Move the rook to castle
                    if (prevLocation.getChessPiece().isKing && Math.abs(distance) == 2) {
                        if (prevLocation.getChessPiece().getColor().equalsIgnoreCase("white")) {
                            if (distance < 0) { //left castle
                                squareMatrix[7][3].setPiece(squareMatrix[7][0].getChessPiece());
                                squareMatrix[7][0].removePiece();
                            }
                            else { //right castle
                                squareMatrix[7][5].setPiece(squareMatrix[7][7].getChessPiece());
                                squareMatrix[7][7].removePiece();
                            }
                        }
                        else if (prevLocation.getChessPiece().getColor().equalsIgnoreCase("black")) {
                            if (distance < 0) { //left castle
                                squareMatrix[0][3].setPiece(squareMatrix[0][0].getChessPiece());
                                squareMatrix[0][0].removePiece();
                            }
                            else { //right castle
                                squareMatrix[0][5].setPiece(squareMatrix[0][7].getChessPiece());
                                squareMatrix[0][7].removePiece();
                            }
                        }
                    }
                    prevLocation.movePiece(destinationLocation);
                    updateControlledSquares();
                    printControlledSquares();
                    determineChecks();
                }
                else
                    player.sendMessage("Cannot move " + prevLocation.getChessPiece().getColor() + " " + prevLocation.getChessPiece().getName() + ". Invalid location");
            }
            else {
                player.sendMessage("There is no piece to move");
            }
        }
        else
            player.sendMessage("One of the locations doesn't exist");
    }

    private static void determineChecks() {
        for (int row=0; row < 8; row++) {
            for (int column = 0; column < 8; column++) {
                ChessSquare currSquare = squareMatrix[row][column];
                if (currSquare.getChessPiece() != null) {
                    ChessPiece currPiece = currSquare.getChessPiece();
                    currPiece.updateControlledSquares(squareMatrix, false);
                    if (currPiece.getName().equalsIgnoreCase("King") && currSquare.isBeingControlledByOppositeColor(currPiece.getColor())) {
                        currPiece.setIsInCheck(true);
                        player1.sendMessage("You are in check!");
                    }
                }
            }
        }
    }

    private static void updateControlledSquares() {
        resetControlledSquares();
        for (int row=0; row < 8; row++) {
            for (int column=0; column < 8; column++) {
                ChessSquare currSquare = squareMatrix[row][column];
                if (currSquare.getChessPiece() != null) {
                    currSquare.getChessPiece().updateControlledSquares(squareMatrix, false);
                }
            }
        }
    }

    private static void resetControlledSquares() {
        for (int row=0; row < 8; row++) {
            for (int column=0; column < 8; column++) {
                squareMatrix[row][column].setIsControlledBy("");
            }
        }
    }

    private static ChessSquare findLocation(String location) {
        for (int row = 0; row < 8; row++) {
            for (int column = 0; column < 8; column++) {
               if (squareMatrix[row][column].getName().equals(location))
                   return squareMatrix[row][column];
           }
       }
       return null;
    }

    private static void addPiece(World world, ChessSquare square, int row, int column) {

        if (row < 6 && row > 1) {
            return;
        }
        if (row == 6) {
            //square.setPiece(new Pawn(world, -1055 , 116, -360,column+1,  "white"));
            return;
        }
        if (row == 1) {
            //square.setPiece(new Pawn(world, -1055 , 116, -410,column+1,  "black"));
            return;
        }
        switch (column) {
            case 0:
            case 7:
                if ( row == 7)
                    square.setPiece(new Rook(world, -1055 , 116, -350, column+1, "white"));
                else
                    square.setPiece(new Rook(world, -1055 , 116, -420,column+1,  "black"));
                break;
            case 1:
            case 6:
                if ( row == 7)
                    square.setPiece(new Knight(world, -1045 , 116, -350, column+1,"white"));
                else
                    square.setPiece(new Knight(world, -1045 , 116, -420,column+1,  "black"));
                break;
            case 2:
            case 5:
                if ( row == 7)
                    square.setPiece(new Bishop(world, -1035 , 116, -350,column+1,  "white"));
                else
                    square.setPiece(new Bishop(world, -1035 , 116, -420,column+1,  "black"));
                break;
            case 3:
                if ( row == 7)
                    square.setPiece(new Queen(world, -1025 , 116, -350,  "white"));
                else
                    square.setPiece(new Queen(world, -1025 , 116, -420,"black"));
                break;
            case 4:
                if ( row == 7)
                    square.setPiece(new King(world, -1015 , 116, -350, "white"));
                else
                    square.setPiece(new King(world, -1015 , 116, -420,"black"));
        }
    }

    private static void setupChessBoard(Location initialLocation) {

        World world = initialLocation.getWorld();
        int a1_x = BORDER_SIZE + (int) initialLocation.getX();
        int a1_y = (int) initialLocation.getY();
        int a1_z = BORDER_SIZE + SQUARE_SIZE*7 + (int) initialLocation.getZ();

        for (int row = 0; row < 8; row++) {
            for (int column = 0; column < 8; column++) {
                int xOffset = a1_x+(SQUARE_SIZE*row);
                int zOffset = a1_z-(SQUARE_SIZE*column);
                Location worldLocation = new Location(world, xOffset, a1_y, zOffset);
                ChessSquare square = new ChessSquare();

                square.setWorldLocation(worldLocation);

                square.setName(getColumnName(column) + getRowName(row));
                square.setColumn(column);
                square.setRow(row);
                if (column%2 == 0) {
                    if (row%2 == 0)
                        square.setSquareColor("white");
                    else
                        square.setSquareColor("black");
                }
                else {
                    if (row%2 == 0)
                        square.setSquareColor("black");
                    else
                        square.setSquareColor("white");
                }

                addPiece(initialLocation.getWorld(), square, row, column);

                squareMatrix[row][column] = square;

            }
        }
    }

    private static int getRowName(int row) {
        switch (row) {
            case 0:
                return 8;
            case 1:
                return 7;
            case 2:
                return 6;
            case 3:
                return 5;
            case 4:
                return 4;
            case 5:
                return 3;
            case 6:
                return 2;
            case 7:
                return 1;
        }
        return -1;
    }

    private static String getColumnName(int column) {
        switch (column) {
            case 0:
                return "a";
            case 1:
                return "b";
            case 2:
                return "c";
            case 3:
                return "d";
            case 4:
                return "e";
            case 5:
                return "f";
            case 6:
                return "g";
            case 7:
                return "h";
        }
        return null;
    }

    private static void createChessBorder(Location initialLocation) {
        int xInitial = (int) initialLocation.getX();
        int yInitial = (int) initialLocation.getY();
        int zInitial = (int) initialLocation.getZ();


        for (int z = 0; z <= (SQUARE_SIZE*8+BORDER_SIZE + 1); z++) {
            initialLocation.setZ(zInitial + z);
            for (int x = 0; x <= (SQUARE_SIZE*8+BORDER_SIZE + 1); x++) {
                initialLocation.setX(xInitial + x);
                initialLocation.getBlock().setType(Material.BRICK_SLAB);
            }
        }
        initialLocation.setX(xInitial);
        initialLocation.setY(yInitial);
        initialLocation.setZ(zInitial);
    }
}
