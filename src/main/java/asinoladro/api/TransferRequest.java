package asinoladro.api;

import org.hibernate.validator.constraints.NotEmpty;
import org.joda.money.Money;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import asinoladro.api.serialization.MoneyDeserializer;
import asinoladro.api.serialization.MoneySerializer;

public class TransferRequest {
	
	@NotEmpty
	private final Money amount;
	
	@NotEmpty
	private final AccountSpec fromAccount;
	
	@NotEmpty
	private final AccountSpec toAccount;

    @JsonCreator
	public TransferRequest(
			@JsonProperty("amount") @JsonDeserialize(using = MoneyDeserializer.class) Money amount,
			@JsonProperty("fromAccount") AccountSpec fromAccount,
			@JsonProperty("toAccount") AccountSpec toAccount
	) {
		this.amount = amount;
		this.fromAccount = fromAccount;
		this.toAccount = toAccount;
	}
	
	@JsonProperty
    @JsonSerialize(using = MoneySerializer.class)
	public Money getAmount() {
		return amount;
	}
	
	@JsonProperty
	public AccountSpec getFromAccount() {
		return fromAccount;
	}
	
	@JsonProperty
	public AccountSpec getToAccount() {
		return toAccount;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((amount == null) ? 0 : amount.hashCode());
		result = prime * result + ((fromAccount == null) ? 0 : fromAccount.hashCode());
		result = prime * result + ((toAccount == null) ? 0 : toAccount.hashCode());
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
		TransferRequest other = (TransferRequest) obj;
		if (amount == null) {
			if (other.amount != null)
				return false;
		} else if (!amount.equals(other.amount))
			return false;
		if (fromAccount == null) {
			if (other.fromAccount != null)
				return false;
		} else if (!fromAccount.equals(other.fromAccount))
			return false;
		if (toAccount == null) {
			if (other.toAccount != null)
				return false;
		} else if (!toAccount.equals(other.toAccount))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TransferRequest [amount=" + amount + ", fromAccount=" + fromAccount + ", toAccount=" + toAccount + "]";
	}
}
