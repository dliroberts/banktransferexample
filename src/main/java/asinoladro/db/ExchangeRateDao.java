package asinoladro.db;

import java.math.BigDecimal;

import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

public interface ExchangeRateDao {
	/**
	 * fetches exchange rate. note that each currency pair is listed unidirectionally.
	 * 
	 * @param fromCurrencyLowerAlpha the alphabetically lower currency
	 * @param toCurrencyHigherAlpha the alphabetically higher currency
	 * @return current exchange rate
	 */
	@SqlQuery("select rate from exchangeRates where "
			+ "fromCurrencyLowerAlpha = :from and toCurrencyHigherAlpha = :to")
	BigDecimal getExchangeRate(
			@Bind("from") String fromCurrencyLowerAlpha,
			@Bind("to") String toCurrencyHigherAlpha);
}
