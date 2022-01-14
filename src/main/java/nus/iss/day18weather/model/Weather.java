package nus.iss.day18weather.model;

public class Weather {
    
    private String cityName;
    private String main;
    private String description;
    private String icon;
    private Float temperature;
    private Float longtitude;
    private Float latitude;

    public void setcityName(String cityName) {
        this.cityName = cityName;
    }

    public String getcityName() {
        return this.cityName;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public String getmain() {
        return this.main;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    public void setIcon(String iconCode) {
        this.icon = "http://openweathermap.org/img/wn/%s@2x.png".formatted(iconCode);
    }

    public String getIcon() {
        return this.icon;
    }

    public void setTemperature(Float temperature) {
        this.temperature = temperature;
    }

    public Float getTemperature() {
        return this.temperature;
    }

    public void setLongtitude(Float longtitude) {
        this.longtitude = longtitude;
    }

    public Float getLongtitude() {
        return this.longtitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public Float getLatitude() {
        return this.latitude;
    }

}
