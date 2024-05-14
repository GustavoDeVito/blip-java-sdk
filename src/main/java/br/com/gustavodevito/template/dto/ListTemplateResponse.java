package br.com.gustavodevito.template.dto;

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
        private List<Component> components;
        private String language;
        private String lastUpdatedTime;
        private String name;
        private String rejectedReason;
        private String status;
    }

    @Data
    @AllArgsConstructor
    public static class Component {
        private String type;
        private String format;
        private String text;
        private Example example;
    }

    @Data
    @AllArgsConstructor
    public static class Example {
        private List<String> header_handler;
        private List<List<String>> body_text;
    }

    @Data
    @AllArgsConstructor
    public static class Metadata {
        private String commandUri;
        private String uberTraceId;
    }
}
