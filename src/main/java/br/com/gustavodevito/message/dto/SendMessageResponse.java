package br.com.gustavodevito.message.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SendMessageResponse {
    private String type;
    private Resource resource;
    private Reason reason;
    private String method;
    private String status;
    private String id;
    private String from;
    private String to;
    private Metadata metadata;

    @Data
    @AllArgsConstructor
    public static class Resource {
        private String id;
        private String name;
        private String campaignType;
        private String masterState;
        private String flowId;
        private String stateId;
        private String status;
        private String created;
    }

    @Data
    @AllArgsConstructor
    public static class Reason {
        private int code;
        private String description;
    }

    @Data
    @AllArgsConstructor
    public static class Metadata {
        private String traceparent;
        private String commandUri;
    }
}
