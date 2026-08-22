package org.example.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabasePool {
    private static final HikariDataSource dataSource;

    static{
        HikariConfig config = new HikariConfig();

        config.setJdbcUrl("jdbc:postgresql://localhost:5432/postgres");
        config.setUsername("postgres");
        config.setPassword("12345");

        config.setMaximumPoolSize(10);
        config.setMinimumIdle(5);
        config.setIdleTimeout(300000);
        config.setConnectionTimeout(20000);

        dataSource = new HikariDataSource(config);
    }

    public DatabasePool() {
    }
    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
    public static void close(){
        if(dataSource != null && !dataSource.isClosed()){
            dataSource.close();
        }
    }
}
