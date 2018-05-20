package asinoladro.db;

import java.math.BigDecimal;

import org.jdbi.v3.sqlobject.CreateSqlObject;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindMethods;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jdbi.v3.sqlobject.transaction.Transaction;
import org.joda.money.Money;

import asinoladro.api.Account;

public interface TransactionDao {
	
	@CreateSqlObject
	AccountDao getAccountDao();

	@GetGeneratedKeys
	@SqlUpdate("insert into transactions "
			+ "(fromIban, fromAmount, fromCurrency, toIban, toAmount, toCurrency, exchangeRate) "
			+ "values "
			+ "(:fromIban, :fromMoney.getAmount.toString, :fromMoney.getCurrencyUnit.toString,"
			+ "   :toIban,   :toMoney.getAmount.toString,   :toMoney.getCurrencyUnit.toString,"
			+ " :exchangeRate)")
	public abstract long addTransaction(
		@Bind("fromIban") String fromIban,
		@BindMethods("fromMoney") Money fromMoney,
		@Bind("toIban") String toIban,
		@BindMethods("toMoney") Money toMoney,
		@Bind("exchangeRate") BigDecimal exchangeRate
	);
	
	@Transaction
	default long execTransaction(
			Account fromAccount, Money fromMoney,
			Account toAccount, Money toMoney,
			BigDecimal exchangeRate) {
		AccountDao accountDao = getAccountDao();
		
		accountDao.addFunds(fromAccount.getIban(), fromMoney.getAmount().negate());
		accountDao.addFunds(toAccount.getIban(), toMoney.getAmount());
		
		return addTransaction(
			fromAccount.getIban(),
			fromMoney,
			toAccount.getIban(),
			toMoney,
			exchangeRate
		);
	}
}
