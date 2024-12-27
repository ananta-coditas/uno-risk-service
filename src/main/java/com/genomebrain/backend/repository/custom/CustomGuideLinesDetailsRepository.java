package com.genomebrain.backend.repository.custom;

import com.genomebrain.backend.domain.GuideLinesDetails;

import java.util.List;

public interface CustomGuideLinesDetailsRepository {

    List<GuideLinesDetails> findAll();
    
    List<GuideLinesDetails> getAllTheGuidelines(List<List<String>> tags);
}
