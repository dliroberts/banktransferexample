package asinoladro.db;

import java.math.BigDecimal;

import org.jdbi.v3.sqlobject.CreateSqlObject;
import org.jdbi.v3.sqlobject.transaction.Transaction;
import org.joda.money.Money;

import asinoladro.api.Account;

public interface TransactionExecutorDao {
	
	@CreateSqlObject
	AccountDao getAccountDao();
	
	@CreateSqlObject
	TransactionDao getTransactionDao();
	
	@Transaction
	default long execTransaction(
			Account fromAccount, Money fromMoney,
			Account toAccount, Money toMoney,
			BigDecimal exchangeRate) {
		AccountDao accountDao = getAccountDao();
		TransactionDao transactionDao = getTransactionDao();
		
		accountDao.addFunds(fromAccount.getIban(), fromMoney.getAmount().negate());
		accountDao.addFunds(toAccount.getIban(), toMoney.getAmount());
		
		return transactionDao.addTransaction(
			fromAccount.getIban(),
			fromMoney,
			toAccount.getIban(),
			toMoney,
			exchangeRate
		);
	}

}
