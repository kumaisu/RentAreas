/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.rentareas.control;

import java.util.Map;
import java.util.UUID;
import java.util.Arrays;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import com.mycompany.rentareas.Absorption;
import com.mycompany.rentareas.config.Config;

/**
 *
 * @author sugichan
 */
public class InvMenu {

    public static Map< UUID, Inventory > inv;
    public static Map< UUID, Location > loc;
    public static Map< UUID, String > reg;

    /**
     * 貸借に関するインベントリメニュー表示 
     *
     * @param player
    */
    public static void printMenu( Player player ) {
        if ( player == null ) return;
        int slot = 27;

        Inventory TempInv;
        TempInv = Bukkit.createInventory( null, slot, Config.MenuString );

        //  Remove Icon 作成
        ItemStack UpIcon = new ItemStack( Material.END_CRYSTAL, 1 );
        ItemMeta UpMeta = Bukkit.getItemFactory().getItemMeta( Material.END_CRYSTAL );
        UpMeta.setDisplayName( ChatColor.WHITE + "Update" );
        List<String> UpLore = Arrays.asList( ChatColor.AQUA + "Update Sign", ChatColor.AQUA + "Rewrite Infomation" );
        UpMeta.setLore( UpLore );
        UpIcon.setItemMeta( UpMeta );
        TempInv.setItem( 16, UpIcon );

        //  借用
        TempInv.setItem( 11, Absorption.OK() );

        //  退去
        TempInv.setItem( 13, Absorption.NG() );

        inv.put( player.getUniqueId(), TempInv );
        player.openInventory( TempInv );
    }
}
