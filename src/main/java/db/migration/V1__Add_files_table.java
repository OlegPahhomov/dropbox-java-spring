package db.migration;

import org.flywaydb.core.api.migration.spring.SpringJdbcMigration;
import org.springframework.jdbc.core.JdbcTemplate;

public class V1__Add_files_table implements SpringJdbcMigration {
    public void migrate(JdbcTemplate jdbcTemplate) throws Exception {
        jdbcTemplate.update("CREATE TABLE file (\n" +
                        "  id      BIGSERIAL PRIMARY KEY                  NOT NULL,\n" +
                        "  name    TEXT CHECK (NAME <> '')                NOT NULL,\n" +
                        "  content BYTEA                                  NOT NULL,\n" +
                        "  image_width INTEGER                            NOT NULL,\n" +
                        "  image_height INTEGER                           NOT NULL\n" +
                        ");"
        );
    }

}
