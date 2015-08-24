package application.service;

import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class Validator {

    public static boolean invalidDelete(String idString) {
        return !validId(idString);
    }

    public static boolean invalidGetById(String request) {
        return !validId(request);
    }

    public static boolean invalidInsert(MultipartFile[] files) {
        return empty(files) || !validContent(files);
    }

    /**
     * There is a non-null integer id
     */
    private static boolean validId(String idString) {
        try {
            Integer id = Integer.valueOf(idString);
            id = id + 1;
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean empty(MultipartFile[] files) {
        return files.length == 0;
    }

    /**
     * All are non-null pictures
     */
    public static boolean validContent(MultipartFile[] files) {
        try {
            for (MultipartFile file : files) {
                BufferedImage img = ImageIO.read(file.getInputStream());
                img.getHeight();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
