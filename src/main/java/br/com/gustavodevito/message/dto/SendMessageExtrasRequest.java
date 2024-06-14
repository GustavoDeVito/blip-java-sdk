package br.com.gustavodevito.message.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SendMessageExtrasRequest {
    private String key;
    private String value;
}
