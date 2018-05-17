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
	private final Account fromAccount;
	
	@NotEmpty
	private final Account toAccount;

    @JsonCreator
	public TransferRequest(
			@JsonProperty("amount") @JsonDeserialize(using = MoneyDeserializer.class) Money amount,
			@JsonProperty("fromAccount") Account fromAccount,
			@JsonProperty("toAccount") Account toAccount
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
	public Account getFromAccount() {
		return fromAccount;
	}
	
	@JsonProperty
	public Account getToAccount() {
		return toAccount;
	}
}
