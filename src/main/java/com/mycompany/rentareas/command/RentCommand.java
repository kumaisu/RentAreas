/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.rentareas.command;

import java.util.UUID;
import org.bukkit.block.Sign;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.mycompany.rentareas.RentAreas;
import com.mycompany.rentareas.config.Config;
import com.mycompany.rentareas.config.ConfigManager;
import com.mycompany.rentareas.control.RentControl;
import com.mycompany.rentareas.database.DiffDate;
import com.mycompany.rentareas.database.RentData;
import com.mycompany.kumaisulibraries.Tools;

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

        String region = "";
        String username = "";
        UUID uuid = null;
        Sign sign = null;

        if ( args.length > 1 ) {
            region = args[1];
            sign.setLine( 0, region );
        }
        if ( args.length > 2 ) {
            username = args[2];
            uuid = Tools.getUUID( username );
            sign.setLine( 1, username );
        }

        if ( args.length > 0 ) {
            switch( args[0].toLowerCase() ) {
                case "reload":
                    ConfigManager.load();
                    Tools.Prt( player, ( "Rental Areas Config Reloaded." ), Config.programCode );
                    return true;
                case "console":
                    if ( !Tools.setDebug( args[1], Config.programCode ) ) {
                        Tools.entryDebugFlag( Config.programCode, Tools.consoleMode.normal );
                        Tools.Prt( player, ChatColor.RED + "Config Debugモードの指定値が不正なので、normal設定にしました", Config.programCode );
                    }
                    Tools.Prt( player,
                        ChatColor.GREEN + "System Debug Mode is [ " +
                        ChatColor.RED + Tools.consoleFlag.get( Config.programCode ).toString() +
                        ChatColor.GREEN + " ]",
                        Config.programCode
                    );
                    return true;
                case "status":
                    ConfigManager.Status( player );
                    return true;
                case "list":
                    RentData.ListRegions( player );
                    return true;
                case "info":
                    if ( RentData.RegionInfo( player, region ) ) {
                        return true;
                    } else {
                        Tools.Prt( player, ChatColor.RED + "No Information.", Config.programCode );
                        return false;
                    }
                case "entry":
                    if ( !RentData.GetRegion( region ) ) {
                        RentData.AddSQL( region );
                        RentData.RegionInfo( player, region );
                        return true;
                    } else {
                        Tools.Prt( player, ChatColor.YELLOW + "Already Entry to Region.", Config.programCode );
                        return false;
                    }
                case "remove":
                    if ( RentData.GetRegion( region ) ) {
                        RentData.DelSQL( region );
                        return true;
                    } else {
                        Tools.Prt( player, ChatColor.RED + "No Entry Region.", Config.programCode );
                        return false;
                    }
                case "expired":
                    DiffDate.List( player );
                    return true;
                case "define":
                    if ( ( !region.equals( "" ) ) && ( uuid != null ) ) {
                        Tools.Prt( player, "Manual Rent IN [" + region + "] " + username, Tools.consoleMode.full, Config.programCode );
                        if ( !RentControl.Rental( uuid, username, sign, true ) ) {
                            Tools.Prt( player, ChatColor.RED + "Already Rental now", Tools.consoleMode.full, Config.programCode );
                        }
                        return true;
                    }
                    break;
                case "undefine":
                    if ( !region.equals( "" ) ) {
                        Tools.Prt( player, "Manual Rent Out [" + region + "]", Tools.consoleMode.full, Config.programCode );
                        sign.setLine( 1, ChatColor.BLUE + "for Rent" );
                        if ( RentControl.Rental( null, "", sign, false ) ) {
                            Tools.Prt( player, ChatColor.AQUA + "Rental released", Tools.consoleMode.full, Config.programCode );
                        }
                        return true;
                    }
                    break;
                default:
                    Tools.Prt( player, "Unknown Command [" + args[0] + "]", Config.programCode );
                    break;
            }
        }
        Tools.Prt( player, "=== Rental Areas Command Help ===", Config.programCode );
        Tools.Prt( player, "/rent entry [region]", Config.programCode );
        Tools.Prt( player, "/rent remove [region]", Config.programCode );
        Tools.Prt( player, "/rent list", Config.programCode );
        Tools.Prt( player, "/rent info [region]", Config.programCode );
        Tools.Prt( player, "/rent expired", Config.programCode );
        Tools.Prt( player, "/rent defne [region] [player]", commandLabel);
        Tools.Prt( player, "/rent undefne [region]", commandLabel);
        Tools.Prt( player, "/rent stauts", Config.programCode );
        Tools.Prt( player, "/rent reload", Config.programCode );
        Tools.Prt( player, "/rent console [max,full,normal,stop]", Config.programCode );
        return false;
    }
}
