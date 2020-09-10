package net.iceyleagons.bingo.storage;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author TOTHTOMI
 */
@AllArgsConstructor
public enum DatabaseType {

    MYSQL("com.mysql.cj.jdbc.Driver","org.hibernate.dialect.MySQL5Dialect"),
    HSQL("org.hsqldb.jdbcDriver","org.hibernate.dialect.HSQLDialect"),
    POSTGRESQL("org.postgresql.Driver","org.hibernate.dialect.PostgreSQL82Dialect"),
    H2("org.h2.Driver","org.hibernate.dialect.H2Dialect"),
    MARIADB("org.mariadb.jdbc.Driver","org.hibernate.dialect.MariaDBDialect");

    ;

    @Getter
    private final String driver;
    @Getter
    private final String dialect;



}
