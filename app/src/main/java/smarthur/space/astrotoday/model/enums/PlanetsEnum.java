package smarthur.space.astrotoday.model.enums;

import smarthur.space.astrotoday.model.enums.SkyObjectEnum;

public enum PlanetsEnum implements SkyObjectEnum {
    MERCURY("Mercury", -2.48f, 7.25f, 4.5f, 13f),
    VENUS("Venus", -4.92f, -2.98f, 9.7f, 66.0f),
    MARS("Mars", -2.94f,1.86f,3.5f,25.1f),
    JUPITER("Jupiter", -2.94f, 1.66f, 29.8f, 50.1f),
    SATURN("Saturn", -0.55f, 1.5f, 14.5f, 20.1f),
    URANUS("Uranus", 5.38f, 6.03f, 3.3f, 4.1f),
    NEPTUNE("Neptune", 7.67f, 8.00f, 2.2f, 2.4f);

    public String name;
    public String url;
    public float minMagnitude;
    public float maxMagnitude;
    public float minSize;
    public float maxSize;

    PlanetsEnum(String name, float minMagnitude, float maxMagnitude, float minSize, float maxSize) {
        this.name = name;
        this.minMagnitude = minMagnitude;
        this.maxMagnitude = maxMagnitude;
        this.minSize = minSize;
        this.maxSize = maxSize;
        this.url = String.format("https://theskylive.com/%s-info", name.toLowerCase());
    }
}
