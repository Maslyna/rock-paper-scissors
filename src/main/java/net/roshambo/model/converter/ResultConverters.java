package net.roshambo.model.converter;

import net.roshambo.model.Result;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;

public class ResultConverters {
    @WritingConverter
    public static class ResultWritingConverter implements Converter<Result, Short> {
        @Override
        public Short convert(Result source) {
            return source.value;
        }
    }

    @ReadingConverter
    public static class ResultReadingConverter implements Converter<Short, Result> {

        @Override
        public Result convert(Short source) {
            return Result.valueOf(source);
        }
    }
}
