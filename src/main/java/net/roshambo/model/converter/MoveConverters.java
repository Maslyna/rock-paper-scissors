package net.roshambo.model.converter;

import net.roshambo.model.Move;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;

public class MoveConverters {
    @ReadingConverter
    public static class MoveReadingConverter implements Converter<Short, Move> {
        @Override
        public Move convert(Short source) {
            return Move.valueOf(source);
        }
    }

    @WritingConverter
    public static class MoveWritingConverter implements Converter<Move, Short> {
        @Override
        public Short convert(Move source) {
            return source.value;
        }
    }

}
