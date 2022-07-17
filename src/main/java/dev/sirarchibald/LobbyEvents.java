package dev.sirarchibald;

import dev.sirarchibald.commands.Join;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Collections;

public class LobbyEvents implements Listener {
    private final Main plugin;
    public LobbyEvents(Main plugin) { this.plugin = plugin; }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        plugin.addOnlinePlayer(player);
        event.setJoinMessage(Util.format("&8[&a+&8]&f " + Util.format(plugin.getOnlinePlayer(player).getChatPrefix()) + player.getName()));
        player.setGameMode(GameMode.ADVENTURE);
        player.teleport(Util.getSpawnLocation());
        player.setPlayerListName(Util.format(plugin.getOnlinePlayer(player).getChatPrefix()) + Util.format(player.getName()));
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.setQuitMessage(Util.format("&8[&C-&8]&f " + Util.format(plugin.getOnlinePlayer(event.getPlayer()).getChatPrefix()) + event.getPlayer().getName()));
        plugin.removeOnlinePlayer(event.getPlayer());
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();
        event.setFormat(Util.format(plugin.getOnlinePlayer(player).getChatPrefix()) + Util.format(player.getName() + " &f: " + message));
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.getPlayer().getGameMode() != GameMode.CREATIVE) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(Util.format("&c&lHey! &7You can't break blocks here!"));
            event.getPlayer().playSound(event.getPlayer(), Sound.BLOCK_LAVA_EXTINGUISH, 1, 1);
        }
    }

    /* disable fall damage */
    @EventHandler
    public void onPlayerFall(EntityDamageEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Player && event.getCause() == EntityDamageEvent.DamageCause.FALL) {
            event.setCancelled(true);
        }
        if (entity instanceof Player && event.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
            event.setCancelled(true);
        }
    }

    /* tp back to spawn if fallen off the platform */
    @EventHandler
    public void onPlayerFall(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location location = player.getLocation();
        if (event.getPlayer().getGameMode() != GameMode.CREATIVE) {
            if (location.getY() <= -36) {
                player.teleport(Util.getSpawnLocation());
                player.playSound(player, Sound.ENTITY_ENDERMAN_TELEPORT, 10, 1);
            }
        }
    }

    /* games menu handler */
    @EventHandler
    public void onLobbyInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked().getGameMode() != GameMode.CREATIVE) {
            if (event.getView().getTitle().equalsIgnoreCase("Games")) {
                if (event.isLeftClick()) {
                    event.setCancelled(true);
                    boolean playerInGame = false;
                    for (Game game : plugin.getGameManager().getGames()) {
                        if (game.getPlayers().contains((Player) event.getWhoClicked())) {
                            event.getView().getPlayer().sendMessage(Util.format("&cYou are already in a game!"));
                            Util.playErrorSound((Player) event.getWhoClicked());
                            event.getWhoClicked().closeInventory();
                            playerInGame = true;
                        }
                    }

                    if (!playerInGame) {
                        ItemStack clickedItem = event.getCurrentItem();
                        Game game = plugin.getGameManager().getGames().stream().findFirst().orElseThrow();

                        if (game.getPlayers().size() == 10) {
                            event.getWhoClicked().sendMessage(Util.format("&cThis game is full!"));
                            Util.playErrorSound((Player) event.getWhoClicked());
                        } else if (game.getGameState() == GameState.RUNNING) {
                            event.getWhoClicked().sendMessage(Util.format("&cThis game has already started!"));
                            Util.playErrorSound((Player) event.getWhoClicked());
                        } else {
                            game.addPlayer(plugin.getOnlinePlayer((Player) event.getWhoClicked()));
                            event.getWhoClicked().sendMessage(Util.format("&l&9" + event.getWhoClicked().getName() + "&f joined the game! (" + game.getPlayers().size() + "/10)"));
                            Util.playSuccessSound((Player) event.getWhoClicked());
                            event.getWhoClicked().closeInventory();

                            ItemStack gameLeaveItem = new ItemStack(Material.RED_WOOL, 1);
                            ItemMeta gameLeaveItemMeta = gameLeaveItem.getItemMeta();
                            gameLeaveItemMeta.setDisplayName(Util.format("&cLeave Game"));
                            gameLeaveItemMeta.setLore(Collections.singletonList(Util.format("&fRight click to leave the game!")));
                            gameLeaveItem.setItemMeta(gameLeaveItemMeta);
                            event.getWhoClicked().getInventory().setItem(8, gameLeaveItem);

                            if (game.getPlayers().size() == 10) {
                                for (Location arenaBlock : plugin.getGameManager().getArenas()) {
                                    if (arenaBlock.getBlock().getType() == Material.RED_WOOL) {
                                        arenaBlock.getBlock().setType(Material.GREEN_WOOL);
                                        for (GamePlayer gamePlayer : game.getPlayers()) {
                                            gamePlayer.getPlayer().teleport(new Location(Bukkit.getWorld("world"), arenaBlock.getX(), arenaBlock.getY() + 2, arenaBlock.getZ()));
                                            gamePlayer.getPlayer().sendMessage(Util.format("&l&2GAME STARTING!"));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /* leave game handler */
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getPlayer().getGameMode() != GameMode.CREATIVE) {
            if (event.getPlayer().getInventory().getItemInMainHand().getType() != Material.AIR) {
                if (event.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName().equalsIgnoreCase(Util.format("&cLeave Game"))) {
                    for (Game game : plugin.getGameManager().getGames()) {
                        if (game.getPlayers().contains(event.getPlayer())) {
                            game.removePlayer(plugin.getOnlinePlayer(event.getPlayer()));
                            for (GamePlayer player : game.getPlayers()) {
                                player.getPlayer().sendMessage(Util.format("&l&9" + event.getPlayer().getDisplayName() + "&f left the game!"));
                            }
                        }
                    }
                    event.getPlayer().sendMessage(Util.format("&aYou left the game!"));
                    event.getPlayer().getInventory().setItem(8, null);
                }
            }
        }
    }

    /* game menu npc */
    @EventHandler
    public void onPlayerInteractWithEntity(PlayerInteractEntityEvent event) {
        if (event.getRightClicked().getType() != EntityType.VILLAGER) return;
        Player player = event.getPlayer();
        Bukkit.dispatchCommand(player, "join");
    }
}