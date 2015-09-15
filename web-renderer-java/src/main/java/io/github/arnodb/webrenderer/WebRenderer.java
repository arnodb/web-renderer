package io.github.arnodb.webrenderer;

import java.lang.ClassNotFoundException;
import java.lang.IllegalAccessException;
import java.lang.InstantiationException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

/**
 * Web renderer CLI.
 * 
 * @author arnodb
 */
public class WebRenderer {

    /**
     * Entry point.
     * 
     * @param args
     *            see usage.
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        WebRenderer main = new WebRenderer();
        CmdLineParser parser = new CmdLineParser(main);
        try {
            parser.parseArgument(args);
            main.run();
        } catch (CmdLineException e) {
            System.err.println(e.getMessage());
            parser.printUsage(System.err);
            System.err.println();
            throw e;
        }
    }

    /**
     * Loads a URL in the given driver and returns PNG data.
     * 
     * @param driver
     *            the driver to load the URL in.
     * @param url
     *            the URL to load.
     * @return PNG data.
     */
    public static byte[] urlToPng(WebDriver driver, String url) {
        driver.get(url);
        TakesScreenshot ts = (TakesScreenshot) driver;
        byte[] data = ts.getScreenshotAs(OutputType.BYTES);
        return data;
    }

    /**
     * Loads a URL in the given driver and writes PNG data to the given output.
     * 
     * @param driver
     *            the driver to load the URL in.
     * @param url
     *            the URL to load.
     * @param output
     *            the output file.
     * @throws IOException
     */
    public static void urlToPngFile(WebDriver driver, String url, File output) throws IOException {
        byte[] data = urlToPng(driver, url);
        IOUtils.write(data, new FileOutputStream(output));
    }

    /**
     * Loads a URL in the given driver and writes PNG data to the given output.
     * 
     * @param driver
     *            the driver to load the URL in.
     * @param url
     *            the URL to load.
     * @param output
     *            the output file path.
     * @throws IOException
     */
    public static void urlToPngFile(WebDriver driver, String url, String output) throws IOException {
        byte[] data = urlToPng(driver, url);
        IOUtils.write(data, new FileOutputStream(new File(output)));
    }

    /**
     * Loads a URL in the given driver and writes PNG data to the given output.
     * 
     * @param driver
     *            the driver to load the URL in.
     * @param url
     *            the URL to load.
     * @param output
     *            the output stream.
     * @throws IOException
     */
    public static void urlToPngStream(WebDriver driver, String url, OutputStream output) throws IOException {
        byte[] data = urlToPng(driver, url);
        IOUtils.write(data, output);
    }

    /**
     * Driver class override, default driver is the Firefox.
     */
    @Option(name = "-driver", usage = "driver class name", metaVar = "DRIVER")
    private String driverClassName = "org.openqa.selenium.firefox.FirefoxDriver";

    /**
     * Output file path, <code>"-"</code> for stdout (default).
     */
    @Option(name = "-o", usage = "output file", metaVar = "OUTPUT")
    private String output;

    /**
     * The URL to load, required.
     */
    @Argument(index = 0, usage = "url", metaVar = "URL", required = true)
    private String url;

    public void run() throws ClassNotFoundException, IllegalAccessException, InstantiationException, IOException {
        Class<?> driverClass = Class.forName(driverClassName);
        WebDriver driver = (WebDriver) driverClass.newInstance();
        if (output == null || "-".equals(output)) {
            urlToPngStream(driver, url, System.out);
        } else {
            urlToPngFile(driver, url, new File(output));
        }
        driver.quit();
    }

}
