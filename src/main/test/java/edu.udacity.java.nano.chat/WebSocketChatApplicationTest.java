package edu.udacity.java.nano.chat;

import edu.udacity.java.nano.chat.model.Message;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.concurrent.TimeUnit;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest
public class WebSocketChatApplicationTest {

    @Autowired
    private MockMvc mockMvc;
    WebDriver webDriver;

    @Test
    public void testLogin() throws Exception {
        this.mockMvc.perform(get("/")).andDo(print()).andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    public void testDisplayChatRoomPage() throws Exception {
        this.mockMvc.perform(get("/index?username=Johnny")).andExpect(status().isOk())
                .andExpect(content().string(containsString("Chat Room")))
                .andExpect(view().name("chat"));
    }
    @Test
    public void webSocketJoinedTest() throws InterruptedException {
        System.setProperty("webdriver.gecko.driver", "/usr/bin/gekodriver");
        webDriver = new FirefoxDriver();
        webDriver.get("http://127.0.0.1:8080/");
        webDriver.findElement(By.id("username")).sendKeys("Test User");
        webDriver.findElement(By.id("loginButton")).click();
        for (int i = 0; i < 10; i++) {
            webDriver.findElement(By.id("msg")).sendKeys("Hi! My name is Johnny Depp!!!");
            webDriver.findElement(By.id("sendButton")).click();
        }
        Thread.sleep(5000);
        webDriver.findElement(By.id("logoutButton")).click();
        webDriver.close();


    }

}
