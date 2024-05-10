package br.com.gustavodevito.api;

import br.com.gustavodevito.template.dto.ListTemplateRequest;
import br.com.gustavodevito.template.dto.ListTemplateResponse;
import br.com.gustavodevito.message.dto.VerifyMessageRequest;
import br.com.gustavodevito.message.dto.VerifyMessageResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiRepository {

    @POST("/commands")
    Call<VerifyMessageResponse> verifyMessage(@Body VerifyMessageRequest verifyMessageRequest);

    @POST("/commands")
    Call<ListTemplateResponse> listTemplates(@Body ListTemplateRequest listTemplateRequest);

}
