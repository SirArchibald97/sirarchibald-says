package dev.sirarchibald.commands;

import dev.sirarchibald.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Db implements CommandExecutor {
    private Main plugin;

    public Db(Main plugin) { this.plugin = plugin; }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        Player player = (Player) sender;

        try {
            Connection conn = plugin.getDataSource().getConnection();
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM users WHERE uuid = '" + player.getUniqueId() + "'");
            ResultSet rs = statement.executeQuery();
            rs.next();
            sender.sendMessage("Your rank: " + rs.getString("rank"));
            conn.close();
            statement.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return true;
    }
}
