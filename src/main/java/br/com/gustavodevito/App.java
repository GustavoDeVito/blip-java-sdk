package br.com.gustavodevito;

import java.io.IOException;
import java.util.List;

import br.com.gustavodevito.api.ApiService;
import retrofit2.Retrofit;

public class App {
    public static void main(String[] args) throws IOException {
        ApiService apiService = new ApiService(new Retrofit.Builder());

        // var verifyMessage =
        // apiService.verifyMessage("83af4900-3825-4b47-b791-14976ee44876");
        // System.out.println(verifyMessage.toString());

        // var getTemplates = apiService.getTemplates();
        // System.out.println(getTemplates.getLast().toString());

        // var getTemplate = apiService.getTemplate("employee_04");
        // System.out.println(getTemplate.toString());

        // var verifyPhone = apiService.verifyPhone("5519997926206");
        // System.out.println(verifyPhone);

        var verifyParams = apiService.verifyParams("clinical_staff_nurse_07", List.of(
                "1", "2", "3"));

        System.out.println(verifyParams);

    }

}
