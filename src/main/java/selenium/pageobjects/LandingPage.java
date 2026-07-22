//157 to 164
//need to see
package selenium.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import selenium.AbstractComponents.AbstractComponent;

public class LandingPage extends AbstractComponent
{
	WebDriver driver;
	
	public LandingPage(WebDriver driver)
	{
		//constructor will be executed before any other method 
		//initialization is good to be done in constructor
		super(driver); //From child class constructor u r sending driver to parent class constructor
		this.driver=driver;
		PageFactory.initElements(driver,this); //page factory driver
 
		//go and initialize all the elements. When initializing it needs driver? It takes driver as argument and uses this to initialize
	}
	

	//Page Factory

	//at run time the above code will be constructed like as shown below:
	//WebElement userEmail = driver.findElement(By.id("userEmail"));

	
	@FindBy(id="userEmail")
	WebElement userEmail;
	
	@FindBy(id="userPassword")
	WebElement passwordEle;
	
	@FindBy(id="login")
	WebElement submit;
	
	@FindBy(css="[class*='flyInOut']")
	WebElement errorMessage;
	
	public ProductCatalogue loginApplication(String email, String password)
	{
		userEmail.sendKeys(email);
		passwordEle.sendKeys(password);
		submit.click();
		ProductCatalogue productcatalogue = new ProductCatalogue(driver);
		return productcatalogue;
	}
	public String getErrorMessage()
	{
		waitForWebElementToAppear(errorMessage);
		return errorMessage.getText();
	}
	
	public void goTo()
	{
		driver.get("https://rahulshettyacademy.com/client/");

	}
	
}
