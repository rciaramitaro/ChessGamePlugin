package com.apollo.chess;

import com.apollo.chess.commands.ChessCommands;
import org.bukkit.plugin.java.JavaPlugin;

public class ChessMain extends JavaPlugin {

    @Override
    public void onEnable() {
        System.out.println("The Chess Plugin is enabled");

        getCommand("startChess").setExecutor(new ChessCommands());
        getCommand("selectSquare").setExecutor(new ChessCommands());
        getCommand("movePiece").setExecutor(new ChessCommands());
    }
}
