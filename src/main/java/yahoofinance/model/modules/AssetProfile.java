package yahoofinance.model.modules;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.Setter;
import yahoofinance.model.common.AbstractQuoteSummaryModule;
import yahoofinance.model.common.CompanyOfficer;

import java.util.List;

@Getter
@Setter
public class AssetProfile extends AbstractQuoteSummaryModule<AssetProfile> {
	private String address1;
	private String city;
	private String state;
	private String zip;
	private String country;
	private String phone;
	private String website;
	private String industry;
	private String industryKey;
	private String industryDisp;
	private String sector;
	private String sectorKey;
	private String sectorDisp;
	private String longBusinessSummary;
	private Long fullTimeEmployees;
	private List<CompanyOfficer> companyOfficers;
	private Integer auditRisk;
	private Integer boardRisk;
	private Integer compensationRisk;
	private Integer shareHolderRightsRisk;
	private Integer overallRisk;
	private Long governanceEpochDate;
	private Long compensationAsOfEpochDate;
	private String irWebsite;
	private Integer maxAge;

	@Override
	protected AssetProfile parseInternal(JsonNode node) {
		AssetProfile profile = new AssetProfile();

		profile.setAddress1(getStringValue(node, "address1"));
		profile.setCity(getStringValue(node, "city"));
		profile.setState(getStringValue(node, "state"));
		profile.setZip(getStringValue(node, "zip"));
		profile.setCountry(getStringValue(node, "country"));
		profile.setPhone(getStringValue(node, "phone"));
		profile.setWebsite(getStringValue(node, "website"));

		profile.setIndustry(getStringValue(node, "industry"));
		profile.setIndustryKey(getStringValue(node, "industryKey"));
		profile.setIndustryDisp(getStringValue(node, "industryDisp"));
		profile.setSector(getStringValue(node, "sector"));
		profile.setSectorKey(getStringValue(node, "sectorKey"));
		profile.setSectorDisp(getStringValue(node, "sectorDisp"));

		profile.setLongBusinessSummary(getStringValue(node, "longBusinessSummary"));
		profile.setFullTimeEmployees(getLongValue(node, "fullTimeEmployees"));

		if (node.has("companyOfficers")) {
			profile.setCompanyOfficers(parseModuleArray(node.get("companyOfficers"), CompanyOfficer.class));
		}

		profile.setAuditRisk(getIntegerValue(node, "auditRisk"));
		profile.setBoardRisk(getIntegerValue(node, "boardRisk"));
		profile.setCompensationRisk(getIntegerValue(node, "compensationRisk"));
		profile.setShareHolderRightsRisk(getIntegerValue(node, "shareHolderRightsRisk"));
		profile.setOverallRisk(getIntegerValue(node, "overallRisk"));

		profile.setGovernanceEpochDate(getLongValue(node, "governanceEpochDate"));
		profile.setCompensationAsOfEpochDate(getLongValue(node, "compensationAsOfEpochDate"));

		profile.setIrWebsite(getStringValue(node, "irWebsite"));
		profile.setMaxAge(getIntegerValue(node, "maxAge"));

		return profile;
	}

	public static AssetProfile fromJson(JsonNode node) {
		return new AssetProfile().parse(node);
	}
}
