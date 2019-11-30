/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.rentareas.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.mycompany.rentareas.RentAreas;
import com.mycompany.rentareas.config.Config;
import com.mycompany.kumaisulibraries.Tools;
import com.mycompany.rentareas.config.ConfigManager;
import com.mycompany.rentareas.database.RentData;

/**
 *
 * @author sugichan
 */
public class RentCommand implements CommandExecutor {

    private final RentAreas instance;

    public RentCommand( RentAreas instance ) {
        this.instance = instance;
    }

    /**
     * コマンド入力があった場合に発生するイベント
     *
     * @param sender
     * @param cmd
     * @param commandLabel
     * @param args
     * @return
     */
    @Override
    public boolean onCommand( CommandSender sender,Command cmd, String commandLabel, String[] args ) {
        Player player = ( sender instanceof Player ) ? ( Player ) sender : ( Player ) null;

        Tools.Prt( "Rental Areas Command", Tools.consoleMode.max, Config.programCode );

        if ( ( player != null ) && !player.hasPermission( "rentareas.admin" ) ) {
            Tools.Prt( player,ChatColor.RED + "操作権限がありません", Tools.consoleMode.normal, Config.programCode );
            return false;
        }

        if ( args.length > 0 ) {
            switch( args[0].toLowerCase() ) {
                case "status":
                    ConfigManager.Status( player );
                    return true;
                case "list":
                    RentData.ListRegions( player );
                    return true;
                case "info":
                    if ( RentData.RegionInfo( player, args[1] ) ) {
                        return true;
                    } else {
                        Tools.Prt( player, ChatColor.RED + "No Information.", Config.programCode );
                        return false;
                    }
                case "entry":
                    if ( !RentData.GetRegion( args[1] ) ) {
                        RentData.AddSQL( args[1] );
                        RentData.RegionInfo( player, args[1] );
                        return true;
                    } else {
                        Tools.Prt( player, ChatColor.YELLOW + "Already Entry to Region.", Config.programCode );
                        return false;
                    }
                case "remove":
                    if ( RentData.GetRegion( args[1] ) ) {
                        RentData.DelSQL( args[1] );
                        return true;
                    } else {
                        Tools.Prt( player, ChatColor.RED + "No Entry Region.", Config.programCode );
                        return false;
                    }
                case "expired":
                    return true;
                default:
            }
        }
        Tools.Prt( player, "=== Rental Areas Command Help ===", Config.programCode );
        Tools.Prt( player, "/rent entry [RegionName]", Config.programCode );
        Tools.Prt( player, "/rent remove [RegionName]", Config.programCode );
        Tools.Prt( player, "/rent list", Config.programCode );
        Tools.Prt( player, "/rent info [RegionName]", Config.programCode );
        Tools.Prt( player, "/rent expired", Config.programCode );
        Tools.Prt( player, "/rent stauts", Config.programCode );
        return false;
    }
}
