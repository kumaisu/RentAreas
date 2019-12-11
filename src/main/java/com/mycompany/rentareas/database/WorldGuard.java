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
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import org.bukkit.ChatColor;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import com.mycompany.kumaisulibraries.Tools;
import com.mycompany.rentareas.config.Config;
import static com.mycompany.rentareas.config.Config.programCode;

/**
 *
 * @author sugichan
 */
public class WorldGuard {
    private static HikariDataSource WGdataSource = null;

    /**
     * Database Open(接続) 処理
     */
    public static void connect() {
        if ( WGdataSource != null ) {
            if ( WGdataSource.isClosed() ) {
                Tools.Prt( ChatColor.RED + "WG database closed.", Tools.consoleMode.max, programCode );
                disconnect();
            } else {
                Tools.Prt( ChatColor.AQUA + "WG dataSource is not null", Tools.consoleMode.max, programCode );
                return;
            }
        } else {
            Tools.Prt( ChatColor.YELLOW + "WG database First Opened", Tools.consoleMode.max, programCode );
        }

        // HikariCPの初期化
        HikariConfig config = new HikariConfig();

        config.setJdbcUrl( "jdbc:mysql://" + Config.host + ":" + Config.port + "/" + Config.worldguard );
        config.setPoolName( Config.worldguard );
        config.setAutoCommit( true );
        config.setConnectionInitSql( "SET SESSION query_cache_type=0" );
        config.setMaximumPoolSize( 2 );
        config.setMinimumIdle( 2 );
        config.setMaxLifetime( TimeUnit.MINUTES.toMillis( 15 ) );
        //  config.setConnectionTimeout(0);
        //  config.setIdleTimeout(0);
        config.setUsername( Config.username );
        config.setPassword( Config.password );

        Properties properties = new Properties();
        properties.put( "useSSL", "false" );
        properties.put( "autoReconnect", "true" );
        properties.put( "maintainTimeStats", "false" );
        properties.put( "elideSetAutoCommits", "true" );
        properties.put( "useLocalSessionState", "true" );
        properties.put( "alwaysSendSetIsolation", "false" );
        properties.put( "cacheServerConfiguration", "true" );
        properties.put( "cachePrepStmts", "true" );
        properties.put( "prepStmtCacheSize", "250" );
        properties.put( "prepStmtCacheSqlLimit", "2048" );
        properties.put( "useUnicode", "true" );
        properties.put( "characterEncoding", "UTF-8" );
        properties.put( "characterSetResults", "UTF-8" );
        properties.put( "useServerPrepStmts", "true" );

        config.setDataSourceProperties( properties );

        WGdataSource = new HikariDataSource( config );
    }

    /**
     * Database Close 処理
     */
    public static void disconnect() {
        if ( WGdataSource != null ) {
            Tools.Prt( ChatColor.YELLOW + "SQL WG Disconnected.", Tools.consoleMode.max, Config.programCode );
            WGdataSource.close();
        }
    }

    /**
     * regionがDB登録されているか？
     *
     * @param region
     * @return 
     */
    public static boolean GetRegion( String region ) {
        boolean retStat = false;
        connect();
        try ( Connection con = WGdataSource.getConnection() ) {
            Statement stmt = con.createStatement();
            String sql = "SELECT * FROM region WHERE id = '" + region + "';";
            Tools.Prt( "SQL : " + sql, Tools.consoleMode.max, Config.programCode );
            ResultSet rs = stmt.executeQuery( sql );
            if ( rs.next() ) {
                Tools.Prt( "Get Data from WG Success.", Tools.consoleMode.max, Config.programCode );
                retStat = true;
            }
            con.close();
        } catch ( SQLException e ) {
            Tools.Prt( ChatColor.RED + "Error WG GetRegion : " + e.getMessage(), Config.programCode );
            retStat = false;
        }
        disconnect();
        return retStat;
    }


}
