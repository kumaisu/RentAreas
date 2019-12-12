/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.rentareas.listener;

import org.bukkit.plugin.Plugin;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import com.mycompany.kumaisulibraries.Tools;
import com.mycompany.rentareas.config.Config;

/**
 *
 * @author sugichan
 */
public class BreakListener implements Listener {

    public BreakListener( Plugin plugin ) {
        plugin.getServer().getPluginManager().registerEvents( this, plugin );
    }

    /**
     * ブロックが破壊された時の処理
     *
     * @param event
     */
    @EventHandler
    public void onBlockBreak( BlockBreakEvent event ) {
        Tools.Prt( ChatColor.GOLD + "get BlockBreak Event", Tools.consoleMode.max, Config.programCode );

        Player player = event.getPlayer();
        Block block = event.getBlock();
        Material material = block.getType();
        Tools.Prt( "Material = " + material.name(), Tools.consoleMode.max, Config.programCode );

        if ( !material.name().contains( "SIGN" ) ) return;
        //  if ( player.hasPermission( "rentareas.admin" ) ) return;

        //  DBからデータ取得
        Tools.Prt( ChatColor.YELLOW + player.getName() + " SignLoc = " + block.getLocation().toString(), Tools.consoleMode.max, Config.programCode );
        Sign sign = ( Sign ) block.getState();
        if ( sign.getLine( 3 ).equals( ChatColor.GOLD + "[" + ChatColor.AQUA + Config.SignSetKey + ChatColor.GOLD + "]" ) ) {
            Tools.Prt( ChatColor.GOLD + "get [" + ChatColor.AQUA + "RentArea"+ ChatColor.GOLD + "] Sign Break Cancelled", Tools.consoleMode.max, Config.programCode );
            event.setCancelled( true );
        }
    }
}
