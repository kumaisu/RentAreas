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
import com.mycompany.rentareas.config.ConfigManager;
import com.mycompany.rentareas.database.DataList;
import com.mycompany.rentareas.database.RentData;
import com.mycompany.kumaisulibraries.Tools;
import com.mycompany.rentareas.control.RentControl;

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

        if ( args.length > 1 ) { region = args[1]; }
        if ( args.length > 2 ) { username = args[2]; }

        if ( args.length > 0 ) {
            switch( args[0].toLowerCase() ) {
                case "reload":
                    ConfigManager.load();
                    Tools.Prt( player, ( "Rental Areas Config Reloaded." ), Config.programCode );
                    break;
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
                    break;
                case "status":
                    ConfigManager.Status( player );
                    break;
                case "vacancy":
                    DataList.List( player, -1 );
                    break;
                case "list":
                    DataList.List( player, 0 );
                    break;
                case "tenant":
                    DataList.List( player, 1 );
                    break;
                case "info":
                    if ( RentData.RegionInfo( player, region ) ) {
                        break;
                    } else {
                        Tools.Prt( player, ChatColor.RED + "No Information.", Config.programCode );
                        return false;
                    }
                case "expired":
                    DataList.Expired( player );
                    break;
                case "define":
                    if ( ( region.equals( "" ) ) || ( Tools.getUUID( username ) == null ) ) { return false; }
                    Tools.Prt( player, "Manual Rent IN [" + region + "] " + username, Tools.consoleMode.full, Config.programCode );
                    RentControl.in( player, region, Tools.getUUID( username ), username );
                    break;
                case "undefine":
                    if ( region.equals( "" ) ) { return false; }
                    Tools.Prt( player, "Manual Rent Out [" + region + "]", Tools.consoleMode.full, Config.programCode );
                    RentControl.out( player, region );
                    break;
                case "help":
                    Tools.Prt( player, "=== Rental Areas Command Help ===", Config.programCode );
                    Tools.Prt( player, "空室リスト      /rent vacancy", Config.programCode );
                    Tools.Prt( player, "全部屋リスト    /rent list", Config.programCode );
                    Tools.Prt( player, "入居者リスト    /rent tenant", Config.programCode );
                    Tools.Prt( player, "期限切れリスト  /rent expired", Config.programCode );
                    Tools.Prt( player, "部屋情報        /rent info [region]", Config.programCode );
                    Tools.Prt( player, "手動入居処理    /rent defne [region] [player]", Config.programCode );
                    Tools.Prt( player, "手動退去処理    /rent undefne [region]", Config.programCode );
                    Tools.Prt( player, "システム状態    /rent stauts", Config.programCode );
                    Tools.Prt( player, "設定再読込      /rent reload", Config.programCode );
                    Tools.Prt( player, "表示モード切替  /rent console [max,full,normal,stop]", Config.programCode );
                    break;
                default:
                    Tools.Prt( player, ChatColor.RED + "Unknown Command [" + args[0] + "]", Config.programCode );
                    return false;
            }
        }
        return true;
    }
}
