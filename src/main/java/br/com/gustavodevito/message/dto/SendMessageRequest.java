package br.com.gustavodevito.message.dto;

import java.util.Map;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SendMessageRequest {
    private String id;
    private String to;
    private String method;
    private String uri;
    private String type;
    private Resource resource;

    @Data
    @AllArgsConstructor
    public static class Resource {
        private Map<String, Object> campaign;
        private Map<String, Object> audience;
        private Map<String, Object> message;
    }

    public static SendMessageRequest init(Map<String, Object> campaign, Map<String, Object> audience,
            Map<String, Object> message) {

        return new SendMessageRequest(UUID.randomUUID().toString(), "postmaster@activecampaign.msging.net", "set",
                "/campaign/full", "application/vnd.iris.activecampaign.full-campaign+json",
                new Resource(campaign, audience, message));
    }
}
