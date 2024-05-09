package br.com.gustavodevito.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VerifyMessageResponse {
    private String type;
    private Resource resource;
    private String method;
    private String status;

    @Data
    @AllArgsConstructor
    public static class Resource {
        private Integer total;
        private String itemType;
        private List<Item> items;
    }

    @Data
    @AllArgsConstructor
    public static class Item {
        private String id;
        private String name;
        private String messageTemplate;
        private String masterState;
        private String flowId;
        private String stateId;
        private String sendDate;
        private List<StatusAudience> statusAudience;
    }

    @Data
    @AllArgsConstructor
    public static class StatusAudience {
        private String recipientIdentity;
        private String status;
        private String processed;
        private String received;
        private String numberStatus;
    }

    @Data
    @AllArgsConstructor
    public static class Metadata {
        private String traceparent;
        private String commandUri;
    }
}
