package smarthur.space.astrotoday.model.enums;

import smarthur.space.astrotoday.util.Constants;

public enum GalaxyEnum implements SkyObjectEnum {
    PINWHEEL_GALAXY_M101(
            "Pinwheel Galaxy (M101)",
            Constants.SKY_LIVE_INFO_PREFIX + "messier-101-the-pinwheel-galaxy-object",
            Constants.WIKIMEDIA_PREFIX + "/thumb/c/c5/M101_hires_STScI-PRC2006-10a.jpg/1280px-M101_hires_STScI-PRC2006-10a.jpg"),
    BODES_GALAXY_M81("Bode's Galaxy (M81)",
            Constants.SKY_LIVE_INFO_PREFIX + "messier-81-bodes-galaxy-object",
            Constants.WIKIMEDIA_PREFIX + "/thumb/6/63/Messier_81_HST.jpg/1280px-Messier_81_HST.jpg"),
    SOUTHERN_PINWHEEL_GALAXY_M83("Southern Pinwheel Galaxy (M83)",
            Constants.SKY_LIVE_INFO_PREFIX + "messier-83-southern-pinwheel-galaxy-object",
            Constants.WIKIMEDIA_PREFIX + "/thumb/d/d5/Hubble_view_of_barred_spiral_galaxy_Messier_83.jpg/1280px-Hubble_view_of_barred_spiral_galaxy_Messier_83.jpg"),
    TRIANGULUM_GALAXY_M33("Triangulum Galaxy (M33)",
            Constants.SKY_LIVE_INFO_PREFIX + "messier-33-triangulum-galaxy-object",
            Constants.WIKIMEDIA_PREFIX + "/thumb/6/64/VST_snaps_a_very_detailed_view_of_the_Triangulum_Galaxy.jpg/859px-VST_snaps_a_very_detailed_view_of_the_Triangulum_Galaxy.jpg"),
    SCULPTOR_GALAXY_NGC253("Sculptor Galaxy (NGC 253)",
            Constants.SKY_LIVE_INFO_PREFIX + "ngc253-sculptor-filament-object",
            Constants.WIKIMEDIA_PREFIX + "/thumb/0/08/Sculptor_Galaxy_by_VISTA.jpg/1280px-Sculptor_Galaxy_by_VISTA.jpg"),
    CROCS_EYE_GALAXY("Croc's Eye Galaxy (M94)",
            Constants.SKY_LIVE_INFO_PREFIX + "messier-94-crocs-eye-galaxy-object",
            Constants.WIKIMEDIA_PREFIX + "/1/1c/Messier_94.jpg"),
    VACUUM_CLEANER_GALAXY_M109("Vacuum Cleaner Galaxy (M109)",
        Constants.SKY_LIVE_INFO_PREFIX +"messier-109-object",
        Constants.WIKIMEDIA_PREFIX +"/3/3d/Messier109_-_SDSS_DR14_(panorama).jpg"),
    NGC_2997("NGC 2997",
           Constants.SKY_LIVE_INFO_PREFIX +"ngc2997-object",
           Constants.WIKIMEDIA_PREFIX +"/4/4c/N2997s.jpg");

    public String name;
    public String url;
    public String imageUrl;

    GalaxyEnum(String name, String url, String imageUrl) {
        this.name = name;
        this.url = url;
        this.imageUrl = imageUrl;
    }
}
