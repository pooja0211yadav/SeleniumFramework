package Selenium.TestComponents;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
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

		// Cross-platform path separator
		FileInputStream fis = new FileInputStream(
				System.getProperty("user.dir") + "/src/main/java/Sel/resources/GlobalData.properties");
		prop.load(fis);

		// Read browser parameter from Maven (-Dbrowser=chrome) or fallback to GlobalData.properties
		String browserName = System.getProperty("browser") != null ? System.getProperty("browser")
				: prop.getProperty("browser");

		if (browserName.contains("chrome")) {
			WebDriverManager.chromedriver().setup();
			ChromeOptions options = new ChromeOptions();

			// Necessary arguments for running in Docker/Jenkins Linux environment
			if (browserName.contains("headless")) {
				options.addArguments("--headless=new");
			}
			
			options.addArguments("--no-sandbox");
			options.addArguments("--disable-dev-shm-usage");
			options.addArguments("--window-size=1920,1080");

			driver = new ChromeDriver(options);
		} else if (browserName.equalsIgnoreCase("firefox")) {
			// firefox implementation
		} else if (browserName.equalsIgnoreCase("edge")) {
			// edge implementation
		}

		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

		// Maximize browser if running with standard GUI mode
		if (!browserName.contains("headless")) {
			driver.manage().window().maximize();
		}

		return driver;
	}

	public List<HashMap<String, String>> getJsonDataToMap(String filePath) throws IOException {
		// read json to string
		String jsonContent = FileUtils.readFileToString(new File(filePath), StandardCharsets.UTF_8);

		// String to HashMap - Jackson Databind
		ObjectMapper mapper = new ObjectMapper();
		List<HashMap<String, String>> data = mapper.readValue(jsonContent,
				new TypeReference<List<HashMap<String, String>>>() {
				});
		return data;
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
		// Prevents NullPointerException if driver setup fails in @BeforeMethod
		if (driver != null) {
			driver.quit();
		}
	}
}
