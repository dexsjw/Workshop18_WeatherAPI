package nus.iss.day18weather.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import nus.iss.day18weather.Day18weatherApplication;
import nus.iss.day18weather.model.Weather;
import nus.iss.day18weather.repository.WeatherRepository;

@Service
public class WeatherCacheService {

    private Logger logger = Logger.getLogger(Day18weatherApplication.class.getName());

    @Autowired
    public WeatherRepository weatherRepo;

    public void save(String cityName, List<Weather> weather) {
        JsonArrayBuilder arrBuilder = Json.createArrayBuilder();
        weather.stream()
            .forEach(v -> arrBuilder.add(v.toJson()));
        String arrString = arrBuilder.build().toString();
/*         
        // equivalent of stream method
        for (Weather w: weather) {
            arrBuilder.add(w.toJson());
        }
 */        
        logger.info("String msg to save: %s".formatted(arrString));
        weatherRepo.save(cityName, arrString);
    }

    public Optional<List<Weather>> get(String cityName) {

        List<Weather> weather = new ArrayList<>();
        Optional<String> cityCache = weatherRepo.get(cityName);
        if (cityCache.isEmpty()) {
            return Optional.empty();
        }
        try {
            InputStream is = new ByteArrayInputStream(cityCache.get().getBytes());
            JsonReader reader = Json.createReader(is);
            JsonArray ja = reader.readArray();
            weather = ja.stream()
                .map(v -> (JsonObject)v)
                .map(Weather::createForCache)
                .collect(Collectors.toList());
            logger.info("Cached info: %s".formatted(weather));
            return Optional.of(weather);
        } catch (Exception e) {
            logger.log(Level.WARNING, "InputStream error", e);
        }
        return Optional.ofNullable(weather);
    }
/* 
    // Create a method to take in String and return JsonArray/JsonObject to be used in both services
    private JsonArray toJsonArray(String s) {
        try {
            InputStream is = new ByteArrayInputStream(s.getBytes());
            JsonReader reader = Json.createReader(is);
            return reader.readArray();
        } catch (Exception e) {
            logger.log(Level.WARNING, "Parsing error", e);
        }
        return Json.createArrayBuilder().build();
    }
 */    
}
