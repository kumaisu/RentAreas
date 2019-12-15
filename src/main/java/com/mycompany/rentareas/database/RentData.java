package com.mycompany.rentareas.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import com.mycompany.rentareas.config.Config;
import com.mycompany.kumaisulibraries.Tools;

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
     * @param loc
     */
    public static void AddSQL( String region, Location loc ) {
        try ( Connection con = Database.dataSource.getConnection() ) {
            String sql = "INSERT INTO area (region, uuid, name, world, x, y, z, entry, logout, noexp ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
            Tools.Prt( "SQL : " + sql, Tools.consoleMode.max, Config.programCode );
            PreparedStatement preparedStatement = con.prepareStatement( sql );
            preparedStatement.setString( 1, region );
            preparedStatement.setString( 2, "" );
            preparedStatement.setString( 3, "" );
            preparedStatement.setString( 4, loc.getWorld().getName() );
            preparedStatement.setInt( 5, loc.getBlockX() );
            preparedStatement.setInt( 6, loc.getBlockY() );
            preparedStatement.setInt( 7, loc.getBlockZ() );
            preparedStatement.setString( 8, Database.sdf.format( new Date() ) );
            preparedStatement.setString( 9, Database.sdf.format( new Date() ) );
            preparedStatement.setInt( 10, 0 );
            preparedStatement.executeUpdate();
            con.close();
            Tools.Prt( "Add Data to SQL Success.", Tools.consoleMode.max, Config.programCode );
        } catch ( SQLException e ) {
            Tools.Prt( ChatColor.RED + "Error AddSQL : " + e.getMessage(), Config.programCode );
        }
    }

    public static void setDatabase( ResultSet rs ) throws SQLException {
        Database.region     = rs.getString( "region" );
        Database.uuidstr    = rs.getString( "uuid" );
        Database.name       = rs.getString( "name" );
        Database.world      = rs.getString( "world" );
        Database.entry      = rs.getTimestamp( "entry" );
        Database.logout     = rs.getTimestamp( "logout" );
        Database.Position   = new Location(
            Bukkit.getWorld( Database.world ),
            rs.getInt( "x" ),
            rs.getInt( "y" ),
            rs.getInt( "z" ) );
        Database.NoExp      = rs.getInt( "noexp" );
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
            Tools.Prt( "SQL : " + sql, Tools.consoleMode.max, Config.programCode );
            ResultSet rs = stmt.executeQuery( sql );
            if ( rs.next() ) {
                setDatabase( rs );
                Tools.Prt( "Get Data from SQL Success.", Tools.consoleMode.max, Config.programCode );
                retStat = true;
            }
            con.close();
            return retStat;
        } catch ( SQLException e ) {
            Tools.Prt( ChatColor.RED + "Error GetSQL : " + e.getMessage(), Config.programCode );
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
            Tools.Prt( "SQL : " + sql, Tools.consoleMode.max, Config.programCode );
            ResultSet rs = stmt.executeQuery( sql );
            if ( rs.next() ) {
                setDatabase( rs );
                Tools.Prt( "Get Data from SQL Success.", Tools.consoleMode.max, Config.programCode );
                retStat = true;
            }
            con.close();
            return retStat;
        } catch ( SQLException e ) {
            Tools.Prt( ChatColor.RED + "Error GetRegion : " + e.getMessage(), Config.programCode );
            return false;
        }
    }

    /**
     * 件数取得
     * mode -1  : 空き部屋
     * mode 0   : 全て
     * mode 1   : 入居済み
     *
     * @param mode
     * @return 
     */
    public static int GetCount( int mode ) {
        int count = 0;
        try ( Connection con = Database.dataSource.getConnection() ) {
            Statement stmt = con.createStatement();
            String sql = "SELECT count(*) FROM area ";
            if ( mode < 0 ) { sql += "WHERE name LIKE '';"; }
            if ( mode > 0 ) { sql += "WHERE name NOT LIKE '';"; }
            Tools.Prt( "SQL : " + sql, Tools.consoleMode.max, Config.programCode );
            ResultSet rs = stmt.executeQuery( sql );
            if ( rs.next() ) { count = rs.getInt( "count(*)" ); }
            con.close();
        } catch ( SQLException e ) {
            Tools.Prt( ChatColor.RED + "Error GetCount : " + e.getMessage(), Config.programCode );
        }
        return count;
    }

    /**
     * UUIDによる、借部屋数取得
     *
     * @param uuid
     * @return 
     */
    public static int RentCount( UUID uuid ) {
        int count = 0;
        try ( Connection con = Database.dataSource.getConnection() ) {
            Statement stmt = con.createStatement();
            String sql = "SELECT count(*) FROM area WHERE uuid = '" + uuid.toString() + "';";
            Tools.Prt( "SQL : " + sql, Tools.consoleMode.max, Config.programCode );
            ResultSet rs = stmt.executeQuery( sql );
            if ( rs.next() ) { count = rs.getInt( "count(*)" ); }
            con.close();
        } catch ( SQLException e ) {
            Tools.Prt( ChatColor.RED + "Error RentCount : " + e.getMessage(), Config.programCode );
        }
        return count;
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
            Tools.Prt( "SQL : " + sql, Tools.consoleMode.max, Config.programCode );
            PreparedStatement preparedStatement = con.prepareStatement( sql );
            preparedStatement.executeUpdate();
            Tools.Prt( "Delete Data from SQL Success.", Tools.consoleMode.max, Config.programCode );
            con.close();
            return true;
        } catch ( SQLException e ) {
            Tools.Prt( ChatColor.RED + "Error DelSQL : " + e.getMessage(), Config.programCode );
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
            Tools.Prt( player, ChatColor.GREEN + "=== Region Information ===", Config.programCode );
            Tools.Prt( player, ChatColor.YELLOW + "Region  : " + ChatColor.WHITE + Database.region, Config.programCode );
            Tools.Prt( player, ChatColor.YELLOW + "Player  : " + ChatColor.WHITE + 
                ( Database.name.equals( "" ) ? "For Rent" : Database.name + " [" + Database.uuidstr + "]" ),
                Config.programCode
            );
            Tools.Prt( player, ChatColor.YELLOW + "Entry   : " + ChatColor.WHITE + Database.sdf.format( Database.entry ), Config.programCode );
            Tools.Prt( player, ChatColor.YELLOW + "Logout  : " + ChatColor.WHITE + Database.sdf.format( Database.logout ), Config.programCode );
            Tools.Prt( player, ChatColor.YELLOW + "Location: " + ChatColor.WHITE +
                Database.Position.getBlockX() + "," +
                Database.Position.getBlockY() + "," +
                Database.Position.getBlockZ() + "[" +
                Database.world + "]",
                Config.programCode
            );
            Tools.Prt( player, ChatColor.YELLOW + "Expired : " + ChatColor.WHITE +
                ( Database.NoExp == 0 ? "Expired Check" : ( Database.NoExp == 1 ) ? "Ignore Expiration" : "No display list" ),
                Config.programCode
            );
            Tools.Prt( player, ChatColor.GREEN + "=== EoF ===", Config.programCode );
            return true;
        } else {
            Tools.Prt( ChatColor.RED + "Error GetInformation.", Config.programCode );
            return false;
        }
    }

    /**
     * プレイヤー情報を登録
     *
     * @param uuid
     * @param name
     * @param region 
     */
    public static void SetPlayerToSQL( UUID uuid, String name, String region ) {
        try ( Connection con = Database.dataSource.getConnection() ) {
            //  uuid
            String sql =
                "UPDATE area SET " + 
                ( ( uuid == null ) ? "uuid = ''" : "uuid = '" + uuid.toString() + "'" ) +
                " WHERE region = '" + region + "';";
            Tools.Prt( "SQL : " + sql, Tools.consoleMode.max, Config.programCode );
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.executeUpdate();

            //  name
            sql = "UPDATE area SET name = '" + name + "' WHERE region = '" + region + "';";
            Tools.Prt( "SQL : " + sql, Tools.consoleMode.max, Config.programCode );
            preparedStatement = con.prepareStatement(sql);
            preparedStatement.executeUpdate();

            con.close();
            Tools.Prt( "Set Player Date to SQL Success.", Tools.consoleMode.max, Config.programCode );
        } catch ( SQLException e ) {
            Tools.Prt( ChatColor.RED + "Error SetPlayerToSQL : " + e.getMessage(), Config.programCode );
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
            Tools.Prt( "SQL : " + sql, Tools.consoleMode.max, Config.programCode );
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.executeUpdate();
            con.close();
            Tools.Prt( "Set logout Date to SQL Success.", Tools.consoleMode.max, Config.programCode );
        } catch ( SQLException e ) {
            Tools.Prt( ChatColor.RED + "Error SetLogoutToSQL : " + e.getMessage(), Config.programCode );
        }
    }

    /**
     * Region から部屋の期限切れ情報を延長する
     * 主に長期ログインが無く、且つ、部屋を保持したい場合に使用
     *
     * @param region
     */
    public static void SetExtension( String region ) {
        try ( Connection con = Database.dataSource.getConnection() ) {
            String sql = "UPDATE area SET logout = '" + Database.sdf.format( new Date() ) + "' WHERE region = '" + region + "';";
            Tools.Prt( "SQL : " + sql, Tools.consoleMode.max, Config.programCode );
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.executeUpdate();
            con.close();
            Tools.Prt( "Set Extension Date to SQL Success.", Tools.consoleMode.max, Config.programCode );
        } catch ( SQLException e ) {
            Tools.Prt( ChatColor.RED + "Error SetExtension : " + e.getMessage(), Config.programCode );
        }
    }

    /**
     * 指定Regionの期限切れ管理フラグの設定
     * 0 : 管理する
     * 1 : 期限切れ管理しない
     * 2 : リスト表示もしない
     *
     * @param region 
     * @param mode 
     */
    public static void SetLimit( String region, int mode ) {
        try ( Connection con = Database.dataSource.getConnection() ) {
            String sql = "UPDATE area SET noexp = '" + mode + "' WHERE region = '" + region + "';";
            Tools.Prt( "SQL : " + sql, Tools.consoleMode.max, Config.programCode );
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.executeUpdate();
            con.close();
            Tools.Prt( "Set Limit Data to SQL Success.", Tools.consoleMode.max, Config.programCode );
        } catch ( SQLException e ) {
            Tools.Prt( ChatColor.RED + "Error SetLimit : " + e.getMessage(), Config.programCode );
        }
    }
}
