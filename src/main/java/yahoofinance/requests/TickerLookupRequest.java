package yahoofinance.requests;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import yahoofinance.model.lookup.LookupResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static yahoofinance.util.Utils.*;

@AllArgsConstructor
public class TickerLookupRequest extends QuoteRequest<LookupResult> {
    private static final String LOOKUP_REQUEST = "https://query1.finance.yahoo.com/v1/finance/lookup";
    private final String symbol;
    private final LOOKUP_TYPES type;


    @Override
    public Map<String, String> getParams() {
        return Map.of("query", symbol,
                "type", type.getValue(),
                "formatted", String.valueOf(false),
                "fetchPricingData", String.valueOf(true));
    }

    @Override
    protected boolean requiresSymbol() {
        return false;
    }

    @Override
    protected boolean useCookieAndCrumb() {
        return false;
    }

    @Override
    public String getURL() {
        return LOOKUP_REQUEST;
    }

    @Override
    public LookupResult parseJson(JsonNode jsonNode) {
        if (jsonNode == null || jsonNode.isNull()) {
            return null;
        }

        jsonNode = jsonNode.get(0);

        LookupResult result = new LookupResult();

        result.setStart(getIntegerValue(jsonNode, "start"));
        result.setCount(getIntegerValue(jsonNode, "count"));
        result.setTotal(getIntegerValue(jsonNode, "total"));
        result.setError(getStringValue(jsonNode, "error"));

        JsonNode documentsNode = jsonNode.get("documents");
        if (documentsNode != null && documentsNode.isArray()) {
            List<LookupResult.Document> documents = new ArrayList<>();
            for (JsonNode documentNode : documentsNode) {
                LookupResult.Document document = parseDocument(documentNode);
                if (document != null) {
                    documents.add(document);
                }
            }
            result.setDocuments(documents);
        }

        JsonNode lookupTotalsNode = jsonNode.get("lookupTotals");
        if (lookupTotalsNode != null) {
            result.setLookupTotals(parseLookupTotals(lookupTotalsNode));
        }

        return result;
    }

    private static LookupResult.Document parseDocument(JsonNode documentNode) {
        if (documentNode == null || documentNode.isNull()) {
            return null;
        }

        LookupResult.Document document = new LookupResult.Document();

        document.setExchange(getStringValue(documentNode, "exchange"));
        document.setIndustryLink(getStringValue(documentNode, "industryLink"));
        document.setIndustryName(getStringValue(documentNode, "industryName"));
        document.setQuoteType(getStringValue(documentNode, "quoteType"));
        document.setRank(getLongValue(documentNode, "rank"));
        document.setRegularMarketChange(getDoubleValue(documentNode, "regularMarketChange"));
        document.setRegularMarketPercentChange(getDoubleValue(documentNode, "regularMarketPercentChange"));
        document.setRegularMarketPrice(getDoubleValue(documentNode, "regularMarketPrice"));
        document.setShortName(getStringValue(documentNode, "shortName"));
        document.setSymbol(getStringValue(documentNode, "symbol"));

        return document;
    }

    private static LookupResult.LookupTotals parseLookupTotals(JsonNode lookupTotalsNode) {
        if (lookupTotalsNode == null || lookupTotalsNode.isNull()) {
            return null;
        }

        LookupResult.LookupTotals lookupTotals = new LookupResult.LookupTotals();

        lookupTotals.setAll(getIntegerValue(lookupTotalsNode, "all"));
        lookupTotals.setEquity(getIntegerValue(lookupTotalsNode, "equity"));
        lookupTotals.setMutualfund(getIntegerValue(lookupTotalsNode, "mutualfund"));
        lookupTotals.setEtf(getIntegerValue(lookupTotalsNode, "etf"));
        lookupTotals.setIndex(getIntegerValue(lookupTotalsNode, "index"));
        lookupTotals.setCurrency(getIntegerValue(lookupTotalsNode, "currency"));
        lookupTotals.setFuture(getIntegerValue(lookupTotalsNode, "future"));
        lookupTotals.setCryptocurrency(getIntegerValue(lookupTotalsNode, "cryptocurrency"));
        lookupTotals.setPrivateCompany(getIntegerValue(lookupTotalsNode, "privateCompany"));

        return lookupTotals;
    }

    public enum LOOKUP_TYPES {
        ALL("all"),
        EQUITY("equity"),
        MUTUALFUND("mutualfund"),
        ETF("etf"),
        INDEX("index"),
        FUTURE("future"),
        CURRENCY("currency"),
        CRYPTOCURRENCY("cryptocurrency");

        @Getter
        private final String value;

        LOOKUP_TYPES(String value) {
            this.value = value;
        }
    }
}
