package org.re.mdtlog.databind;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.re.mdtlog.domain.MdtLogGpsCondition;

import java.io.IOException;

public class MdtLogGpsConditionSerializer extends StdSerializer<MdtLogGpsCondition> {
    public MdtLogGpsConditionSerializer() {
        super(MdtLogGpsCondition.class);
    }

    @Override
    public void serialize(MdtLogGpsCondition value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value == null) {
            gen.writeNull();
        } else {
            gen.writeString(value.getCode());
        }
    }
}
