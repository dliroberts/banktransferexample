package asinoladro.api;

import org.joda.money.Money;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import asinoladro.api.serialization.MoneyDeserializer;
import asinoladro.api.serialization.MoneySerializer;

public class Account {
	private String iban;
	
	private Money balance;
    
	private Customer customer;

	public Account(
			@JsonProperty("iban") String iban,
			@JsonProperty("balance") @JsonDeserialize(using = MoneyDeserializer.class) Money balance,
			@JsonProperty("customer") Customer customer
		) {
		this.iban = iban;
		this.balance = balance;
		this.customer = customer;
	}

	@JsonProperty
	public String getIban() {
		return iban;
	}

	@JsonProperty
    @JsonSerialize(using = MoneySerializer.class)
	public Money getBalance() {
		return balance;
	}

	@JsonProperty
	public Customer getCustomer() {
		return customer;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((balance == null) ? 0 : balance.hashCode());
		result = prime * result + ((customer == null) ? 0 : customer.hashCode());
		result = prime * result + ((iban == null) ? 0 : iban.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Account other = (Account) obj;
		if (balance == null) {
			if (other.balance != null)
				return false;
		} else if (!balance.equals(other.balance))
			return false;
		if (customer == null) {
			if (other.customer != null)
				return false;
		} else if (!customer.equals(other.customer))
			return false;
		if (iban == null) {
			if (other.iban != null)
				return false;
		} else if (!iban.equals(other.iban))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Account [iban=" + iban + ", balance=" + balance + ", customer=" + customer + "]";
	}
}
