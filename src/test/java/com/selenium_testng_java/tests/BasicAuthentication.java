package com.selenium_testng_java.tests;

import com.google.common.collect.ImmutableMap;
import com.selenium_testng_java.driver.DriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.HasDevTools;
import org.openqa.selenium.devtools.v124.network.Network;
import org.openqa.selenium.devtools.v124.network.model.Headers;
import org.testng.annotations.Test;

import java.net.URI;
import java.util.Base64;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static com.selenium_testng_java.pages.BasePage.waitFor;
import static org.testng.AssertJUnit.assertEquals;

public class BasicAuthentication extends BaseTest {
    DevTools devTools;

    @Test(description = "Basic authentication using BiDi API", priority = 1)
    public void TestPredicate() {
        Predicate<URI> uriPredicate = uri -> uri.toString().contains("herokuapp.com");
        Supplier<Credentials> authentication = UsernameAndPassword.of("admin", "admin");

        ((HasAuthentication) DriverManager.getDriver()).register(uriPredicate, authentication);

        DriverManager.getDriver().get("https://the-internet.herokuapp.com/basic_auth");

        String successMessage = "Congratulations! You must have the proper credentials.";
        WebElement elementMessage = DriverManager.getDriver().findElement(By.tagName("p"));
        assertEquals(successMessage, elementMessage.getText());
        waitFor(3);
    }
    @Test(description = "Basic authentication using Chrome Devtools",priority = 2)
    public void basicAuth() {
        devTools = ((HasDevTools) DriverManager.getDriver()).getDevTools();
        devTools.createSession();
        devTools.send(Network.enable(Optional.of(100000), Optional.of(100000), Optional.of(100000)));

        String encodedAuth = Base64.getEncoder().encodeToString("admin:admin".getBytes());
        Map<String, Object> headers = ImmutableMap.of("Authorization", "Basic " + encodedAuth);

        devTools.send(Network.setExtraHTTPHeaders(new Headers(headers)));

        DriverManager.getDriver().get("https://the-internet.herokuapp.com/basic_auth");

        assertEquals(
                "Congratulations! You must have the proper credentials.",
                DriverManager.getDriver().findElement(By.tagName("p")).getText());
    }
}
