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

/**
 *
 * @author sugichan
 */
public class InvMenu {
    public static Map< UUID, Inventory > inv;
    public static Map< UUID, Location > loc;

    /**
     * 貸借に関するインベントリメニュー表示 
     *
     * @param player
    */
    public static void printMenu( Player player ) {
        int slot = 54;

        Inventory TempInv;
        TempInv = Bukkit.createInventory( null, slot, "Rental Menu" );

            //  Remove Icon 作成
            ItemStack DelIcon = new ItemStack( Material.BARRIER, 1 );
            ItemMeta DelMeta = Bukkit.getItemFactory().getItemMeta( Material.BARRIER );
            DelMeta.setDisplayName( ChatColor.WHITE + "Remove" );
            List<String> DelLore = Arrays.asList( ChatColor.RED + "いいね看板を", ChatColor.RED + "削除します" );
            DelMeta.setLore( DelLore );
            DelIcon.setItemMeta( DelMeta );
            TempInv.setItem( slot - 1, DelIcon );

            //  Remove Icon 作成
            ItemStack UpIcon = new ItemStack( Material.END_CRYSTAL, 1 );
            ItemMeta UpMeta = Bukkit.getItemFactory().getItemMeta( Material.END_CRYSTAL );
            UpMeta.setDisplayName( ChatColor.WHITE + "Update" );
            List<String> UpLore = Arrays.asList( ChatColor.AQUA + "Update Sign", ChatColor.AQUA + "Rewrite Infomation" );
            UpMeta.setLore( UpLore );
            UpIcon.setItemMeta( UpMeta );
            TempInv.setItem( slot - 2, UpIcon );

        //  イイネ解除
        TempInv.setItem( slot - 8, Absorption.Unlike() );

        //  イイネ解除
        TempInv.setItem( slot - 9, Absorption.Like() );

        player.openInventory( TempInv );
    }
}
