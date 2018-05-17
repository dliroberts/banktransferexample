package asinoladro.api.serialization;

import java.io.IOException;

import org.joda.money.Money;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class MoneySerializer extends StdSerializer<Money> {
	private static final long serialVersionUID = 1920438524821L;

	public MoneySerializer() {
		super(Money.class);
	}

	@Override
	public void serialize(Money value, JsonGenerator jgen, SerializerProvider provider)
				throws IOException, JsonProcessingException {
		jgen.writeStringField("str", value.toString());
	}
}