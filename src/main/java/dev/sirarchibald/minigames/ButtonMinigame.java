package dev.sirarchibald.minigames;

import dev.sirarchibald.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashMap;
import java.util.UUID;

public class ButtonMinigame extends Minigame implements Listener {
    private final Main plugin;
    private final HashMap<UUID, Integer> roundScores = new HashMap<>();
    private final int gameDuration = 10;

    public ButtonMinigame(Main plugin) { super("Buttons", "Click the buttons!"); this.plugin = plugin; }

    public int getGameDuration() { return gameDuration; }

    @Override
    public void runMinigame(Main main, Game game) {
        World world = game.getPlayers().stream().findFirst().orElseThrow().getPlayer().getWorld();
        Block arenaCorner1 = world.getBlockAt(Util.getArenaCorner1());
        Block arenaCorner2 = world.getBlockAt(Util.getArenaCorner2());
        for (int x = arenaCorner1.getX(); x >= arenaCorner2.getX(); x-- ) {
            for (int z = arenaCorner1.getZ(); z <= arenaCorner2.getZ(); z++) {
                Block selectedBlock = world.getBlockAt(x, -27, z);
                if (selectedBlock.getType() == Material.LIME_TERRACOTTA) {
                    Block blockAboveSelectedBlock = world.getBlockAt(selectedBlock.getLocation().add(0, 1, 0));
                    BlockData stoneButtonData = Material.STONE_BUTTON.createBlockData("[face=floor]");
                    blockAboveSelectedBlock.setBlockData(stoneButtonData);
                }
            }
        }

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            resetArena();
            for (GamePlayer gamePlayer : game.getPlayers()) {
                gamePlayer.getPlayer().sendMessage("Minigame ended!");
                gamePlayer.getPlayer().sendMessage(gamePlayer.getPlayer().getDisplayName() + " scored: " + roundScores.get(gamePlayer.getPlayer().getUniqueId()) + " points!");
            }
            System.out.println(roundScores);
        }, gameDuration * 20);
    }

    @Override
    public HashMap<UUID, Integer> getMinigameScore() { return roundScores; }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Block blockClicked = event.getClickedBlock();
        if (blockClicked == null) return;

        if (blockClicked.getType() == Material.STONE_BUTTON && event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Player player = event.getPlayer();

            if (roundScores.get(player.getUniqueId()) == null) {
                roundScores.put(player.getUniqueId(), 1);
            } else {
                roundScores.replace(player.getUniqueId(), roundScores.get(player.getUniqueId()) + 1);
            }
            System.out.println(roundScores);

            Bukkit.getScheduler().runTaskLater(plugin, () -> { blockClicked.setType(Material.AIR); }, 1);
            player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BANJO, 10, 1);
            player.sendMessage(Util.format("&a+1 Point"));
        }
    }
}
