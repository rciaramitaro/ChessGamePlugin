package com.apollo.chess.events;

import com.apollo.chess.Chess;
import com.apollo.chess.ChessSquare;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
public class PlayerEvents implements Listener {

    private Player currentPlayer;
    private int playerNum;
    private static final double MOVE_TOLERANCE = 0.01;
    private static ChessSquare[][] squareMatrix;
    private ChessSquare selectedSquare;
    private  int moveNumber = 1;
    public void incMoveNumber() {moveNumber++;}
    private boolean continueGame = true;

    public PlayerEvents(ChessSquare[][] squareMatrix, Player currentPlayer, int playerNum) {
        this.currentPlayer = currentPlayer;
        this.playerNum = playerNum;
        PlayerEvents.squareMatrix = squareMatrix;
        selectedSquare = squareMatrix[7][0];
    }

    public void endGame() {
        continueGame = false;
    }

    @EventHandler
    public void onPlayerMoved(PlayerMoveEvent event) {
        if (continueGame) {
            Location prevLocation = event.getFrom();
            Location currLocation = event.getTo();

            if (event.getPlayer() == currentPlayer) {
                assert currLocation != null;
                if (!didPlayerOnlyLook(prevLocation, currLocation)) {
                    if ((moveNumber % 2 == 0 && playerNum == 2) || (moveNumber % 2 != 0 && playerNum == 1))
                        determineMoveDirection(prevLocation, currLocation);

                    currentPlayer.teleport(prevLocation);
                }
            }
        }
    }

    private boolean didPlayerOnlyLook(Location prevLocation, Location currLocation) {
        return prevLocation.getX() == currLocation.getX() && prevLocation.getY() == currLocation.getY() && prevLocation.getZ() == currLocation.getZ();
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (continueGame) {
            if (event.getPlayer() == currentPlayer) {
                if ((moveNumber % 2 == 0 && playerNum == 2) || (moveNumber % 2 != 0 && playerNum == 1)) {
                    if (event.getAction().name().equalsIgnoreCase("RIGHT_CLICK_AIR")) {
                        Chess.onMovePiece(currentPlayer, selectedSquare);
                    }
                    if (event.getAction().name().equalsIgnoreCase("LEFT_CLICK_AIR"))
                        Chess.onSelectSquare(currentPlayer, selectedSquare);
                }
            }
        }
    }

    private void determineMoveDirection(Location prevLocation, Location currLocation) {
        selectedSquare.setUnSelected();

        if (playerMovedForward(prevLocation, currLocation)) {
            if (selectedSquare.getRow() > 0)
                selectedSquare = squareMatrix[selectedSquare.getRow() - 1][selectedSquare.getColumn()];
        } else if (playerMovedBackwards(prevLocation, currLocation)) {
            if (selectedSquare.getRow() < 7)
                selectedSquare = squareMatrix[selectedSquare.getRow() + 1][selectedSquare.getColumn()];
        } else if (playerMovedLeft(prevLocation, currLocation)) {
            if (selectedSquare.getColumn() > 0)
                selectedSquare = squareMatrix[selectedSquare.getRow()][selectedSquare.getColumn() - 1];
        } else if (playerMovedRight(prevLocation, currLocation)) {
            if (selectedSquare.getColumn() < 7)
                selectedSquare = squareMatrix[selectedSquare.getRow()][selectedSquare.getColumn() + 1];
        }
        selectSquare(selectedSquare);
    }

    private static void selectSquare(ChessSquare selectedSquare) {
        selectedSquare.setSelected();
    }

    private static boolean playerMovedForward(Location prev, Location curr) {
        return (curr.getX() + MOVE_TOLERANCE) < prev.getX();
    }

    private static boolean playerMovedBackwards(Location prev, Location curr) {
        return (curr.getX() - MOVE_TOLERANCE) > prev.getX();
    }

    private static boolean playerMovedLeft(Location prev, Location curr) {
        return (curr.getZ() - MOVE_TOLERANCE) > prev.getZ();
    }

    private static boolean playerMovedRight(Location prev, Location curr) {
        return (curr.getZ() + MOVE_TOLERANCE) < prev.getZ();
    }
}
