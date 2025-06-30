package yahoofinance.model.modules;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.Setter;
import yahoofinance.model.common.AbstractQuoteSummaryModule;
import yahoofinance.model.common.FormattedValue;

@Getter
@Setter
public class MajorHoldersBreakdown extends AbstractQuoteSummaryModule<MajorHoldersBreakdown> {
	private Integer maxAge;

	// Ownership Breakdown Percentages
	private FormattedValue insidersPercentHeld;
	private FormattedValue institutionsPercentHeld;
	private FormattedValue institutionsFloatPercentHeld;

	// Institution Count
	private FormattedValue institutionsCount;

	@Override
	protected MajorHoldersBreakdown parseInternal(JsonNode node) {
		MajorHoldersBreakdown breakdown = new MajorHoldersBreakdown();

		breakdown.setMaxAge(getIntegerValue(node, "maxAge"));

		breakdown.setInsidersPercentHeld(parseFormattedValue(node.get("insidersPercentHeld")));
		breakdown.setInstitutionsPercentHeld(parseFormattedValue(node.get("institutionsPercentHeld")));
		breakdown.setInstitutionsFloatPercentHeld(parseFormattedValue(node.get("institutionsFloatPercentHeld")));

		breakdown.setInstitutionsCount(parseFormattedValue(node.get("institutionsCount")));

		return breakdown;
	}


	public static MajorHoldersBreakdown fromJson(JsonNode node) {
		return new MajorHoldersBreakdown().parse(node);
	}
}
