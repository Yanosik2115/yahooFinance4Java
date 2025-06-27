package yahoofinance.util;

import lombok.Getter;

import java.time.LocalDateTime;

public class Crumb {
	@Getter
	String value;
	LocalDateTime expire;

	public final boolean isExpired() {
		if (value == null || value.trim().isEmpty()) {
			return true;
		}

		if (expire == null) {
			return true;
		}

		return LocalDateTime.now().isAfter(expire);
	}

	public final boolean isValid() {
		return !isExpired();
	}


	/**
	 * Sets the crumb value and expiration time
	 * @param value the crumb value
	 * @param expire the expiration time
	 */
	public void setValue(String value, LocalDateTime expire) {
		this.value = value;
		this.expire = expire;
	}

	/**
	 * Clears the crumb value and expiration
	 */
	public void clear() {
		this.value = null;
		this.expire = null;
	}

	@Override
	public String toString() {
		return String.format("Crumb{value='%s', expire=%s, isValid=%s}",
				value, expire, isValid());
	}
}