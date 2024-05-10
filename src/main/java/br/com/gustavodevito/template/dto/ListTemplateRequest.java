package br.com.gustavodevito.template.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ListTemplateRequest {
    private String id;
    private String to;
    private String method;
    private String uri;

    public static ListTemplateRequest init() {
        return new ListTemplateRequest(UUID.randomUUID().toString(), "postmaster@wa.gw.msging.net", "get",
                "/message-templates");
    }
}
