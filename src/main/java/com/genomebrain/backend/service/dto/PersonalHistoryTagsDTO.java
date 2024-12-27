package com.genomebrain.backend.service.dto;

import java.util.Set;

public class PersonalHistoryTagsDTO {

    Set<String> tags;
    PersonalHistoryDetails personalHistoryDetails;

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }

    public PersonalHistoryDetails getPersonalHistoryDetails() {
        return personalHistoryDetails;
    }

    public void setPersonalHistoryDetails(PersonalHistoryDetails personalHistoryDetails) {
        this.personalHistoryDetails = personalHistoryDetails;
    }
}
