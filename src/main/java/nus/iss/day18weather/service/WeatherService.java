package nus.iss.day18weather.service;

import static nus.iss.day18weather.Constants.*;

import java.net.URL;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import nus.iss.day18weather.model.Weather;

@Service
public class WeatherService {

    private static final Logger logger = Logger.getLogger(WeatherService.class.getName());
    private final String apiKey;

    public WeatherService() {
        String k = System.getenv(ENV_OPENWEATHERMAP_KEY);
        if ((null != k) && (k.trim().length() > 0))
            apiKey = k;
        else
        apiKey = "";
        logger.log(Level.WARNING, "Please set required API Key in Environmental Variables first before running program");
        System.exit(1);
    }
    

    public List<Weather> getWeather(String city) {

        final String url = UriComponentsBuilder
            .fromUriString(URL_WEATHER)
            .queryParam("q", city)
            .queryParam("appid", apiKey)
            .queryParam("units", "metric")
            .toUriString();

        final RequestEntity<Void> req = RequestEntity
            .get(url)
            .accept(MediaType.APPLICATION_JSON)
            .build();

        final RestTemplate template = new RestTemplate();
        
    }

}
