package phantomjsparser;

import java.io.File;
import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AfishaYandexMovieParser implements Parser<Ticket> {
	private final String phantombinary = "/home/shest/eclipse/phantomjs-2.1.1-linux-x86_64/bin/phantomjs";

	private static final String URL = "https://afisha.yandex.ru/events?city=saint-petersburg&tag=cinema&preset=today";

	private WebDriver driver = setDriver();
	private WebDriver ticketFrame = setDriver();

	private WebDriverWait wait = new WebDriverWait(driver, 30);
	private WebDriverWait ticketWait = new WebDriverWait(ticketFrame, 30);

	@Override
	public List<Ticket> parse() {
		List<WebElement> elements = getMovieElements();
		List<Ticket> tickets = new ArrayList<Ticket>();

		// parse for tickets
		for (WebElement el : elements) {
			Movie movie = makeMovie(el);
			if (!movie.hasTickets())
				continue;

			waitAndClick(el.findElement(By.cssSelector("a")));

			Collections.addAll(tickets, getTickets(movie).toArray(new Ticket[0]));

		}

		return tickets;
	}

	public void close() {
		driver.quit();
		ticketFrame.quit();
	}

	private void waitAndClick(WebElement el) {
		while (true) {
			try {
				el.click();
				return;
			} catch (Exception ex) {
			}
		}
	}

	private List<WebElement> getMovieElements() {
		driver.get(URL);
		openAllMovies();
		return driver.findElements(Selectors.MOVIE_ELEMENTS.getSelector());
	}

	private boolean hasMoreButton() {
		wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(Selectors.MORE_MOVIE_BUTTON.getSelector()));
		List<WebElement> buttons = driver.findElements(Selectors.MORE_MOVIE_BUTTON.getSelector());
		assert buttons.size() == 1;
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return buttons.get(0).isDisplayed();
	}

	private WebElement getMoreButton() {
		return driver.findElement(Selectors.MORE_MOVIE_BUTTON.getSelector());
	}

	private void openAllMovies() {

		while (hasMoreButton()) {
			WebElement b = getMoreButton();
			b.click();
		}
	}

	private Movie makeMovie(WebElement el) {
		Movie movie = new Movie();

		movie.setTitle(getTitle(el));
		movie.setGenre(getGenre(el));
		movie.setReview(getShortReview(el));
		movie.setRate(getRate(el));
		movie.hasTickets(getHasTicket(el));

		// TODO parse Image

		return movie;
	}

	private String getTitle(WebElement el) {
		List<WebElement> title = el.findElements(Selectors.MOVIE_TITLE.getSelector());
		assert title.size() <= 1;
		return title.get(0).getAttribute("innerText");
	}

	private String getGenre(WebElement el) {
		List<WebElement> genre = el.findElements(Selectors.GENRE.getSelector());
		assert genre.size() <= 1;
		return genre.isEmpty() ? "" : genre.get(0).getAttribute("innerText");
	}

	private String getShortReview(WebElement el) {
		List<WebElement> review = el.findElements(Selectors.SHORT_REVIEW.getSelector());
		assert review.size() <= 1;
		return review.isEmpty() ? "" : review.get(0).getAttribute("innerText");
	}

	private Float getRate(WebElement el) {
		List<WebElement> rate = el.findElements(Selectors.RATE.getSelector());
		assert rate.size() <= 1;

		return rate.isEmpty() ? null : Float.parseFloat(rate.get(0).getAttribute("innerText"));
	}

	private boolean getHasTicket(WebElement el) {
		List<WebElement> hasTickets = el.findElements(Selectors.AVAILABLE_TICKETS.getSelector());
		assert hasTickets.size() <= 1;
		return hasTickets.isEmpty() ? false : true;
	}

	private List<Ticket> getTickets(Movie movie) {
		setMovie(movie);
		List<Ticket> tickets = new ArrayList<Ticket>();

		// select time buttons
		List<WebElement> btn = getSessionButtons();
		if (btn.isEmpty()) {
			closePopup();
			return tickets;
		}
		// go to movie information frame
		gotoTicketFrame(btn.get(0));
		// select all movie theaters for the movie
		List<WebElement> cinemas = getSessionItems();

		assert !cinemas.isEmpty();

		for (WebElement cinemaEl : cinemas) {
			// make Movie theater
			String name = cinemaEl.findElement(Selectors.CINEMA_NAME.getSelector()).getAttribute("innerText");
			Cinema theater = new Cinema(name);

			// select all formats for the movie theater
			List<WebElement> ticketItems = getTicketItems(cinemaEl);
			assert !ticketItems.isEmpty();
			Collections.addAll(tickets, addTickets(ticketItems, movie, theater).toArray(new Ticket[0]));
		}
		gotoMainWindow();
		return tickets;
	}

	private void closeInnerFrame() {
		List<WebElement> closeBtn = driver.findElements(Selectors.FRAME_CLOSER.getSelector());
		assert !closeBtn.isEmpty();
		waitAndClick(closeBtn.get(0));
		driver.switchTo().defaultContent();
	}

	private void closePopup() {
		wait.until(ExpectedConditions.presenceOfElementLocated(Selectors.POPUP_CLOSER.getSelector()));
		List<WebElement> closeBtn = driver.findElements(Selectors.POPUP_CLOSER.getSelector());
		assert closeBtn.size() == 1;
		waitAndClick(closeBtn.get(0));
	}

	private void gotoMainWindow() {
		// closeInnerFrame();
		// closePopup();
		// takeScreenshot("mainWindow");
	}

	private Integer getRuntime() {
		List<WebElement> elements = driver.findElements(Selectors.RUNTIME.getSelector());
		if (elements.isEmpty())
			return null;
		assert elements.size() == 1;

		Scanner in = new Scanner(elements.get(0).getAttribute("innerText"));
		try {
			return in.nextInt();
		} finally {
			in.close();
		}
	}

	private void setMovie(Movie movie) {
		wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(Selectors.POPUP_HEADING.getSelector()));
		movie.setRuntime(getRuntime());
		// TODO add Country, year, director etc

	}

	private void gotoTicketFrame(WebElement button) {

		button.findElement(By.cssSelector("li")).click();

		List<WebElement> frames = driver.findElements(By.tagName("iframe"));
		for (WebElement frame : frames) {
			if (frame.getAttribute("outerHTML").contains("src=\"https://widget.tickets.yandex.ru")) {
				ticketFrame.get(frame.getAttribute("src"));
				break;
			}
		}

		ticketWait.until(
				ExpectedConditions.presenceOfAllElementsLocatedBy(Selectors.TICKET_FRAME_BACK_BUTTON.getSelector()));

		List<WebElement> backButton = ticketFrame.findElements(Selectors.TICKET_FRAME_BACK_BUTTON.getSelector());
		assert backButton.size() == 1;
		waitAndClick(backButton.get(0));
	}

	private void takeScreenshot(String name) {
		File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		try {
			FileUtils.copyFile(scrFile, new File("/home/shest/eclipse/" + name + ".png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private List<WebElement> getSessionButtons() {
		return driver.findElements(Selectors.TICKET_FRAME_BUTTON.getSelector());
	}

	private List<WebElement> getSessionItems() {
		ticketWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(Selectors.SESSIONS.getSelector()));
		return ticketFrame.findElements(Selectors.SESSIONS.getSelector());
	}

	private List<WebElement> getTicketItems(WebElement el) {
		return el.findElements(Selectors.TICKETS.getSelector());
	}

	private String getFormat(WebElement el) {
		List<WebElement> formats = el.findElements(Selectors.TICKET_FORMAT.getSelector());
		assert formats.size() == 1;
		return formats.get(0).getAttribute("innerText");
	}

	private List<Integer> parsePrices(String prices) {
		Scanner s = new Scanner(prices);
		Scanner in = s.useDelimiter(",");
		try {
			List<Integer> p = new ArrayList<Integer>();
			while (in.hasNextInt()) {
				p.add(in.nextInt());
			}
			return p;

		} finally {
			s.close();
			in.close();
		}
	}

	private List<Ticket> addTickets(List<WebElement> ticketItems, Movie movie, Cinema theater) {
		List<Ticket> tickets = new ArrayList<Ticket>();
		for (WebElement ticketItem : ticketItems) {

			String format = getFormat(ticketItem);

			List<WebElement> times = ticketItem.findElements(Selectors.TICKET_TIME.getSelector());
			assert !times.isEmpty();

			for (WebElement timeButton : times) {
				LocalTime time = LocalTime.parse(timeButton.getAttribute("innerText"));

				Session session = new Session(movie, theater, time, format);

				String priceStr = timeButton.getAttribute("data-prices");
				if (priceStr == null)
					continue;

				List<Integer> prices = parsePrices(priceStr);
				for (int price : prices) {
					tickets.add(new Ticket(session, price));
				}
			}
		}
		return tickets;
	}

	private WebDriver setDriver() {
		DesiredCapabilities caps = new DesiredCapabilities();
		caps.setJavascriptEnabled(true);
		caps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, phantombinary);
		WebDriver driver = new PhantomJSDriver(caps);
		driver.manage().window().setSize(new Dimension(1280, 1024));
		return driver;
	}

	public static void main(String[] args) {
		AfishaYandexMovieParser parser = new AfishaYandexMovieParser();
		try {
			List<Ticket> movies = parser.parse();
			for (Ticket m : movies) {
				System.out.println(m);
			}
		} finally {
			parser.close();
		}
		// System.out.println(movies.get(0).getSession().getMovie());
	}
}
