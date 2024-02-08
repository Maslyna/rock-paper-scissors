package net.roshambo.config;

import net.roshambo.model.converter.MoveConverters;
import net.roshambo.model.converter.ResultConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions;
import org.springframework.data.r2dbc.dialect.PostgresDialect;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableR2dbcAuditing
public class DatabaseConfig {

    @Bean
    public R2dbcCustomConversions r2dbcCustomConversions() {
        List<Object> converters = new ArrayList<>(4);
        converters.add(new MoveConverters.MoveReadingConverter());
        converters.add(new MoveConverters.MoveWritingConverter());
        converters.add(new ResultConverters.ResultReadingConverter());
        converters.add(new ResultConverters.ResultWritingConverter());
        return R2dbcCustomConversions.of(
                PostgresDialect.INSTANCE,
                converters
        );
    }
}
