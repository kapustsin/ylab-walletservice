package com.ylab.walletservice.domain.mapper;

import com.ylab.walletservice.domain.Transaction;
import com.ylab.walletservice.domain.dto.TransactionRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * Interface for mapping TransactionRequestDto objects to Transaction objects.
 */
@Mapper
public interface TransactionMapper {

    /**
     * Mapper instance for TransactionRequestDto to Transaction conversion.
     */
    TransactionMapper MAPPER = Mappers.getMapper(TransactionMapper.class);

    /**
     * Converts a TransactionRequestDto object to a Transaction object, updating the provided Transaction instance.
     *
     * @param transactionRequestDto The TransactionRequestDto object to be converted.
     * @param transaction           The Transaction object to be updated with data from TransactionRequestDto.
     * @return The updated Transaction object with data from TransactionRequestDto.
     */
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "token", source = "transactionRequestDto.token"),
            @Mapping(target = "creatorId", source = "transactionRequestDto.creatorId"),
            @Mapping(target = "amount", source = "transactionRequestDto.amount"),
            @Mapping(target = "type", source = "transactionRequestDto.type")
    })
    Transaction transactionDtoToTransaction(TransactionRequestDto transactionRequestDto,
            @MappingTarget Transaction transaction);

}