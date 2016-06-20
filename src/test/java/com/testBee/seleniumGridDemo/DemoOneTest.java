package com.testBee.seleniumGridDemo;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;  
import java.net.URL;

import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;  
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;  
import org.openqa.selenium.remote.DesiredCapabilities;  
import org.openqa.selenium.remote.RemoteWebDriver;  
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;  
import org.testng.annotations.BeforeMethod;  
import org.testng.annotations.Parameters;  
import org.testng.annotations.Test;

public class DemoOneTest {
    private WebDriver dr;  
    DesiredCapabilities test;  
    String baseUrl;  
  
    @Parameters({"browser","nodeUrl","webSite"})  
    @BeforeMethod  
    public void setUp(String browser,String nodeUrl,String webSite){  
        baseUrl = webSite;  
          
        if(browser.equals("ie")) test = DesiredCapabilities.internetExplorer();  
        else if(browser.equals("ff")) test = DesiredCapabilities.firefox();  
        else if(browser.equals("chrome")) test = DesiredCapabilities.chrome();  
        else System.out.println("browser参数有误，只能为ie、 ff、chrome");  
          
        String url = nodeUrl + "/wd/hub";  
        URL urlInstance = null;  
        try {  
            urlInstance = new URL(url);  
        } catch (MalformedURLException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
            System.out.println("实例化url出错，检查一下url格式是否正确，格式为：http://192.168.199.65:5555");  
        }  
        dr = new RemoteWebDriver(urlInstance,test);  
        dr.get(webSite);  
    }  

    @Parameters({"browser"})
    @Test  
    public void testLogin(String browser){  
        dr.get(baseUrl);
        dr.manage().window().maximize();
        dr.findElement(By.linkText("登录")).click();
        dr.findElement(By.id("name")).sendKeys(browser);
        try {  
            Thread.sleep(10000);  
        } catch (InterruptedException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }  
        System.out.println("title:"+dr.getTitle());  
        if(browser.equals("ff") ||browser.equals("chrome")){
        	Assert.assertEquals(1, 2);
        }
    }  
    
    @Parameters({"browser"})
	@AfterMethod(alwaysRun = true)
	public void afterMethod(ITestResult result,String browser) throws Exception {
      if (!result.isSuccess())
    	  catchExceptions(result,browser);
	}
    
    @AfterMethod  
    public void quit(){  
        dr.quit();  
    }
 
//用例未执行通过时，捕获异常并截图至报告中    
	public void catchExceptions(ITestResult result,String browser) {

	      if (!result.isSuccess()) {
	          File file = new File("");
	          Reporter.setCurrentTestResult(result);
	          //打印项目地址
	          System.out.println(file.getAbsolutePath()+"\n");
	          //将项目地址输出到报告日志中
	          Reporter.log(file.getAbsolutePath());
	          String filePath = file.getAbsolutePath();
	          Reporter.log("<img src='"+filePath+File.separator+"surefire-reports"+File.separator+"html"+File.separator+"output"+File.separator+result.getName()+"_"+browser+".png' hight='100' width='100'/>");
	          File screenShotFile = new File(filePath+File.separator+"surefire-reports"+File.separator+"html"+File.separator+"output"+File.separator+result.getName()+"_"+browser+".png");
	          try {
	        	  File scrFile = ((TakesScreenshot)dr).getScreenshotAs(OutputType.FILE);
	        	  FileUtils.copyFile(scrFile, screenShotFile);
	          } catch (IOException e) {
	              e.printStackTrace();
	          }
	      }
		}	
}
