package com.apollo.chess.commands;

import com.apollo.chess.Chess;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class ChessCommands implements CommandExecutor {

    private Plugin plugin;

    public ChessCommands (Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {

        if (!(commandSender instanceof Player)) {
            System.out.println("This command can only be executed by players");
            return true;
        }

        Player player = (Player) commandSender;

        if (command.getName().equalsIgnoreCase("startChess")) {
            player.sendMessage("Starting Chess");
            if (args.length == 1)
                Chess.init(plugin, player, args[0]);
            else
                player.sendMessage("/startChess <player username>");
        }
        //else if (command.getName().equalsIgnoreCase("movePiece"))
            //Chess.onMovePiece(player, args[0]);
        /*else if (command.getName().equalsIgnoreCase("selectSquare"))
            Chess.onSelectSquare(player, args[0]);*/
        return true;
    }
}
