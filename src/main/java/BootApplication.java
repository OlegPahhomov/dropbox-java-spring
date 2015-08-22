import com.google.gson.Gson;
import config.AppDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.Connection;
import java.sql.SQLException;


@Controller
@EnableAutoConfiguration
public class BootApplication {

    static QueryRunner queryRunner = new QueryRunner();

    @RequestMapping("/")
    @ResponseBody
    String home() {
        return "Hello World!";
    }

    @RequestMapping("/files")
    @ResponseBody
    String getFiles() throws SQLException {
        try (Connection connection = AppDataSource.getConnection()) {
            return new Gson().toJson(queryRunner.query(connection, "SELECT *, IMAGE_WIDTH::float / IMAGE_HEIGHT AS RATIO FROM FILE", new MapListHandler()));
        }
    }


    public static void main(String[] args) throws Exception {
        SpringApplication.run(BootApplication.class, args);
    }
}