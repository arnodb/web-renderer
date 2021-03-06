package io.github.arnodb.webrenderer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.PrintStream;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;
import org.junit.Test;
import org.kohsuke.args4j.CmdLineException;
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

    private void assertBlueBox(BufferedImage image) {
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

    @Test
    public void testCommandLine() throws Exception {
        File output = new File("target/test-resources/testCommandLine.png");
        if (output.exists())
            output.delete();
        File parent = output.getParentFile();
        if (!parent.exists())
            parent.mkdirs();

        String url = "data:text/html;base64," + new String(Base64.encodeBase64(loadHtml().getBytes("UTF-8")));
        WebRenderer.main(new String[]{ "-o", output.getPath(), url });
        assertTrue("output file '" + output + "' exists", output.exists());

        BufferedImage image = ImageIO.read(output);
        assertBlueBox(image);
    }

    @Test
    public void testCommandLineStdOut() throws Exception {
        File output = new File("target/test-resources/testCommandLine.png");
        if (output.exists())
            output.delete();
        File parent = output.getParentFile();
        if (!parent.exists())
            parent.mkdirs();

        String url = "data:text/html;base64," + new String(Base64.encodeBase64(loadHtml().getBytes("UTF-8")));
        PrintStream oldOut = System.out;
        ByteArrayOutputStream capture = new ByteArrayOutputStream();
        System.setOut(new PrintStream(capture));
        try {
            WebRenderer.main(new String[]{ "-o", "-", url });
        } finally {
            System.out.flush();
            System.setOut(oldOut);
        }

        InputStream bais = new ByteArrayInputStream(capture.toByteArray());
        BufferedImage image = ImageIO.read(bais);
        assertBlueBox(image);
    }

    @Test
    public void testCommandLineUsage() throws Exception {
        PrintStream oldErr = System.err;
        ByteArrayOutputStream capture = new ByteArrayOutputStream();
        System.setErr(new PrintStream(capture));
        try {
            WebRenderer.main(new String[]{ });
            fail("CmdLineException exception expected");
        } catch (CmdLineException e) {
        } finally {
            System.err.flush();
            System.setErr(oldErr);
        }
        assertEquals("usage", "Argument \"URL\" is required\n" +
                " URL            : url\n" +
                " -driver DRIVER : driver class name (default: org.openqa.selenium.firefox.Firefo\n" +
                "                  xDriver)\n" +
                " -o OUTPUT      : output file\n\n", new String(capture.toByteArray()));
    }

    @Test
    public void testUrlToPngFileFile() throws Exception {
        File output = new File("target/test-resources/testUrlToPngFileFile.png");
        if (output.exists())
            output.delete();
        File parent = output.getParentFile();
        if (!parent.exists())
            parent.mkdirs();

        Class<?> driverClass = Class.forName(DRIVER_CLASS_NAME);
        WebDriver driver = (WebDriver) driverClass.newInstance();
        String url = "data:text/html;base64," + new String(Base64.encodeBase64(loadHtml().getBytes("UTF-8")));
        WebRenderer.urlToPngFile(driver, url, output);
        driver.quit();
        assertTrue("output file '" + output + "' exists", output.exists());

        BufferedImage image = ImageIO.read(output);
        assertBlueBox(image);
    }

    @Test
    public void testUrlToPngFileString() throws Exception {
        File output = new File("target/test-resources/testUrlToPngFileString.png");
        if (output.exists())
            output.delete();
        File parent = output.getParentFile();
        if (!parent.exists())
            parent.mkdirs();

        Class<?> driverClass = Class.forName(DRIVER_CLASS_NAME);
        WebDriver driver = (WebDriver) driverClass.newInstance();
        String url = "data:text/html;base64," + new String(Base64.encodeBase64(loadHtml().getBytes("UTF-8")));
        WebRenderer.urlToPngFile(driver, url, output.getPath());
        driver.quit();
        assertTrue("output file '" + output + "' exists", output.exists());

        BufferedImage image = ImageIO.read(output);
        assertBlueBox(image);
    }

    @Test
    public void testUrlToPngStream() throws Exception {
        Class<?> driverClass = Class.forName(DRIVER_CLASS_NAME);
        WebDriver driver = (WebDriver) driverClass.newInstance();
        String url = "data:text/html;base64," + new String(Base64.encodeBase64(loadHtml().getBytes("UTF-8")));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        WebRenderer.urlToPngStream(driver, url, baos);
        driver.quit();

        InputStream bais = new ByteArrayInputStream(baos.toByteArray());
        BufferedImage image = ImageIO.read(bais);
        assertBlueBox(image);
    }

}
