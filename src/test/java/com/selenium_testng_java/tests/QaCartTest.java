package com.selenium_testng_java.tests;


import com.selenium_testng_java.apis.apitests.RegisterApi;
import com.selenium_testng_java.constants.FrameworkConstants;
import com.selenium_testng_java.driver.DriverManager;
import com.selenium_testng_java.pages.ToDoPage;
import com.selenium_testng_java.utils.BrowserUtils;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v123.network.Network;
import org.openqa.selenium.devtools.v123.network.model.RequestId;
import org.testng.annotations.Test;

import java.util.Optional;


public class QaCartTest extends BaseTest {
    String access_Token;

    @Test(priority = 1)
    @Step("Test UI")
    public void TestUI() {
        Allure.step("Register via UI");
        registerPage
                .loadPage(url)
                .enterFirstName(faker.name().firstName())
                .enterLastName(faker.name().lastName())
                .enterEmail(faker.internet().emailAddress())
                .enterPassword(FrameworkConstants.getPassword())
                .enterConfirmPassword(FrameworkConstants.getPassword())
                .clickSubmitButton();
        Allure.step("Verify registration on ToDoPage");
        toDoPage.verifyWelcomeMessage()
                .clickAddNewTodoButton();
        Allure.step("Creating new todo item on new todo page");
        newTodoPage.verifyCreateNewTodoHeader(createNewToDoText)
                .verifyReadyToMarkSubHeader(readyToMark)
                .enterToDoItem(todoItem)
                .clickCreateTodoButton();
        Allure.step("Verify created task on todo page");
        toDoPage.verifyAddedTask(todoItem);
        Allure.step("Delete created test on todo page");
        toDoPage.clickCheckBox()
                .clickDeleteButton();
        Allure.step("Verify created task is deleted");
        toDoPage.verifyNoToDosMessage();
    }

    @Test(priority = 0)
    @Step("Test API UI API")
    public void TestAPIUI() {
        Allure.step("Registration via API");
        RegisterApi.registrationViaAPI();
        Allure.step("Adding cookie to webpage");
        toDoPage.addCookie(accessToken, DriverManager.getDriver(), herOkuUrl, todoUrl);
        Allure.step("login via UI using registered email and password via API");
        RegisterApi.loginToPage();
        Allure.step("Verify logout button that shows we are in todo page");
        toDoPage.verifyLogoutButton();
        RegisterApi.createTaskViaAPI();
        DriverManager.getDriver().navigate().refresh();
        toDoPage.verifyAddedTask("Learn")
                .clickCheckBox()
                .clickDeleteButton()
                .verifyNoToDosMessage();

    }

    @Test(priority = 2)
    @Step("Test  UI API UI")
    public void TestUIAPIUI() {
        Allure.step("Register via UI");
        validateNetworkResponseBody("/register");
        registerPage
                .loadPage(url)
                .enterFirstName(faker.name().firstName())
                .enterLastName(faker.name().lastName())
                .enterEmail(faker.internet().emailAddress())
                .enterPassword(FrameworkConstants.getPassword())
                .enterConfirmPassword(FrameworkConstants.getPassword())
                .clickSubmitButton();
        ToDoPage.waitFor(1);
        Allure.step("Creating Task using API");
        String objectId = RegisterApi.createTaskViaAPI(access_Token);
        Allure.step("Verify created task on todo page");
        ToDoPage.navigateTo_URL(todoUrl);
        toDoPage.verifyAddedTask("Learn");
        Allure.step("Delete created task using API");
        RegisterApi.delete(access_Token, objectId);
        Allure.step("Verify created task is deleted");
        ToDoPage.navigateTo_URL(todoUrl);
        toDoPage.verifyNoToDosMessage();
        BrowserUtils.closeCDPSession();
    }

    public String validateNetworkResponseBody(String path) {
        final RequestId[] requestIds = new RequestId[1];
        DevTools devTools = ((ChromeDriver) DriverManager.getDriver()).getDevTools();
        devTools.createSession();
        devTools.send(Network.enable(Optional.of(100000000), Optional.empty(), Optional.empty()));

        devTools.addListener(Network.responseReceived(), responseReceived -> {
            if (responseReceived.getResponse().getUrl().contains(path)) {
                responseReceived.getResponse().getHeaders().toJson().forEach((k, v) -> System.out.println((k + ":" + v)));
                requestIds[0] = responseReceived.getRequestId();
                String responseBody = devTools.send(Network.getResponseBody(requestIds[0])).getBody();

                String[] parts = responseBody.split("\"access_token\":\"");

                access_Token = parts[1].split("\"")[0];
                System.out.println(access_Token);

            }
        });
        return access_Token;
    }

}
