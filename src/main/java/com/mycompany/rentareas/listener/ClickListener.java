/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.rentareas.listener;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.block.Action;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;
import com.mycompany.rentareas.control.InvMenu;
import com.mycompany.rentareas.config.Config;
import com.mycompany.kumaisulibraries.Tools;
import static com.mycompany.rentareas.config.Config.programCode;

/**
 *
 * @author sugichan
 */
public class ClickListener implements Listener {

    public ClickListener( Plugin plugin ) {
        plugin.getServer().getPluginManager().registerEvents( this, plugin );
    }

    /**
     * 看板設置時に文章を記載した時に発生するイベント
     *
     * @param event
     */
    @EventHandler
    public void onClick( PlayerInteractEvent event ) {
        Tools.Prt( ChatColor.GOLD + "get Click Event", Tools.consoleMode.max, programCode );

        if ( event.getAction() != Action.RIGHT_CLICK_BLOCK ) return;

        Player player = event.getPlayer();
        Block clickedBlock = event.getClickedBlock();
        Material material = clickedBlock.getType();
        Tools.Prt( "Material = " + material.name(), Tools.consoleMode.max, programCode);

        if ( !material.name().contains( "SIGN" ) ) { return; }

        Sign sign = ( Sign ) clickedBlock.getState();
        Tools.Prt( ChatColor.YELLOW + player.getName() + " SignLOC = " + clickedBlock.getLocation().toString(), Tools.consoleMode.max, programCode );
        Tools.Prt( 
            ChatColor.WHITE + "[" +
            sign.getLine( 3 ) +
            ChatColor.WHITE + "] : [" +
            ChatColor.GOLD + "[" + ChatColor.AQUA + Config.SignSetKey + ChatColor.GOLD + "]" +
            ChatColor.WHITE + "]",
            Tools.consoleMode.max, Config.programCode
        );
        if ( sign.getLine( 3 ).equals( ChatColor.GOLD + "[" + ChatColor.AQUA + Config.SignSetKey + ChatColor.GOLD + "]" ) ) {
            event.setCancelled( true );
            for ( int i = 0; i < 4; i++ ) { Tools.Prt( ChatColor.YELLOW + "Sign Line " + i + " : " + sign.getLine( i ), Tools.consoleMode.max, programCode ); }
            //  看板内容更新
            InvMenu.reg.put( player.getUniqueId(), sign.getLine( 0 ) );
            InvMenu.printMenu( player );
        }
    }
}
