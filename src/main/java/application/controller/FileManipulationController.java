package application.controller;

import application.service.FileResizer;
import application.service.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.SQLException;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class FileManipulationController {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @RequestMapping(value = "/remove/{id}", method = POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public String removeFiles(@PathVariable(value = "id") String idString) {
        if (Validator.invalidDelete(idString)) return "failure";
        Long id = Long.valueOf(idString);
        jdbcTemplate.update("DELETE FROM FILE WHERE ID=?", id);
        return "";
    }

    @RequestMapping(value = "/add", method = POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public String handleFileUpload(@RequestParam("file") MultipartFile[] files) throws IOException, SQLException {
        if (Validator.invalidInsert(files)) return "";
        for (MultipartFile file : files) {
            saveOneFile(file);
        }
        return "";
    }

    private void saveOneFile(MultipartFile file) throws IOException {
        BufferedImage img = ImageIO.read(file.getInputStream());
        if (FileResizer.needsResize(img)) {
            BufferedImage resizedImage = FileResizer.dynamicResize(img);
            ByteArrayOutputStream os = FileResizer.getByteArrayOutputStream(resizedImage);
            updateFile(file, os.toByteArray(), resizedImage);
        } else {
            updateFile(file, file.getBytes(), img);
        }
    }

    private void updateFile(MultipartFile file, byte[] bytes, BufferedImage img) throws IOException {
        updateFile(file.getOriginalFilename(), bytes, img.getWidth(), img.getHeight());
    }

    private int updateFile(String name, byte[] content, int width, int height) {
        return jdbcTemplate.update("INSERT INTO FILE(name, content, image_width, image_height) VALUES (?,?,?,?)", name, content, width, height);
    }
}
