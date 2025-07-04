package yahoofinance.model.market.modules;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.Setter;
import yahoofinance.model.common.AbstractQuoteSummaryModule;
import yahoofinance.model.common.FormattedValue;

import java.util.List;import static yahoofinance.util.Utils.*;


@Getter
@Setter
public class InstitutionOwnership extends AbstractQuoteSummaryModule<InstitutionOwnership> {
	private Integer maxAge;
	private List<InstitutionalHolder> ownershipList;

	@Override
	protected InstitutionOwnership parseInternal(JsonNode node) {
		InstitutionOwnership ownership = new InstitutionOwnership();

		ownership.setMaxAge(getIntegerValue(node, "maxAge"));

		// Parse institutional holders array
		if (node.has("ownershipList")) {
			ownership.setOwnershipList(parseModuleArray(node.get("ownershipList"), InstitutionalHolder.class));
		}

		return ownership;
	}

	/**
	 * Static factory method for cleaner usage
	 */
	public static InstitutionOwnership fromJson(JsonNode node) {
		return new InstitutionOwnership().parse(node);
	}

	@Getter
	@Setter
	public static class InstitutionalHolder extends AbstractQuoteSummaryModule<InstitutionalHolder> {
		private Integer maxAge;

		// Institution Information
		private String organization;
		private FormattedValue reportDate;

		// Ownership Metrics
		private FormattedValue pctHeld;
		private FormattedValue position;
		private FormattedValue value;
		private FormattedValue pctChange;

		@Override
		protected InstitutionalHolder parseInternal(JsonNode node) {
			InstitutionalHolder holder = new InstitutionalHolder();

			holder.setMaxAge(getIntegerValue(node, "maxAge"));

			holder.setOrganization(getStringValue(node, "organization"));
			holder.setReportDate(parseFormattedValue(node.get("reportDate")));

			holder.setPctHeld(parseFormattedValue(node.get("pctHeld")));
			holder.setPosition(parseFormattedValue(node.get("position")));
			holder.setValue(parseFormattedValue(node.get("value")));
			holder.setPctChange(parseFormattedValue(node.get("pctChange")));

			return holder;
		}

		public static InstitutionalHolder fromJson(JsonNode node) {
			return new InstitutionalHolder().parse(node);
		}
	}
}