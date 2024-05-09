package br.com.gustavodevito;

import java.io.IOException;

import br.com.gustavodevito.service.ApiService;
import retrofit2.Retrofit;

public class App {
    public static void main(String[] args) throws IOException {
        ApiService apiService = new ApiService(new Retrofit.Builder());
        // apiService.verifyMessage("83af4900-3825-4b47-b791-14976ee44876");
        apiService.getTemplates();
    }
}
