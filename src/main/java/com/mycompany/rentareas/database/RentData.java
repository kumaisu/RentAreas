package com.mycompany.rentareas.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import com.mycompany.kumaisulibraries.Tools;
import static com.mycompany.rentareas.config.Config.programCode;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author sugichan
 */
public class RentData {
    /**
     * プレイヤー情報を新規追加する
     *
     * @param player
     * @param region
     */
    public static void AddSQL( Player player, String region ) {
        try ( Connection con = Database.dataSource.getConnection() ) {
            String sql = "INSERT INTO area (region, uuid, name, create, logout) VALUES (?, ?, ?, ?, ?);";
            Tools.Prt( "SQL : " + sql, Tools.consoleMode.max, programCode );
            PreparedStatement preparedStatement = con.prepareStatement( sql );
            preparedStatement.setString( 1, region );
            preparedStatement.setString( 2, player.getUniqueId().toString() );
            preparedStatement.setString( 3, player.getName() );
            preparedStatement.setString( 4, Database.sdf.format( new Date() ) );
            preparedStatement.setString( 5, Database.sdf.format( new Date() ) );
            preparedStatement.executeUpdate();
            con.close();

            Database.region = region;
            Database.uuidstr = player.getUniqueId().toString();
            Database.name = player.getName();
            Database.create = new Date();
            Database.logout = new Date();

            Tools.Prt( "Add Data to SQL Success.", Tools.consoleMode.max, programCode );
        } catch ( SQLException e ) {
            Tools.Prt( ChatColor.RED + "Error AddToSQL" + e.getMessage(), programCode );
        }
    }

    /**
     * プレイヤー情報を削除する
     *
     * @param region
     * @return
     */
    public static boolean DelSQL( String region ) {
        try ( Connection con = Database.dataSource.getConnection() ) {
            String sql = "DELETE FROM area WHERE region = '" + region + "';";
            Tools.Prt( "SQL : " + sql, Tools.consoleMode.max, programCode );
            PreparedStatement preparedStatement = con.prepareStatement( sql );
            preparedStatement.executeUpdate();
            Tools.Prt( "Delete Data from SQL Success.", Tools.consoleMode.max, programCode );
            con.close();
            return true;
        } catch ( SQLException e ) {
            Tools.Prt( ChatColor.RED + "Error DelFromSQL" + e.getMessage(), programCode );
            return false;
        }
    }

    /**
     * UUIDからプレイヤー情報を取得する
     *
     * @param uuid
     * @param region
     * @return
     */
    public static boolean GetSQL( UUID uuid, String region ) {
        try ( Connection con = Database.dataSource.getConnection() ) {
            boolean retStat = false;
            Statement stmt = con.createStatement();
            String sql = "SELECT * FROM player WHERE uuid = '" + uuid.toString() + "'";
            if ( !"".equals( region ) ) { sql += " AND region = '" + region + "'"; }
            sql += ";";
            Tools.Prt( "SQL : " + sql, Tools.consoleMode.max, programCode );
            ResultSet rs = stmt.executeQuery( sql );
            if ( rs.next() ) {
                Database.region     = rs.getString( "region" );
                Database.uuidstr    = rs.getString( "uuid" );
                Database.name       = rs.getString( "name" );
                Database.create     = rs.getTimestamp( "create" );
                Database.logout     = rs.getTimestamp( "logout" );
                Tools.Prt( "Get Data from SQL Success.", Tools.consoleMode.max, programCode );
                retStat = true;
            }
            con.close();
            return retStat;
        } catch ( SQLException e ) {
            Tools.Prt( ChatColor.RED + "Error GetPlayer" + e.getMessage(), programCode );
            return false;
        }
    }

    /**
     * UUIDからプレイヤーのログアウト日時を更新する
     *
     * @param uuid
     */
    public static void SetLogoutToSQL( UUID uuid ) {
        try ( Connection con = Database.dataSource.getConnection() ) {
            String sql = "UPDATE player SET logout = '" + Database.sdf.format( new Date() ) + "' WHERE uuid = '" + uuid.toString() + "';";
            Tools.Prt( "SQL : " + sql, Tools.consoleMode.max, programCode );
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.executeUpdate();
            con.close();
            Tools.Prt( "Set logout Date to SQL Success.", Tools.consoleMode.max, programCode );
        } catch ( SQLException e ) {
            Tools.Prt( ChatColor.RED + "Error ChangeStatus" + e.getMessage(), programCode );
        }
    }

    /**
     * 投獄者のリストアップする
     *
     * @return 
     */
    public static Map< UUID, Integer > ListJailMenber() {
        Map< UUID, Integer > getList = new HashMap<>();
        try ( Connection con = Database.dataSource.getConnection() ) {
            Statement stmt = con.createStatement();
            String sql = "SELECT * FROM player WHERE reason > 0;";
            Tools.Prt( "SQL : " + sql, Tools.consoleMode.max, programCode );
            ResultSet rs = stmt.executeQuery( sql );
            while( rs.next() ) {
                getList.put( UUID.fromString( rs.getString( "uuid" ) ), rs.getInt( "reason" ) );
            }
            con.close();
            Tools.Prt( "Listed Jail Member Success", Tools.consoleMode.max , programCode );
            return getList;
        } catch ( SQLException e ) {
            Tools.Prt( ChatColor.RED + "Error GetPlayer" + e.getMessage(), programCode );
            return null;
        }
    }
}
