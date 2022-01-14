package nus.iss.day18weather.controller;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import nus.iss.day18weather.model.Weather;
import nus.iss.day18weather.service.CityWeatherService;

@Controller
@RequestMapping(path="/city")
public class WeatherController {

    private static final Logger logger = Logger.getLogger(WeatherController.class.getName());

    @Autowired
    private CityWeatherService cityWeatherSvc;

    @GetMapping
    public String getWeather(@RequestBody MultiValueMap<String, String> form, Model model) {
        String cityName = form.getFirst("name");
        model.addAttribute("city", new Weather());
        return "cityWeather";
    }
    
}
