/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.rentareas.config;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import com.mycompany.kumaisulibraries.Tools;
import static com.mycompany.rentareas.config.Config.programCode;

/**
 *
 * @author sugichan
 */
public class ConfigManager {

    private static Plugin plugin;
    private static FileConfiguration config = null;

    public ConfigManager(Plugin plugin) {
        this.plugin = plugin;
        Tools.entryDebugFlag( programCode, Tools.consoleMode.print );
        Tools.Prt( "Config Loading now...", programCode );
        load();
    }

    /*
     * 設定をロードします
     */
    public static void load() {
        // 設定ファイルを保存
        plugin.saveDefaultConfig();
        if ( config != null ) { // configが非null == リロードで呼び出された
            Tools.Prt( "Config Reloading now...", programCode );
            plugin.reloadConfig();
        }
        config = plugin.getConfig();

        if ( !Tools.setDebug( config.getString( "Debug" ), programCode ) ) {
            Tools.entryDebugFlag( programCode, Tools.consoleMode.normal );
            Tools.Prt( ChatColor.RED + "Config Debugモードの指定値が不正なので、normal設定にしました", programCode );
        }

        Config.host         = config.getString( "mysql.host" );
        Config.port         = config.getString( "mysql.port" );
        Config.database     = config.getString( "mysql.database" );
        Config.worldguard   = config.getString( "mysql.worldguard" );
        Config.username     = config.getString( "mysql.username" );
        Config.password     = config.getString( "mysql.password" );

        Config.SignSetKey   = config.getString( "SignSetKey" );

        Config.MenuString   = config.getString( "Menu", "Rental Menu" );
        Config.RentIn       = config.getString( "RentIn" );
        Config.RentOut      = config.getString( "RentOut" );
        Config.Expired      = config.getInt( "RentExpired", 0 );
        Config.RentNum      = config.getInt( "RentNum", 0 );
        Config.Warning      = config.getBoolean( "Warning", false );
    }

    public static void Status( Player p ) {
        Tools.Prt( p, ChatColor.GREEN + "=== RentAreas Status ===", programCode );
        Tools.Prt( p, ChatColor.WHITE + "Degub Mode   : " + ChatColor.YELLOW + Tools.consoleFlag.get( programCode ).toString(), programCode );
        Tools.Prt( p, ChatColor.WHITE + "Mysql        : " + ChatColor.YELLOW + Config.host + ":" + Config.port, programCode );
        Tools.Prt( p, ChatColor.WHITE + "DB Name      : " + ChatColor.YELLOW + Config.database, programCode );
        Tools.Prt( p, ChatColor.WHITE + "WG DB Name   : " + ChatColor.YELLOW + Config.worldguard, programCode );
        if ( ( p == null ) || p.hasPermission( "rentareas.console" ) ) {
            Tools.Prt( p, ChatColor.WHITE + "DB UserName  : " + ChatColor.YELLOW + Config.username, programCode );
            Tools.Prt( p, ChatColor.WHITE + "DB Password  : " + ChatColor.YELLOW + Config.password, programCode );
        }
        Tools.Prt( p, ChatColor.WHITE + "Sign Set Key : " + ChatColor.YELLOW + Config.SignSetKey, programCode );
        Tools.Prt( p, ChatColor.WHITE + "Warning      : " + ChatColor.YELLOW + ( Config.Warning ? "Notification" : "Unannounced" ), programCode );
        Tools.Prt( p, ChatColor.WHITE + "Menu String  : " + ChatColor.YELLOW + Config.MenuString, programCode );
        Tools.Prt( p, ChatColor.WHITE + "Rental IN    : " + ChatColor.YELLOW + Config.RentIn, programCode );
        Tools.Prt( p, ChatColor.WHITE + "Rental Out   : " + ChatColor.YELLOW + Config.RentOut, programCode );
        Tools.Prt( p, ChatColor.WHITE + "Expired      : " + ChatColor.YELLOW + Config.Expired + " days", programCode );
        Tools.Prt( p, ChatColor.WHITE + "Rent Max     : " + ChatColor.YELLOW + Config.RentNum, programCode );
        Tools.Prt( p, ChatColor.GREEN + "==========================", programCode );
    }
}
