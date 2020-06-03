package br.com.emailversaoautomatico;

import br.com.emailversaoautomatico.enums.EnumQuadro;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import org.apache.commons.collections.CollectionUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EmailApplication {

    public static WebDriver WEB_DRIVER;
    public static JavascriptExecutor js;

    public static void main(String[] args) throws IOException {
        System.setProperty("webdriver.chrome.driver", "/home/raphaelmoreira/Documentos/chromedriver");

        WEB_DRIVER = new ChromeDriver();

        WEB_DRIVER.manage().window().maximize();
        WEB_DRIVER.get("https://finchsolucoes.atlassian.net/");

        login(args[0], args[1]);
        gerarEmail(args[2], EnumQuadro.PENDENTE_VERSAO_QA.toString());
    }

    public static void gerarEmail(String versao, String quadroTasks) throws IOException {
        List<String> corpoEmail = new ArrayList<>();

        if (WEB_DRIVER instanceof JavascriptExecutor) {
            js = (JavascriptExecutor)WEB_DRIVER;

            (new WebDriverWait(WEB_DRIVER, 30))
                    .until(ExpectedConditions.elementToBeClickable(By.className("css-1yx6h60")));

            WEB_DRIVER.get("https://finchsolucoes.atlassian.net/secure/RapidBoard.jspa?rapidView=2");

            addJQuery();

            (new WebDriverWait(WEB_DRIVER, 30))
                    .until(ExpectedConditions.elementToBeClickable(By.id("ghx-complete-sprint")));

            List<WebElement> tasks = (List<WebElement>) js.executeScript("return $('.ghx-columns [data-column-id=" + quadroTasks + "] .ghx-issue.ghx-newcard.js-detailview.js-issue.js-parent-drag.ghx-card-color-enabled')");

            corpoEmail.add("Boa tarde a todos!");
            corpoEmail.add("\n");
            corpoEmail.add("\n");
            corpoEmail.add("\n");
            corpoEmail.add("Gerada a versão de homologação " + versao + " com os itens abaixo:");
            corpoEmail.add("\n");
            corpoEmail.add("\n");
            corpoEmail.add("\n");

            if (CollectionUtils.isNotEmpty(tasks)) {
                corpoEmail.addAll(
                        tasks
                            .stream()
                            .map(task -> "[" + task.getAttribute("data-issue-key") + "] - " + task.getText().substring(0, task.getText().indexOf("\n")) + "\n")
                            .collect(Collectors.toList())
                );
            }

            System.out.println(corpoEmail
                    .stream()
                    .map(String::toString)
                    .collect(Collectors.joining())
            );
        }
    }

    private static void login(String userName, String passWord) {
        WebElement webElement = (new WebDriverWait(WEB_DRIVER, 30))
                .until(ExpectedConditions.elementToBeClickable(By.id("username")));
        webElement.sendKeys(userName);

        WebElement btnAcessar = (new WebDriverWait(WEB_DRIVER, 30))
                .until(ExpectedConditions.elementToBeClickable(By.id("login-submit")));

        btnAcessar.click();

        webElement = (new WebDriverWait(WEB_DRIVER, 30))
                .until(ExpectedConditions.elementToBeClickable(By.id("password")));
        webElement.sendKeys(passWord);

        btnAcessar.click();
    }

    private static void addJQuery() throws IOException {
        URL jqueryUrl = Resources.getResource("js/jquery-3.5.1.min.js");
        System.out.println(jqueryUrl.getPath());
        String jqueryText = Resources.toString(jqueryUrl, Charsets.UTF_8);
        js.executeScript(jqueryText);
    }
}
