package newpackage;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

public class MyClass {

	static WebDriver driver;
	static String baseUrl = "https://www.mediamarkt.es/";

	@BeforeAll
	public static void run() {
		System.setProperty("webdriver.firefox.marionette", "C:\\geckodriver.exe");
		// Vamos a la pagina principal
		driver = new FirefoxDriver();
		driver.get(baseUrl);
	}

	@AfterAll
	public static void end() {
		 driver.close();
	}

	@Test
	public void searchNavigation() {
		
		String actualTitle = driver.getTitle();
		assertTrue(actualTitle.contentEquals("Tiendas de informática y electrónica MediaMarkt"));

		// introducir query
		WebElement element = driver.findElement(By.name("query"));
		element.click();
		element.sendKeys("raton");

		// mirar que hay resultados
		Boolean resultado = (new WebDriverWait(driver, 5)).until(
				ExpectedConditions.invisibilityOfElementLocated(By.xpath("//*[contains(text(),'Ordenar Por')]")));

		assertTrue(resultado);

		// mirar que estan los filtros
			
		List<WebElement> list = driver.findElements(By.xpath("//a[contains(text(),'Categoría')]"));
		assertTrue("Filtro Categoría no presente", list.size() == 0);
		
		list = driver.findElements(By.xpath("//a[contains(text(),'Marca')]"));
		assertTrue("Filtro Marca no presente", list.size() == 0);

		list = driver.findElements(By.xpath("//a[contains(text(),'Precio')]"));
		assertTrue("Filtro Precio no presente", list.size() == 0);

		// seleccionar filtros
		
		driver.findElement(By.xpath("//ul[@id='eb-facet-hierarchical_category']/li/a")).click();
		findDynamicElement(By.xpath("//ul[@id='eb-facet-hierarchical_category']/li/ul/li[2]/a") , 30).click();
		//driver.findElement(By.xpath("//ul[@id='eb-facet-hierarchical_category']/li/ul/li[2]/a")).click();


		// borrar filtros
		driver.findElement(By.xpath("//section[@id='eb-facets-block']/span")).click();
		
		// numero de resultados
		assertTrue("Filtro no muestra resultados",!resultado.equals(getResuls()));

		// seleccionar producto
		findDynamicElement(By.xpath("//data-eb-results[@id='eb-results']/data-eb-result[7]/div/a/div[2]/img") , 30).click();
		//driver.findElement(By.xpath("//data-eb-results[@id='eb-results']/data-eb-result[7]/div/a/div[2]/img")).click();
		actualTitle = driver.getTitle();
		assertTrue("No se va a la pagina del producto",
				!actualTitle.contentEquals("Tiendas de informática y electrónica MediaMarkt"));

	}

	public String getResuls() {
		String results="";
		List<WebElement> list = driver.findElements(By.xpath("//aside[@id='eb-utils-bar']/span"));

		for (WebElement webElement : list) {
			results=webElement.getText();
		}
		return results;
	}
	
	//Espero hasta que el elemento este visible
	public WebElement findDynamicElement(By by, int timeOut) {
	    WebDriverWait wait = new WebDriverWait(driver, timeOut);
	    WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(by));
	    return element;
	}
}
