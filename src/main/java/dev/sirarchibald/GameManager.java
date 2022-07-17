package dev.sirarchibald;

import dev.sirarchibald.minigames.ButtonMinigame;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.*;

public class GameManager {
    private final Main plugin;
    private final ArrayList<Game> games = new ArrayList<>();
    private final Minigame[] minigames;
    private final ArrayList<Location> arenas = new ArrayList<>();

    public GameManager(Main plugin) {
        this.plugin = plugin;
        this.minigames = new Minigame[]{ new ButtonMinigame(plugin) };

        int[] arenaStateBlocks = { -11, 17, -22, 0, -11, -17, 0, 0, 11, -17, 22, 0, 11, 17 };
        for (int i = 0; i < arenaStateBlocks.length; i += 2) {
            arenas.add(new Location(Bukkit.getWorld("world"), arenaStateBlocks[i], -38, arenaStateBlocks[i + 1]));
        }
    }

    public ArrayList<Game> getGames() { return games; }
    public void addGame(Game newGame) { games.add(newGame); }
    public void endGame(Game game) { games.remove(game); }

    public ArrayList<Location> getArenas() { return arenas; }

    public Minigame getMinigameByName(String minigameName) {
        for (Minigame minigame : minigames) {
            if (minigame.getName().equalsIgnoreCase(minigameName)) {
                return minigame;
            }
        }
        return null;
    }

    public Minigame getRandomMinigame(ArrayList<Minigame> playedMinigames) {
        List<Minigame> remainingMinigames = Arrays.stream(minigames).filter(mg -> !playedMinigames.contains(mg)).toList();
        Collections.shuffle(remainingMinigames);
        System.out.println(remainingMinigames);
        return remainingMinigames.stream().findFirst().orElseThrow();
    }
}
