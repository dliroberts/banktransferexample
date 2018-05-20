package asinoladro.db;

import java.math.BigDecimal;

import org.jdbi.v3.sqlobject.customizer.BindMethods;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.joda.money.CurrencyUnit;

public interface ExchangeRateDao {
	@SqlQuery("select rate from exchangeRates where "
			+ "fromCurrency = :from.toString and toCurrency = :to.toString")
	BigDecimal getExchangeRate(
			@BindMethods("from") CurrencyUnit fromCurrency,
			@BindMethods("to") CurrencyUnit toCurrency);
}
