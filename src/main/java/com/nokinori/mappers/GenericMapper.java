package com.nokinori.mappers;

import com.nokinori.api.io.GigabytesRs;
import com.nokinori.api.io.MinutesRs;
import com.nokinori.repository.entities.Gigabytes;
import com.nokinori.repository.entities.Minutes;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface GenericMapper {

    MinutesRs toMinutesRs(Minutes minutes);

    GigabytesRs toGigabyteRs(Gigabytes gigabytes);
}
