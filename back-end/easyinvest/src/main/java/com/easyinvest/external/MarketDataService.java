package com.easyinvest.external;

import com.easyinvest.dtos.MarketResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.net.URI;

@Service
public class MarketDataService {

    private final RestTemplate restTemplate;

    @Value("${alphavantage.api.key}")
    private String apiKey;

    public MarketDataService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public BigDecimal getCurrentPrice (String ticker) {
        String url =
                "https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol="
                + ticker
                        + ".SA&apikey="
                        + apiKey;

        MarketResponseDTO response =
                restTemplate.getForObject(url, MarketResponseDTO.class);

        if (response == null || response.getGlobalQuote() == null || response.getGlobalQuote().getPrice() == null) {
            throw new IllegalArgumentException("Não foi possível obter o preço do ativo");
        }

        return new BigDecimal(
                response.getGlobalQuote().getPrice()
        );
    }
}
