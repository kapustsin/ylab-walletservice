package com.ylab.walletservice.domain.mapper;

import com.ylab.walletservice.domain.Player;
import com.ylab.walletservice.domain.dto.LoggedInPlayerDto;
import com.ylab.walletservice.domain.dto.RegistrationDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PlayerMapper {

    PlayerMapper MAPPER = Mappers.getMapper(PlayerMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "login", source = "login")
    @Mapping(target = "password", source = "password")
    @Mapping(target = "balance", constant = "0")
    Player registrationDtoToPlayer(RegistrationDto registrationDto);

    @Mapping(target = "login", source = "login")
    @Mapping(target = "id", source = "id")
    LoggedInPlayerDto playerToLoggedInPlayerDto(Player player);

}