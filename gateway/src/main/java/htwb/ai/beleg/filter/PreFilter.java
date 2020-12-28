package htwb.ai.beleg.filter;

import htwb.ai.beleg.model.User;
import htwb.ai.beleg.service.AuthUserService;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.SetStatusGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.Optional;

@Component
public class PreFilter extends SetStatusGatewayFilterFactory {

    private AuthUserService authUserService;

    public PreFilter(AuthUserService authUserService) {
        this.authUserService = authUserService;
    }

    /**
     * Gets authorized user id and forwards it to underlying service (song list)
     * @param config
     * @return GatewayFilter
     */
    @Override
    public GatewayFilter apply(SetStatusGatewayFilterFactory.Config config) {
        return (exchange, chain) ->
            Optional.ofNullable(getAuthorization(exchange.getRequest()))
                .map(token -> {
                    String authorizedUserId = getAuthorizedUserId(token);
                    if (authorizedUserId == null) {
                        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                        return exchange.getResponse().setComplete();
                    }

                    ServerHttpRequest request = exchange.getRequest().mutate()
                        .header("authorizedUserId", authorizedUserId)
                        .build();


                    return chain.filter(exchange.mutate().request(request).build());
                }).orElseGet(() -> {
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
                });
    }

    private String getAuthorization(ServerHttpRequest request) {
        if (ObjectUtils.isEmpty(request.getHeaders().get("Authorization"))) {
            return null;
        }

        return request.getHeaders().get("Authorization").get(0);
    }

    private String getAuthorizedUserId(String token) {
        User userByToken = authUserService.getUserByToken(token);
        if (userByToken == null) {
            return null;
        }
        return userByToken.getUserId();
    }

}
