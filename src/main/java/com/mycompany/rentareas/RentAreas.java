/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.rentareas;

import java.util.HashMap;
import org.bukkit.plugin.java.JavaPlugin;
import com.mycompany.rentareas.config.ConfigManager;
import com.mycompany.rentareas.database.MySQLControl;
import com.mycompany.rentareas.command.RentCommand;
import com.mycompany.rentareas.command.FreeRentCommand;
import com.mycompany.rentareas.control.InvMenu;
import com.mycompany.rentareas.listener.BreakListener;
import com.mycompany.rentareas.listener.ClickListener;
import com.mycompany.rentareas.listener.PlaceListener;
import com.mycompany.rentareas.listener.InventoryListener;
import com.mycompany.rentareas.listener.PlayerListener;
import com.mycompany.rentareas.TabComplete.RentTabComp;
import com.mycompany.rentareas.TabComplete.FreeTabComp;

/**
 *
 * @author sugichan
 */
public class RentAreas extends JavaPlugin {

    @Override
    public void onEnable() {
        new ConfigManager( this );
        MySQLControl.connect();
        MySQLControl.TableUpdate();
        new PlaceListener( this );
        new BreakListener( this );
        new ClickListener( this );
        new InventoryListener( this );
        new PlayerListener( this );
        getCommand( "rent" ).setExecutor( new RentCommand( this ) );
        getCommand( "freerent" ).setExecutor( new FreeRentCommand( this ) );
        getCommand( "rent" ).setTabCompleter( new RentTabComp() );
        getCommand( "freerent" ).setTabCompleter( new FreeTabComp() );
        InvMenu.inv = new HashMap<>();
        InvMenu.reg = new HashMap<>();
    }

    @Override
    public void onDisable() {
        super.onDisable(); //To change body of generated methods, choose Tools | Templates.
        //  MySQLControl.disconnect();
    }

    @Override
    public void onLoad() {
        super.onLoad(); //To change body of generated methods, choose Tools | Templates.
    }
    
}
