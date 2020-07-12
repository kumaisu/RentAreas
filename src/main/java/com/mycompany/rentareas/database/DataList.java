/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.rentareas.database;

import java.util.UUID;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

    public static String PrtLine( ResultSet rs, boolean mode ) throws SQLException {
        Date entryDate = rs.getTimestamp( "logout" );
        int progress = Utility.dateDiff( entryDate, new Date() );
        return
            ChatColor.WHITE + rs.getString( "region" ) + " : " +
            ( mode ? 
                ChatColor.WHITE + 
                    String.format( "%5d", rs.getInt( "x" ) ) + "," +
                    String.format( "%5d", rs.getInt( "y" ) ) + "," +
                    String.format( "%5d", rs.getInt( "z" ) ) + " { " +
                    rs.getString( "world" ) + " }"
                :
                ChatColor.GOLD + String.format( "%3d", progress ) + "日" +
                ( rs.getString( "name" ).equals( "" ) ?
                    ChatColor.GREEN + " [" + Database.sdf.format( rs.getTimestamp( "entry" ) ) + "] " + ChatColor.LIGHT_PURPLE + "For Rent"
                :
                    ChatColor.YELLOW + " [" + Database.sdf.format( rs.getTimestamp( "logout" ) ) + "] " + ChatColor.AQUA + rs.getString( "name" )
                )
            );
    }

    /**
     * 登録データ一覧表示
     * mode -1  : 空き部屋
     * mode 0   : 全て
     * mode 1   : 入居済み
     *
     * @param player
     * @param mode
     * @param page
     */
    public static void List( Player player, int mode, int page ) {
        int ClientLine = 8;
        int PrtMode = 3;
        int lines = ( page == 0 ? 1 : ( ( page - 1 ) * ClientLine ) + 1 );

        try ( Connection con = Database.dataSource.getConnection() ) {
            Statement stmt = con.createStatement();
            String sql = "SELECT * FROM area ";
            String title = "全部屋リスト（保護名,日数,更新日,Player）";
            if ( mode < 0 ) {
                title = "空き部屋リスト（保護名,場所[x,y,z,world]）";
                sql += "WHERE name LIKE ''";
                PrtMode = 2;
            }
            if ( mode > 0 ) {
                title = "入居者リスト（保護名,日数,更新日,Player）";
                sql += "WHERE name NOT LIKE ''";
                PrtMode = 2;
            }
            sql += " ORDER BY region ASC;";
            Tools.Prt( "SQL : " + sql, Tools.consoleMode.max, Config.programCode );
            ResultSet rs = stmt.executeQuery( sql );

            Tools.Prt( player, title, Config.programCode );

            int pi = 0;
            int di = 0;

            while( rs.next() && ( pi < ClientLine ) ) {
                di++;
                if ( ( rs.getInt( "noexp" ) < PrtMode ) && ( ( di >= lines ) || ( page == 0 ) ) ) { 
                    Tools.Prt( player, PrtLine( rs, ( mode < 0 ) ), Config.programCode );
                    if ( page > 0 ) pi++;
                }
            }
            con.close();
        } catch ( SQLException e ) {
            Tools.Prt( ChatColor.RED + "Error ListRegions : " + e.getMessage(), Config.programCode );
        }

        int freeAll     = RentData.GetCount( mode );
        int pageData    = ( freeAll / ClientLine ) + ( ( ( freeAll % ClientLine ) == 0 ? 0 : 1 ) );
        Tools.Prt( player,
            "Rental " +
                ( page == 0 ? "" : "Data Pages (" + page + "/" + pageData + ") " ) +
                "Get " + freeAll + " Data's",
            Config.programCode
        );
        /*
        Tools.Prt( "全て     : " + RentData.GetCount( 0 ), Config.programCode );
        Tools.Prt( "空き部屋 : " + RentData.GetCount( -1 ), Config.programCode );
        Tools.Prt( "入居済み : " + RentData.GetCount( 1 ), Config.programCode );
        */
    }

    public static void Expired( Player player ) {
        List< String > StringData = new ArrayList<>();

        try ( Connection con = Database.dataSource.getConnection() ) {
            Statement stmt = con.createStatement();
            String sql = "SELECT * FROM area ORDER BY region ASC;";
            Tools.Prt( "SQL : " + sql, Tools.consoleMode.max, Config.programCode );
            ResultSet rs = stmt.executeQuery( sql );

            while( rs.next() ) {
                Date entryDate = rs.getTimestamp( "logout" );
                int progress = Utility.dateDiff( entryDate, new Date() );
                Tools.Prt( "Logout date : " + entryDate.toString() + 
                    " Diff [" + progress + "日] " +
                    "Expired [" + Config.Expired + "日]",
                    Tools.consoleMode.max, Config.programCode
                );
                if (
                    ( rs.getInt( "noexp" ) == 0 ) &&
                    ( Config.Expired > 0 ) &&
                    ( progress > Config.Expired ) &&
                    ( !rs.getString( "name" ).equals( "" ) ) 
                ) {
                    StringData.add( PrtLine( rs, false ) );
                }
            }
            con.close();
        } catch ( SQLException e ) {
            Tools.Prt( ChatColor.RED + "Error Expired : " + e.getMessage(), Config.programCode );
        }
        
        if ( StringData.size() > 0 ) {
            Tools.Prt( player, "期限切れ一覧（保護名,日数,Player,更新日,場所）", Config.programCode );
            StringData.forEach( ( s ) -> { Tools.Prt( player, s, Config.programCode ); } );
        }
    }

    /**
     * プレイヤーの持ち物件を一覧表示する
     *
     * @param player
     * @param uuid
     * @param name
     */
    public static void ListPlayer( Player player, UUID uuid, String name ) {
        if ( uuid == null ) { return; }
        try ( Connection con = Database.dataSource.getConnection() ) {
            Statement stmt = con.createStatement();
            String sql = "SELECT * FROM area WHERE uuid = '" + uuid.toString() + "' ORDER BY region ASC;";
            String title = name + " さんの部屋リスト（部屋番号,場所[x,y,z,world]）";
            Tools.Prt( "SQL : " + sql, Tools.consoleMode.max, Config.programCode );
            ResultSet rs = stmt.executeQuery( sql );
            Tools.Prt( player, title, Config.programCode );

            while( rs.next() ) {
                Tools.Prt( player, PrtLine( rs, true ), Config.programCode );
            }
            con.close();
        } catch ( SQLException e ) {
            Tools.Prt( ChatColor.RED + "Error ListPlayer : " + e.getMessage(), Config.programCode );
        }
    }
}
