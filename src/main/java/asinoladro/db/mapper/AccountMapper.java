package asinoladro.db.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import org.joda.money.Money;

import asinoladro.api.Account;
import asinoladro.api.Customer;

public class AccountMapper implements RowMapper<Account> {
    @Override
    public Account map(ResultSet rs, StatementContext ctx) throws SQLException {
    		String iban = rs.getString("iban");
    		String balanceStr = rs.getString("currencyCode") + " " + rs.getString("balance");
    		Money balance = Money.parse(balanceStr);
    		
    		int customerId = rs.getInt("id");
    		String fullName = rs.getString("fullName");
    		Customer customer = new Customer(customerId, fullName);
    		
        return new Account(iban, balance, customer);
    }

}
