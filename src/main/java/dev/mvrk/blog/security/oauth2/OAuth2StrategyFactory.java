package dev.mvrk.blog.security.oauth2;

import dev.mvrk.blog.security.oauth2.strategy.OAuth2UserStrategy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class OAuth2StrategyFactory {
    private final Map<String, OAuth2UserStrategy> strategies;

    public OAuth2StrategyFactory(List<OAuth2UserStrategy> strategyList) {
        strategies = new HashMap<>();
        for (OAuth2UserStrategy strategy : strategyList) {
            strategies.put(strategy.getProviderName().toLowerCase(), strategy);
        }
    }

    public OAuth2UserStrategy getStrategy(String registrationId) {
        OAuth2UserStrategy strategy = strategies.get(registrationId.toLowerCase());
        if (strategy == null) {
            throw new UnsupportedOperationException("Unknown provider: " + registrationId);
        }
        return strategy;
    }
}
