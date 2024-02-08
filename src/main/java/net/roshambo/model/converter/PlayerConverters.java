package net.roshambo.model.converter;

import net.roshambo.model.Player;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

public class PlayerConverters {
    public static class PlayerReadingConverter implements Converter<Short, Player> {
        @Override
        public Player convert(Short source) {
            return Player.valueOf(source);
        }
    }

    @WritingConverter
    public static class PlayerWritingConverter implements Converter<Player, Short> {
        @Override
        public Short convert(Player source) {
            return source.value;
        }
    }
}
