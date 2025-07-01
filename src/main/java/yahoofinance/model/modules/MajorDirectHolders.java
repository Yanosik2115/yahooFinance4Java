package yahoofinance.model.modules;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.Setter;
import yahoofinance.model.common.AbstractQuoteSummaryModule;
import yahoofinance.model.common.FormattedValue;

import java.util.List;

@Getter
@Setter
public class MajorDirectHolders extends AbstractQuoteSummaryModule<MajorDirectHolders> {
	private List<DirectHolder> holders;
	private Integer maxAge;

	@Override
	protected MajorDirectHolders parseInternal(JsonNode node) {
		MajorDirectHolders majorDirectHolders = new MajorDirectHolders();

		majorDirectHolders.setMaxAge(getIntegerValue(node, "maxAge"));

		if (node.has("holders")) {
			majorDirectHolders.setHolders(parseModuleArray(node.get("holders"), DirectHolder.class));
		}

		return majorDirectHolders;
	}


	public static MajorDirectHolders fromJson(JsonNode node) {
		return new MajorDirectHolders().parse(node);
	}

	@Getter
	@Setter
	public static class DirectHolder extends AbstractQuoteSummaryModule<DirectHolder> {
		private Integer maxAge;

		// Personal Information
		private String name;
		private String relation;
		private String url;

		// Transaction Information
		private FormattedValue transactionDescription;
		private FormattedValue latestTransDate;

		// Position Information
		private FormattedValue positionDirect;
		private FormattedValue positionDirectDate;
		private FormattedValue positionIndirect;
		private FormattedValue positionIndirectDate;

		@Override
		protected DirectHolder parseInternal(JsonNode node) {
			DirectHolder holder = new DirectHolder();

			holder.setMaxAge(getIntegerValue(node, "maxAge"));

			holder.setName(getStringValue(node, "name"));
			holder.setRelation(getStringValue(node, "relation"));
			holder.setUrl(getStringValue(node, "url"));

			holder.setTransactionDescription(parseFormattedValue(node.get("transactionDescription")));
			holder.setLatestTransDate(parseFormattedValue(node.get("latestTransDate")));

			holder.setPositionDirect(parseFormattedValue(node.get("positionDirect")));
			holder.setPositionDirectDate(parseFormattedValue(node.get("positionDirectDate")));
			holder.setPositionIndirect(parseFormattedValue(node.get("positionIndirect")));
			holder.setPositionIndirectDate(parseFormattedValue(node.get("positionIndirectDate")));

			return holder;
		}

		public static DirectHolder fromJson(JsonNode node) {
			return new DirectHolder().parse(node);
		}
	}
}