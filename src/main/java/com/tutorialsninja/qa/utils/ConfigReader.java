package com.tutorialsninja.qa.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class ConfigReader {

    private static final Logger logger = LogManager.getLogger(ConfigReader.class);
    private static final Properties prop = new Properties();

    static {
        loadResource("config.properties");
        loadResource("testdata.properties");
    }

    private ConfigReader() {}

    private static void loadResource(String fileName) {
        boolean loaded = false;

        // 1. Thread Context ClassLoader (Resolves src/test/resources during test execution)
        try {
            ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
            if (contextClassLoader != null) {
                try (InputStream is = contextClassLoader.getResourceAsStream(fileName)) {
                    if (is != null) {
                        prop.load(is);
                        loaded = true;
                        logger.info("Loaded {} via Thread Context ClassLoader", fileName);
                    }
                }
            }
        } catch (Exception ignored) {}

        // 2. Class ClassLoader
        if (!loaded) {
            try (InputStream is = ConfigReader.class.getClassLoader().getResourceAsStream(fileName)) {
                if (is != null) {
                    prop.load(is);
                    loaded = true;
                    logger.info("Loaded {} via ConfigReader ClassLoader", fileName);
                }
            } catch (Exception ignored) {}
        }

        // 3. Fallback: Search common maven filesystem paths
        if (!loaded) {
            String[] searchPaths = {
                Paths.get(System.getProperty("user.dir"), "src", "test", "resources", fileName).toString(),
                Paths.get(System.getProperty("user.dir"), "src", "main", "resources", fileName).toString(),
                Paths.get(System.getProperty("user.dir"), "target", "test-classes", fileName).toString(),
                Paths.get(System.getProperty("user.dir"), "target", "classes", fileName).toString()
            };

            for (String path : searchPaths) {
                File file = new File(path);
                if (file.exists() && file.isFile()) {
                    try (FileInputStream fis = new FileInputStream(file)) {
                        prop.load(fis);
                        loaded = true;
                        logger.info("Loaded {} from direct filesystem path: {}", fileName, path);
                        break;
                    } catch (Exception ignored) {}
                }
            }
        }

        if (!loaded) {
            logger.error("FATAL: Could not locate {} on classpath or filesystem.", fileName);
        }
    }

    public static String getProperty(String key) {
        String sysProp = System.getProperty(key);
        if (sysProp != null && !sysProp.isBlank()) {
            return sysProp.trim();
        }

        String val = prop.getProperty(key);
        if (val == null) {
            throw new IllegalArgumentException(
                "Property '" + key + "' was not found in config.properties, testdata.properties, or System properties. " +
                "Loaded keys count: " + prop.size()
            );
        }
        return val.trim();
    }

    public static String getBrowser() {
        return System.getProperty("browser", prop.getProperty("browserName", "chrome")).trim().toLowerCase();
    }

    public static boolean isHeadless() {
        return Boolean.parseBoolean(System.getProperty("headless", prop.getProperty("headless", "false")));
    }

    public static String getUrl() {
        return getProperty("url");
    }
}