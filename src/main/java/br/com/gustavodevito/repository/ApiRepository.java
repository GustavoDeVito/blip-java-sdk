package br.com.gustavodevito.repository;

import br.com.gustavodevito.dto.ListTemplateRequest;
import br.com.gustavodevito.dto.ListTemplateResponse;
import br.com.gustavodevito.dto.VerifyMessageRequest;
import br.com.gustavodevito.dto.VerifyMessageResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiRepository {

    @POST("/commands")
    Call<VerifyMessageResponse> verifyMessage(@Body VerifyMessageRequest verifyMessageRequest);

    @POST("/commands")
    Call<ListTemplateResponse> listTemplates(@Body ListTemplateRequest listTemplateRequest);

}
