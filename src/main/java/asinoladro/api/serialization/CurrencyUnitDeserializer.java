package asinoladro.api.serialization;

import java.io.IOException;

import org.joda.money.CurrencyUnit;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class CurrencyUnitDeserializer extends StdDeserializer<CurrencyUnit> {
	private static final long serialVersionUID = 2529348324322L;

	public CurrencyUnitDeserializer() {
		super(CurrencyUnit.class);
	}

	@Override
	public CurrencyUnit deserialize(JsonParser jp, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		String value = jp.getValueAsString().toUpperCase();
		
		return CurrencyUnit.of(value);
	}
}