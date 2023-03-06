package com.ohh_really.ringdingdong.location.kakaoapi;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.Optional;

@Component
public class KakaoApi {

    RestTemplate restTemplate = new RestTemplate();

    @Value("${KAKAO_API_KEY}")
    private String apiKey;

    public Optional<Document> getRegion(Double X, Double Y) {
        String url = "https://dapi.kakao.com/v2/local/geo/coord2regioncode.json";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url).queryParam("x", X).queryParam("y", Y);


        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + apiKey);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<Root> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity, Root.class);

        ArrayList<Document> documents = response.getBody().getDocuments();

        for (Document document : documents) {
            {
                if (document.getRegionType().equals("B")) {
                    return Optional.of(document);
                }
            }
        }
        return Optional.empty();
    }
}
