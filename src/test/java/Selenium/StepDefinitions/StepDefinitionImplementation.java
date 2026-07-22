package Selenium.StepDefinitions;

import java.io.IOException;
import java.util.List;

import org.openqa.selenium.WebElement;
import org.testng.Assert;

import Selenium.TestComponents.BaseTest;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import selenium.pageobjects.LandingPage;
import selenium.pageobjects.ProductCatalogue;

public class StepDefinitionImplementation extends BaseTest
{
	public LandingPage landingPage;
	public ProductCatalogue productCatalogue;
	@Given("I landed on Ecommerce Page") 
	public void I_landed_on_Ecommerce_Page() throws IOException
	{
		landingPage =launchApplication();
	}

    @Given ("^Logged in with username (.+) and password (.+ )$")
    public void logged_in_username_and_password(String username,String password)
    {
    	productCatalogue = landingPage.loginApplication(username,password);
    }

    @When("^I add product (.+) to Cart$")
    public void i_add_product_to_cart(String productName) throws InterruptedException
    {
    	List<WebElement> products = productCatalogue.getProductList();
    	productCatalogue.addProductToCart(productName);
    }
    @Then("^\"([^\"]*)\" message is displayed$")
    public void something_message_is_displayed(String strArg1) throws Throwable 
    {
    	Assert.assertEquals(strArg1,  landingPage.getErrorMessage());
    	driver.close();
    }
}