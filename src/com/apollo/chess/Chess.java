package com.apollo.chess;

import com.apollo.chess.chessPieces.*;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.sql.SQLOutput;


public class Chess {

    private static final int SQUARE_SIZE = 10;
    private static final int BORDER_SIZE = 2;

    private static final ChessSquare[][] squareMatrix = new ChessSquare[8][8];

    public static void init(Player player) {

        Location chessStartLocation = new Location(player.getWorld(), -1235, 81, -333);

        createChessBorder(chessStartLocation);

        resetChessBoard(chessStartLocation);
        setupChessBoard(chessStartLocation);

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

    public static void onMovePiece(Player player, String previous, String destination) {
        ChessSquare prevLocation = findLocation(previous);
        ChessSquare destinationLocation = findLocation(destination);


        if (prevLocation != null && destinationLocation != null) {
            if (prevLocation.getChessPiece() != null) {
                if (prevLocation.getChessPiece().isDestinationOk(squareMatrix, destinationLocation)) {
                    player.sendMessage("Moving " + prevLocation.getChessPiece().getColor() + " " + prevLocation.getChessPiece().getName());
                    prevLocation.movePiece(destinationLocation);
                } else
                    player.sendMessage("Cannot move " + prevLocation.getChessPiece().getColor() + " " + prevLocation.getChessPiece().getName() + ". Invalid location");
            }
            else {
                player.sendMessage("There is no piece to move");
            }
        }
        else
            player.sendMessage("One of the locations doesnt exist");
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
            square.setPiece(new Pawn(world, -1055 , 116, -360,column+1,  "white"));
            return;
        }
        if (row == 1) {
            square.setPiece(new Pawn(world, -1055 , 116, -410,column+1,  "black"));
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
                    square.setPiece(new Queen(world, -1025 , 116, -350,-1,  "white"));
                else
                    square.setPiece(new Queen(world, -1025 , 116, -420,-1,  "black"));
                break;
            case 4:
                if ( row == 7)
                    square.setPiece(new King(world, -1015 , 116, -350,-1,  "white"));
                else
                    square.setPiece(new King(world, -1015 , 116, -420,-1,  "black"));
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

                createChessSquare(worldLocation, row, column);

                square.setName(getColumnName(column) + getRowName(row));
                square.setColumn(column);
                square.setRow(row);
                //System.out.println(square.getName() + " column: " + square.getColumn() + " row: " + square.getRow());

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

    private static void createChessSquare(Location worldLocation, int row, int column) {
        Location initialLocation = new Location(worldLocation.getWorld(), worldLocation.getX(), worldLocation.getY(), worldLocation.getZ());
        int xInitial = (int) initialLocation.getX();
        int zInitial = (int) initialLocation.getZ();

        for (int z = 0; z <= SQUARE_SIZE - 1; z++) {
            initialLocation.setZ(zInitial + z);
            for (int x = 0; x <= SQUARE_SIZE - 1; x++) {
                initialLocation.setX(xInitial + x);

                if (column%2 == 0) {
                    if (row%2 == 0)
                        initialLocation.getBlock().setType(Material.WHITE_CONCRETE);
                    else
                        initialLocation.getBlock().setType(Material.BLACK_CONCRETE);
                }
                else {
                    if (row%2 == 0)
                        initialLocation.getBlock().setType(Material.BLACK_CONCRETE);
                    else
                        initialLocation.getBlock().setType(Material.WHITE_CONCRETE);
                }
            }
        }
        initialLocation.setZ(zInitial);
        initialLocation.setX(xInitial);
    }
}
