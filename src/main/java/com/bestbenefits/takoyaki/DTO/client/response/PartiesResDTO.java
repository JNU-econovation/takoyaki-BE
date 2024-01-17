package com.bestbenefits.takoyaki.DTO.client.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class PartiesResDTO {
    private final List<PartyListResDTO> partyDTOlist;
    private final int totalPages;
}
