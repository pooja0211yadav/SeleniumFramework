//157 to 164  ,    169 , 171, 172, 173, 174
package SeleniumFrameworkPackage.Tests;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.AssertJUnit;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import Selenium.TestComponents.BaseTest;
import io.github.bonigarcia.wdm.WebDriverManager;
import selenium.pageobjects.CartPage;
import selenium.pageobjects.CheckoutPage;
import selenium.pageobjects.ConfirmationPage;
import selenium.pageobjects.LandingPage;
import selenium.pageobjects.OrderPage;
import selenium.pageobjects.ProductCatalogue;

public class SubmitOrderTest2 extends BaseTest {
	String productName = "ZARA COAT 3";

	@Test(dataProvider = "getData", groups = { "Purchase" })
	//public void submitOrder(String email, String password, String productName) throws IOException, InterruptedException
	 public void submitOrder(HashMap<String,String> input) throws IOException
	// InterruptedException

	{
		String product = "ZARA COAT 3";

		//ProductCatalogue productcatalogue = landingPage.loginApplication("javaselenium@gmail.com", "Java1234");

		ProductCatalogue productcatalogue =  landingPage.loginApplication(input.get("email"), input.get("password"));
		List<WebElement> products = productcatalogue.getProductList();
		// productcatalogue.addProductToCart(input.get("product"));

		//productcatalogue.addProductToCart(product);
		CartPage cartPage = productcatalogue.goToCartPage();

		Boolean match = cartPage.VerifyProductDisplay(input.get("product"));

		//Boolean match = cartPage.VerifyProductDisplay(product);
		Assert.assertTrue(match);
		CheckoutPage checkoutPage = cartPage.goToCheckout();
		checkoutPage.selectCountry("india");
		ConfirmationPage confirmationPage = checkoutPage.submitOrder();

		String confirmMessage = confirmationPage.getConfirmationMessage();
		Assert.assertTrue(confirmMessage.equalsIgnoreCase("Thankyou for the order."));

	}

	@Test(dependsOnMethods = { "submitOrder" })
	public void OrderHistoryTest() {
		ProductCatalogue productcatalogue = landingPage.loginApplication("javaselenium@gmail.com", "Java1234");
		OrderPage orderspage = productcatalogue.goToOrdersPage();
		Assert.assertTrue(orderspage.VerifyorderDisplay(productName));
	}

//	@DataProvider
//	public Object[][] getData() throws IOException
//	{
////		HashMap<String,String> map = new HashMap<String,String>();
////		map.put("email", "javaselenium@gmail.com");
////		map.put("password", "Java1234");
////		map.put("product", "ZARA COAT 3");
////		
////		HashMap<String,String> map1 = new HashMap<String,String>();
////		map1.put("email", "selenium@gmail.com");
////		map1.put("password", "abc@123");
////		map1.put("product", "ADIDAS ORIGINAL");
//		
//		
//		List<HashMap<String, String>> data = getJsonDataToMap(System.getProperty("user.dir")+"\\src\\test\\java\\Selenium\\data\\PurchaseOrder.json");
//		return new Object[][] {{data.get(0)},{data.get(1)}};
//	}

	@DataProvider
	public Object[][] getData() {
		return new Object[][] { { "javaselenium@gmail.com", "Java1234", "ZARA COAT 3" },
				{ "selenium@gmail.com", "abc@123", "ADIDAS ORIGINAL" } };
	}

}
