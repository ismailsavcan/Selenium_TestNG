package com.selenium_testng_java.utils;

import com.selenium_testng_java.driver.DriverManager;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.DevTools;

public class BrowserUtils {
    /**
     * This method enables to close the CDP session.
     */
    public static void closeCDPSession() {
        DevTools devTools = ((ChromeDriver) DriverManager.getDriver()).getDevTools();
        devTools.disconnectSession();
        devTools.close();
    }
}
