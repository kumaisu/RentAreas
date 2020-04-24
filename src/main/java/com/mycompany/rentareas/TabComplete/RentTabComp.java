/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.rentareas.TabComplete;

import java.util.List;
import java.util.ArrayList;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

/**
 *  Copyright (c) 2019 sugichan. All rights reserved.
 *
 * @author sugichan
 */
public class RentTabComp implements TabCompleter {

    /**
     * HOME Command TAB Complete list
     *
     * @param sender
     * @param command
     * @param alias
     * @param args
     * @return 
     */
    @Override
    public List<String> onTabComplete( CommandSender sender, Command command, String alias, String[] args ) {
        List< String > list = new ArrayList<>();
        Player player = ( Player ) ( sender instanceof Player ? sender : null );

        switch ( args.length ) {
            case 1:
                if ( player == null ? true : player.hasPermission("rentareas.console" ) ) {
                    list.add( "reload" );
                    list.add( "console" );
                }
                list.add( "status" );
                list.add( "vacancy" );
                list.add( "list" );
                list.add( "tenant" );
                list.add( "info" );
                list.add( "expired" );
                list.add( "extension" );
                list.add( "limit" );
                list.add( "define" );
                list.add( "undefine" );
                list.add( "search" );
                list.add( "help" );
                break;
            case 2:
                switch ( args[0] ) {
                    case "vacancy":
                    case "list":
                    case "tenant":
                        list.add( "[page]" );
                        break;
                    case "extension":
                    case "limit":
                        list.add( "[region]" );
                        break;
                    case "info":
                    case "define":
                    case "undefine":
                        list.add( "[region]" );
                        break;
                    case "search":
                        if ( player != null ) { list.add( player.getName() ); }
                        break;
                    case "Console":
                        list.add( "full" );
                        list.add( "max" );
                        list.add( "normal" );
                        list.add( "stop" );
                        break;
                }
                break;
            case 3:
                if ( ( player != null ) && ( args[0].equals( "define" ) ) ) {
                    list.add( player.getName() );
                }
                if ( args[0].equals( "limit" ) ) {
                    list.add( "0" );
                    list.add( "1" );
                    list.add( "2" );
                }
                break;
        }
        return list;
    }
}
