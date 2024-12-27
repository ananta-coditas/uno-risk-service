package com.genomebrain.backend.service;

import com.genomebrain.backend.service.dto.GenomeBrainResponseDTO;


public interface GuideLineService {

    GenomeBrainResponseDTO getGuidelinesForTags(String tags);
}
