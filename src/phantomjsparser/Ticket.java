package phantomjsparser;

public class Ticket extends Event {
	private Session session;
	private int price;
	private int id = 0;
	private static int idCounter = 0;
	
	public Ticket(Session session, int price) {
		this.session = session;
		this.price = price;
		id = idCounter++;
	}
	@Override
	public String toString() {
		return "id: " + id + " " + session + " " +  price;
	}
	public Session getSession() {
		return session;
	}
	public void setSession(Session session) {
		this.session = session;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
}
