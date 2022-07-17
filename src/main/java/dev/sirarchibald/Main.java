package dev.sirarchibald;

import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;
import com.mysql.cj.jdbc.MysqlDataSource;
import dev.sirarchibald.commands.Create;
import dev.sirarchibald.commands.Db;
import dev.sirarchibald.commands.Join;
import dev.sirarchibald.commands.Start;
import dev.sirarchibald.minigames.ButtonMinigame;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

public class Main extends JavaPlugin {
    private GameManager gameManager;
    private DataSource dataSource;
    private final HashMap<UUID, GamePlayer> onlinePlayers = new HashMap<>();

    @Override
    public void onEnable() {
        super.onEnable();
        this.gameManager = new GameManager(this);

        Objects.requireNonNull(this.getCommand("create")).setExecutor(new Create(this));
        Objects.requireNonNull(this.getCommand("join")).setExecutor(new Join(this));
        Objects.requireNonNull(this.getCommand("start")).setExecutor(new Start(this));
        Objects.requireNonNull(this.getCommand("db")).setExecutor(new Db(this));

        getServer().getPluginManager().registerEvents(new LobbyEvents(this), this);
        getServer().getPluginManager().registerEvents(new ButtonMinigame(this), this);

        try { this.dataSource = initMySQLDataSource(); } catch (SQLException e) { this.getLogger().warning("Could not establish database connection!"); }
    }

    public GameManager getGameManager() { return gameManager; }

    public void testDataSource(DataSource dataSource) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            if (!connection.isValid(1)) {
                this.getLogger().warning("Failed to connect to database!");
                throw new SQLException("Could not establish database connection!");
            }
        }
    }

    private DataSource initMySQLDataSource() throws SQLException {
        MysqlDataSource dataSource = new MysqlConnectionPoolDataSource();
        dataSource.setServerName("135.181.250.92");
        dataSource.setPortNumber(3306);
        dataSource.setDatabaseName("archibaldmc");
        dataSource.setUser("archie");
        dataSource.setPassword("Xellence_123");

        testDataSource(dataSource);
        this.getLogger().info("Connected to database!");
        return dataSource;
    }

    public DataSource getDataSource() { return dataSource; }

    public GamePlayer getOnlinePlayer(Player player) { return onlinePlayers.get(player.getUniqueId()); }
    public void addOnlinePlayer(Player player) { onlinePlayers.put(player.getUniqueId(), new GamePlayer(this, player)); }
    public void removeOnlinePlayer(Player player) { onlinePlayers.remove(player.getUniqueId()); }
}
