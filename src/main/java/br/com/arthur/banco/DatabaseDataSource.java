package br.com.arthur.banco;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public final class DatabaseDataSource {

    private DatabaseDataSource() {
    }

    public static HikariDataSource criar(
            DatabaseConfig config) {

        HikariConfig hikariConfig = new HikariConfig();

        hikariConfig.setJdbcUrl(
                config.getUrl());

        hikariConfig.setUsername(
                config.getUsuario());

        hikariConfig.setPassword(
                config.getSenha());

        hikariConfig.setPoolName(
                "SistemaBancarioPool");

        return new HikariDataSource(
                hikariConfig);
    }
}