package com.example.mushrooming.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class NotificationEmail {
    private String subject;
    private String recipient;
    private String body;
}
