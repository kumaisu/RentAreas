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
        
        //if ( !SignData.GetSignLoc( InvMenu.loc.get( player.getUniqueId() ) ) ) return;

        event.setCancelled( true );

        try {
            switch( event.getCurrentItem().getType().name() ) {
                case "BARRIER":
                    if ( event.getCurrentItem().getItemMeta().getDisplayName().contains( "Remove" ) ) {
                        event.getWhoClicked().closeInventory();
                        Tools.Prt( ChatColor.YELLOW + player.getName() + " Signloc = " + InvMenu.loc.get( player.getUniqueId() ).toString(), Tools.consoleMode.max, Config.programCode );
                        //  DBから看板削除 & イイネDBをクリアー
                        //  SignData.DelSQL( Database.ID );
                        //  LikePlayerData.DelSQL( Database.ID );
                        //  対象看板を破壊する操作※追加予定　暫定的に1行目を赤にして破壊可能にする
                        sign.setLine( 0, ChatColor.RED + "#[ThisLike]#" );
                        sign.update();
                        //  Tools.Prt( player, Config.ReplaceString( Config.Remove ), Tools.consoleMode.full, programCode );
                        return;
                    }
                    break;
                case "END_CRYSTAL":
                    event.getWhoClicked().closeInventory();
                    if ( event.getCurrentItem().getItemMeta().getDisplayName().contains( "Update" ) ) {
                        //  for ( int i = 0; i < 4; i++ ) { sign.setLine( i, Config.ReplaceString( Config.SignBase.get( i ) ) ); }
                        sign.update();
                        return;
                    }
                    break;
                case "BLUE_WOOL":
                    event.getWhoClicked().closeInventory();
                    //  if ( event.getCurrentItem().getItemMeta().getDisplayName().contains( Config.like ) ) {
                        //  LikeControl.SetLike( Database.ID, player );
                    //  }
                    break;
                case "RED_WOOL":
                    event.getWhoClicked().closeInventory();
                    //  if ( event.getCurrentItem().getItemMeta().getDisplayName().contains( Config.unlike ) ) {
                    //      LikeControl.SetUnlike( Database.ID, player );
                    //  }
                    break;
                case "WOOL":    // 1.12.2 対応
                    Tools.Prt( "WOOL", Tools.consoleMode.max, Config.programCode );
                    event.getWhoClicked().closeInventory();
                    //  if ( event.getCurrentItem().getItemMeta().getDisplayName().equals( Config.like ) ) {
                    //      LikeControl.SetLike( Database.ID, player );
                    //  }
                    //  if ( event.getCurrentItem().getItemMeta().getDisplayName().equals( Config.unlike ) ) {
                    //      LikeControl.SetUnlike( Database.ID, player );
                    //  }
                    break;
                default:
                    Tools.Prt( event.getCurrentItem().getType().name(), Tools.consoleMode.max, Config.programCode );
            }

            //  看板内容更新
            //  sign.setLine( 3, Config.ReplaceString( Config.SignBase.get( 3 ) ) );
            sign.update();
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
