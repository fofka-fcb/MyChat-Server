package org.example.domain;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Message {
    private final String from;
    private final String to_text;
}
