/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.rentareas;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.DyeColor;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Wool;

/**
 *
 * @author sugichan
 */
public class Absorption {
    
    public static ItemStack Like() {
        //  イイネ解除
        ItemStack Like;
        try {
            Like = new ItemStack( Material.valueOf( "BLUE_WOOL" ), 1 );
        } catch( Exception e ) {
            Like = new Wool( DyeColor.BLUE ).toItemStack( 1 );
        }
        ItemMeta lm = Like.getItemMeta();
        lm.setDisplayName( "借りる" );
        Like.setItemMeta( lm );
        return Like;
    }

    public static ItemStack Unlike() {
        //  イイネ解除
        ItemStack Unlike;
        try {
            Unlike = new ItemStack( Material.valueOf( "RED_WOOL" ), 1 );
        } catch( Exception e ) {
            Unlike = new Wool( DyeColor.RED ).toItemStack( 1 );
        }
        ItemMeta um = Unlike.getItemMeta();
        um.setDisplayName( "退去する" );
        Unlike.setItemMeta( um );
        return Unlike;
    }
}
