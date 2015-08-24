package db.migration;

import application.service.FileResizer;
import org.apache.commons.io.IOUtils;
import org.flywaydb.core.api.migration.spring.SpringJdbcMigration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.SQLException;

public class V2__Insert_files implements SpringJdbcMigration {

    public static final String CUTE_KITTENS = "src/main/resources/db/pics/Cute-Kittens.jpg";
    public static final String TERMINATOR = "src/main/resources/db/pics/Terminator.jpeg";
    public static final String HOME = "src/main/resources/db/pics/Home.jpeg";
    public static final String DRAKENSANG = "src/main/resources/db/pics/Drakensang.jpg";


    public void migrate(JdbcTemplate jdbcTemplate) throws Exception {
        insertToDb(jdbcTemplate, CUTE_KITTENS);
        insertToDb(jdbcTemplate, TERMINATOR);
        insertToDb(jdbcTemplate, DRAKENSANG);
        insertToDb(jdbcTemplate, HOME);
    }

    private void insertToDb(JdbcTemplate jdbcTemplate, String filename) throws IOException, SQLException {
        File file = new File(filename);
        BufferedImage img = ImageIO.read(file);
        if (FileResizer.needsResize(img)) {
            BufferedImage resizedImage = FileResizer.dynamicResize(img);
            ByteArrayOutputStream os = FileResizer.getByteArrayOutputStream(resizedImage);
            fillPsAndExecute(jdbcTemplate, file.getName(), resizedImage, os.toByteArray());
        } else {
            byte[] bytes = IOUtils.toByteArray(new FileInputStream(file));
            fillPsAndExecute(jdbcTemplate, file.getName(), img, bytes);
        }
    }

    private void fillPsAndExecute(JdbcTemplate jdbcTemplate, String fileName, BufferedImage img, byte[] content) throws SQLException {
        jdbcTemplate.update("INSERT INTO FILE(name, content, image_width, image_height) VALUES (?, ?, ?, ?)",
                fileName, content, img.getWidth(), img.getHeight());
    }

}
