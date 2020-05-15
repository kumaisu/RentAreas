/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.rentareas.control;

import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import com.mycompany.kumaisulibraries.Tools;
import com.mycompany.rentareas.config.Config;
import com.mycompany.rentareas.database.DataList;
import com.mycompany.rentareas.database.Database;
import com.mycompany.rentareas.database.RentData;

/**
 *
 * @author sugichan
 */
public class RentControl {

    public static boolean in( Player player, String region, UUID uuid, String playerName ) {
        Tools.Prt( "Rent IN [" + Database.region + "]", Tools.consoleMode.full, Config.programCode );

        if ( !player.hasPermission( "rentareas.rental" ) ) {
            Tools.Prt( player, ChatColor.RED + playerName + " さんは借りる権限がありません", Tools.consoleMode.full, Config.programCode );
            return false;
        }
        
        if ( RentData.RentCount( uuid ) >= Config.RentNum ) {
            Tools.Prt( player, ChatColor.RED + playerName + " さんはこれ以上の借入はできません", Tools.consoleMode.full, Config.programCode );
            DataList.ListPlayer( player, player.getUniqueId(), player.getName() );
            return false;
        }
        
        RentData.GetRegion( region );
        Sign sign = ( Sign ) Database.Position.getBlock().getState();
        if ( Database.name.equals( "" ) ) {
            sign.setLine( 1, playerName );
            sign.update();
            // rg addowner -w world_basic 1101 Kumaisu
            String Command = "rg addowner -w " + Database.world + " " + Database.region + " " + playerName;
            Tools.Prt( ChatColor.YELLOW + "Command : " + Command, Tools.consoleMode.max, Config.programCode );
            Bukkit.getServer().dispatchCommand( Bukkit.getConsoleSender(), Command );
            RentData.SetPlayerToSQL( uuid, playerName, Database.region );
            RentData.SetLogoutToSQL( uuid );
            Tools.Prt( player, ChatColor.GREEN + "Completed move-in", Tools.consoleMode.full, Config.programCode );
            return true;
        } else {
            Tools.Prt( player, ChatColor.RED + "Already Rental now", Tools.consoleMode.full, Config.programCode );
            return false;
        }
    }
    public static boolean in( Player player, String region ) {
        return in( player, region, player.getUniqueId(), player.getName() );
    }

    public static boolean out( Player player, String region ) {
        Tools.Prt( player, "Rent OUT [" + region + "]", Tools.consoleMode.full, Config.programCode );
        RentData.GetRegion( region );
        Sign sign = ( Sign ) Database.Position.getBlock().getState();
        if ( Database.name.equals( player.getName() ) || player.hasPermission( "rentareas.admin" ) ) {
            sign.setLine( 1, ChatColor.BLUE + "for Rent" );
            sign.update();
            //rg removeowner -w world_basic 1101 Kumaisu
            String Command = "rg removeowner -w " + Database.world + " " + Database.region + " " + Database.name;
            Tools.Prt( ChatColor.YELLOW + "Command : " + Command, Tools.consoleMode.max, Config.programCode );
            Bukkit.getServer().dispatchCommand( Bukkit.getConsoleSender(), Command );
            RentData.SetPlayerToSQL( null, "", Database.region );
            Tools.Prt( player, ChatColor.GREEN + "Removal completed", Tools.consoleMode.full, Config.programCode );
            return true;
        } else {
            Tools.Prt( player, ChatColor.RED + "Cant Release " + player.getName(), Tools.consoleMode.full, Config.programCode );
            return false;
        }
    }
}
