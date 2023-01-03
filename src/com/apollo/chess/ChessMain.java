package com.apollo.chess;

import com.apollo.chess.commands.ChessCommands;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.OutputStreamWriter;

public class ChessMain extends JavaPlugin {

    private Process process = null;
    private BufferedReader reader = null;
    private OutputStreamWriter writer = null;

    @Override
    public void onEnable() {

        getCommand("startChess").setExecutor(new ChessCommands(this));
        getCommand("selectSquare").setExecutor(new ChessCommands(this));
        getCommand("movePiece").setExecutor(new ChessCommands(this));
        System.out.println("The Chess Plugin is enabled");


    }
}
