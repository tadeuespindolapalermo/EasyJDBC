package com.github.tadeuespindolapalermo.easyjdbc.connection;

import com.github.tadeuespindolapalermo.easyjdbc.enumeration.EnumDatabase;

import java.sql.Connection;

public class InfoConnection {

    private InfoConnection() {
    }

    private static String nameDatabase;
    private static String password;
    private static String user;
    private static EnumDatabase database;
    private static String host;
    private static String port;
    private static Connection connection;
    private static String url;
    private static String databaseFilePath;
    private static String databaseFile;

    public static String getNameDatabase() {
        return nameDatabase;
    }

    public static void setNameDatabase(String nameDatabase) {
        InfoConnection.nameDatabase = nameDatabase;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        InfoConnection.password = password;
    }

    public static String getUser() {
        return user;
    }

    public static void setUser(String user) {
        InfoConnection.user = user;
    }

    public static EnumDatabase getDatabase() {
        return database;
    }

    public static void setDatabase(EnumDatabase database) {
        InfoConnection.database = database;
    }

    public static String getHost() {
        return host;
    }

    public static void setHost(String host) {
        InfoConnection.host = host;
    }

    public static String getPort() {
        return port;
    }

    public static void setPort(String port) {
        InfoConnection.port = port;
    }

    public static Connection getConnection() {
        return connection;
    }

    public static void setConnection(Connection connection) {
        InfoConnection.connection = connection;
    }

    public static String getUrl() {
        return url;
    }

    public static void setUrl(String url) {
        InfoConnection.url = url;
    }

    public static String getDatabaseFilePath() {
        return databaseFilePath;
    }

    public static void setDatabaseFilePath(String databaseFilePath) {
        InfoConnection.databaseFilePath = databaseFilePath;
    }

    public static String getDatabaseFile() {
        return databaseFile;
    }

    public static void setDatabaseFile(String databaseFile) {
        InfoConnection.databaseFile = databaseFile;
    }

}
