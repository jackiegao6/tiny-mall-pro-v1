package com.gzc.api.dto.req;

import lombok.Data;

import java.util.List;

@Data
public class TeamFinishNotifyRequestDTO {

    private String teamId;
    private List<String> outTradeNoList;

}
