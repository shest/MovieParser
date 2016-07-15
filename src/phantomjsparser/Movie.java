package phantomjsparser;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

public class Movie {
	private String title;
	private String genre;
	private String review;
	private Integer runtime;
	private Float rate;
	private boolean hasTickets;
	private String description;

	public boolean isHasTickets() {
		return hasTickets;
	}

	public void setHasTickets(boolean hasTickets) {
		this.hasTickets = hasTickets;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDate getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(LocalDate releaseDate) {
		this.releaseDate = releaseDate;
	}

	public String getDirector() {
		return Director;
	}

	public void setDirector(String director) {
		Director = director;
	}

	public List<String> getActors() {
		return Actors;
	}

	public void setActors(List<String> actors) {
		Actors = actors;
	}

	public String getCountry() {
		return Country;
	}

	public void setCountry(String country) {
		Country = country;
	}

	public LocalDate getProductionYear() {
		return productionYear;
	}

	public void setProductionYear(LocalDate productionYear) {
		this.productionYear = productionYear;
	}

	public String getOriginalTitle() {
		return OriginalTitle;
	}

	public void setOriginalTitle(String originalTitle) {
		OriginalTitle = originalTitle;
	}

	private LocalDate releaseDate;
	private String Director;
	private List<String> Actors = new LinkedList<String>();
	private String Country;
	private LocalDate productionYear;
	private String OriginalTitle;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public String getReview() {
		return review;
	}

	public void setReview(String review) {
		this.review = review;
	}

	public Integer getRuntime() {
		return runtime;
	}

	public void setRuntime(Integer runtime) {
		this.runtime = runtime;
	}

	public float getRate() {
		return rate;
	}

	public void setRate(Float rate) {
		this.rate = rate;
	}

	public boolean hasTickets() {
		return hasTickets;
	}

	public void hasTickets(boolean hasTickets) {
		this.hasTickets = hasTickets;
	}

	@Override
	public String toString() {
		return "Title: " + title + "\nGenre: " + genre + "\nShortReview: " + 
				review + "\nRate: " + rate + "\nRuntime: " + runtime + " мин.";
	}

}
