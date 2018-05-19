package asinoladro.db;

import java.math.BigDecimal;

import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import asinoladro.api.Account;
import asinoladro.db.mapper.AccountMapper;

public interface AccountDao {
	@SqlQuery("select * from accounts a join customers c on a.customerId = c.id where iban = :iban")
	@RegisterRowMapper(AccountMapper.class)
	Account findAccountByIban(@Bind("iban") String iban);
	
	@SqlUpdate("update accounts set balance = balance + :amount where iban = :iban")
	void addFunds(@Bind("iban") String iban, @Bind("amount") BigDecimal amount);
}
