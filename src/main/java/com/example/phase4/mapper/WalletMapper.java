package com.example.phase4.mapper;

import com.example.phase4.dto.request.WalletRequestDTO;
import com.example.phase4.dto.response.WalletResponseDTO;
import com.example.phase4.entity.Wallet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface WalletMapper extends GenericMapper<Wallet, WalletRequestDTO,WalletResponseDTO > {
    WalletMapper INSTANCE = Mappers.getMapper(WalletMapper.class);

    @Override
    @Mapping(source = "user.id",target = "userId")
    WalletResponseDTO modelToDTO(Wallet wallet);
}
