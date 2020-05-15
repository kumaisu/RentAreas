/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.rentareas.listener;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.block.Sign;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import com.mycompany.rentareas.config.Config;
import com.mycompany.rentareas.control.InvMenu;
import com.mycompany.rentareas.control.RentControl;
import com.mycompany.rentareas.database.Database;
import com.mycompany.rentareas.database.RentData;
import com.mycompany.kumaisulibraries.Tools;

/**
 *
 * @author sugichan
 */
public class InventoryListener implements Listener {

    /**
     *
     * @param plugin
     */
    public InventoryListener( Plugin plugin ) {
        plugin.getServer().getPluginManager().registerEvents( this, plugin );
    }

    /**
     * クリックされた時の処理
     *
     * @param event 
     */
    @EventHandler
    public void onInventoryClick( InventoryClickEvent event ) {
        Tools.Prt( ChatColor.GOLD + "get Inventory Click Event", Tools.consoleMode.max, Config.programCode );
        Player player = ( Player ) event.getWhoClicked();
        if ( !event.getInventory().equals( InvMenu.inv.get( player.getUniqueId() ) ) ) return;
        if ( !RentData.GetRegion( InvMenu.reg.get( player.getUniqueId() ) ) ) return;

        Sign sign = ( Sign ) Database.Position.getBlock().getState();
        event.setCancelled( true );

        try {
            switch( event.getCurrentItem().getType().name() ) {
                case "BARRIER":
                    sign.setLine( 3, ChatColor.RED + "# " + Config.SignSetKey + " #" );
                    sign.update();
                    RentData.DelSQL( Database.region );
                    break;
                case "END_CRYSTAL":
                    sign.setLine( 0, Database.region );
                    sign.setLine( 1, ( Database.name.equals( "" ) ? ChatColor.BLUE + "For Rent" : Database.name ) );
                    sign.setLine( 3, ChatColor.GOLD + "[" + ChatColor.AQUA + Config.SignSetKey + ChatColor.GOLD + "]" );
                    sign.update();
                    break;
                case "BLUE_WOOL":
                    RentControl.in( player, InvMenu.reg.get( player.getUniqueId() ) );
                    break;
                case "RED_WOOL":
                    RentControl.out( player, InvMenu.reg.get( player.getUniqueId() ) );
                    break;
                case "WOOL":    // 1.12.2 対応
                    Tools.Prt( "WOOL", Tools.consoleMode.max, Config.programCode );
                    event.getWhoClicked().closeInventory();
                    if ( event.getCurrentItem().getItemMeta().getDisplayName().equals( Config.RentIn ) ) { RentControl.in( player, InvMenu.reg.get( player.getUniqueId() ) ); }
                    if ( event.getCurrentItem().getItemMeta().getDisplayName().equals( Config.RentOut ) ) { RentControl.out( player, InvMenu.reg.get( player.getUniqueId() ) ); }
                    break;
                default:
                    Tools.Prt( event.getCurrentItem().getType().name(), Tools.consoleMode.max, Config.programCode );
                    return;
            }
            event.getWhoClicked().closeInventory();
        } catch ( NullPointerException e ) {}
    }

    /**
     * インベントリを閉じた時の処理
     *
     * @param event
     */
    @EventHandler
    public void onInventoryClose( InventoryCloseEvent event ) {
        Tools.Prt( ChatColor.GOLD + "get Inventory Close Event", Tools.consoleMode.max, Config.programCode );
        Player player = ( Player ) event.getPlayer();
        if ( event.getInventory().equals( InvMenu.inv.get( player.getUniqueId() ) ) ) {
            InvMenu.inv.remove( player.getUniqueId() );
        }
    }
}
