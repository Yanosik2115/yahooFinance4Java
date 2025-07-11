package yahoofinance.model.lookup;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class LookupResult {
    private Integer start;
    private Integer count;
    private Integer total;
    private List<Document> documents;
    private LookupTotals lookupTotals;
    private String error;

    @Getter
    @Setter
    public static class LookupTotals {
        private Integer all;
        private Integer equity;
        private Integer mutualfund;
        private Integer etf;
        private Integer index;
        private Integer currency;
        private Integer future;
        private Integer cryptocurrency;
        private Integer privateCompany;
    }

    @Getter
    @Setter
    public static class Document {
        private String exchange;
        private String industryLink;
        private String industryName;
        private String quoteType;
        private Long rank;
        private Double regularMarketChange;
        private Double regularMarketPercentChange;
        private Double regularMarketPrice;
        private String shortName;
        private String symbol;
    }
}
