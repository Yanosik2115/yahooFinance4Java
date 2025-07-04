package yahoofinance.model.market.modules;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.Setter;
import yahoofinance.model.common.AbstractQuoteSummaryModule;

import java.util.List;
import static yahoofinance.util.Utils.*;

@Getter
@Setter
public class UpgradeDowngradeHistory extends AbstractQuoteSummaryModule<UpgradeDowngradeHistory> {
	private List<AnalystRating> history;
	private Integer maxAge;

	@Override
	protected UpgradeDowngradeHistory parseInternal(JsonNode node) {
		UpgradeDowngradeHistory upgradeDowngradeHistory = new UpgradeDowngradeHistory();

		upgradeDowngradeHistory.setMaxAge(getIntegerValue(node, "maxAge"));

		if (node.has("history")) {
			upgradeDowngradeHistory.setHistory(parseModuleArray(node.get("history"), AnalystRating.class));
		}

		return upgradeDowngradeHistory;
	}

	public static UpgradeDowngradeHistory fromJson(JsonNode node) {
		return new UpgradeDowngradeHistory().parse(node);
	}

	@Getter
	@Setter
	public static class AnalystRating extends AbstractQuoteSummaryModule<AnalystRating> {
		// Rating Change Information
		private Long epochGradeDate;
		private String firm;
		private String fromGrade;
		private String toGrade;
		private String action;

		// Price Target Information
		private String priceTargetAction;
		private Double currentPriceTarget;

		@Override
		protected AnalystRating parseInternal(JsonNode node) {
			AnalystRating rating = new AnalystRating();

			rating.setEpochGradeDate(getLongValue(node, "epochGradeDate"));
			rating.setFirm(getStringValue(node, "firm"));
			rating.setFromGrade(getStringValue(node, "fromGrade"));
			rating.setToGrade(getStringValue(node, "toGrade"));
			rating.setAction(getStringValue(node, "action"));

			rating.setPriceTargetAction(getStringValue(node, "priceTargetAction"));
			rating.setCurrentPriceTarget(getDoubleValue(node, "currentPriceTarget"));

			return rating;
		}

		public static AnalystRating fromJson(JsonNode node) {
			return new AnalystRating().parse(node);
		}

		public boolean isUpgrade() {
			return "up".equalsIgnoreCase(action) ||
			       (action != null && action.toLowerCase().contains("upgrade"));
		}

		public boolean isDowngrade() {
			return "down".equalsIgnoreCase(action) ||
			       (action != null && action.toLowerCase().contains("downgrade"));
		}

		public boolean isInitiation() {
			return "init".equalsIgnoreCase(action) ||
			       (action != null && action.toLowerCase().contains("init"));
		}

		public boolean isPriceTargetRaise() {
			return "up".equalsIgnoreCase(priceTargetAction) ||
			       (priceTargetAction != null && priceTargetAction.toLowerCase().contains("up"));
		}

		public boolean isPriceTargetLower() {
			return "down".equalsIgnoreCase(priceTargetAction) ||
			       (priceTargetAction != null && priceTargetAction.toLowerCase().contains("down"));
		}
	}
}