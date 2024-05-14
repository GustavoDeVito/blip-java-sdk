package br.com.gustavodevito.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.com.gustavodevito.template.dto.ListTemplateRequest;
import br.com.gustavodevito.template.dto.ListTemplateResponse.DataItem;
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

    public Boolean verifyPhone(String phone) {
        String regex = "^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$";
        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(phone);

        return matcher.matches();

    }

    public Boolean verifyParams(String templateName, List<String> params) throws IOException {
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

}
