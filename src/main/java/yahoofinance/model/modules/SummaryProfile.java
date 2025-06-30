package yahoofinance.model.modules;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.Setter;
import yahoofinance.model.StockQuoteSummary;
import yahoofinance.model.common.AbstractQuoteSummaryModule;
import yahoofinance.model.common.CompanyOfficer;
import yahoofinance.model.common.FormattedValue;

import java.util.List;

@Getter
@Setter
public class SummaryProfile extends AbstractQuoteSummaryModule<SummaryProfile> {
	private Integer maxAge;

	// Company Address
	private String address1;
	private String city;
	private String state;
	private String zip;
	private String country;

	// Contact Information
	private String phone;
	private String website;
	private String irWebsite;

	// Industry Classification
	private String industry;
	private String industryKey;
	private String industryDisp;
	private String sector;
	private String sectorKey;
	private String sectorDisp;

	// Business Information
	private String longBusinessSummary;
	private Long fullTimeEmployees;

	// Leadership
	private List<CompanyOfficer> companyOfficers;
	private List<CompanyOfficer> executiveTeam;

	@Override
	protected SummaryProfile parseInternal(JsonNode node) {
		SummaryProfile profile = new SummaryProfile();

		profile.setMaxAge(getIntegerValue(node, "maxAge"));

		profile.setAddress1(getStringValue(node, "address1"));
		profile.setCity(getStringValue(node, "city"));
		profile.setState(getStringValue(node, "state"));
		profile.setZip(getStringValue(node, "zip"));
		profile.setCountry(getStringValue(node, "country"));

		profile.setPhone(getStringValue(node, "phone"));
		profile.setWebsite(getStringValue(node, "website"));
		profile.setIrWebsite(getStringValue(node, "irWebsite"));

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
		if (node.has("executiveTeam")) {
			profile.setExecutiveTeam(parseModuleArray(node.get("executiveTeam"), CompanyOfficer.class));
		}

		return profile;
	}

	public static SummaryProfile fromJson(JsonNode node) {
		return new SummaryProfile().parse(node);
	}
}
