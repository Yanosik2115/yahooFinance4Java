package yahoofinance.model.market.modules;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.Setter;
import yahoofinance.model.common.AbstractQuoteSummaryModule;

import java.util.List;
import static yahoofinance.util.Utils.*;

@Getter
@Setter
public class SecFilings extends AbstractQuoteSummaryModule<SecFilings> {
	private List<Filing> filings;
	private Integer maxAge;

	@Override
	protected SecFilings parseInternal(JsonNode node) {
		SecFilings secFilings = new SecFilings();

		secFilings.setMaxAge(getIntegerValue(node, "maxAge"));

		if (node.has("filings")) {
			secFilings.setFilings(parseModuleArray(node.get("filings"), Filing.class));
		}

		return secFilings;
	}

	public static SecFilings fromJson(JsonNode node) {
		return new SecFilings().parse(node);
	}

	@Getter
	@Setter
	public static class Filing extends AbstractQuoteSummaryModule<Filing> {
		private Integer maxAge;

		// Filing Information
		private String date;
		private Long epochDate;
		private String type;
		private String title;
		private String edgarUrl;

		// Associated Exhibits
		private List<Exhibit> exhibits;

		@Override
		protected Filing parseInternal(JsonNode node) {
			Filing filing = new Filing();

			filing.setMaxAge(getIntegerValue(node, "maxAge"));

			filing.setDate(getStringValue(node, "date"));
			filing.setEpochDate(getLongValue(node, "epochDate"));
			filing.setType(getStringValue(node, "type"));
			filing.setTitle(getStringValue(node, "title"));
			filing.setEdgarUrl(getStringValue(node, "edgarUrl"));

			if (node.has("exhibits")) {
				filing.setExhibits(parseModuleArray(node.get("exhibits"), Exhibit.class));
			}

			return filing;
		}

		public static Filing fromJson(JsonNode node) {
			return new Filing().parse(node);
		}
	}

	@Getter
	@Setter
	public static class Exhibit extends AbstractQuoteSummaryModule<Exhibit> {
		private String type;
		private String url;
		private String downloadUrl;

		@Override
		protected Exhibit parseInternal(JsonNode node) {
			Exhibit exhibit = new Exhibit();

			exhibit.setType(getStringValue(node, "type"));
			exhibit.setUrl(getStringValue(node, "url"));
			exhibit.setDownloadUrl(getStringValue(node, "downloadUrl"));

			return exhibit;
		}

		public static Exhibit fromJson(JsonNode node) {
			return new Exhibit().parse(node);
		}
	}
}