package com.github.tadeuespindolapalermo.easyjdbc.connection;

import com.github.tadeuespindolapalermo.easyjdbc.enumeration.EnumDatabase;
import com.github.tadeuespindolapalermo.easyjdbc.enumeration.EnumLogMessages;
import com.github.tadeuespindolapalermo.easyjdbc.util.LogUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.EnumMap;

import static com.github.tadeuespindolapalermo.easyjdbc.util.ValidatorUtil.isNotNull;
import static com.github.tadeuespindolapalermo.easyjdbc.util.ValidatorUtil.isNull;

public class SingletonConnection extends MountConnection {

	private static Connection connection = InfoConnection.getConnection();

	private static final EnumMap<EnumDatabase, ConnectionUrl> MAP_DATABASE_URL = new EnumMap<>(EnumDatabase.class);

	private SingletonConnection() {
	}

	static {
		MAP_DATABASE_URL.put(EnumDatabase.MYSQL, new MySQLConnectionUrl());
		MAP_DATABASE_URL.put(EnumDatabase.SQLITE, new SQLiteConnectionUrl());
		MAP_DATABASE_URL.put(EnumDatabase.ORACLE, new OracleConnectionUrl());
		MAP_DATABASE_URL.put(EnumDatabase.POSTGRE, new PostgreConnectionUrl());
		MAP_DATABASE_URL.put(EnumDatabase.H2, new H2ConnectionUrl());
		toConnect();
	}

	private static void toConnect() {
		try {
			if (isNull(connection)) {

				String url = MAP_DATABASE_URL.get(InfoConnection.getDatabase()).getUrl();

				if (InfoConnection.getDatabase().equals(EnumDatabase.SQLITE)) {
					connection = DriverManager.getConnection(url);
				} else {
					connection = DriverManager.getConnection(url, InfoConnection.getUser(),
							InfoConnection.getPassword());
				}

				LogUtil.getLogger(SingletonConnection.class)
						.info(EnumLogMessages.CONN_SUCCESS.getMessage() + "\nBank: "
								+ InfoConnection.getDatabase().name() + "\nDatabase: "
								+ (isNotNull(InfoConnection.getNameDatabase()) ? InfoConnection.getNameDatabase()
										: "informed directly in the connection URL"));
			}
			connection.setAutoCommit(false);
		} catch (Exception e) {
			LogUtil.getLogger(SingletonConnection.class)
					.error(EnumLogMessages.CONN_FAILED.getMessage() + "\n" + e.getCause().toString());
		}
	}

	public static Connection getConnection() {
		return connection;
	}

}
