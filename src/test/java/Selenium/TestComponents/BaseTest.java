package Selenium.TestComponents;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL; // <-- Fixes 'cannot find symbol: class URL'
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver; // <-- Fixes 'cannot find symbol: class RemoteWebDriver'
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.bonigarcia.wdm.WebDriverManager;
import selenium.pageobjects.LandingPage;

public class BaseTest {

	public LandingPage landingPage;
	public WebDriver driver;

	public WebDriver initializeDriver() throws IOException {
		Properties prop = new Properties();

		// Cross-platform file path using forward slashes
		FileInputStream fis = new FileInputStream(
				System.getProperty("user.dir") + "/src/main/java/Sel/resources/GlobalData.properties");
		prop.load(fis);

		// Read browser parameter from Maven (-Dbrowser=chromeheadless) or fallback to properties file
		String browserName = System.getProperty("browser") != null 
				? System.getProperty("browser")
				: prop.getProperty("browser");

		if (browserName.contains("chrome")) {
			ChromeOptions options = new ChromeOptions();
			options.addArguments("--no-sandbox");
			options.addArguments("--disable-dev-shm-usage");
			options.addArguments("--window-size=1920,1080");

			if (browserName.contains("headless")) {
				options.addArguments("--headless=new");
				
				// Connects to the standalone Chrome container via host gateway
				URL gridUrl = new URL("http://host.docker.internal:4444/wd/hub");
				driver = new RemoteWebDriver(gridUrl, options);
			} else {
				WebDriverManager.chromedriver().setup();
				driver = new ChromeDriver(options);
			}
		} else if (browserName.equalsIgnoreCase("firefox")) {
			// Firefox implementation
		} else if (browserName.equalsIgnoreCase("edge")) {
			// Edge implementation
		}

		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

		if (!browserName.contains("headless")) {
			driver.manage().window().maximize();
		}

		return driver;
	}

	public List<HashMap<String, String>> getJsonDataToMap(String filePath) throws IOException {
		String jsonContent = FileUtils.readFileToString(new File(filePath), StandardCharsets.UTF_8);
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(jsonContent, new TypeReference<List<HashMap<String, String>>>() {});
	}

	public String getScreenshot(String testCaseName, WebDriver driver) throws IOException {
		TakesScreenshot ts = (TakesScreenshot) driver;
		File source = ts.getScreenshotAs(OutputType.FILE);
		File file = new File(System.getProperty("user.dir") + "//reports//" + testCaseName + ".png");
		FileUtils.copyFile(source, file);
		return System.getProperty("user.dir") + "//reports//" + testCaseName + ".png";
	}

	@BeforeMethod(alwaysRun = true)
	public LandingPage launchApplication() throws IOException {
		driver = initializeDriver();
		landingPage = new LandingPage(driver);
		landingPage.goTo();
		return landingPage;
	}

	@AfterMethod(alwaysRun = true)
	public void tearDown() {
		if (driver != null) {
			driver.quit();
		}
	}
}
