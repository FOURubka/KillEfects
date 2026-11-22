package com.killeffects.effects.actions;

import com.killeffects.api.Action;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public record CommandAction(String command) implements Action {
    @Override
    public void execute(Location location, Player killer, Entity victim) {
        String formatted = command
                .replace("%player%", killer.getName())
                // A mob's name tag is player input and must not reach the console
                .replace("%victim%", victim instanceof Player ? victim.getName() : victim.getType().getKey().getKey());
        
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), formatted);
    }
}
