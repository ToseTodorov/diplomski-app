package mk.ukim.finki.diplomski.client;

import mk.ukim.finki.diplomski.aplication.EmailService;
import mk.ukim.finki.diplomski.domain.value.UserId;
import mk.ukim.finki.sharedkernel.domain.dto.MailDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class EmailServiceClient implements EmailService {

    private final RestTemplate restTemplate;
    private final String serverUrl;

    public EmailServiceClient(@Value("${app.user-management.url}") String serverUrl,
                             @Value("${app.user-management.connect-timeout-ms}") int connectTimeout,
                             @Value("${app.user-management.read-timeout-ms}") int readTimeout){
        this.serverUrl = serverUrl;
        this.restTemplate = new RestTemplate();

        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(connectTimeout);
        requestFactory.setReadTimeout(readTimeout);

        restTemplate.setRequestFactory(requestFactory);
    }

    private UriComponentsBuilder uri() {
        return UriComponentsBuilder.fromUriString(serverUrl);
    }

    @Override
    public void sendMail(String to, String subject, String text) {
        try {
            HttpEntity<MailDTO> requestEntity = new HttpEntity<>(new MailDTO(to,subject,text));
            restTemplate.exchange(
                    uri().path(String.format("/mail/send")).build().toUri(),
                    HttpMethod.POST,
                    requestEntity,
                    new ParameterizedTypeReference<Void>() {
                    }
            ).getBody();

        } catch (Exception ex) {
            System.err.printf("Error retrieving users by id; %s\n", ex);
        }
    }
}
