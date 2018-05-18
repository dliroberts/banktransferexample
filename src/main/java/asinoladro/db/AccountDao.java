package asinoladro.db;

import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import asinoladro.api.Account;
import asinoladro.db.mapper.AccountMapper;

public interface AccountDao {
	@GetGeneratedKeys
	@SqlUpdate("insert into transactions (fromIban, toIban, debitAmount, creditAmount) values (:id, :name)")
	long addTransaction(@Bind("id") int id, @Bind("name") String name);
	
	@SqlQuery("select * from accounts a join customers c on a.customerId = c.id where iban = :iban")
	@RegisterRowMapper(AccountMapper.class)
	Account findAccountByIban(@Bind("iban") String iban);
}
