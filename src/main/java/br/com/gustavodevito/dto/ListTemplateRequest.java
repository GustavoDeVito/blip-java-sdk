package br.com.gustavodevito.dto;

import java.util.UUID;

public record ListTemplateRequest(String id, String to, String method, String uri) {

    public static ListTemplateRequest init() {
        return new ListTemplateRequest(UUID.randomUUID().toString(), "postmaster@wa.gw.msging.net", "get",
                "/message-templates");
    }

}
