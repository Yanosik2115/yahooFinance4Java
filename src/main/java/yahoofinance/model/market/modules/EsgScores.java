package yahoofinance.model.market.modules;

import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.databind.JsonNode;
import yahoofinance.model.common.AbstractQuoteSummaryModule;
import yahoofinance.model.common.FormattedValue;
import java.util.ArrayList;
import java.util.List;
import static yahoofinance.util.Utils.*;

@Getter
@Setter
public class EsgScores extends AbstractQuoteSummaryModule<EsgScores> {
	private Integer maxAge;

	// Core ESG Scores
	private FormattedValue totalEsg;
	private FormattedValue environmentScore;
	private FormattedValue socialScore;
	private FormattedValue governanceScore;

	// Rating Information
	private Integer ratingYear;
	private Integer ratingMonth;
	private Double highestControversy;
	private Integer peerCount;
	private String esgPerformance;
	private String peerGroup;
	private List<String> relatedControversy;

	// Peer Performance Comparisons
	private PeerPerformance peerEsgScorePerformance;
	private PeerPerformance peerEnvironmentPerformance;
	private PeerPerformance peerSocialPerformance;
	private PeerPerformance peerGovernancePerformance;
	private PeerPerformance peerHighestControversyPerformance;

	// Percentile Rankings
	private Double percentile;
	private Double environmentPercentile;
	private Double socialPercentile;
	private Double governancePercentile;

	// Industry Involvement Flags
	private Boolean adult;
	private Boolean alcoholic;
	private Boolean animalTesting;
	private Boolean catholic;
	private Boolean controversialWeapons;
	private Boolean smallArms;
	private Boolean furLeather;
	private Boolean gambling;
	private Boolean gmo;
	private Boolean militaryContract;
	private Boolean nuclear;
	private Boolean pesticides;
	private Boolean palmOil;
	private Boolean coal;
	private Boolean tobacco;

	@Override
	protected EsgScores parseInternal(JsonNode node) {
		EsgScores scores = new EsgScores();

		scores.setMaxAge(getIntegerValue(node, "maxAge"));

		scores.setTotalEsg(parseFormattedValue(node.get("totalEsg")));
		scores.setEnvironmentScore(parseFormattedValue(node.get("environmentScore")));
		scores.setSocialScore(parseFormattedValue(node.get("socialScore")));
		scores.setGovernanceScore(parseFormattedValue(node.get("governanceScore")));

		scores.setRatingYear(getIntegerValue(node, "ratingYear"));
		scores.setRatingMonth(getIntegerValue(node, "ratingMonth"));
		scores.setHighestControversy(getDoubleValue(node, "highestControversy"));
		scores.setPeerCount(getIntegerValue(node, "peerCount"));
		scores.setEsgPerformance(getStringValue(node, "esgPerformance"));
		scores.setPeerGroup(getStringValue(node, "peerGroup"));

		scores.setRelatedControversy(parseStringArray(node.get("relatedControversy")));

		if (node.has("peerEsgScorePerformance")) {
			scores.setPeerEsgScorePerformance(PeerPerformance.fromJson(node.get("peerEsgScorePerformance")));
		}
		if (node.has("peerEnvironmentPerformance")) {
			scores.setPeerEnvironmentPerformance(PeerPerformance.fromJson(node.get("peerEnvironmentPerformance")));
		}
		if (node.has("peerSocialPerformance")) {
			scores.setPeerSocialPerformance(PeerPerformance.fromJson(node.get("peerSocialPerformance")));
		}
		if (node.has("peerGovernancePerformance")) {
			scores.setPeerGovernancePerformance(PeerPerformance.fromJson(node.get("peerGovernancePerformance")));
		}
		if (node.has("peerHighestControversyPerformance")) {
			scores.setPeerHighestControversyPerformance(PeerPerformance.fromJson(node.get("peerHighestControversyPerformance")));
		}

		scores.setPercentile(getDoubleValue(node, "percentile"));
		scores.setEnvironmentPercentile(getDoubleValue(node, "environmentPercentile"));
		scores.setSocialPercentile(getDoubleValue(node, "socialPercentile"));
		scores.setGovernancePercentile(getDoubleValue(node, "governancePercentile"));

		scores.setAdult(getBooleanValue(node, "adult"));
		scores.setAlcoholic(getBooleanValue(node, "alcoholic"));
		scores.setAnimalTesting(getBooleanValue(node, "animalTesting"));
		scores.setCatholic(getBooleanValue(node, "catholic"));
		scores.setControversialWeapons(getBooleanValue(node, "controversialWeapons"));
		scores.setSmallArms(getBooleanValue(node, "smallArms"));
		scores.setFurLeather(getBooleanValue(node, "furLeather"));
		scores.setGambling(getBooleanValue(node, "gambling"));
		scores.setGmo(getBooleanValue(node, "gmo"));
		scores.setMilitaryContract(getBooleanValue(node, "militaryContract"));
		scores.setNuclear(getBooleanValue(node, "nuclear"));
		scores.setPesticides(getBooleanValue(node, "pesticides"));
		scores.setPalmOil(getBooleanValue(node, "palmOil"));
		scores.setCoal(getBooleanValue(node, "coal"));
		scores.setTobacco(getBooleanValue(node, "tobacco"));

		return scores;
	}

	private List<String> parseStringArray(JsonNode arrayNode) {
		List<String> strings = new ArrayList<>();

		if (arrayNode != null && arrayNode.isArray()) {
			for (JsonNode stringNode : arrayNode) {
				if (!stringNode.isNull()) {
					strings.add(stringNode.asText());
				}
			}
		}

		return strings;
	}

	public static EsgScores fromJson(JsonNode node) {
		return new EsgScores().parse(node);
	}

	@Getter
	@Setter
	public static class PeerPerformance extends AbstractQuoteSummaryModule<PeerPerformance> {
		private Double min;
		private Double avg;
		private Double max;

		@Override
		protected PeerPerformance parseInternal(JsonNode node) {
			PeerPerformance performance = new PeerPerformance();

			performance.setMin(getDoubleValue(node, "min"));
			performance.setAvg(getDoubleValue(node, "avg"));
			performance.setMax(getDoubleValue(node, "max"));

			return performance;
		}

		public static PeerPerformance fromJson(JsonNode node) {
			return new PeerPerformance().parse(node);
		}
	}
}