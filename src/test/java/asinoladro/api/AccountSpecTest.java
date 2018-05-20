package asinoladro.api;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.Test;

public class AccountSpecTest {

	@Test
	public void exactMatch() {
		AccountSpec as1 = new AccountSpec(
				"GB55ZAFY89851748597528",
				"duncan roberts",
				CurrencyUnit.GBP);
		
		Account a1 = new Account(
			"GB55ZAFY89851748597528",
			Money.of(CurrencyUnit.GBP, new BigDecimal("333")),
			new Customer(1, "duncan roberts"));
		
		assertTrue(as1.matches(a1));
	}

	@Test
	public void matchDifferentCase() {
		AccountSpec as1 = new AccountSpec(
				"GB55ZAFY89851748597528",
				"duncan roberts",
				CurrencyUnit.GBP);
		
		Account a1 = new Account(
			"GB55ZAFY89851748597528",
			Money.of(CurrencyUnit.GBP, new BigDecimal("333")),
			new Customer(1, "Duncan ROBERTS"));
		
		assertTrue(as1.matches(a1));
	}

	@Test
	public void matchInitial() {
		AccountSpec as1 = new AccountSpec(
				"GB55ZAFY89851748597528",
				"duncan roberts",
				CurrencyUnit.GBP);
		
		Account a1 = new Account(
			"GB55ZAFY89851748597528",
			Money.of(CurrencyUnit.GBP, new BigDecimal("333")),
			new Customer(1, "d. Roberts"));
		
		assertTrue(as1.matches(a1));
	}

	@Test
	public void matchTitle() {
		AccountSpec as1 = new AccountSpec(
				"GB55ZAFY89851748597528",
				"Mr. Duncan Roberts",
				CurrencyUnit.GBP);
		
		Account a1 = new Account(
			"GB55ZAFY89851748597528",
			Money.of(CurrencyUnit.GBP, new BigDecimal("333")),
			new Customer(1, "duncan roberts"));
		
		assertTrue(as1.matches(a1));
	}

	@Test
	public void matchSpaces() {
		AccountSpec as1 = new AccountSpec(
				"GB55ZAFY89851748597528",
				"          Mr.      Duncan    Roberts",
				CurrencyUnit.GBP);
		
		Account a1 = new Account(
			"GB55ZAFY89851748597528",
			Money.of(CurrencyUnit.GBP, new BigDecimal("333")),
			new Customer(1, "duncan roberts"));
		
		assertTrue(as1.matches(a1));
	}

	@Test
	public void differentIban() {
		AccountSpec as1 = new AccountSpec(
				"ES2364265841767173822054",
				"duncan roberts",
				CurrencyUnit.GBP);
		
		Account a1 = new Account(
			"GB55ZAFY89851748597528",
			Money.of(CurrencyUnit.GBP, new BigDecimal("333")),
			new Customer(1, "duncan roberts"));
		
		assertFalse(as1.matches(a1));
	}

	@Test
	public void differentName() {
		AccountSpec as1 = new AccountSpec(
				"ES2364265841767173822054",
				"damian roberts",
				CurrencyUnit.GBP);
		
		Account a1 = new Account(
			"GB55ZAFY89851748597528",
			Money.of(CurrencyUnit.GBP, new BigDecimal("333")),
			new Customer(1, "duncan roberts"));
		
		assertFalse(as1.matches(a1));
	}

	@Test
	public void differentCurrency() {
		AccountSpec as1 = new AccountSpec(
				"ES2364265841767173822054",
				"damian roberts",
				CurrencyUnit.JPY);
		
		Account a1 = new Account(
			"GB55ZAFY89851748597528",
			Money.of(CurrencyUnit.GBP, new BigDecimal("333")),
			new Customer(1, "duncan roberts"));
		
		assertFalse(as1.matches(a1));
	}

}
