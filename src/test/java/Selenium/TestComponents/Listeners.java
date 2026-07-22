package Selenium.TestComponents;

import java.io.IOException;

import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import Sel.resources.ExtentReporterNG;

public class Listeners extends BaseTest implements ITestListener 
{
	ExtentTest test;
	ExtentReports extent = ExtentReporterNG. getReportObject();
	ThreadLocal<ExtentTest> extentTest = new ThreadLocal<ExtentTest>();  //Thread safe
	
	@Override
	public void onTestStart(ITestResult result) 
	{	
		test = extent.createTest(result.getMethod().getMethodName());
		extentTest.set(test);  //unique thread id
	}

	@Override
	public void onTestSuccess(ITestResult result) 
	{
		
		extentTest.get().log(Status.PASS,"Test Passed");
	}

	@Override
	public void onTestFailure(ITestResult result) 
	{	
		test.log(Status.FAIL,"Test Failed");
		extentTest.get().fail(result.getThrowable()); 
		// Screenshot, Attach to report
		
		try 
		{
			driver = (WebDriver)result.getTestClass().getRealClass().getDeclaredField("driver").get(result.getInstance());
		}
		catch(Exception e1)
		{
			e1.printStackTrace();
		
		}
		
		String filepath =null;
		try 
		{
			filepath = getScreenshot(result.getMethod().getMethodName(),driver);
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
		extentTest.get().addScreenCaptureFromPath(filepath, result.getMethod().getMethodName());
		
	}

	@Override
	public void onTestSkipped(ITestResult result) {
		
		ITestListener.super.onTestSkipped(result);
	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		
		ITestListener.super.onTestFailedButWithinSuccessPercentage(result);
	}

	@Override
	public void onTestFailedWithTimeout(ITestResult result) {
		
		ITestListener.super.onTestFailedWithTimeout(result);
	}

	@Override
	public void onStart(ITestContext context) {
		
		ITestListener.super.onStart(context);
	}

	@Override
	public void onFinish(ITestContext context) {
		
		extent.flush();
	}
	

}
