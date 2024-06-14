package br.com.gustavodevito.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.com.gustavodevito.template.dto.ListTemplateRequest;
import br.com.gustavodevito.template.dto.ListTemplateResponse.DataItem;
import br.com.gustavodevito.message.dto.SendMessageExtrasRequest;
import br.com.gustavodevito.message.dto.SendMessageRequest;
import br.com.gustavodevito.message.dto.SendMessageResponse;
import br.com.gustavodevito.message.dto.VerifyMessageRequest;
import br.com.gustavodevito.message.dto.VerifyMessageResponse.StatusAudience;
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

    public Optional<StatusAudience> verifyMessage(String id) throws IOException {
        var content = VerifyMessageRequest.init(id);

        var request = this.retrofit.create(ApiRepository.class).verifyMessage(content).execute();

        if (request.isSuccessful()) {
            return Optional.of(request.body().getResource().getItems().getFirst().getStatusAudience().getFirst());
        } else {
            return Optional.empty();
        }
    }

    public List<DataItem> getTemplates() throws IOException {
        var content = ListTemplateRequest.init();

        var request = this.retrofit.create(ApiRepository.class).listTemplates(content).execute();

        if (request.isSuccessful()) {
            return request.body().getResource().getData();
        } else {
            return Collections.emptyList();
        }
    }

    public Optional<DataItem> getTemplate(String templateName) throws IOException {
        var content = ListTemplateRequest.init();

        var request = this.retrofit.create(ApiRepository.class).listTemplates(content).execute();

        if (request.isSuccessful()) {
            return Optional.of(request.body().getResource().getData().stream()
                    .filter(item -> item.getName().equals(templateName))
                    .toList().getFirst());
        } else {
            return Optional.empty();
        }
    }

    public boolean verifyPhone(String phone) {
        String regex = "^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$";
        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(phone);

        return matcher.matches();
    }

    public boolean verifyParams(String templateName, List<String> params) throws IOException {
        var template = getTemplate(templateName);

        if (template.isEmpty()) {
            return false;
        }

        var media = params.getFirst();

        var text = new ArrayList<String>(params);

        var isMedia = !"BODY".equals(template.get().getComponents().getFirst().getType());

        if (isMedia) {
            text.removeFirst();
            var format = template.get().getComponents().getFirst().getFormat();

            if (format.equals("IMAGE")) {
                String regex = "/([a-z\\-_0-9\\/\\:\\.]*\\.(jpg|jpeg|png|gif))/i";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(media);

                if (!matcher.matches()) {
                    return false;
                }
            } else if (format.equals("DOCUMENT")) {
                String regex = "/([a-z\\-_0-9\\/\\:\\.]*\\.(pdf))/i";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(media);

                if (!matcher.matches()) {
                    return false;
                }
            } else if (format.equals("VIDEO")) {
                String regex = "^(https?://)([^\\s]+).(mp4)$";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(media);

                if (!matcher.matches()) {
                    return false;
                }
            }
        }

        var body = template.get().getComponents().stream().filter(item -> item.getType().equals("BODY")).toList()
                .getFirst();

        return body.getExample().getBody_text().getFirst().size() == text.size();
    }

    public SendMessageResponse sendMessage(String identity, String phone, String templateName, List<String> params,
            String masterState,
            String flowId, String stateId, List<SendMessageExtrasRequest> extras) throws IOException {
        var isValidPhone = this.verifyPhone(phone);
        if (!isValidPhone) {
            throw new IllegalArgumentException("Não foi possível validar o número de celular");
        }

        var template = this.getTemplate(templateName);
        if (template.isEmpty()) {
            throw new IllegalArgumentException("Não foi possível encontrar o template #" + templateName);
        }

        var isValidParams = this.verifyParams(templateName, params);
        if (!isValidParams) {
            throw new IllegalArgumentException("Parâmetros da mensagem inválido");
        }

        Map<String, Object> campaign = new HashMap<>();
        campaign.put("name", identity + "-" + UUID.randomUUID().toString());
        campaign.put("campaignType", "Individual");
        campaign.put("masterState", masterState + "@msging.net");
        campaign.put("flowId", flowId);
        campaign.put("stateId", stateId);

        Map<String, Object> audience = new HashMap<>();
        audience.put("recipient", phone);

        Map<String, Object> messageParamsAudience = new HashMap<>();
        audience.put("messageParams", messageParamsAudience);

        Map<String, Object> message = new HashMap<>();
        message.put("messageTemplate", templateName);

        List<String> messageParamsList = new ArrayList<>();
        message.put("messageParams", messageParamsList);

        for (int i = 0; i < params.size(); i++) {
            int indexParam = i + 1;

            messageParamsList.add(String.valueOf(indexParam));

            messageParamsAudience.put(String.valueOf(indexParam), params.get(i));
        }

        extras.forEach(item -> messageParamsAudience.put(item.getKey(), item.getValue()));

        if (audience.get("messageParams") != null && ((Map<?, ?>) audience.get("messageParams")).isEmpty()) {
            audience.remove("messageParams");
        }

        if (messageParamsList.isEmpty()) {
            message.remove("messageParams");
        }

        var content = SendMessageRequest.init(campaign, audience, message);

        var request = this.retrofit.create(ApiRepository.class).sendMessage(content).execute();

        if (request.body().getStatus().equals("success")) {
            return request.body();
        } else {
            throw new IllegalArgumentException("Não foi possível enviar a mensagem.");
        }
    }

}
