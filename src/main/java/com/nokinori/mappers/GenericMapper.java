package com.nokinori.mappers;

import com.nokinori.api.io.GigabytesRs;
import com.nokinori.api.io.MinutesRs;
import com.nokinori.repository.entities.GigabytesPack;
import com.nokinori.repository.entities.MinutesPack;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * MapStruct mapper for Interface objects.
 */
@Mapper(componentModel = "spring")
@Component
public interface GenericMapper {

    List<MinutesRs> toMinutesPacksRs(List<MinutesPack> minutes);

    List<GigabytesRs> toGigabytePacksRs(List<GigabytesPack> gigabytes);
}
