package dev.sirarchibald;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class Util {
    public static String format(String message) { return ChatColor.translateAlternateColorCodes('&', message); }

    public static void playErrorSound(Player player) { player.playSound(player, Sound.ENTITY_ENDERMAN_TELEPORT, 1, 3); }
    public static void playSuccessSound(Player player) { player.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1); }

    public static boolean playerHasPermission(Player player, String permission) { return player.hasPermission(permission); }

    public static Location getSpawnLocation() { return new Location(Bukkit.getWorld("world"), 0.5, -22, 0.5); }
    public static Location getArenaLocation() { return new Location(Bukkit.getWorld("world"), 0, -27, 72); }
    public static Location getArenaCorner1() { return new Location(Bukkit.getWorld("world"), 7, -27, 65); }
    public static Location getArenaCorner2() { return new Location(Bukkit.getWorld("world"), -7, -27, 79); }
}
