package controller;

import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import files.crud.FileReader;
import files.validator.FileValidator;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import static spark.Spark.halt;

@RestController
public class GreetingController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @Autowired
    JdbcTemplate jdbcTemplate;

    @RequestMapping("/files")
    public List<Map<String, Object>> getFiles() {
        return jdbcTemplate.queryForList("SELECT *, IMAGE_WIDTH::float / IMAGE_HEIGHT AS RATIO FROM FILE");
    }

    @RequestMapping(value = "/picture/{id}")
    public HttpEntity<byte[]> getPicture(@PathVariable(value = "id") String idString) {
        //todo validation
        //response.type("image/jpeg");
        Long id = Long.valueOf(idString);
        byte[] picture = (byte[]) jdbcTemplate.queryForObject("SELECT content FROM FILE WHERE ID=?", new Object[]{id}, Object.class);

        /*byte[] picture = (byte[]) jdbcTemplate
                .query("SELECT content FROM FILE WHERE ID=?", new Object[]{id},
                        rs -> {
                            rs.next();
                            return (byte[]) rs.getObject(1);
                        });*/

        if (picture.length != 0) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG); //or what ever type it is
            headers.setContentLength(picture.length);
            return new HttpEntity<>(picture, headers);
        }
        return null;
    }

    @RequestMapping("/greeting")
    public Greeting greeting(@RequestParam(value = "name", defaultValue = "World")
                             String name) {
        System.out.println("==== in greeting ====");
        return new Greeting(counter.incrementAndGet(), String.format(template, name));
    }
}