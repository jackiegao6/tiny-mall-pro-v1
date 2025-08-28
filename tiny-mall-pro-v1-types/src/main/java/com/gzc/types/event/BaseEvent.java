package com.gzc.types.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

public abstract class BaseEvent<T> {

    public abstract String getTopic();
    public abstract EventMessage<T> getEventMessage(T data);

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class EventMessage<T>{
        private String eventId;
        private Date timestamp;
        private T data;
    }

}
