package yahoofinance.model.common;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyOfficer extends AbstractQuoteSummaryModule<CompanyOfficer> {
	private Integer maxAge;
	private String name;
	private Integer age;
	private String title;
	private Integer yearBorn;
	private Integer fiscalYear;
	private FormattedValue totalPay;
	private FormattedValue exercisedValue;
	private FormattedValue unexercisedValue;

	@Override
	protected CompanyOfficer parseInternal(JsonNode node) {
		CompanyOfficer officer = new CompanyOfficer();

		officer.setMaxAge(getIntegerValue(node, "maxAge"));
		officer.setName(getStringValue(node, "name"));
		officer.setAge(getIntegerValue(node, "age"));
		officer.setTitle(getStringValue(node, "title"));
		officer.setYearBorn(getIntegerValue(node, "yearBorn"));
		officer.setFiscalYear(getIntegerValue(node, "fiscalYear"));

		// Parse formatted values for compensation data
		officer.setTotalPay(parseFormattedValue(node.get("totalPay")));
		officer.setExercisedValue(parseFormattedValue(node.get("exercisedValue")));
		officer.setUnexercisedValue(parseFormattedValue(node.get("unexercisedValue")));

		return officer;
	}

	public static CompanyOfficer fromJson(JsonNode node) {
		return new CompanyOfficer().parse(node);
	}
}