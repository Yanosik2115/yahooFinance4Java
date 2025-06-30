package yahoofinance.model;

import lombok.Getter;
import lombok.Setter;
import yahoofinance.model.common.QuoteSummaryModule;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Setter
@Getter
public class StockQuoteSummary {
	private List<QuoteSummaryModule<?>> modules;
	private String error;

	@SuppressWarnings("unchecked")
	public <T extends QuoteSummaryModule<T>> Optional<T> getModule(Class<T> moduleClass) {
		return modules.stream()
				.filter(moduleClass::isInstance)
				.map(module -> (T) module)
				.findFirst();
	}

	public Map<Class<?>, QuoteSummaryModule<?>> getModulesMap() {
		Map<Class<?>, QuoteSummaryModule<?>> moduleMap = new HashMap<>();
		if (modules != null) {
			for (QuoteSummaryModule<?> module : modules) {
				moduleMap.put(module.getClass(), module);
			}
		}
		return moduleMap;
	}

	public <T extends QuoteSummaryModule<T>> boolean hasModule(Class<T> moduleClass) {
		return getModule(moduleClass).isPresent();
	}
}