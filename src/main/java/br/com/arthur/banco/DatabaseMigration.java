package br.com.arthur.banco;

import javax.sql.DataSource;

import org.flywaydb.core.Flyway;

public final class DatabaseMigration {

    private DatabaseMigration() {
    }

    public static void migrate(
            DataSource dataSource) {

        if (dataSource == null) {
            throw new IllegalArgumentException(
                    "DataSource não pode ser nulo.");
        }

        Flyway flyway = Flyway.configure()
                .dataSource(dataSource)
                .baselineOnMigrate(true)
                .load();

        flyway.migrate();
    }
}