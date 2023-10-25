package com.ylab.walletservice.domain.mapper;

import com.ylab.walletservice.domain.Transaction;
import com.ylab.walletservice.domain.dto.TransactionRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TransactionMapper {

    TransactionMapper MAPPER = Mappers.getMapper(TransactionMapper.class);

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