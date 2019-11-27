/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.rentareas.database;

import java.util.Date;
import java.text.SimpleDateFormat;
import com.zaxxer.hikari.HikariDataSource;

/**
 *
 * @author sugichan
 */
public class Database {
    public static HikariDataSource dataSource = null;
    public static final SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );

    //  テーブル
    //      region : varchar(20)    WorldGuard Region Name
    //      uuid : varchar(36)      player uuid
    //      name : varchar(20)      player name
    //      create : DATETIME       Rent Date
    //      logout : DATETIME       player Logout Date

    public static String region = "";
    public static String uuidstr = "";
    public static String name = "Unknown";
    public static Date entry;
    public static Date logout;
}
