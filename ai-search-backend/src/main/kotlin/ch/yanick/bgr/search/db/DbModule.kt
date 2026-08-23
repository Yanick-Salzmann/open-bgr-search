package ch.yanick.bgr.search.db

import ch.yanick.bgr.config.AppConfig
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.application.Application
import io.ktor.server.plugins.di.dependencies
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.v1.jdbc.Database
import javax.sql.DataSource

fun Application.registerDependencies(cfg: AppConfig) {
    val hikariCfg = HikariConfig().also {
        it.jdbcUrl = cfg.db.jdbcUrl
        it.username = cfg.db.username
        it.password = cfg.db.password
    }

    val ds: DataSource = HikariDataSource(hikariCfg)

    dependencies {
        provide { ds }
        provide { CaseRecordRepository() }
    }
}

fun Application.configureDatabaseModule() {
    val cfg: AppConfig by dependencies
    registerDependencies(cfg)

    val ds: DataSource by dependencies

    Flyway.configure()
        .dataSource(ds)
        .locations("classpath:/db/migration")
        .load()
        .migrate()

    Database.connect(ds)
}