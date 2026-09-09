package br.com.arthur.banco;

import org.flywaydb.core.Flyway;

public final class DatabaseMigration {

    private DatabaseMigration() {
    }

    public static void migrate(DatabaseConfig config) {

        Flyway flyway = Flyway.configure()
                .dataSource(
                        config.getUrl(),
                        config.getUsuario(),
                        config.getSenha())
                .baselineOnMigrate(true)
                .load();

        flyway.migrate();
    }
}