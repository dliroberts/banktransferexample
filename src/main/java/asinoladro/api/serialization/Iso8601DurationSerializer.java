package asinoladro.api.serialization;

import java.io.IOException;

import org.joda.time.Duration;
import org.joda.time.format.ISOPeriodFormat;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class Iso8601DurationSerializer extends StdSerializer<Duration> {
	private static final long serialVersionUID = 89434378888L;
	
	Iso8601DurationSerializer() {
		super(Duration.class);
	}
	
	@Override
	public void serialize(Duration value, JsonGenerator jgen, SerializerProvider provider)
				throws IOException, JsonProcessingException {
		String human = ISOPeriodFormat.standard().print(value.toPeriod());
		jgen.writeString(human);
	}
}
