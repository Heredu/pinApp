package com.pinApp.customerManagement.mq.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmailEvent {
    private String to;
    private String subject;
    private String body;
    private boolean isHtml;
}
