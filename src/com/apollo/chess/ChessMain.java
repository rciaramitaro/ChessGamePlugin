package com.apollo.chess;

import com.apollo.chess.commands.ChessCommands;
import org.bukkit.plugin.java.JavaPlugin;

public class ChessMain extends JavaPlugin {

    @Override
    public void onEnable() {

        getCommand("startChess").setExecutor(new ChessCommands(this));
        getCommand("selectSquare").setExecutor(new ChessCommands(this));
        getCommand("movePiece").setExecutor(new ChessCommands(this));
        System.out.println("The Chess Plugin is enabled");

    }
}
