package dev.sirarchibald;

import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class GamePlayer {
    private Main plugin;
    private Player player;
    private Rank rank;
    private String chatPrefix;
    private Game currentGame = null;

    public GamePlayer(Main plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
        loadPlayerData();
    }

    private void loadPlayerData() {
        try (Connection conn = plugin.getDataSource().getConnection(); PreparedStatement statement = conn.prepareStatement("SELECT * FROM users WHERE uuid = '" + player.getUniqueId() + "'")) {
            ResultSet rs = statement.executeQuery();
            rs.next();

            HashMap<String, Rank> ranks = new HashMap<>();
            ranks.put("admin", Rank.ADMIN);
            ranks.put("member", Rank.MEMBER);
            rank = ranks.get(rs.getString("rank"));

            HashMap<String, String> prefixes = new HashMap<>();
            prefixes.put("admin", "&c[ADMIN] ");
            prefixes.put("member", "&f");
            chatPrefix = prefixes.get(rs.getString("rank"));

        } catch (SQLException throwables) { throwables.printStackTrace(); }
    }

    public Player getPlayer() { return player; }

    public Rank getRank() { return rank; }
    public String getChatPrefix() { return chatPrefix; }

    public Game getCurrentGame() { return currentGame; }
    public void setCurrentGame(Game newGame) { currentGame = newGame; }
}
