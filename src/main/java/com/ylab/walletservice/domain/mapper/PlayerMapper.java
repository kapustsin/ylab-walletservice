package com.ylab.walletservice.domain.mapper;

import com.ylab.walletservice.domain.Player;
import com.ylab.walletservice.domain.dto.LoggedInPlayerDto;
import com.ylab.walletservice.domain.dto.RegistrationDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * Interface for mapping RegistrationDto objects to Player objects.
 */
@Mapper
public interface PlayerMapper {

    /**
     * Mapper instance for RegistrationDto to Player conversion.
     */
    PlayerMapper MAPPER = Mappers.getMapper(PlayerMapper.class);

    /**
     * Converts a RegistrationDto object to a Player object.
     *
     * @param registrationDto The RegistrationDto object to be converted.
     * @return A new Player object populated with data from the RegistrationDto.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "login", source = "login")
    @Mapping(target = "password", source = "password")
    @Mapping(target = "balance", constant = "0")
    Player registrationDtoToPlayer(RegistrationDto registrationDto);

    /**
     * Converts a Player object to a LoggedInPlayerDto object.
     *
     * @param player The Player object to be converted.
     * @return A new LoggedInPlayerDto object populated with data from the Player.
     */
    @Mapping(target = "login", source = "login")
    @Mapping(target = "id", source = "id")
    LoggedInPlayerDto playerToLoggedInPlayerDto(Player player);

}