package com.mycompany.rentareas.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
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
     * @param region
     */
    public static void AddSQL( String region ) {
        try ( Connection con = Database.dataSource.getConnection() ) {
            String sql = "INSERT INTO area (region, uuid, name, entry, logout) VALUES (?, ?, ?, ?, ?);";
            Tools.Prt( "SQL : " + sql, Tools.consoleMode.max, programCode );
            PreparedStatement preparedStatement = con.prepareStatement( sql );
            preparedStatement.setString( 1, region );
            preparedStatement.setString( 2, "" );
            preparedStatement.setString( 3, "" );
            preparedStatement.setString( 4, Database.sdf.format( new Date() ) );
            preparedStatement.setString( 5, Database.sdf.format( new Date() ) );
            preparedStatement.executeUpdate();
            con.close();

            Database.region     = region;
            Database.uuidstr    = "";
            Database.name       = "ForRent";
            Database.entry      = new Date();
            Database.logout     = new Date();

            Tools.Prt( "Add Data to SQL Success.", Tools.consoleMode.max, programCode );
        } catch ( SQLException e ) {
            Tools.Prt( ChatColor.RED + "Error AddSQL : " + e.getMessage(), programCode );
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
            Tools.Prt( ChatColor.RED + "Error DelSQL : " + e.getMessage(), programCode );
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
            String sql = "SELECT * FROM area WHERE uuid = '" + uuid.toString() + "'";
            if ( !"".equals( region ) ) { sql += " AND region = '" + region + "'"; }
            sql += ";";
            Tools.Prt( "SQL : " + sql, Tools.consoleMode.max, programCode );
            ResultSet rs = stmt.executeQuery( sql );
            if ( rs.next() ) {
                Database.region     = rs.getString( "region" );
                Database.uuidstr    = rs.getString( "uuid" );
                Database.name       = rs.getString( "name" );
                Database.entry      = rs.getTimestamp( "entry" );
                Database.logout     = rs.getTimestamp( "logout" );
                Tools.Prt( "Get Data from SQL Success.", Tools.consoleMode.max, programCode );
                retStat = true;
            }
            con.close();
            return retStat;
        } catch ( SQLException e ) {
            Tools.Prt( ChatColor.RED + "Error GetSQL : " + e.getMessage(), programCode );
            return false;
        }
    }

    /**
     * regionがDB登録されているか？
     *
     * @param region
     * @return 
     */
    public static boolean GetRegion( String region ) {
        try ( Connection con = Database.dataSource.getConnection() ) {
            boolean retStat = false;
            Statement stmt = con.createStatement();
            String sql = "SELECT * FROM area WHERE region = '" + region + "';";
            Tools.Prt( "SQL : " + sql, Tools.consoleMode.max, programCode );
            ResultSet rs = stmt.executeQuery( sql );
            if ( rs.next() ) {
                Database.region     = rs.getString( "region" );
                Database.uuidstr    = rs.getString( "uuid" );
                Database.name       = rs.getString( "name" );
                Database.entry      = rs.getTimestamp( "entry" );
                Database.logout     = rs.getTimestamp( "logout" );
                Tools.Prt( "Get Data from SQL Success.", Tools.consoleMode.max, programCode );
                retStat = true;
            }
            con.close();
            return retStat;
        } catch ( SQLException e ) {
            Tools.Prt( ChatColor.RED + "Error GetRegion : " + e.getMessage(), programCode );
            return false;
        }
    }

    /**
     * 登録Region情報
     * 
     * @param player
     * @param region 
     * @return  
     */
    public static boolean RegionInfo( Player player, String region ) {
        if ( GetRegion( region ) ) {
            Tools.Prt( player, ChatColor.GREEN + "=== Region Information ===", programCode );
            Tools.Prt( player, ChatColor.YELLOW + "Region : " + ChatColor.WHITE + Database.region, programCode );
            Tools.Prt( player, ChatColor.YELLOW + "Player : " + ChatColor.WHITE + 
                ( Database.name.equals( "" ) ? "For Rent" : Database.name + " [" + Database.uuidstr + "]" ),
                programCode
            );
            Tools.Prt( player, ChatColor.YELLOW + "Entry  : " + ChatColor.WHITE + Database.sdf.format( Database.entry ), programCode );
            Tools.Prt( player, ChatColor.YELLOW + "Logout : " + ChatColor.WHITE + Database.sdf.format( Database.logout ), programCode );
            Tools.Prt( player, ChatColor.GREEN + "=== EoF ===", programCode );
            return true;
        } else {
            Tools.Prt( ChatColor.RED + "Error GetInformation.", programCode );
            return false;
        }
    }

    /**
     * プレイヤー情報を登録
     *
     * @param player
     * @param region 
     */
    public static void SetPlayerToSQL( Player player, String region ) {
        try ( Connection con = Database.dataSource.getConnection() ) {
            //  uuid
            String sql =
                "UPDATE area SET " + 
                ( ( player == null ) ? "uuid = ''" : "uuid = '" + player.getUniqueId().toString() + "'" ) +
                " WHERE region = '" + region + "';";
            Tools.Prt( "SQL : " + sql, Tools.consoleMode.max, programCode );
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.executeUpdate();

            //  name
            sql = "UPDATE area SET " +
                ( ( player == null ) ? "name = ''" : "name = '" + player.getName() + "'" ) +
                " WHERE region = '" + region + "';";
            Tools.Prt( "SQL : " + sql, Tools.consoleMode.max, programCode );
            preparedStatement = con.prepareStatement(sql);
            preparedStatement.executeUpdate();

            con.close();
            Tools.Prt( "Set Player Date to SQL Success.", Tools.consoleMode.max, programCode );
        } catch ( SQLException e ) {
            Tools.Prt( ChatColor.RED + "Error SetPlayerToSQL : " + e.getMessage(), programCode );
        }
    }

    /**
     * UUIDからプレイヤーのログアウト日時を更新する
     *
     * @param uuid
     */
    public static void SetLogoutToSQL( UUID uuid ) {
        try ( Connection con = Database.dataSource.getConnection() ) {
            String sql = "UPDATE area SET logout = '" + Database.sdf.format( new Date() ) + "' WHERE uuid = '" + uuid.toString() + "';";
            Tools.Prt( "SQL : " + sql, Tools.consoleMode.max, programCode );
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.executeUpdate();
            con.close();
            Tools.Prt( "Set logout Date to SQL Success.", Tools.consoleMode.max, programCode );
        } catch ( SQLException e ) {
            Tools.Prt( ChatColor.RED + "Error ChangeStatus : " + e.getMessage(), programCode );
        }
    }

    public static void ListRegions( Player player ) {
        try ( Connection con = Database.dataSource.getConnection() ) {
            Statement stmt = con.createStatement();
            String sql = "SELECT * FROM area ORDER BY region ASC;";
            Tools.Prt( "SQL : " + sql, Tools.consoleMode.max, programCode );
            ResultSet rs = stmt.executeQuery( sql );

            Tools.Prt( player, "借用者一覧リスト（保護名、Player、借用日、Logout日）", programCode );
            Tools.Prt( player, "====================================================", programCode );

            while( rs.next() ) {
                Tools.Prt( player,
                    ChatColor.WHITE + rs.getString( "region" ) + " : " +
                    ChatColor.AQUA + ( rs.getString( "name" ).equals( "" ) ?
                        "For Rent" + ChatColor.GREEN + " [" + Database.sdf.format( rs.getTimestamp( "entry" ) ) + "]" :
                        rs.getString( "name" ) + ChatColor.YELLOW + " [" + Database.sdf.format( rs.getTimestamp( "logout" ) ) + "]" ),
                    programCode
                );
            }

            Tools.Prt( player, "====================================================", programCode );

            con.close();
        } catch ( SQLException e ) {
            Tools.Prt( ChatColor.RED + "Error ListRegions : " + e.getMessage(), programCode );
        }
    }
}
