/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.rentareas.listener;

import org.bukkit.plugin.Plugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import com.mycompany.rentareas.config.Config;
import com.mycompany.rentareas.database.RentData;
import com.mycompany.kumaisulibraries.Tools;
import org.bukkit.ChatColor;

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
