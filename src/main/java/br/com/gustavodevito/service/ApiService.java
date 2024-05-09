package br.com.gustavodevito.service;

import java.io.IOException;

import br.com.gustavodevito.dto.ListTemplateRequest;
import br.com.gustavodevito.dto.VerifyMessageRequest;
import br.com.gustavodevito.repository.ApiRepository;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiService {
    private static final String API_KEY;

    private final Retrofit retrofit;

    static {
        API_KEY = System.getenv("API_KEY");
    }

    public ApiService(Retrofit.Builder builder) {
        if (API_KEY.isEmpty()) {
            throw new IllegalArgumentException("API KEY is not defined.");
        }

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request originalRequest = chain.request();
                    Request.Builder newRequestBuilder = originalRequest.newBuilder()
                            .addHeader("Authorization", API_KEY);
                    Request newRequest = newRequestBuilder.build();
                    return chain.proceed(newRequest);
                })
                .build();

        this.retrofit = builder.baseUrl("https://msging.net/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient).build();
    }

    public void verifyMessage(String id) throws IOException {
        var content = VerifyMessageRequest.init(id);

        var request = this.retrofit.create(ApiRepository.class).verifyMessage(content).execute();

        if (request.isSuccessful()) {
            System.out.printf("Success Call, Message Status: %s",
                    request.body().getResource().getItems().getFirst().getStatusAudience().getFirst().getStatus());
        } else {
            System.out.printf("Error Call %s", request.errorBody().toString());
        }
    }

    public void getTemplates() throws IOException {
        var content = ListTemplateRequest.init();

        var request = this.retrofit.create(ApiRepository.class).listTemplates(content).execute();

        if (request.isSuccessful()) {
            for (var item : request.body().getResource().getData()) {
                System.out.printf("Template %s - Status %s \n", item.getName(), item.getStatus());
            }
        } else {
            System.out.printf("Error Call %s", request.errorBody().toString());
        }
    }

}
