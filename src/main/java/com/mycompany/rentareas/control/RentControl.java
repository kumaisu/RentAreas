/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.rentareas.control;

import com.mycompany.kumaisulibraries.Tools;
import com.mycompany.rentareas.config.Config;
import com.mycompany.rentareas.database.Database;
import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import com.mycompany.rentareas.database.RentData;
import org.bukkit.Bukkit;

/**
 *
 * @author sugichan
 */
public class RentControl {

    public static boolean Rental( Player player, Sign sign, boolean rentF ) {
        String region   = sign.getLine( 0 );
        String name     = sign.getLine( 1 );
        String world    = sign.getLocation().getWorld().getName();
        String Command;

        RentData.GetRegion( region );
        if ( rentF ) {
            if ( Database.name.equals( "" ) ) {
                RentData.SetPlayerToSQL( player, region );
                sign.setLine( 1, player.getName() );
                // rg addowner -w world_basic 1101 Kumaisu
                Command = "rg addowner -w " + world + " " + region + " " + player.getName();
            } else return false;
        } else {
            if ( Database.name.equals( player.getName() ) ) {
                RentData.SetPlayerToSQL( null, region );
                sign.setLine( 1, ChatColor.BLUE + "for Rent" );
                //rg removeowner -w world_basic 1101 Kumaisu
                Command = "rg removeowner -w " + world + " " + region + " " + name;
            } else return false;
        }
        sign.update();
        Tools.Prt( ChatColor.YELLOW + "Command : " + Command, Tools.consoleMode.max, Config.programCode );
        Bukkit.getServer().dispatchCommand( Bukkit.getConsoleSender(), Command );
        return true;
    }
}
