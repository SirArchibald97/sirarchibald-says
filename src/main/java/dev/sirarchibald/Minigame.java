package dev.sirarchibald;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

public abstract class Minigame {

    private final String name;
    public Minigame(String name) { this.name = name; }

    public abstract void runMinigame(Main main, Game game);
    public abstract HashMap<UUID, Integer> getMinigameScores();
    public abstract void setMinigameScores(HashMap<UUID, Integer> scores);

    public String getName() { return name; }

    public void roundCountdown(Main main, Game game) {
        for (int i = 5; i > 0; i--) {
            int finalI = i;
            Bukkit.getScheduler().runTaskLater(main, () -> {
                for (GamePlayer gamePlayer : game.getPlayers()) {
                    gamePlayer.getPlayer().sendMessage(Util.format("Minigame starting in " + finalI + " seconds!"));
                }
            }, 20);
        }
    }

    public void resetArena() {
        World world = Bukkit.getWorld("world");
        Block arenaCorner1 = world.getBlockAt(Util.getArenaCorner1());
        Block arenaCorner2 = world.getBlockAt(Util.getArenaCorner2());
        for (int x = arenaCorner1.getX(); x >= arenaCorner2.getX(); x-- ) {
            for (int z = arenaCorner1.getZ(); z <= arenaCorner2.getZ(); z++) {
                Block selectedBlock = world.getBlockAt(x, -27, z);
                if (selectedBlock.getType() == Material.LIME_TERRACOTTA) {
                    Block blockAboveSelectedBlock = world.getBlockAt(selectedBlock.getLocation().add(0, 1, 0));
                    blockAboveSelectedBlock.setType(Material.AIR);
                }
            }
        }
    }
}
