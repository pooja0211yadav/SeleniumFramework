//167    168
package SeleniumFrameworkPackage.Tests;
import java.io.IOException;
import java.util.List;

import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import Selenium.TestComponents.BaseTest;
import Selenium.TestComponents.Retry;
import selenium.pageobjects.CartPage;
import selenium.pageobjects.CheckoutPage;
import selenium.pageobjects.ConfirmationPage;
import selenium.pageobjects.ProductCatalogue;
public class ErrorValidations extends BaseTest
{
	@Test(groups={"ErrorHandling"},retryAnalyzer=Retry.class)
	
	public void LoginErrorValidation() throws IOException, InterruptedException
	{
		landingPage.loginApplication("javaselenium@gmail.com", "Java1234");
		Assert.assertEquals("Incorrect email or password.",landingPage.getErrorMessage());
	}

	
@Test
	
	public void ProductErrorValidation() throws IOException, InterruptedException
	{
		String productName="ZARA COAT 3";
	
		//LandingPage landingPage = launchApplication();
		
		ProductCatalogue productcatalogue = landingPage.loginApplication("javaselenium@gmail.com", "Java1234");
		List<WebElement> products = productcatalogue.getProductList();		
		productcatalogue.addProductToCart(productName);

		CartPage cartPage = productcatalogue.goToCartPage();
		
		Boolean match = cartPage.VerifyProductDisplay(productName);
		Assert.assertFalse(match);


		
	}

	
	
}
