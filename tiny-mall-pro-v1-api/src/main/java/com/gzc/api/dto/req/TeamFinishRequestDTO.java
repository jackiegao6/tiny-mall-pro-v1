package com.gzc.api.dto.req;

import lombok.Data;

import java.util.Map;

@Data
public class TeamFinishRequestDTO {
    private Map<String, Map<String, String>> user2orderMap;

}
