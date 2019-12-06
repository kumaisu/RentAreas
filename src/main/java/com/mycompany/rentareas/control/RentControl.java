/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.rentareas.control;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import com.mycompany.kumaisulibraries.Tools;
import com.mycompany.rentareas.config.Config;
import com.mycompany.rentareas.database.Database;
import com.mycompany.rentareas.database.RentData;
import java.util.UUID;

/**
 *
 * @author sugichan
 */
public class RentControl {

    public static boolean in( Player player, UUID uuid, String playerName ) {
        RentData.GetRegion( InvMenu.reg.get( player.getUniqueId() ) );
        
        Tools.Prt( "Rent IN [" + Database.region + "]", Tools.consoleMode.full, Config.programCode );

        String Command;

        Sign sign = ( Sign ) Database.Position.getBlock().getState();
        if ( Database.name.equals( "" ) ) {
            sign.setLine( 1, playerName );
            sign.update();
            // rg addowner -w world_basic 1101 Kumaisu
            Command = "rg addowner -w " + Database.world + " " + Database.region + " " + playerName;
            Tools.Prt( ChatColor.YELLOW + "Command : " + Command, Tools.consoleMode.max, Config.programCode );
            Bukkit.getServer().dispatchCommand( Bukkit.getConsoleSender(), Command );
            RentData.SetPlayerToSQL( uuid, playerName, Database.region );
            Tools.Prt( player, ChatColor.GREEN + "Completed move-in", Tools.consoleMode.full, Config.programCode );
            return true;
        } else {
            Tools.Prt( player, ChatColor.RED + "Already Rental now", Tools.consoleMode.full, Config.programCode );
            return false;
        }
    }
    public static boolean in( Player player ) {
        return in( player, player.getUniqueId(), player.getName() );
    }

    public static boolean out( Player player ) {
        RentData.GetRegion( InvMenu.reg.get( player.getUniqueId() ) );

        Tools.Prt( "Rent OUT", Tools.consoleMode.full, Config.programCode );

        String Command;

        Sign sign = ( Sign ) Database.Position.getBlock().getState();
        if ( Database.name.equals( player.getName() ) ) {
            sign.setLine( 1, ChatColor.BLUE + "for Rent" );
            sign.update();
            //rg removeowner -w world_basic 1101 Kumaisu
            Command = "rg removeowner -w " + Database.world + " " + Database.region + " " + Database.name;
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
