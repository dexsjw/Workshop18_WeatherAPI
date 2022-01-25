package nus.iss.day18weather.service;

import static nus.iss.day18weather.Constants.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonValue;
import nus.iss.day18weather.Day18weatherApplication;
import nus.iss.day18weather.model.Weather;
import nus.iss.day18weather.repository.WeatherRepository;

@Service
public class WeatherService {

    @Autowired
    private WeatherRepository weatherRepo;

    private static final Logger logger = Logger.getLogger(Day18weatherApplication.class.getName());
    private final String apiKey;

    public WeatherService() {
        String k = System.getenv(ENV_OPENWEATHERMAP_KEY);
        if ((null != k) && (k.trim().length() > 0)) {
            apiKey = k;
            logger.log(Level.INFO, "OPENWEATHERMAP_KEY set!");
        } else {
            apiKey = "";
            logger.log(Level.WARNING, "OPENWEATHERMAP_KEY not set!"); // set password in env variables
            System.exit(1);
        }
    }
    
    public List<Weather> getWeather(String city) {

        final String url = UriComponentsBuilder
            .fromUriString(URL_WEATHER)
            .queryParam("q", city.trim().replace(" ", "+"))
            .queryParam("appid", System.getenv(ENV_OPENWEATHERMAP_KEY))
            .queryParam("units", "metric")
            .toUriString();

        final RequestEntity<Void> req = RequestEntity
            .get(url)
            .accept(MediaType.APPLICATION_JSON)
            .build();

        final RestTemplate template = new RestTemplate();
        final ResponseEntity<String> resp = template.exchange(req, String.class);

        if (resp.getStatusCode() != HttpStatus.OK)
            throw new IllegalArgumentException(
                "Error: Status code %s".formatted(resp.getStatusCode())
                );

        final String respBody = resp.getBody();

        try (InputStream is = new ByteArrayInputStream(respBody.getBytes())) {
            final JsonReader reader = Json.createReader(is);
            final JsonObject data = reader.readObject();
            final JsonArray weatherInfo = data.getJsonArray("weather");
            final String cityName = data.getString("name");
            final float temperature = (float)data.getJsonObject("main").getJsonNumber("temp").doubleValue();

            return weatherInfo.stream()
            .map(v -> (JsonObject)v)
            .map(Weather::create)
            .map(w -> {
                w.setCityName(cityName);
                w.setTemperature(temperature);
                return w;
            })
            .collect(Collectors.toList());

/*             
            // Equivalent of streaming and mapping of above code
            List<Weather> weather = new ArrayList<>();
            for (JsonValue jv: weatherInfo) {
                JsonObject jo = (JsonObject)jv;
                Weather w = Weather.create(jo);
                w.setCityName(cityName);
                w.setTemperature(temperature);
                weather.add(w);
            }
            return weather;
  */           
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return Collections.emptyList();
    }

}
