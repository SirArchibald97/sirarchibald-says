package dev.sirarchibald.minigames;

import dev.sirarchibald.Game;
import dev.sirarchibald.Main;
import dev.sirarchibald.Util;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashMap;
import java.util.UUID;

public class ButtonMinigameEvents implements Listener {
    private final Main plugin;
    private final Game game;
    private final ButtonMinigame minigame;

    public ButtonMinigameEvents(Main plugin, Game game, ButtonMinigame minigame) { this.plugin = plugin; this.game = game; this.minigame = minigame; }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Block blockClicked = event.getClickedBlock();
        if (blockClicked == null) return;

        if (blockClicked.getType() == Material.STONE_BUTTON && event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Player player = event.getPlayer();

            HashMap<UUID, Integer> scores = minigame.getMinigameScores();
            if (scores.get(player.getUniqueId()) == null) {
                scores.put(player.getUniqueId(), 1);
            } else {
                scores.replace(player.getUniqueId(), scores.get(player.getUniqueId()) + 1);
            }
            minigame.setMinigameScores(scores);
            System.out.println(minigame.getMinigameScores());

            Bukkit.getScheduler().runTaskLater(plugin, () -> blockClicked.setType(Material.AIR), 1);
            player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BANJO, 10, 1);
            player.sendMessage(Util.format("&a+1 Point"));
        }
    }
}