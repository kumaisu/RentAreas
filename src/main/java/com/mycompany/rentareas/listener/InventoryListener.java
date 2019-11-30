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
import com.mycompany.kumaisulibraries.Tools;
import com.mycompany.rentareas.control.RentControl;

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
        Sign sign = (Sign) InvMenu.loc.get( player.getUniqueId() ).getBlock().getState();
        String region = sign.getLine( 0 );
        event.setCancelled( true );

        try {
            switch( event.getCurrentItem().getType().name() ) {
                case "END_CRYSTAL":     //  予備メニュー
                    event.getWhoClicked().closeInventory();
                    break;
                case "BLUE_WOOL":
                    event.getWhoClicked().closeInventory();
                    Tools.Prt( player, "Rent IN [" + region + "]", Tools.consoleMode.full, Config.programCode );
                    if ( !RentControl.Rental( player, sign, true ) ) {
                        Tools.Prt( player, ChatColor.RED + "Already Rental now", Tools.consoleMode.full, Config.programCode );
                    }
                    break;
                case "RED_WOOL":
                    event.getWhoClicked().closeInventory();
                    Tools.Prt( player, "Rent OUT [" + region + "]", Tools.consoleMode.full, Config.programCode );
                    RentControl.Rental( player, sign, false );
                    break;
                case "WOOL":    // 1.12.2 対応
                    Tools.Prt( "WOOL", Tools.consoleMode.max, Config.programCode );
                    event.getWhoClicked().closeInventory();
                    if ( event.getCurrentItem().getItemMeta().getDisplayName().equals( Config.RentIn ) ) {
                        Tools.Prt( player, "Rent IN", Tools.consoleMode.full, Config.programCode );
                        RentControl.Rental( player, sign, true );
                    }
                    if ( event.getCurrentItem().getItemMeta().getDisplayName().equals( Config.RentOut ) ) {
                        Tools.Prt( player, "Rent OUT", Tools.consoleMode.full, Config.programCode );
                        RentControl.Rental( player, sign, false );
                    }
                    break;
                default:
                    Tools.Prt( event.getCurrentItem().getType().name(), Tools.consoleMode.max, Config.programCode );
            }
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
