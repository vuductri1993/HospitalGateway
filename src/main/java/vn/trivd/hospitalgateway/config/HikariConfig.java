package vn.trivd.hospitalgateway.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/** {@link DataSource} HikariCP; {@code HOSPITAL_*}: ưu tiên {@link System#getenv}, sau đó {@link System#getProperty} (từ {@code local.env}). */
@Configuration
public class HikariConfig {

    private static final String DEFAULT_URL = envOrProperty("HOSPITAL_URL");
    private static final String DEFAULT_USERNAME = envOrProperty("HOSPITAL_USERNAME");
    private static final String DEFAULT_PASSWORD = envOrProperty("HOSPITAL_PASSWORD");
    private static final String DEFAULT_DRIVER = envOrProperty("HOSPITAL_DRIVER_CLASS_NAME");
    private static final String DEFAULT_MAX_POOL = envOrProperty("HOSPITAL_MAXIMUM_POOL_SIZE");
    private static final String DEFAULT_MIN_IDLE = envOrProperty("HOSPITAL_MINIMUM_IDLE");
    private static final String DEFAULT_POOL_NAME = envOrProperty("HOSPITAL_POOL_NAME");

    private static String envOrProperty(String key) {
        String fromEnv = System.getenv(key);
        if (fromEnv != null && !fromEnv.isBlank()) {
            return fromEnv.trim();
        }
        String fromProp = System.getProperty(key);
        if (fromProp != null && !fromProp.isBlank()) {
            return fromProp.trim();
        }
        return null;
    }

    @Bean
    @Primary
    public DataSource dataSource() {
        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl(DEFAULT_URL);
        ds.setUsername(DEFAULT_USERNAME);
        ds.setPassword(DEFAULT_PASSWORD);
        ds.setDriverClassName(DEFAULT_DRIVER);
        ds.setMaximumPoolSize(
                Integer.parseInt(DEFAULT_MAX_POOL == null || DEFAULT_MAX_POOL.isBlank() ? "10" : DEFAULT_MAX_POOL));
        ds.setMinimumIdle(
                Integer.parseInt(DEFAULT_MIN_IDLE == null || DEFAULT_MIN_IDLE.isBlank() ? "2" : DEFAULT_MIN_IDLE));
        ds.setPoolName(DEFAULT_POOL_NAME);
        return ds;
    }
}
