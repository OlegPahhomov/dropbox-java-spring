package application.controller;

import application.service.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;


@RestController
public class FileGetController {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @RequestMapping("/files")
    public List<Map<String, Object>> getFiles() {
        return jdbcTemplate.queryForList("SELECT *, IMAGE_WIDTH::float / IMAGE_HEIGHT AS RATIO FROM FILE");
    }

    @RequestMapping(value = "/picture/{id}")
    public HttpEntity<?> getPicture(@PathVariable(value = "id") String idString) {
        if (Validator.invalidGetById(idString)) return new HttpEntity<>("vale id");
        byte[] picture = getPictureContentBy(Long.valueOf(idString));
        if (picture == null) return new HttpEntity<>("No picture found");
        return pictureEntity(picture);
    }

    private HttpEntity<byte[]> pictureEntity(byte[] picture) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG); //or what ever type it is
        headers.setContentLength(picture.length);
        return new HttpEntity<>(picture, headers);
    }

    public byte[] getPictureContentBy(Long id) {
        return jdbcTemplate.query("SELECT content FROM FILE WHERE ID=?", new Object[]{id},
                (ResultSet rs) -> rs.next() ? (byte[]) rs.getObject(1) : null);
    }
}