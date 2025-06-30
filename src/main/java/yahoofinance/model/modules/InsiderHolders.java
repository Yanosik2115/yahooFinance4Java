package yahoofinance.model.modules;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.Setter;
import yahoofinance.model.StockQuoteSummary;
import yahoofinance.model.common.AbstractQuoteSummaryModule;
import yahoofinance.model.common.FormattedValue;

import java.util.List;

@Getter
@Setter
public class InsiderHolders extends AbstractQuoteSummaryModule<InsiderHolders> {
	private List<InsiderHolder> holders;
	private Integer maxAge;

	@Override
	protected InsiderHolders parseInternal(JsonNode node) {
		InsiderHolders insiderHolders = new InsiderHolders();

		insiderHolders.setMaxAge(getIntegerValue(node, "maxAge"));

		if (node.has("holders")) {
			insiderHolders.setHolders(parseModuleArray(node.get("holders"), InsiderHolder.class));
		}

		return insiderHolders;
	}


	public static InsiderHolders fromJson(JsonNode node) {
		return new InsiderHolders().parse(node);
	}

	@Getter
	@Setter
	public static class InsiderHolder extends AbstractQuoteSummaryModule<InsiderHolder> {
		private Integer maxAge;

		// Personal Information
		private String name;
		private String relation;
		private String url;

		// Transaction Information
		private String transactionDescription;
		private FormattedValue latestTransDate;

		// Position Information
		private FormattedValue positionDirect;
		private FormattedValue positionDirectDate;
		private FormattedValue positionIndirect;
		private FormattedValue positionIndirectDate;

		@Override
		protected InsiderHolder parseInternal(JsonNode node) {
			InsiderHolder holder = new InsiderHolder();

			holder.setMaxAge(getIntegerValue(node, "maxAge"));

			holder.setName(getStringValue(node, "name"));
			holder.setRelation(getStringValue(node, "relation"));
			holder.setUrl(getStringValue(node, "url"));

			holder.setTransactionDescription(getStringValue(node, "transactionDescription"));
			holder.setLatestTransDate(parseFormattedValue(node.get("latestTransDate")));

			holder.setPositionDirect(parseFormattedValue(node.get("positionDirect")));
			holder.setPositionDirectDate(parseFormattedValue(node.get("positionDirectDate")));
			holder.setPositionIndirect(parseFormattedValue(node.get("positionIndirect")));
			holder.setPositionIndirectDate(parseFormattedValue(node.get("positionIndirectDate")));

			return holder;
		}

		public static InsiderHolder fromJson(JsonNode node) {
			return new InsiderHolder().parse(node);
		}
	}
}