/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.rentareas.listener;

import org.bukkit.plugin.Plugin;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import com.mycompany.kumaisulibraries.Tools;
import com.mycompany.rentareas.config.Config;

/**
 *
 * @author sugichan
 */
public class PlaceListener implements Listener {

    /**
     *
     * @param plugin
     */
    public PlaceListener( Plugin plugin ) {
        plugin.getServer().getPluginManager().registerEvents( this, plugin );
    }

    /**
     * 看板設置時に文章を記載した時に発生するイベント
     *
     * @param event
     */
    @EventHandler
    public void onSignChange( SignChangeEvent event ) {
        Tools.Prt( ChatColor.GOLD + "get Sign Change Event", Tools.consoleMode.max, Config.programCode );
        Player player = event.getPlayer();
        Material material = event.getBlock().getType();
        String Title = event.getLine( 1 );
        Tools.Prt( ChatColor.YELLOW + "Material = " + material.name(), Tools.consoleMode.max, Config.programCode );
        for ( int i = 0; i < 4; i++ ) {
            Tools.Prt( ChatColor.YELLOW + "Old Sign " + i + " : " + event.getLine( i ), Tools.consoleMode.max, Config.programCode );
        }

        if ( event.getLine( 3 ).equals( Config.SignSetKey ) ) {
            //  event.setLine( 0, "WorldGuard Region name" );         
            event.setLine( 1, "for Rent" );         //  Rental Player name area
            //  event.setLine( 2, "Free Message Area" );
            event.setLine( 3, ChatColor.GOLD + "[" + ChatColor.AQUA + Config.SignSetKey + ChatColor.GOLD + "]" );
        }
    }
}
