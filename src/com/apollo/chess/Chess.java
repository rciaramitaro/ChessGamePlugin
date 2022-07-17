package com.apollo.chess;

import com.apollo.chess.chessPieces.*;
import com.apollo.chess.events.PlayerEvents;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;


public class Chess {

    private static final int SQUARE_SIZE = 10;
    private static final int BORDER_SIZE = 2;
    private static Plugin plugin = null;
    private static String currCastleNotation;

    private static ChessSquare[][] squareMatrix = new ChessSquare[8][8];
    private static ChessSquare selectedSquare;
    private static Player player1, player2;
    private static Location player1Location, player2Location;
    private static PlayerEvents player1Events, player2Events;
    private static int moveNum = 1;

    public static void init(Plugin plugin, Player player1, String player2) {
        Chess.plugin = plugin;
        Chess.player1 = player1;
        List<Player> list = new ArrayList<>(Bukkit.getOnlinePlayers());
        for (Player player : list) {
            if (player.getName().equals(player2)) {
                Chess.player2 = player;
                System.out.println(player.getName());
            }
        }

        Chess.player1Location = new Location(Chess.player1.getWorld(), -1146, 130, -291, 90, 37);
        Chess.player2Location = new Location(Chess.player2.getWorld(), -1239, 130, -291, -90, 37);

        if (Chess.player2 != null) {

            startGame();
        }
        else
            player1.sendMessage("Specified player doesn't exist");
    }

    public static void startGame() {
        Location chessStartLocation = new Location(player1.getWorld(), -1235, 81, -333);

        createChessBorder(chessStartLocation);

        resetChessBoard(chessStartLocation);
        setupChessBoard(chessStartLocation);

        ChessPiece.updateAllControlledSquares(squareMatrix);

        selectSquare(squareMatrix[7][0]);
        teleportToPlayersToPlayingPositions();
        player1Events = new PlayerEvents(squareMatrix, player1, 1);
        player2Events = new PlayerEvents(squareMatrix, player2, 2);
        player1.getServer().getPluginManager().registerEvents(player1Events, plugin);
        player2.getServer().getPluginManager().registerEvents(player2Events, plugin);
        player1.sendMessage("Your Move, White");


        /*
        TODO: Put chess in its own world so anyone can use plugin without disrupting overworld
        */
    }

    private static void teleportToPlayersToPlayingPositions() {
        player1.teleport(player1Location);
        player2.teleport(player2Location);
    }

    private static void selectSquare(ChessSquare selectedSquare) {
        selectedSquare.setSelected();
    }

    public static void onSelectSquare(Player player, ChessSquare selectedSquare) {
        if (selectedSquare.getChessPiece() != null) {
            if ((player == player1 && selectedSquare.getChessPiece().getColor() == "white") || (player == player2 && selectedSquare.getChessPiece().getColor() == "black")) {
                Chess.selectedSquare = selectedSquare;
                resetAllHighlightedSquares();
                selectedSquare.setSquareColor("green");
                selectedSquare.getChessPiece().resetControlledSquares();
                ChessPiece.resetBoardControlledSquares(squareMatrix);
                selectedSquare.getChessPiece().setControlledSquares(squareMatrix);
                ChessPiece.updateAllControlledSquares(squareMatrix);
                selectedSquare.getChessPiece().getAvailableSquares(squareMatrix);
                selectedSquare.getChessPiece().showAvailableSquares();

                System.out.println("AVAILABLE SQUARES FOR PIECE");
                selectedSquare.getChessPiece().printAvailableSquares();
                System.out.println("SQUARES CONTROLLED BY PIECE");
                selectedSquare.getChessPiece().printControlledSquares();
                System.out.println("CONTROLLED SQUARES BY ALL PIECES");
                printBoardControlledSquares();
            }
            else
                player.sendMessage("You may only select your own color pieces");
        }
        else
            player.sendMessage("This square doesn't have a piece on it");
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

    public static void onMovePiece(Player player, ChessSquare destinationLocation) {
        ChessSquare prevLocation = selectedSquare;
        currCastleNotation = null;

        if (prevLocation != null && destinationLocation != null) {
            if (prevLocation.getChessPiece() != null) {
                if (destinationLocation.getIsHighlighted()) {
                    int currColumn = prevLocation.getColumn();
                    int destColumn = destinationLocation.getColumn();
                    int distance = destColumn - currColumn;

                    //Move the rook to castle
                    if (prevLocation.getChessPiece().isKing && Math.abs(distance) == 2)
                        castleKing(prevLocation, distance);

                    sendMoveMessage(prevLocation, destinationLocation);
                    prevLocation.getChessPiece().setMoved();
                    prevLocation.movePiece(destinationLocation);

                    //Pawn promotion
                    if (destinationLocation.getChessPiece().isPawn)
                        determinePawnPromotion(destinationLocation);

                    resetAllHighlightedSquares();
                    showRecentSquares(prevLocation, destinationLocation);
                    destinationLocation.getChessPiece().resetControlledSquares();
                    ChessPiece.resetBoardControlledSquares(squareMatrix);
                    ChessPiece.updateAllControlledSquares(squareMatrix);
                    determineGameEnd();

                    moveNum++;
                    player1Events.incMoveNumber();
                    player2Events.incMoveNumber();

                     /*System.out.println("PIECE ON BOARD");
                     destinationLocation.getChessPiece().printControlledSquares();
                     System.out.println("ALL PIECES ON BOARD");
                     printBoardControlledSquares();*/
                }
                else {
                    player.sendMessage("Cannot move " + prevLocation.getChessPiece().getColor() + " " + prevLocation.getChessPiece().getName() + ". Invalid location");
                }
            }
        }
    }

    private static void showRecentSquares(ChessSquare prev, ChessSquare curr) {
        prev.setRecent();
        curr.setRecent();
    }

    private static void determinePawnPromotion(ChessSquare destination) {
        if (destination.getRow() == 0) {
            destination.setPiece(new Queen(player1.getWorld(), -1025 , 116, -350,  "white"));
        }
        else if (destination.getRow() == 7) {
            destination.setPiece(new Queen(player1.getWorld(), -1025 , 116, -420,"black"));
        }

    }

    private static void determineGameEnd() {
        int blackAvailableMoves = 0, whiteAvailableMoves = 0;
        for (int row=0; row < 8; row++) {
            for (int column=0; column < 8; column++) {
                ChessSquare currSquare = squareMatrix[row][column];
                if (currSquare.getChessPiece() != null) {
                    ChessPiece currPiece = currSquare.getChessPiece();
                    if (currPiece.getColor() == "white")
                        whiteAvailableMoves += currPiece.getAvailableSquares(squareMatrix);
                    else if (currPiece.getColor() == "black")
                        blackAvailableMoves += currPiece.getAvailableSquares(squareMatrix);
                }
            }
        }

        if (blackAvailableMoves == 0)
            determineMate("black");
        else if (whiteAvailableMoves == 0)
            determineMate("white");
    }

    private static void determineMate(String color) {
        for (int row=0; row < 8; row++) {
            for (int column=0; column < 8; column++) {
                ChessSquare currSquare = squareMatrix[row][column];

                if (currSquare.getChessPiece() != null) {
                    if (currSquare.getChessPiece().getColor() == color && currSquare.getChessPiece().isKing) {
                        ChessPiece king = currSquare.getChessPiece();
                        if (king.getCurrentLocation().isBeingControlledByOppositeColor(king.getColor()))
                            plugin.getServer().broadcastMessage(king.getColor() + " just got Checkmated");
                        else
                            plugin.getServer().broadcastMessage(king.getColor() + " just got Stalemated");
                    }
                }
            }
        }
        endGame();
    }

    private static void endGame() {
        player1Events.endGame();
        player2Events.endGame();
    }

    private static void castleKing(ChessSquare prevLocation, int distance) {
        player1.sendMessage("CASTLING");
        if (prevLocation.getChessPiece().getColor().equalsIgnoreCase("white")) {
            if (distance < 0) { //left castle
                currCastleNotation = "O-O-O";
                squareMatrix[7][3].setPiece(squareMatrix[7][0].getChessPiece());
                squareMatrix[7][0].removePiece();
            } else { //right castle
                currCastleNotation = "O-O";
                squareMatrix[7][5].setPiece(squareMatrix[7][7].getChessPiece());
                squareMatrix[7][7].removePiece();
            }
        } else if (prevLocation.getChessPiece().getColor().equalsIgnoreCase("black")) {
            if (distance < 0) { //left castle
                currCastleNotation = "O-O-O";
                squareMatrix[0][3].setPiece(squareMatrix[0][0].getChessPiece());
                squareMatrix[0][0].removePiece();
            } else { //right castle
                currCastleNotation = "O-O";
                squareMatrix[0][5].setPiece(squareMatrix[0][7].getChessPiece());
                squareMatrix[0][7].removePiece();
            }
        }
    }

    private static void sendMoveMessage(ChessSquare prevLoc, ChessSquare destLoc) {
        String moveNotation = getMoveNotation(prevLoc, destLoc);

        player1.sendMessage(moveNotation);
        player2.sendMessage(moveNotation);

        if (moveNum%2 == 0)
            player2.sendMessage("Your Move, Black");
        else
            player1.sendMessage("Your Move, White");
    }

    private static String getMoveNotation(ChessSquare prevLoc, ChessSquare destLoc) {
        ChessPiece takenPiece = destLoc.getChessPiece();
        String takes = "";
        if (takenPiece != null)
            takes = "x";
        String movedPieceLetter = determinePieceLetter(prevLoc, takes=="x");
        String destName = destLoc.getName();


        if (currCastleNotation == null)
            return movedPieceLetter + takes + destName;
        else
            return currCastleNotation;
    }

    private static String determinePieceLetter(ChessSquare prevLoc, Boolean isTaking) {
        char firstLetter = prevLoc.getChessPiece().getName().charAt(0);
        if (firstLetter == 'P')
            if (isTaking)
                return getColumnName(prevLoc.getColumn());
            else
                return "";

        if (firstLetter == 'K') {
            if (prevLoc.getChessPiece().getName().charAt(1) == 'i')
                return "K";
            else
                return "N";
        }
        return Character.toString(firstLetter);
    }

    public static void resetAllHighlightedSquares() {
        for (int row=0; row < 8; row++) {
            for (int column=0; column < 8; column++) {
                ChessSquare currSquare = squareMatrix[row][column];
                currSquare.setSquareColor(currSquare.getColor());
                if (currSquare.getChessPiece() != null)
                    currSquare.getChessPiece().resetAvailableSquares();
            }
        }
    }

    public static void printBoardControlledSquares() {
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

    public static void printBoardPieces() {
        for (int row=0; row < 8; row++) {
            System.out.println();
            for (int column = 0; column < 8; column++) {
                if (squareMatrix[row][column].getChessPiece() != null)
                    System.out.print(squareMatrix[row][column].getChessPiece().getName() + " ");
                else
                    System.out.print("X    ");
            }
        }
        System.out.println();
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
