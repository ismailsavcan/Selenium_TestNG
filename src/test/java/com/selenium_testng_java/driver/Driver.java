package com.selenium_testng_java.driver;

import com.selenium_testng_java.factory.DriverFactory;

import java.time.Duration;
import java.util.Objects;

public class Driver {

    private Driver() {
    }

    public static void initDriver() {
        if (Objects.isNull(DriverManager.getDriver())) {
            DriverManager.setDriver(DriverFactory.createDriver());
            DriverManager.getDriver().manage().window().maximize();
            DriverManager.getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
        }
    }

    public static void quitDriver() {
        if (Objects.nonNull(DriverManager.getDriver())) {
            DriverManager.getDriver().close();
            DriverManager.getDriver().quit();
            DriverManager.unload();

        }
    }
}
