package asinoladro.api.serialization;

import java.io.IOException;

import org.joda.money.Money;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class MoneyDeserializer extends StdDeserializer<Money> {
	private static final long serialVersionUID = 2529348324322L;

	public MoneyDeserializer() {
		super(Money.class);
	}

	@Override
	public Money deserialize(JsonParser jp, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		String value = jp.getValueAsString().toUpperCase();
		
		return Money.parse(value);
	}
}