package hello;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.dbutils.handlers.MapListHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class GreetingController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @Autowired
    JdbcTemplate jdbcTemplate;

    @RequestMapping("/files")
    public
    @ResponseBody
    List<Map<String, Object>> getFiles(){
        return jdbcTemplate.queryForList("SELECT *, IMAGE_WIDTH::float / IMAGE_HEIGHT AS RATIO FROM FILE");
    }

    @RequestMapping("/greeting")
    public
    @ResponseBody
    Greeting greeting(
            @RequestParam(value = "name", required = false, defaultValue = "World")
            String name) {
        System.out.println("==== in greeting ====");
        return new Greeting(counter.incrementAndGet(), String.format(template, name));
    }
}