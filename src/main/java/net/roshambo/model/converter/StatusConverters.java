package net.roshambo.model.converter;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.roshambo.model.Status;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StatusConverters {
    @WritingConverter
    public static class StatusWritingConverter implements Converter<Status, Short> {
        @Override
        public Short convert(Status source) {
            return source.value;
        }
    }

    @ReadingConverter
    public static class StatusReadingConverter implements Converter<Short, Status> {

        @Override
        public Status convert(Short source) {
            return Status.valueOf(source);
        }
    }
}
