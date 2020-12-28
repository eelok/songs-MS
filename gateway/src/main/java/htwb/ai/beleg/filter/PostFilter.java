package htwb.ai.beleg.filter;

import htwb.ai.beleg.model.User;
import htwb.ai.beleg.service.AuthUserService;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.SetStatusGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Component
public class PostFilter extends SetStatusGatewayFilterFactory {

    private AuthUserService authUserService;

    public PostFilter(AuthUserService authUserService) {
        this.authUserService = authUserService;
    }

    /**
     * Check authorization token and forwards the request accordingly to song service
     * if token is valid. Otherwise returns unauthorized status
     * @param config
     * @return GatewayFilter
     */
    @Override
    public GatewayFilter apply(SetStatusGatewayFilterFactory.Config config) {
        return (exchange, chain) -> Optional.ofNullable(getAuthorization(exchange))
            .map(token -> {
                boolean isTokenValid = isTokenValid(token);
                if (!isTokenValid) {
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
                }
                return chain.filter(exchange);
            }).orElseGet(() -> {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            });
    }

    private String getAuthorization(org.springframework.web.server.ServerWebExchange exchange) {
        if (ObjectUtils.isEmpty(exchange.getRequest().getHeaders().get("Authorization"))) {
            return null;
        }

        return exchange.getRequest().getHeaders().get("Authorization").get(0);
    }

    private boolean isTokenValid(String token) {
        if (StringUtils.isEmpty(token)) {
            return false;
        }
        User userFromToken = authUserService.getUserByToken(token);
        if (userFromToken == null) {
            return false;
        }
        return true;
    }

}
