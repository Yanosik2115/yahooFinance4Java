package yahoofinance.model.modules;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.Setter;
import yahoofinance.model.common.AbstractQuoteSummaryModule;
import yahoofinance.model.common.FormattedValue;

@Getter
@Setter
public class NetSharePurchaseActivity extends AbstractQuoteSummaryModule<NetSharePurchaseActivity> {
	private Integer maxAge;
	private String period;

	// Buy Activity
	private FormattedValue buyInfoCount;
	private FormattedValue buyInfoShares;
	private FormattedValue buyPercentInsiderShares;

	// Sell Activity
	private FormattedValue sellInfoCount;
	private FormattedValue sellInfoShares;
	private FormattedValue sellPercentInsiderShares;

	// Net Activity
	private FormattedValue netInfoCount;
	private FormattedValue netInfoShares;
	private FormattedValue netPercentInsiderShares;

	// Total Insider Holdings
	private FormattedValue totalInsiderShares;

	@Override
	protected NetSharePurchaseActivity parseInternal(JsonNode node) {
		NetSharePurchaseActivity activity = new NetSharePurchaseActivity();

		activity.setMaxAge(getIntegerValue(node, "maxAge"));
		activity.setPeriod(getStringValue(node, "period"));

		activity.setBuyInfoCount(parseFormattedValue(node.get("buyInfoCount")));
		activity.setBuyInfoShares(parseFormattedValue(node.get("buyInfoShares")));
		activity.setBuyPercentInsiderShares(parseFormattedValue(node.get("buyPercentInsiderShares")));
		activity.setSellInfoCount(parseFormattedValue(node.get("sellInfoCount")));
		activity.setSellInfoShares(parseFormattedValue(node.get("sellInfoShares")));
		activity.setSellPercentInsiderShares(parseFormattedValue(node.get("sellPercentInsiderShares")));
		activity.setNetInfoCount(parseFormattedValue(node.get("netInfoCount")));
		activity.setNetInfoShares(parseFormattedValue(node.get("netInfoShares")));
		activity.setNetPercentInsiderShares(parseFormattedValue(node.get("netPercentInsiderShares")));
		activity.setTotalInsiderShares(parseFormattedValue(node.get("totalInsiderShares")));

		return activity;
	}

	public static NetSharePurchaseActivity fromJson(JsonNode node) {
		return new NetSharePurchaseActivity().parse(node);
	}
}