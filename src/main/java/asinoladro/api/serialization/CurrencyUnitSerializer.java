package asinoladro.api.serialization;

import java.io.IOException;

import org.joda.money.CurrencyUnit;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class CurrencyUnitSerializer extends StdSerializer<CurrencyUnit> {
	private static final long serialVersionUID = 1920438524821L;

	public CurrencyUnitSerializer() {
		super(CurrencyUnit.class);
	}

	@Override
	public void serialize(CurrencyUnit value, JsonGenerator jgen, SerializerProvider provider)
				throws IOException, JsonProcessingException {
		jgen.writeString(value.toString());
	}
}