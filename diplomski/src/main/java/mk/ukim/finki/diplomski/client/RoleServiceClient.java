package mk.ukim.finki.diplomski.client;


import mk.ukim.finki.diplomski.aplication.RoleService;
import mk.ukim.finki.sharedkernel.domain.role.RoleName;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

@Service
public class RoleServiceClient implements RoleService {

    private final RestTemplate restTemplate;
    private final String serverUrl;

    public RoleServiceClient(@Value("${app.user-management.url}") String serverUrl,
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
    public UUID findRoleIdByRoleName(RoleName roleName) {
        try {
            // /api/roles/{roleName}/id
            return restTemplate.exchange(uri().path(String.format("/api/roles/%s/id", roleName)).build().toUri(), HttpMethod.GET, null,
                    new ParameterizedTypeReference<UUID>() {
                    }).getBody();
        } catch (Exception ex) {
            System.err.printf("Error retrieving product by id; %s\n", ex);
            return null;
        }
    }

    @Override
    public RoleName findRoleNameByRoleId(UUID roleId){
        try {
            return restTemplate.exchange(
                    uri().path(String.format("/api/roles/%s/name", roleId.toString())).build().toUri(),
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<RoleName>() {
                    }).getBody();
        } catch (Exception ex) {
            System.err.printf("Error retrieving roleName by id; %s\n", ex);
            return null;
        }
    }

}
