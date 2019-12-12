/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.rentareas.control;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Arrays;
import java.text.SimpleDateFormat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import com.mycompany.rentareas.Absorption;
import com.mycompany.rentareas.config.Config;
import com.mycompany.rentareas.database.Database;
import com.mycompany.rentareas.database.RentData;
import com.mycompany.kumaisulibraries.Utility;

/**
 *
 * @author sugichan
 */
public class InvMenu {

    public static Map< UUID, Inventory > inv;
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
        TempInv = Bukkit.createInventory( null, slot, Config.MenuString + "[" + reg.get( player.getUniqueId() ) + "]" );

        if ( player.isOp() ) {
            //  Remove Icon 作成
            ItemStack DelIcon = new ItemStack( Material.BARRIER, 1 );
            ItemMeta DelMeta = Bukkit.getItemFactory().getItemMeta( Material.BARRIER );
            DelMeta.setDisplayName( ChatColor.WHITE + "Remove" );
            DelMeta.setLore( Arrays.asList( ChatColor.RED + "Remove", ChatColor.RED + "Rental", ChatColor.RED + "Sign Post" ) );
            DelIcon.setItemMeta( DelMeta );
            TempInv.setItem( 26, DelIcon );

            //  Update Sign Icon 作成
            ItemStack UpIcon = new ItemStack( Material.END_CRYSTAL, 1 );
            ItemMeta UpMeta = Bukkit.getItemFactory().getItemMeta( Material.END_CRYSTAL );
            UpMeta.setDisplayName( ChatColor.WHITE + "Update" );
            UpMeta.setLore( Arrays.asList( ChatColor.AQUA + "Update Sign", ChatColor.AQUA + "Rewrite Infomation" ) );
            UpIcon.setItemMeta( UpMeta );
            TempInv.setItem( 25, UpIcon );
        }

        RentData.GetRegion( reg.get( player.getUniqueId() ) );
        if ( Database.name.equals( "" ) ) {
            //  借用
            TempInv.setItem( 11, Absorption.OK() );
        } else {
            int progress = Utility.dateDiff( Database.logout, new Date() );
            SimpleDateFormat ddf = new SimpleDateFormat( "yyyy/MM/dd" );
            SimpleDateFormat tdf = new SimpleDateFormat( "HH:mm:ss" );
            List< String > Lore = Arrays.asList( ddf.format( Database.logout ),tdf.format( Database.logout ), String.format( "%3d days", progress ) );
            TempInv.setItem( 11, Absorption.getPlayerHead( Database.name, Lore ) );
        }

        //  退去
        TempInv.setItem( 15, Absorption.NG() );

        inv.put( player.getUniqueId(), TempInv );
        player.openInventory( TempInv );
    }
}
