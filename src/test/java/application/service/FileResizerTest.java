package application.service;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FileResizerTest {

    public static final int HEIGHT_MAX = FileResizer.HEIGHT_MAX;
    public static final int WIDTH_MAX = FileResizer.WIDTH_MAX;
    public static final int HEIGHT_SMALLER_BY_100 = FileResizer.HEIGHT_MAX - 100;
    public static final int WIDTH_SMALLER_BY_100 = FileResizer.WIDTH_MAX - 100;
    public static final int HEIGHT_BIGGER_BY_100 = FileResizer.HEIGHT_MAX + 100;
    public static final int WIDTH_BIGGER_BY_200 = FileResizer.WIDTH_MAX + 200;

    @Test
    public void shouldBeFalseWhenSmallerThanMaxAllowed() throws Exception {
        assertFalse(FileResizer.needsResize(WIDTH_SMALLER_BY_100, HEIGHT_SMALLER_BY_100));
    }

    @Test
    public void shouldBeFalseWhenEquals() throws Exception {
        assertFalse(FileResizer.needsResize(WIDTH_MAX, HEIGHT_MAX));
    }

    @Test
    public void shouldBeTrueIfOneIsBiggerThanAllowed() throws Exception {
        assertTrue(FileResizer.needsResize(WIDTH_SMALLER_BY_100, HEIGHT_BIGGER_BY_100));
    }

    @Test
    public void shouldBeTrueIfOneIsBiggerThanAllowed2() throws Exception {
        assertTrue(FileResizer.needsResize(WIDTH_BIGGER_BY_200, HEIGHT_SMALLER_BY_100));
    }

    @Test
    public void shouldResizeHeightOrWidthToItsMaximum() throws Exception {
        int testHeight = HEIGHT_BIGGER_BY_100;
        int testWidth = WIDTH_BIGGER_BY_200;
        double actual = FileResizer.maxResizePercent(testWidth, testHeight);
        int resizedWidth = (int) (actual * testWidth);
        int resizedHeight = (int) (actual * testHeight);
        assertTrue(WIDTH_MAX == resizedWidth || HEIGHT_MAX == resizedHeight);
    }

    @Test
    public void shouldResizeHeightAndWidthSmallerOrEqualsToMax() throws Exception {
        int testHeight = HEIGHT_BIGGER_BY_100;
        int testWidth = WIDTH_BIGGER_BY_200;
        double actual = FileResizer.maxResizePercent(testWidth, testHeight);
        int resizedWidth = (int) (actual * testWidth);
        int resizedHeight = (int) (actual * testHeight);
        assertTrue(WIDTH_MAX >= resizedWidth);
        assertTrue(HEIGHT_MAX >= resizedHeight);
    }


}