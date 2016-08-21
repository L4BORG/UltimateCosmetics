package com.j0ach1mmall3.ultimatecosmetics;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.NameTagVisibility;

import java.util.HashSet;
import java.util.Set;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 5/04/2016
 */
public final class HiddenPlayersRunnable implements Runnable {
    private final Set<String> hiddenPlayers = new HashSet<>();

    @SuppressWarnings("deprecation")
    @Override
    public void run() {
        for(Player player : Bukkit.getOnlinePlayers()) {
            if(player.getScoreboard().getTeam("UC_HIDDEN") == null) player.getScoreboard().registerNewTeam("UC_HIDDEN");
            player.getScoreboard().getTeam("UC_HIDDEN").setNameTagVisibility(NameTagVisibility.NEVER);
            for(String s : this.hiddenPlayers) {
                player.getScoreboard().getTeam("UC_HIDDEN").addEntry(s);
            }
        }
    }

    public void addPlayer(Player player) {
        this.hiddenPlayers.add(player.getUniqueId().toString());
    }

    public void removePlayer(Player player) {
        this.hiddenPlayers.remove(player.getUniqueId().toString());
    }

    public boolean containsPlayer(Player player) {
        return this.hiddenPlayers.contains(player.getUniqueId().toString());
    }
}
