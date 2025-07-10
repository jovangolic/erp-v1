package com.jovan.erp_v1.response;

import java.time.LocalDateTime;

import com.jovan.erp_v1.model.EventLog;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventLogResponse {
    private Long id;
    private LocalDateTime timestamp;
    private String description;

    public EventLogResponse(EventLog log) {
        this.id = log.getId();
        this.timestamp = log.getTimestamp();
        this.description = log.getDescription();
    }
}