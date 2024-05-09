package br.com.gustavodevito.dto;

import java.util.UUID;

public record VerifyMessageRequest(String id, String to, String method, String uri) {

    public static VerifyMessageRequest init(String id) {
        return new VerifyMessageRequest(UUID.randomUUID().toString(), "postmaster@activecampaign.msging.net", "get",
                String.format("/campaigns/%s/summaries", id));
    }

}
