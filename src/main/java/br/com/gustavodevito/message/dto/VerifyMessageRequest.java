package br.com.gustavodevito.message.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VerifyMessageRequest {
    private String id;
    private String to;
    private String method;
    private String uri;

    public static VerifyMessageRequest init(String id) {
        return new VerifyMessageRequest(UUID.randomUUID().toString(), "postmaster@activecampaign.msging.net", "get",
                String.format("/campaigns/%s/summaries", id));
    }
}
