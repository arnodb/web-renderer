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

public class WebRenderer {

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

    public static byte[] urlToPng(WebDriver driver, String url) {
        driver.get(url);
        TakesScreenshot ts = (TakesScreenshot)driver;
        byte[] data = ts.getScreenshotAs(OutputType.BYTES);
        return data;
    }

    public static void urlToPngFile(WebDriver driver, String url, File output) throws IOException {
        byte[] data = urlToPng(driver, url);
        IOUtils.write(data, new FileOutputStream(output));
    }

    public static void urlToPngFile(WebDriver driver, String url, String output) throws IOException {
        byte[] data = urlToPng(driver, url);
        IOUtils.write(data, new FileOutputStream(new File(output)));
    }

    public static void urlToPngStream(WebDriver driver, String url, OutputStream output) throws IOException {
        byte[] data = urlToPng(driver, url);
        IOUtils.write(data, output);
    }

    @Option(name = "-driver", usage = "driver class name", metaVar = "DRIVER")
    private String driverClassName = "org.openqa.selenium.firefox.FirefoxDriver";

    @Option(name = "-o", usage = "output file", metaVar = "OUTPUT")
    private String output;

    @Argument(index = 0, usage = "url", metaVar = "URL", required = true)
    private String url;

    public void run() throws ClassNotFoundException, IllegalAccessException, InstantiationException, IOException {
        Class driverClass = Class.forName(driverClassName);
        WebDriver driver = (WebDriver)driverClass.newInstance();
        if (output == null || "-".equals(output)) {
            urlToPngStream(driver, url, System.out);
        } else {
            urlToPngFile(driver, url, new File(output));
        }
        driver.quit();
    }

}
