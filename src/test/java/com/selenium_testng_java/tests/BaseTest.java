package com.selenium_testng_java.tests;


import com.github.javafaker.Faker;
import com.selenium_testng_java.apis.apitests.RegisterApi;
import com.selenium_testng_java.constants.FrameworkConstants;
import com.selenium_testng_java.driver.Driver;
import com.selenium_testng_java.driver.DriverManager;
import com.selenium_testng_java.pages.NewTodoPage;
import com.selenium_testng_java.pages.RegisterPage;
import com.selenium_testng_java.pages.ToDoPage;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

public class BaseTest {

    protected BaseTest() {
    }

    protected RegisterPage registerPage;
    protected ToDoPage toDoPage;
    protected NewTodoPage newTodoPage;
    protected Faker faker;
    protected String url;
    protected String herOkuUrl;
    protected String todoUrl;
    protected String createNewToDoText;
    protected String readyToMark;
    protected String todoItem;
    protected String email;
    protected String accessToken;
    @BeforeMethod
    public void setUp() {
        Driver.initDriver();
        registerPage = new RegisterPage(DriverManager.getDriver());
        toDoPage = new ToDoPage(DriverManager.getDriver());
        newTodoPage = new NewTodoPage(DriverManager.getDriver());
        faker = new Faker();
        url = FrameworkConstants.getURL();
        herOkuUrl = FrameworkConstants.getHerokuUrl();
        todoUrl = FrameworkConstants.getTodoUrl();
        createNewToDoText = FrameworkConstants.getCreateNewTodo();
        readyToMark = FrameworkConstants.getReadyToMark();
        todoItem = FrameworkConstants.getTodoItem();
        email = RegisterApi.email;
        accessToken = RegisterApi.accessToken;
    }

    @AfterMethod
    public void tearDown() {
        Driver.quitDriver();
    }


}
