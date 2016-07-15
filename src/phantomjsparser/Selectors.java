package phantomjsparser;

import org.openqa.selenium.By;

enum Selectors {
	
	TICKET_FRAME_BUTTON(Types.CSS, "span.js-yaticket-button[data-session-id]"),
	SWITCH_TO_TICKET_FRAME(Types.CSS, "iframe[src^='https://widget.tickets.yandex.ru']"),
	TICKET_FRAME_BACK_BUTTON(Types.CSS, "div.hallplan-footer_back b"),
	MOVIE_ELEMENTS(Types.CSS, "div[class^='event'][itemtype='https://schema.org/Event']"),
	MORE_MOVIE_BUTTON(Types.CSS, "div.events-list__more > a[class^='button']"),
	MOVIE_TITLE(Types.CSS, "h2.event__name"),
	GENRE(Types.CSS, "div.event__formats"),
	SHORT_REVIEW(Types.CSS, "div.event__argument"),
	RATE(Types.CSS, "div.event-rating__value"),
	AVAILABLE_TICKETS(Types.CSS, "div.event__tickets"),
	CINEMA_NAME(Types.CSS, "div.session-item-caption > span.session-item-caption_title"),
	FRAME_CLOSER(Types.CSS, "div.container-slide_controls > b.frame-closer"),
	POPUP_CLOSER(Types.CSS, "div[class^='popup'] i.popup__close"),
	POPUP_HEADING(Types.CSS, "div.event-heading"),
	SESSIONS(Types.CSS, "div.session-item"),
	TICKETS(Types.CSS, "li.session-item-list_group"),
	TICKET_FORMAT(Types.CSS, "span[class$='format-inner'] span[data-reactid$='0']"),
	TICKET_TIME(Types.CSS, "b.button"),
	RUNTIME(Types.XPATH, "//*[contains(text(), 'Время')]/../dd");
	

	private String query;
	private Types type;

	Selectors(Types type, String query) {
		this.query = query;
		this.type = type;
	}
	
	private enum Types {
		CSS,
		XPATH;
	}
	
	String asString() {
		return query;
	}

	By getSelector() {
		switch (type) {
		case CSS:
			return By.cssSelector(query);
		case XPATH:
		default:
			return By.xpath(query);
		}
	}
}

