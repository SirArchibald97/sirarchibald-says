package dev.sirarchibald.minigames;

import dev.sirarchibald.Game;
import dev.sirarchibald.Main;
import dev.sirarchibald.Minigame;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.UUID;

public class PortalMinigame extends Minigame implements Listener {
    private final HashMap<UUID, Integer> roundScores = new HashMap<>();
    private final int gameDuration = 30;

    public PortalMinigame() {
        super("Portals", "Enter the right portal!");
    }

    @Override
    public void runMinigame(Main main, Game game) {

    }

    @Override
    public HashMap<UUID, Integer> getMinigameScore() { return roundScores; }

    @Override
    public int getGameDuration() { return gameDuration; }
}