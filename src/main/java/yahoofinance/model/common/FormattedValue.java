package yahoofinance.model.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FormattedValue {
	private BigDecimal raw;
	private String fmt;
	private String longFmt;

	@Override
	public String toString() {
		return fmt != null ? fmt : (raw != null ? raw.toString() : "null");
	}
}