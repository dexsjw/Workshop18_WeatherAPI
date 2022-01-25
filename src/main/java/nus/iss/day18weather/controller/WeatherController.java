package nus.iss.day18weather.controller;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import nus.iss.day18weather.Day18weatherApplication;
import nus.iss.day18weather.model.Weather;
import nus.iss.day18weather.service.WeatherCacheService;
import nus.iss.day18weather.service.WeatherService;

@Controller
@RequestMapping(path="/weather")
public class WeatherController {

    private static final Logger logger = Logger.getLogger(Day18weatherApplication.class.getName());

    @Autowired
    private WeatherCacheService weatherCacheSvc;

    @Autowired
    private WeatherService weatherSvc;

    @GetMapping
    public String getWeather(@RequestParam(required = true) String city, Model model) {
        logger.log(Level.INFO, "City: %s".formatted(city));

        Optional<List<Weather>> weatherCache = weatherCacheSvc.get(city);
        List<Weather> weatherList = Collections.emptyList();
        Weather weather = new Weather();

        if (weatherCache.isPresent()) {
            logger.info("Cache hit for %s".formatted(city));
            weatherList = weatherCache.get();
            // logger.info("City Name: %s".formatted(weatherList.get(0).getCityName()));
        } else {
            try {
                weatherList = weatherSvc.getWeather(city);
                if (weatherList.size() > 0) {
                    logger.info("Saving... %s".formatted(city));
                    weatherCacheSvc.save(city, weatherList);
                }
            } catch (Exception e){
                logger.log(Level.WARNING, "Warning: %s".formatted(e.getMessage()));
            }
        }

        if (weatherList.size() > 0) {
            weather = weatherList.get(0);
            city = weatherList.get(0).getCityName();
        }

        model.addAttribute("weather", weather);
        model.addAttribute("city", city);
        return "cityWeather";
    }

    @GetMapping(path="/{city}", produces="application/json")
    public @ResponseBody ResponseEntity<String> getCityWeather(@PathVariable String city) {
        logger.log(Level.INFO, "City: %s".formatted(city));
        List<Weather> weatherInfo = weatherSvc.getWeather(city);
        Weather cityWeather = weatherInfo.get(0);
        final JsonObject resp = Json.createObjectBuilder()
            .add("name", cityWeather.getCityName())
            .add("main", cityWeather.getMain())
            .add("description", cityWeather.getDescription())
            .add("icon", cityWeather.getIcon())
            .add("temperature", cityWeather.getTemperature())
            .build();

        return ResponseEntity.ok(resp.toString());
    }
    
}
