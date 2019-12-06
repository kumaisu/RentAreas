/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.rentareas.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import com.mycompany.kumaisulibraries.Tools;
import com.mycompany.kumaisulibraries.Utility;
import com.mycompany.rentareas.config.Config;

/**
 *
 * @author sugichan
 */
public class DataList {
    static String sepalateStr = "================================================";

    public static String PrtLine( ResultSet rs ) throws SQLException {
        return
            ChatColor.WHITE + rs.getString( "region" ) + " : " +
            ChatColor.AQUA + ( rs.getString( "name" ).equals( "" ) ?
                "For Rent" + ChatColor.GREEN + " [" + Database.sdf.format( rs.getTimestamp( "entry" ) ) + "]" :
                rs.getString( "name" ) + ChatColor.YELLOW + " [" + Database.sdf.format( rs.getTimestamp( "logout" ) ) + "] " ) +
            ChatColor.WHITE + 
                String.format( "%d", rs.getInt( "x" ) ) + "," +
                String.format( "%d", rs.getInt( "y" ) ) + "," +
                String.format( "%d", rs.getInt( "z" ) ) + "{" +
                rs.getString( "world" ) + "}";
    }

    public static void List( Player player ) {
        try ( Connection con = Database.dataSource.getConnection() ) {
            Statement stmt = con.createStatement();
            String sql = "SELECT * FROM area ORDER BY region ASC;";
            Tools.Prt( "SQL : " + sql, Tools.consoleMode.max, Config.programCode );
            ResultSet rs = stmt.executeQuery( sql );

            Tools.Prt( player, "借用者一覧リスト（保護名、Player、更新日、場所）", Config.programCode );
            Tools.Prt( player, sepalateStr, Config.programCode );

            while( rs.next() ) {
                Tools.Prt( player, PrtLine( rs ), Config.programCode );
            }

            Tools.Prt( player, sepalateStr, Config.programCode );

            con.close();
        } catch ( SQLException e ) {
            Tools.Prt( ChatColor.RED + "Error ListRegions : " + e.getMessage(), Config.programCode );
        }
    }

    public static void Expired( Player player ) {
        List< String > StringData = new ArrayList<>();

        try ( Connection con = Database.dataSource.getConnection() ) {
            Statement stmt = con.createStatement();
            String sql = "SELECT * FROM area ORDER BY region ASC;";
            Tools.Prt( "SQL : " + sql, Tools.consoleMode.max, Config.programCode );
            ResultSet rs = stmt.executeQuery( sql );

            int loopCount = 0;
            while( rs.next() ) {
                Date entryDate = rs.getTimestamp( "logout" );
                int progress = Utility.dateDiff( entryDate, new Date() );
                Tools.Prt( "Logout date : " + entryDate.toString() + 
                    " Diff [" + progress + "日] " +
                    "Expired [" + Config.Expired + "日]",
                    Tools.consoleMode.max, Config.programCode
                );
                if ( ( Config.Expired > 0 ) && ( progress > Config.Expired ) && ( !rs.getString( "name" ).equals( "" ) ) ) {
                    StringData.add( PrtLine( rs ) );
                    loopCount++;
                }
            }
            con.close();
        } catch ( SQLException e ) {
            Tools.Prt( ChatColor.RED + "Error Expired : " + e.getMessage(), Config.programCode );
        }
        
        if ( StringData.size() > 0 ) {
            Tools.Prt( player, "期限切れ一覧（保護名、Player、借用日、場所）", Config.programCode );
            Tools.Prt( player, sepalateStr, Config.programCode );
            StringData.forEach( ( s ) -> { Tools.Prt( player, s, Config.programCode ); } );
            Tools.Prt( player, sepalateStr, Config.programCode );
        }
    }

}
