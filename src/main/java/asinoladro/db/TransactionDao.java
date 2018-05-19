package asinoladro.db;

import java.math.BigDecimal;

import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

public interface TransactionDao {
	@GetGeneratedKeys
	@SqlUpdate("insert into transactions "
			+ "(fromIban, fromAmount, fromCurrency, toIban, toAmount, toCurrency, exchangeRate) "
			+ "values "
			+ "(:fromIban, :fromAmount, :fromCurrency, :toIban, :toAmount, :toCurrency, :exchangeRate)")
	long addTransaction(
		@Bind("fromIban") String fromIban,
		@Bind("fromAmount") BigDecimal fromAmount,
		@Bind("fromCurrency") String fromCurrency,
		@Bind("toIban") String toIban,
		@Bind("toAmount") BigDecimal toAmount,
		@Bind("toCurrency") String toCurrency,
		@Bind("exchangeRate") BigDecimal exchangeRate
	);
}
