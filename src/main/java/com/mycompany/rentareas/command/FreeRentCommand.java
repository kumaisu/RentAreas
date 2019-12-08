/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.rentareas.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.mycompany.rentareas.RentAreas;
import com.mycompany.rentareas.config.Config;
import com.mycompany.rentareas.database.DataList;
import com.mycompany.kumaisulibraries.Tools;
import com.mycompany.rentareas.database.RentData;
import org.bukkit.ChatColor;

/**
 *
 * @author sugichan
 */
public class FreeRentCommand implements CommandExecutor {

    private final RentAreas instance;

    public FreeRentCommand( RentAreas instance ) {
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
        Tools.Prt( "Free Rental Command", Tools.consoleMode.max, Config.programCode );
        int Lines   = 1;
        int getPage = 1;

        if ( args.length > 0 ) {
            try {
                getPage = Integer.parseInt( args[0] );
                Lines = ( ( getPage - 1 ) * 6 ) + 1;
            } catch ( NumberFormatException e ) {
                Tools.Prt( player, ChatColor.RED + "数値を入力してください", Config.programCode );
            }
        }

        DataList.List( player, -1, Lines );

        int freeAll     = RentData.GetCount( -1 );
        int pageData    = ( freeAll / 6 ) + ( ( ( freeAll % 6 ) == 0 ? 0 : 1 ) );
        Tools.Prt( player, "Data Pages (" + getPage + "/" + pageData + ") All Data : " + freeAll, Config.programCode );
        /*
        Tools.Prt( "全て     : " + RentData.GetCount( 0 ), Config.programCode );
        Tools.Prt( "空き部屋 : " + RentData.GetCount( -1 ), Config.programCode );
        Tools.Prt( "入居済み : " + RentData.GetCount( 1 ), Config.programCode );
        */
        return true;
    }
}
