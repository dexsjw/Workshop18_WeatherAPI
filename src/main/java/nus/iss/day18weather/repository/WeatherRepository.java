package nus.iss.day18weather.repository;

import static nus.iss.day18weather.Constants.BEAN_WEATHER_CACHE;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import nus.iss.day18weather.Day18weatherApplication;

@Repository
public class WeatherRepository {

    private Logger logger = Logger.getLogger(Day18weatherApplication.class.getName());

    @Autowired
    @Qualifier(BEAN_WEATHER_CACHE)
    private RedisTemplate<String, String> redisTemplate;

    public void save(String cityName, String weatherInfo) {
        redisTemplate.opsForValue().set(normalize(cityName), weatherInfo, 10L, TimeUnit.MINUTES);
    }

    public Optional<String> get(String cityName) {
        Optional<String> cityCache = Optional.ofNullable(redisTemplate.opsForValue().get(normalize(cityName)));
        return cityCache;
    }

    public String normalize(String s) {
        return s.trim().toLowerCase();
    }
    
}
