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
public class FreeTabComp implements TabCompleter {

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
        if ( sender instanceof Player ) {
            Player player = ( Player ) sender;
            if ( args.length == 1 ) {
                list.add( "myroom" );
                list.add( "num page" );
            }
        }
        return list;
    }
}
