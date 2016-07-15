package phantomjsparser;

import java.time.LocalTime;

public class Session {
	private Movie movie;
	private Cinema cinema;
	private LocalTime time;
	private String format;

	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public Session(Movie movie, Cinema cinema, LocalTime time, String format) {
		this.movie = movie;
		this.cinema = cinema;
		this.time = time;
		this.format = format;
	}
	public Movie getMovie() {
		return movie;
	}
	
	public void setMovie(Movie movie) {
		this.movie = movie;
	}
	
	public Cinema getCinema() {
		return cinema;
	}
	public void setCinema(Cinema cinema) {
		this.cinema = cinema;
	}
	public LocalTime getTime() {
		return time;
	}
	public void setTime(LocalTime time) {
		this.time = time;
	}
	@Override
	public String toString() {
		return movie.getTitle() + " " + cinema.toString() + " " + format + " " + time;
	}
}
