package dev.sirarchibald.commands;

import dev.sirarchibald.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Create implements CommandExecutor {
    private final Main plugin;
    public Create(Main plugin) { this.plugin = plugin; }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        if (plugin.getOnlinePlayer((Player) sender).getRank() != Rank.ADMIN) { sender.sendMessage(Util.format("&cYou do not have permission to use this command!")); return true; }

        plugin.getGameManager().addGame(new Game(plugin));
        sender.sendMessage(Util.format("New game created!"));
        return true;
    }
}
