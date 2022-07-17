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
    private HashMap<UUID, Integer> roundScores = new HashMap<>();

    public ButtonMinigame(Main plugin) { super("Buttons"); this.plugin = plugin; }

    @Override
    public void runMinigame(Main main, Game game) {
        plugin.getServer().getPluginManager().registerEvents(new ButtonMinigameEvents(plugin, game, this), plugin);

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

        int gameDuration = 10;
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            resetArena();
            for (GamePlayer gamePlayer : game.getPlayers()) {
                gamePlayer.getPlayer().sendMessage("Minigame ended!");
                gamePlayer.getPlayer().sendMessage(gamePlayer.getPlayer().getDisplayName() + " scored: " + roundScores.get(gamePlayer.getPlayer().getUniqueId()) + " points!");
            }
            System.out.println(roundScores);
        }, gameDuration * 20);
    }

    public HashMap<UUID, Integer> getMinigameScores() { return roundScores; }
    public void setMinigameScores(HashMap<UUID, Integer> scores) { roundScores = scores; }
}
