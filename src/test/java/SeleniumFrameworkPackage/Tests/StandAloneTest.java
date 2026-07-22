//148 to 156
package SeleniumFrameworkPackage.Tests;
import java.time.Duration;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import io.github.bonigarcia.wdm.WebDriverManager;
import selenium.pageobjects.LandingPage;
public class StandAloneTest 
{
	public static void main(String[] args)
	{
		String productName="Zara Coat 3";
		WebDriverManager.chromedriver().setup();
		WebDriver driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));    //implicit wait
		driver.get("https://rahulshettyacademy.com/client/");
		
		LandingPage landingpage = new LandingPage(driver);
		driver.manage().window().maximize();
		
		driver.findElement(By.id("userEmail")).sendKeys("javaselenium@gmail.com");
		driver.findElement(By.id("userPassword")).sendKeys("Java1234");
		driver.findElement(By.id("login")).click();
		
		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(5));  //explicit wait
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".mb-3")));
		List<WebElement> products = driver.findElements(By.cssSelector(".mb-3")); //finding all products from screen
		
	    WebElement prod = products.stream().filter(product -> product.findElement(By.cssSelector("b")).getText().equals("ZARA COAT 3")).findFirst().orElse(null);
		prod.findElement(By.cssSelector(".card-body button:last-of-type")).click();  //clicks on add to cart button. last-of-type will select the last option out of two button(View,AddToCart)

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#toast-container")));  //popup is displayed for confirmation that Product is added to cart
		wait.until(ExpectedConditions.invisibilityOf(driver.findElement(By.cssSelector(".ng-animating"))));  //to wait until that disappears
		driver.findElement(By.cssSelector("[routerlink*='cart']")).click(); //click on cart button
		
		List <WebElement> cardProducts = driver.findElements(By.cssSelector(".cartSection h3"));
		Boolean match = cardProducts.stream().anyMatch(cardProduct->cardProduct.getText().equalsIgnoreCase(productName));
		Assert.assertTrue(match);
		
		driver.findElement(By.cssSelector(".totalRow button")).click();  //click on checkout button
		Actions a = new Actions(driver);
		a.sendKeys(driver.findElement(By.cssSelector("[placeholder='Select Country']")),"india").build().perform();  //entering the country name
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".ta-results")));
		driver.findElement(By.xpath("(//button[contains(@class,'ta-item')])[2]")).click();  //click on India
		
		driver.findElement(By.cssSelector(".action__submit")).click();  // click on submit button
		String confirmMessage = driver.findElement(By.cssSelector(".hero-primary")).getText();
		Assert.assertTrue(confirmMessage.equalsIgnoreCase("Thankyou for the order."));
		//driver.close();
		System.out.println("Success");	
	}

}




/*
stream() method converts this collection into a stream
The filter() method is used to filter out the elements in the stream based on a condition.
product represents an individual WebElement from the products list, and the lambda expression inside filter is the condition that each product needs to satisfy.

.findFirst()
After filtering, findFirst() retrieves the first element in the stream that satisfies the condition defined in the filter.
It returns an Optional<WebElement>, because the stream might be empty (i.e., no products matched the condition).

.orElse(null)
orElse(null) ensures that if the stream does not contain any element that matches the filter, the result will be null instead of an empty Optional.
So, if no product with the name "ZARA COAT 3" is found, prod will be assigned null.
**/