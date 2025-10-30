package br.com.procedureauthorization.config;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;

import java.sql.Connection;
import java.sql.DriverManager;

@WebListener
public class LiquibaseInitializer implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("=== Starting Liquibase Migrations ===");
        String jdbcUrl = AppConfig.get("jdbc.url");
        String jdbcUser = AppConfig.get("jdbc.user");
        String jdbcPass = AppConfig.get("jdbc.password");

        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPass);
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));

            Liquibase liquibase = new Liquibase(
                    "db/changelog/changelog-master.xml",
                    new ClassLoaderResourceAccessor(),
                    database
            );

            liquibase.update("");

            System.out.println("=== Finished liquibase migrations ===");

            connection.close();
        } catch (Exception e) {
            System.err.println("=== ERROR on executing liquibase migrations ===");
            e.printStackTrace();
            throw new RuntimeException("ERROR:", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("=== End application ===");
    }
}