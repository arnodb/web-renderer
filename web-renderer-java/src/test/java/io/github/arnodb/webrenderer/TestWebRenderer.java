package io.github.arnodb.webrenderer;

import static org.junit.Assert.assertEquals;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;
import org.junit.Test;
import org.openqa.selenium.WebDriver;

public class TestWebRenderer {

    private static final String DRIVER_CLASS_NAME = "org.openqa.selenium.firefox.FirefoxDriver";

    private static String loadHtml() {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>\n");
        html.append("<html>\n");
        html.append("   <body style=\"margin: 0px; padding: 0px;\">\n");
        html.append("       <div style=\"border: 4px solid blue; width: 42px; height: 42px;\">&nbsp;\n");
        html.append("    </body>\n");
        html.append("</html>\n");
        return html.toString();
    }

    @Test
    public void test1() throws Exception {
        Class<?> driverClass = Class.forName(DRIVER_CLASS_NAME);
        WebDriver driver = (WebDriver) driverClass.newInstance();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        String url = "data:text/html;base64," + new String(Base64.encodeBase64(loadHtml().getBytes("UTF-8")));
        WebRenderer.urlToPngStream(driver, url, baos);
        driver.quit();

        InputStream bais = new ByteArrayInputStream(baos.toByteArray());
        BufferedImage image = ImageIO.read(bais);

        int[][] blues = new int[][] {{0, 0}, {3, 3}, {46, 46}, {49, 49}};
        for (int[] coord : blues) {
            int rgb = image.getRGB(coord[0], coord[1]);
            assertEquals("rgb(" + coord[0] + ", " + coord[1] + ") is blue", "ff0000ff", Integer.toHexString(rgb));
        }

        int[][] whites = new int[][] {{4, 4}, {45, 4}, {45, 45}, {4, 45}};
        for (int[] coord : whites) {
            int rgb = image.getRGB(coord[0], coord[1]);
            assertEquals("rgb(" + coord[0] + ", " + coord[1] + ") is blue", "ffffffff", Integer.toHexString(rgb));
        }
    }

}
