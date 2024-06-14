package br.com.gustavodevito;

import java.io.IOException;
import java.util.List;

import br.com.gustavodevito.api.ApiService;
import br.com.gustavodevito.message.dto.SendMessageExtrasRequest;
import br.com.gustavodevito.message.dto.VerifyMessageResponse.Item;
import retrofit2.Retrofit;

public class App {
    public static void main(String[] args) throws IOException {
        ApiService apiService = new ApiService(new Retrofit.Builder());

        // var verifyMessage = apiService.verifyMessage("5798df5f-48c5-4757-b7eb-1fb14634b126");
        // System.out.println(verifyMessage.toString());

        // var getTemplates = apiService.getTemplates();
        // System.out.println(getTemplates.getLast().toString());

        // var getTemplate = apiService.getTemplate("employee_04");
        // System.out.println(getTemplate.toString());

        // var verifyPhone = apiService.verifyPhone("+5519997926206");
        // System.out.println(verifyPhone);

        // var verifyParams = apiService.verifyParams("clinical_staff_nurse_07",
        // List.of("1", "2", "3"));
        // System.out.println(verifyParams);

        // var sendMessage = apiService.sendMessage("Teste", "+5519992806076",
        // "test_employee_11", List.of("Tester"),
        // "menuprincipal118",
        // "d0811ffa-1c90-4681-99bc-10777026a9d0",
        // "b3f95944-c074-45a3-998b-beb8f0124a64", List.of());
        // System.out.println(sendMessage.getResource().getId());

        // var sendMessage = apiService.sendMessage("Teste", "+5519992806076", "test_employee_11",
        //         List.of("Tester"), "menuprincipal118",
        //         "d0811ffa-1c90-4681-99bc-10777026a9d0",
        //         "b3f95944-c074-45a3-998b-beb8f0124a64",
        //         List.of(new SendMessageExtrasRequest("blipSDK", "Java")));
        // System.out.println(sendMessage.getResource().getId());
    }
}
