package nus.iss.day18weather.model;

import jakarta.json.Json;
import jakarta.json.JsonObject;

public class Weather {
    
    private String cityName;
    private String main;
    private String description;
    private String icon;
    private float temperature;
    private float longtitude;
    private float latitude;

    // Constructors

    // Methods
    // Created this method for cache because icon will be stored incorrectly due to setIcon() method
    // else icon will be stored as "http://openweathermap.org/img/wn/ "http://openweathermap.org/img/wn/%s@2x.png" @2x.png"
    public static Weather createForCache(JsonObject jo) {
        final Weather w = new Weather();
        w.setCityName(jo.getString("cityName"));
        w.setMain(jo.getString("main"));
        w.setDescription(jo.getString("description"));
        w.setIconForCache(jo.getString("icon"));
        w.setTemperature((float)jo.getJsonNumber("temperature").doubleValue());
        return w;
    }
    public static Weather create(JsonObject jo) {
        final Weather w = new Weather();
        w.setMain(jo.getString("main"));
        w.setDescription(jo.getString("description"));
        w.setIcon(jo.getString("icon"));
        return w;
    }

    public JsonObject toJson() {
        return Json.createObjectBuilder()
            .add("cityName", cityName)
            .add("main", main)
            .add("description", description)
            .add("icon", icon)
            .add("temperature", temperature)
            .build();
    }

    @Override
    public String toString() {
        return this.toJson().toString();
    }

    // Setters and getters
    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityName() {
        return this.cityName;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public String getMain() {
        return this.main;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    // this setter does not work well with create() method when caching
    public void setIcon(String iconCode) {
        this.icon = "http://openweathermap.org/img/wn/%s@2x.png".formatted(iconCode);
    }    
    
    // created to store icon details correctly
    public void setIconForCache(String icon) {
        this.icon = icon;
    }

    public String getIcon() {
        return this.icon;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public float getTemperature() {
        return this.temperature;
    }

    public void setLongtitude(float longtitude) {
        this.longtitude = longtitude;
    }

    public float getLongtitude() {
        return this.longtitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLatitude() {
        return this.latitude;
    }

}
