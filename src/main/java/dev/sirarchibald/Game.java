package dev.sirarchibald;

import dev.sirarchibald.minigames.ButtonMinigame;
import dev.sirarchibald.minigames.ButtonMinigameEvents;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.function.Supplier;

public class Game {
    private final Main plugin;

    private GameState gameState = GameState.LOBBY;
    private final ArrayList<Minigame> minigamesPlayed = new ArrayList<>();
    private final ArrayList<GamePlayer> players = new ArrayList<>();
    private final HashMap<UUID, Integer> scores = new HashMap<>();

    public Game(Main plugin) { this.plugin = plugin; }

    public GameState getGameState() { return gameState; }
    public void setGameState(GameState newGameState) { gameState = newGameState; }

    public ArrayList<Minigame> getMinigamesPlayed() { return minigamesPlayed; }
    public void addPlayedMinigame(Minigame newMinigame) { minigamesPlayed.add(newMinigame); }

    public ArrayList<GamePlayer> getPlayers() { return players; }
    public void addPlayer(GamePlayer player) { players.add(player); }
    public void removePlayer(GamePlayer player) { players.remove(player); }

    public Integer getPlayerScore(Player player) { return scores.get(player.getUniqueId()) == null ? 0 : scores.get(player.getUniqueId()); }
    public void setPlayerScore(Player player, Integer newScore) {
        if (scores.get(player.getUniqueId()) == null) {
            scores.put(player.getUniqueId(), newScore);
        } else {
            scores.replace(player.getUniqueId(), scores.get(player.getUniqueId()) + newScore);
        }
    }

    public void gameTimer(Game game) {
        while (minigamesPlayed.size() < 10) {
            Minigame nextMinigame = plugin.getGameManager().getRandomMinigame(minigamesPlayed);
            addPlayedMinigame(nextMinigame);
            //nextMinigame.roundCountdown(plugin, game);
            nextMinigame.runMinigame(plugin, game);
        }
        for (GamePlayer gamePlayer : game.getPlayers()) {
            gamePlayer.getPlayer().teleport(Util.getSpawnLocation());
        }
    }

    public void clearGame() {
        players.clear();
        minigamesPlayed.clear();
        scores.clear();
    }
}
