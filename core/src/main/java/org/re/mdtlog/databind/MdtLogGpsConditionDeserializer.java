package org.re.mdtlog.databind;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.re.mdtlog.domain.MdtLogGpsCondition;

import java.io.IOException;

public class MdtLogGpsConditionDeserializer extends JsonDeserializer<MdtLogGpsCondition> {
    @Override
    public MdtLogGpsCondition deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String value = p.getText();
        return getEnumMember(value);
    }

    private MdtLogGpsCondition getEnumMember(String value) {
        for (var enumMember : MdtLogGpsCondition.values()) {
            if (enumMember.getCode().equals(value)) {
                return enumMember;
            }
        }
        return null;
    }
}
