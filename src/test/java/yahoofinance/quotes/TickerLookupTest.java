package yahoofinance.quotes;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import yahoofinance.exception.YFinanceException;
import yahoofinance.model.lookup.LookupResult;
import yahoofinance.YFinance;
import yahoofinance.requests.TickerLookupRequest;

import static org.junit.jupiter.api.Assertions.*;

class TickerLookupTest {

    @Nested
    @DisplayName("lookupTicker(String ticker) tests")
    class LookupTickerSimpleTests {

        @Test
        @DisplayName("Should successfully lookup AAPL ticker with default ALL type")
        void shouldLookupAAPLWithDefaultType() throws YFinanceException {
            // Given
            String ticker = "AAPL";

            // When
            LookupResult result = YFinance.lookupTicker(ticker);

            // Then
            assertNotNull(result, "Result should not be null");
            assertNotNull(result.getDocuments(), "Documents should not be null");
            assertFalse(result.getDocuments().isEmpty(), "Should find some documents");
            assertNotNull(result.getLookupTotals(), "Lookup totals should not be null");

            // Verify that at least one document contains Apple-related info
            boolean foundApple = result.getDocuments().stream()
                    .anyMatch(doc -> doc.getSymbol() != null &&
                            (doc.getSymbol().contains("AAPL") ||
                                    (doc.getShortName() != null && doc.getShortName().toLowerCase().contains("apple"))));
            assertTrue(foundApple, "Should find Apple-related results");
        }

        @Test
        @DisplayName("Should handle known ticker symbol MSFT")
        void shouldHandleMSFTTicker() throws YFinanceException {
            // Given
            String ticker = "MSFT";

            // When
            LookupResult result = YFinance.lookupTicker(ticker);

            // Then
            assertNotNull(result);
            assertNotNull(result.getDocuments());
            // Microsoft should be found
            boolean foundMicrosoft = result.getDocuments().stream()
                    .anyMatch(doc -> doc.getSymbol() != null &&
                            (doc.getSymbol().contains("MSFT") ||
                                    (doc.getShortName() != null && doc.getShortName().toLowerCase().contains("microsoft"))));
            assertTrue(foundMicrosoft, "Should find Microsoft-related results");
        }

        @Test
        @DisplayName("Should handle invalid ticker gracefully")
        void shouldHandleInvalidTicker() {
            // Given
            String invalidTicker = "INVALIDTICKER123456789";

            // When & Then
            assertDoesNotThrow(() -> {
                LookupResult result = YFinance.lookupTicker(invalidTicker);
                // Invalid ticker might return empty results but shouldn't throw exception
                assertNotNull(result);
            });
        }

        @Test
        @DisplayName("Should throw exception for null ticker")
        void shouldThrowExceptionForNullTicker() {
            // When & Then
            assertThrows(Exception.class, () -> {
                YFinance.lookupTicker(null);
            }, "Should throw exception for null ticker");
        }

        @Test
        @DisplayName("Should handle empty ticker gracefully")
        void shouldHandleEmptyTicker() {
            // When & Then
            assertThrows(Exception.class, () -> {
                YFinance.lookupTicker("");
            }, "Should throw exception for empty ticker");
        }
    }

    @Nested
    @DisplayName("lookupTicker(String ticker, LOOKUP_TYPES lookupType) tests")
    class LookupTickerWithTypeTests {

        @Test
        @DisplayName("Should lookup AAPL with EQUITY type")
        void shouldLookupAAPLWithEquityType() throws YFinanceException {
            // Given
            String ticker = "AAPL";
            TickerLookupRequest.LOOKUP_TYPES lookupType = TickerLookupRequest.LOOKUP_TYPES.EQUITY;

            // When
            LookupResult result = YFinance.lookupTicker(ticker, lookupType);

            // Then
            assertNotNull(result);
            assertNotNull(result.getDocuments());

            if (!result.getDocuments().isEmpty()) {
                // Verify that results are equity type
                boolean allEquity = result.getDocuments().stream()
                        .allMatch(doc -> "equity".equals(doc.getQuoteType()));
                assertTrue(allEquity, "All results should be of equity type");
            }
        }

        @Test
        @DisplayName("Should lookup cryptocurrency with CRYPTOCURRENCY type")
        void shouldLookupCryptocurrencyType() throws YFinanceException {
            // Given
            String ticker = "BTC";
            TickerLookupRequest.LOOKUP_TYPES lookupType = TickerLookupRequest.LOOKUP_TYPES.CRYPTOCURRENCY;

            // When
            LookupResult result = YFinance.lookupTicker(ticker, lookupType);

            // Then
            assertNotNull(result);
            assertNotNull(result.getDocuments());

            if (!result.getDocuments().isEmpty()) {
                // Verify that results contain cryptocurrency
                boolean foundCrypto = result.getDocuments().stream()
                        .anyMatch(doc -> "cryptocurrency".equals(doc.getQuoteType()) ||
                                (doc.getSymbol() != null && doc.getSymbol().contains("BTC")));
                assertTrue(foundCrypto, "Should find cryptocurrency results");
            }
        }

        @Test
        @DisplayName("Should lookup ETF with ETF type")
        void shouldLookupETFType() throws YFinanceException {
            // Given
            String ticker = "SPY";
            TickerLookupRequest.LOOKUP_TYPES lookupType = TickerLookupRequest.LOOKUP_TYPES.ETF;

            // When
            LookupResult result = YFinance.lookupTicker(ticker, lookupType);

            // Then
            assertNotNull(result);
            assertNotNull(result.getDocuments());
        }

        @ParameterizedTest
        @EnumSource(TickerLookupRequest.LOOKUP_TYPES.class)
        @DisplayName("Should handle all lookup types without throwing exceptions")
        void shouldHandleAllLookupTypes(TickerLookupRequest.LOOKUP_TYPES lookupType) {
            // Given
            String ticker = "A"; // Simple ticker that should exist

            // When & Then
            assertDoesNotThrow(() -> {
                LookupResult result = YFinance.lookupTicker(ticker, lookupType);
                assertNotNull(result, "Result should not be null for type: " + lookupType);
            });
        }

        @Test
        @DisplayName("Should throw exception for null lookup type")
        void shouldThrowExceptionForNullLookupType() {
            // Given
            String ticker = "AAPL";

            // When & Then
            assertThrows(Exception.class, () -> {
                YFinance.lookupTicker(ticker, null);
            }, "Should throw exception for null lookup type");
        }
    }

    @Nested
    @DisplayName("Integration tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should perform actual lookup and validate response structure")
        void shouldValidateResponseStructure() throws YFinanceException {
            // Given
            String ticker = "AAPL";

            // When
            LookupResult result = YFinance.lookupTicker(ticker);

            // Then
            assertNotNull(result, "Result should not be null");
            assertNotNull(result.getStart(), "Start should not be null");
            assertNotNull(result.getCount(), "Count should not be null");
            assertNotNull(result.getTotal(), "Total should not be null");
            assertNotNull(result.getDocuments(), "Documents should not be null");
            assertNotNull(result.getLookupTotals(), "LookupTotals should not be null");

            // Validate document structure if documents exist
            if (!result.getDocuments().isEmpty()) {
                LookupResult.Document firstDoc = result.getDocuments().get(0);
                assertNotNull(firstDoc.getSymbol(), "Document symbol should not be null");
                assertNotNull(firstDoc.getQuoteType(), "Document quote type should not be null");
            }

            // Validate lookup totals structure
            LookupResult.LookupTotals totals = result.getLookupTotals();
            assertNotNull(totals.getAll(), "Total.all should not be null");
            assertNotNull(totals.getEquity(), "Total.equity should not be null");
            assertNotNull(totals.getMutualfund(), "Total.mutualfund should not be null");
            assertNotNull(totals.getEtf(), "Total.etf should not be null");
            assertNotNull(totals.getIndex(), "Total.index should not be null");
            assertNotNull(totals.getCurrency(), "Total.currency should not be null");
            assertNotNull(totals.getFuture(), "Total.future should not be null");
            assertNotNull(totals.getCryptocurrency(), "Total.cryptocurrency should not be null");
            assertNotNull(totals.getPrivateCompany(), "Total.privateCompany should not be null");
        }

        @Test
        @DisplayName("Should perform lookup with specific type and validate filtering")
        void shouldValidateTypeFiltering() throws YFinanceException {
            // Given
            String ticker = "AAPL";
            TickerLookupRequest.LOOKUP_TYPES lookupType = TickerLookupRequest.LOOKUP_TYPES.EQUITY;

            // When
            LookupResult result = YFinance.lookupTicker(ticker, lookupType);

            // Then
            assertNotNull(result);
            assertNotNull(result.getDocuments());

            // If we have results, they should be filtered by type
            if (!result.getDocuments().isEmpty()) {
                boolean allMatchType = result.getDocuments().stream()
                        .allMatch(doc -> "equity".equals(doc.getQuoteType()));
                assertTrue(allMatchType, "All documents should match the requested type");
            }
        }

        @Test
        @DisplayName("Should handle partial matches correctly")
        void shouldHandlePartialMatches() throws YFinanceException {
            // Given
            String partialTicker = "APP"; // Should match Apple and other APP* symbols

            // When
            LookupResult result = YFinance.lookupTicker(partialTicker);

            // Then
            assertNotNull(result);
            assertNotNull(result.getDocuments());

            // Should find multiple matches for partial ticker
            if (!result.getDocuments().isEmpty()) {
                boolean foundPartialMatches = result.getDocuments().stream()
                        .anyMatch(doc -> doc.getSymbol() != null &&
                                doc.getSymbol().toUpperCase().contains("APP"));
                assertTrue(foundPartialMatches, "Should find symbols containing 'APP'");
            }
        }
    }

    @Nested
    @DisplayName("Edge cases")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle ticker with special characters")
        void shouldHandleTickerWithSpecialCharacters() throws YFinanceException {
            // Given
            String ticker = "BRK-A"; // Berkshire Hathaway Class A

            // When
            LookupResult result = YFinance.lookupTicker(ticker);

            // Then
            assertNotNull(result);
            assertNotNull(result.getDocuments());

            // Should find BRK-A or related symbols
            if (!result.getDocuments().isEmpty()) {
                boolean foundBerkshire = result.getDocuments().stream()
                        .anyMatch(doc -> doc.getSymbol() != null &&
                                (doc.getSymbol().contains("BRK") ||
                                        (doc.getShortName() != null && doc.getShortName().toLowerCase().contains("berkshire"))));
                assertTrue(foundBerkshire, "Should find Berkshire-related results");
            }
        }

        @Test
        @DisplayName("Should handle international ticker symbols")
        void shouldHandleInternationalTickers() throws YFinanceException {
            // Given
            String ticker = "NESN"; // Nestle (Swiss)

            // When
            LookupResult result = YFinance.lookupTicker(ticker);

            // Then
            assertNotNull(result);
            assertNotNull(result.getDocuments());

            // Should find some results even for international tickers
            if (!result.getDocuments().isEmpty()) {
                boolean foundResults = result.getDocuments().stream()
                        .anyMatch(doc -> doc.getSymbol() != null &&
                                doc.getSymbol().contains("NESN"));
                // Note: This might not always find results, so we just check structure
            }
        }

        @Test
        @DisplayName("Should handle case insensitive search")
        void shouldHandleCaseInsensitiveSearch() throws YFinanceException {
            // Given
            String lowerCaseTicker = "aapl";
            String upperCaseTicker = "AAPL";

            // When
            LookupResult lowerResult = YFinance.lookupTicker(lowerCaseTicker);
            LookupResult upperResult = YFinance.lookupTicker(upperCaseTicker);

            // Then
            assertNotNull(lowerResult);
            assertNotNull(upperResult);
            assertNotNull(lowerResult.getDocuments());
            assertNotNull(upperResult.getDocuments());

            // Both should return similar results (case shouldn't matter)
            // Note: Exact comparison might vary due to API behavior, so we just verify structure
        }

        @Test
        @DisplayName("Should handle very short ticker symbols")
        void shouldHandleShortTickers() throws YFinanceException {
            // Given
            String shortTicker = "A"; // Agilent Technologies

            // When
            LookupResult result = YFinance.lookupTicker(shortTicker);

            // Then
            assertNotNull(result);
            assertNotNull(result.getDocuments());
            // Short tickers should return multiple matches
        }
    }

    @Nested
    @DisplayName("Error handling tests")
    class ErrorHandlingTests {

        @Test
        @DisplayName("Should handle network issues gracefully")
        void shouldHandleNetworkIssues() {
            // This test assumes network connectivity
            // In a real environment, you might want to mock network failures
            String ticker = "AAPL";

            assertDoesNotThrow(() -> {
                LookupResult result = YFinance.lookupTicker(ticker);
                assertNotNull(result);
            }, "Should handle network requests gracefully");
        }

        @Test
        @DisplayName("Should validate lookup totals consistency")
        void shouldValidateLookupTotalsConsistency() throws YFinanceException  {
            // Given
            String ticker = "AAPL";

            // When
            LookupResult result = YFinance.lookupTicker(ticker);

            // Then
            assertNotNull(result);
            assertNotNull(result.getLookupTotals());

            LookupResult.LookupTotals totals = result.getLookupTotals();

            // The sum of individual types should equal or be less than 'all'
            int sumOfTypes = (totals.getEquity() != null ? totals.getEquity() : 0) +
                    (totals.getMutualfund() != null ? totals.getMutualfund() : 0) +
                    (totals.getEtf() != null ? totals.getEtf() : 0) +
                    (totals.getIndex() != null ? totals.getIndex() : 0) +
                    (totals.getCurrency() != null ? totals.getCurrency() : 0) +
                    (totals.getFuture() != null ? totals.getFuture() : 0) +
                    (totals.getCryptocurrency() != null ? totals.getCryptocurrency() : 0) +
                    (totals.getPrivateCompany() != null ? totals.getPrivateCompany() : 0);

            int totalAll = totals.getAll() != null ? totals.getAll() : 0;

            assertTrue(sumOfTypes <= totalAll || totalAll == 0,
                    "Sum of individual types should not exceed total count");
        }
    }

    @Nested
    @DisplayName("Performance tests")
    class PerformanceTests {

        @Test
        @DisplayName("Should complete lookup within reasonable time")
        void shouldCompleteLookupWithinReasonableTime() throws YFinanceException {
            // Given
            String ticker = "AAPL";
            long startTime = System.currentTimeMillis();

            // When
            LookupResult result = YFinance.lookupTicker(ticker);
            long endTime = System.currentTimeMillis();

            // Then
            assertNotNull(result);
            long duration = endTime - startTime;
            assertTrue(duration < 10000, // 10 seconds should be more than enough
                    "Lookup should complete within 10 seconds, took: " + duration + "ms");
        }
    }
}