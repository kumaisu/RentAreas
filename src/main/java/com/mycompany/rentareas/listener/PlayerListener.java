/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.rentareas.listener;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import com.mycompany.rentareas.config.Config;
import com.mycompany.rentareas.database.RentData;
import com.mycompany.rentareas.database.DataList;
import com.mycompany.kumaisulibraries.Tools;
import org.bukkit.Sound;

/**
 *
 * @author sugichan
 */
public class PlayerListener implements Listener {

    /**
     *
     * @param plugin
     */
    public PlayerListener( Plugin plugin ) {
        plugin.getServer().getPluginManager().registerEvents( this, plugin );
    }

    /**
     * プレイヤーがログインしようとした時に起きるイベント
     * BANなどされていてもこのイベントは発生する
     *
     * @param event
     */
    @EventHandler( priority = EventPriority.LOWEST )
    public void onPlayerLogin( PlayerJoinEvent event ) {
        Tools.Prt( "onPlayerLogin process", Tools.consoleMode.max, Config.programCode );
        Player player = event.getPlayer();
        if ( Config.Warning && player.hasPermission( "RentAreas.admin" ) ) {
            DataList.Expired( player );
            player.getLocation().getWorld().playSound( player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1 );
        }
    }

    /**
     * プレイヤーのログアウト時処理、参加者であればメモリのスコアをスコアファイルに保存する
     *
     * @param event
     */
    @EventHandler
    public void onPlayerQuit( PlayerQuitEvent event ) {
        Tools.Prt( ChatColor.GREEN + "Rental Data Refreshed", Tools.consoleMode.max, Config.programCode );
        RentData.SetLogoutToSQL( event.getPlayer().getUniqueId() );
    }
}
