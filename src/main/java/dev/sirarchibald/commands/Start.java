package dev.sirarchibald.commands;

import dev.sirarchibald.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Start implements CommandExecutor {
    private final Main plugin;

    public Start(Main plugin) { this.plugin = plugin; }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) { sender.sendMessage(Util.format("&cYou cannot start a game as the console!")); return true; }
        if (plugin.getOnlinePlayer((Player) sender).getRank() != Rank.ADMIN) { sender.sendMessage(Util.format("&cYou do not have permission to use this command!")); return true; }

        GamePlayer gamePlayer = plugin.getOnlinePlayer(player);
        Game game = gamePlayer.getCurrentGame();

        if (game == null) {
            player.sendMessage(Util.format("&cYou are not in a game!"));
            Util.playErrorSound(player);
        } else {
            game.setGameState(GameState.RUNNING);
            for (GamePlayer playerInGame : game.getPlayers()) {
                playerInGame.getPlayer().teleport(Util.getArenaLocation());
                playerInGame.getPlayer().sendMessage(Util.format("&l&cGAME STARTING!"));
            }
            game.gameTimer(game);
        }

        return true;
    }
}