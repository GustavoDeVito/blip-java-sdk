package br.com.gustavodevito.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ListTemplateResponse {
    private String type;
    private Resource resource;
    private String method;
    private String status;
    private String id;
    private String from;
    private String to;
    private Metadata metadata;

    @Data
    @AllArgsConstructor
    public static class Resource {
        private List<DataItem> data;
    }

    @Data
    @AllArgsConstructor
    public static class DataItem {
        private String id;
        private String category;
        private List<Object> components;
        private String language;
        private String lastUpdatedTime;
        private String name;
        private String rejectedReason;
        private String status;
    }

    @Data
    @AllArgsConstructor
    public static class Metadata {
        private String commandUri;
        private String uberTraceId;
    }
}
