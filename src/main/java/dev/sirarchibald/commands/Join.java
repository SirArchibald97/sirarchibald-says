package dev.sirarchibald.commands;

import dev.sirarchibald.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Join implements CommandExecutor {
    private final Main plugin;
    public Join(Main plugin) { this.plugin = plugin; }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) { sender.sendMessage(Util.format("&cThe console cannot join games!")); return true; }
        if (plugin.getGameManager().getGames().size() == 0) { sender.sendMessage(Util.format("&cThere is no game currently running!")); return true; }

        GamePlayer gamePlayer = plugin.getOnlinePlayer(player);
        if (gamePlayer.getCurrentGame() != null) { sender.sendMessage(Util.format("&cYou are already in a game!")); return true; }

        Game game = plugin.getGameManager().getGames().stream().findFirst().orElseThrow();
        if (game.getGameState() == GameState.RUNNING) {
            player.sendMessage(Util.format("&cThe game has already started!"));
        } else {
            game.addPlayer(gamePlayer);
            player.sendMessage(Util.format(plugin.getOnlinePlayer(player).getChatPrefix()) + Util.format(player.getName()) + Util.format("&f has joined the game! (") + game.getPlayers().size() + "/10)");
            gamePlayer.setCurrentGame(game);
        }

        /*
        Inventory gameMenu = Bukkit.createInventory(null, 27, "Games");
        for (Game game : plugin.getGameManager().getGames()) {
            ItemStack gameItem = new ItemStack(Material.OAK_SIGN, 1);
            ItemMeta gameItemMeta = gameItem.getItemMeta();
            gameItemMeta.setDisplayName(Util.format("&fGame #" + game.getGameID()));

            List<String> gameItemLore = new ArrayList<String>();
            gameItemLore.add(Util.format("&fPlayers: &a" + game.getPlayers().size() + "/10"));
            gameItemLore.add(Util.format(""));
            if (game.getGameState() == GameState.LOBBY) {
                gameItemLore.add(Util.format("&cWaiting for players..."));
            } else {
                gameItemLore.add(Util.format("&cGame in progress!"));
            }

            gameItemMeta.setLore(Collections.singletonList(Util.format("&fPlayers: &a" + game.getPlayers().size() + "/10")));
            gameItemMeta.getPersistentDataContainer().set(new NamespacedKey(plugin, "game-id"), PersistentDataType.INTEGER, game.getGameID());

            gameItem.setItemMeta(gameItemMeta);
            gameMenu.addItem(gameItem);
        }
        HumanEntity human = (HumanEntity) sender;
        human.openInventory(gameMenu);
        */
        return true;
    }
}
