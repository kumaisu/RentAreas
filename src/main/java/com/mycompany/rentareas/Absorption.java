/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.rentareas;

import org.bukkit.Material;
import org.bukkit.DyeColor;
import org.bukkit.material.Wool;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import com.mycompany.rentareas.config.Config;

/**
 *
 * @author sugichan
 */
public class Absorption {
    
    public static ItemStack OK() {
        ItemStack Like;
        try {
            Like = new ItemStack( Material.valueOf( "BLUE_WOOL" ), 1 );
        } catch( Exception e ) {
            Like = new Wool( DyeColor.BLUE ).toItemStack( 1 );
        }
        ItemMeta lm = Like.getItemMeta();
        lm.setDisplayName( Config.RentIn );
        Like.setItemMeta( lm );
        return Like;
    }

    public static ItemStack NG() {
        ItemStack Unlike;
        try {
            Unlike = new ItemStack( Material.valueOf( "RED_WOOL" ), 1 );
        } catch( Exception e ) {
            Unlike = new Wool( DyeColor.RED ).toItemStack( 1 );
        }
        ItemMeta um = Unlike.getItemMeta();
        um.setDisplayName( Config.RentOut );
        Unlike.setItemMeta( um );
        return Unlike;
    }
}
