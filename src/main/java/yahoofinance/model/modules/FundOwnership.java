package yahoofinance.model.modules;

import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.databind.JsonNode;
import yahoofinance.model.common.AbstractQuoteSummaryModule;
import yahoofinance.model.common.FormattedValue;
import java.util.List;

@Getter
@Setter
public class FundOwnership extends AbstractQuoteSummaryModule<FundOwnership> {
	private Integer maxAge;
	private List<FundHolder> ownershipList;

	@Override
	protected FundOwnership parseInternal(JsonNode node) {
		FundOwnership ownership = new FundOwnership();

		ownership.setMaxAge(getIntegerValue(node, "maxAge"));

		if (node.has("ownershipList")) {
			ownership.setOwnershipList(parseModuleArray(node.get("ownershipList"), FundHolder.class));
		}

		return ownership;
	}

	public static FundOwnership fromJson(JsonNode node) {
		return new FundOwnership().parse(node);
	}

	@Getter
	@Setter
	public static class FundHolder extends AbstractQuoteSummaryModule<FundHolder> {
		private Integer maxAge;

		private String organization;
		private FormattedValue reportDate;

		private FormattedValue pctHeld;
		private FormattedValue position;
		private FormattedValue value;
		private FormattedValue pctChange;

		@Override
		protected FundHolder parseInternal(JsonNode node) {
			FundHolder holder = new FundHolder();

			holder.setMaxAge(getIntegerValue(node, "maxAge"));

			holder.setOrganization(getStringValue(node, "organization"));
			holder.setReportDate(parseFormattedValue(node.get("reportDate")));

			holder.setPctHeld(parseFormattedValue(node.get("pctHeld")));
			holder.setPosition(parseFormattedValue(node.get("position")));
			holder.setValue(parseFormattedValue(node.get("value")));
			holder.setPctChange(parseFormattedValue(node.get("pctChange")));

			return holder;
		}

		public static FundHolder fromJson(JsonNode node) {
			return new FundHolder().parse(node);
		}
	}
}