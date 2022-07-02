package com.apollo.chess.commands;

import com.apollo.chess.Chess;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChessCommands implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {

        if (!(commandSender instanceof Player)) {
            System.out.println("This command can only be executed by players");
            return true;
        }

        Player player = (Player) commandSender;

        if (command.getName().equalsIgnoreCase("startChess")) {
            player.sendMessage("Starting Chess");
            Chess.init(player);
        }
        else if (command.getName().equalsIgnoreCase("movePiece")) {
            Chess.onMovePiece(player, args[0], args[1]);
        }
        return true;
    }
}
