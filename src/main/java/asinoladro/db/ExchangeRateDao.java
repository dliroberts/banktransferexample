package asinoladro.db;

import java.math.BigDecimal;

import org.jdbi.v3.sqlobject.customizer.BindMethods;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.joda.money.CurrencyUnit;

public interface ExchangeRateDao {
	/**
	 * fetches exchange rate. note that each currency pair is listed unidirectionally.
	 * 
	 * @param fromCurrencyLowerAlpha the alphabetically lower currency
	 * @param toCurrencyHigherAlpha the alphabetically higher currency
	 * @return current exchange rate
	 */
	@SqlQuery("select rate from exchangeRates where "
			+ "fromCurrencyLowerAlpha = :from.toString and toCurrencyHigherAlpha = :to.toString")
	BigDecimal getExchangeRate(
			@BindMethods("from") CurrencyUnit fromCurrencyLowerAlpha,
			@BindMethods("to") CurrencyUnit toCurrencyHigherAlpha);
}
