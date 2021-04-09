package smarthur.space.astrotoday.model.enums;

import smarthur.space.astrotoday.util.Constants;

public enum PlanetaryNebulaeEnum implements SkyObjectEnum {
    SATURN_NEBULA_NGC_7009("Saturn Nebula (NGC 7009)",
            Constants.SKY_LIVE_INFO_PREFIX + "ngc7009-saturn-nebula-object",
            Constants.WIKIMEDIA_PREFIX + "/c/c9/MUSE_image_of_the_Saturn_Nebula.jpg"),
    BLINKING_PLANETARY_NEBULA_NGC_6826("Blinking Planetary (NGC 6826)",
            Constants.SKY_LIVE_INFO_PREFIX + "ngc6826-blinking-planetary-object",
            Constants.WIKIMEDIA_PREFIX + "/8/83/NGC_6826HSTFull.jpg"),
    SPIROGRAPH_NEBULA_IC_418("Sprirograph Nebula (IC 418)",
            Constants.SKY_LIVE_INFO_PREFIX + "ic418-object",
            Constants.WIKIMEDIA_PREFIX + "/6/6b/Spirograph_Nebula_-_Hubble_1999.jpg"),
    CATS_EYE_NEBULA_NGC_6543("Cat's Eye Nebula (NGC 6543)",
            Constants.SKY_LIVE_INFO_PREFIX + "ngc6543-cats-eye-nebula-object",
            Constants.WIKIMEDIA_PREFIX + "/5/5a/NGC6543.jpg"),
    GHOST_OF_JUPITER_NEBULA("Ghost of Jupiter Nebula (NGC 3242)",
            Constants.SKY_LIVE_INFO_PREFIX + "ngc3242-jupiters-ghost-nebula-object",
            Constants.WIKIMEDIA_PREFIX + "/1/14/NGC_3242_%22Ghost_of_Jupiter%22.png"),
    RING_NEBULA_M57("Ring Nebula (M 57)",
            Constants.SKY_LIVE_INFO_PREFIX + "messier-57-ring-nebula-object",
            Constants.WIKIMEDIA_PREFIX + "/1/13/M57_The_Ring_Nebula.JPG"),
    BLUE_RACQUETBALL_NEBULA_NGC6572("Blue Racquetball Nebula (NGC 6572)",
            Constants.SKY_LIVE_INFO_PREFIX + "ngc6572-object",
            Constants.WIKIMEDIA_PREFIX + "/c/cd/NGC_6572.jpg"),
    BLUE_FLASH_NEBULA_NGC_6905("Blue Flash Nebula (NGC 6905)",
            Constants.SKY_LIVE_INFO_PREFIX + "ngc6905-blue-flash-nebula-object",
            Constants.WIKIMEDIA_PREFIX + "/7/70/NGC_6905_-_VLT%28FORS2%29_-_RHaBOIII.png"),

    LITTLE_GEM_NEBULA_NGC_6818("Little Gem Nebula (NGC 6818)",
            Constants.SKY_LIVE_INFO_PREFIX + "ngc6818-little-gem-nebula-object",
            Constants.WIKIMEDIA_PREFIX + "/f/f6/Ngc6818.jpg"),
    HELIX_NEBULA_NGC_7293("Helix Nebula (NGC 7293)",
            Constants.SKY_LIVE_INFO_PREFIX + "ngc7293-helix-nebula-object",
            Constants.WIKIMEDIA_PREFIX + "/thumb/7/7f/Comets_Kick_up_Dust_in_Helix_Nebula_(PIA09178).jpg/1231px-Comets_Kick_up_Dust_in_Helix_Nebula_(PIA09178).jpg"),
    COPELAND_BLUE_SNOWBALL_NGC_7662("Copeland's Blue Snowball (NGC 7662)",
            Constants.SKY_LIVE_INFO_PREFIX + "ngc7662-copelands-blue-snowball-object",
            Constants.WIKIMEDIA_PREFIX + "/7/7e/NGC_7662_%22Blue_Snowball%22.jpg"),
    ESKIMO_NEBULA_NGC_2392("Eskimo Nebula (NGC 2392)",
            Constants.SKY_LIVE_INFO_PREFIX + "ngc2392-eskimo-nebula-object",
            Constants.WIKIMEDIA_PREFIX + "/1/16/Ngc2392.jpg"),
    RETINA_NEBULA_IC_4406("Retina Nebula (IC 4406)",
            Constants.SKY_LIVE_INFO_PREFIX + "ic4406-object",
            Constants.WIKIMEDIA_PREFIX + "/1/1a/Retinanebel.jpg"),
    OWN_NEBULA_M97("Owl Nebula (M97)",
            Constants.SKY_LIVE_INFO_PREFIX + "messier-97-owl-nebula-object",
            Constants.WIKIMEDIA_PREFIX + "/1/10/The_Owl_Nebula_M97_Goran_Nilsson_%26_The_Liverpool_Telescope.jpg");

    public String name;
    public String url;
    public String imageUrl;

    PlanetaryNebulaeEnum(String name, String url, String imageUrl) {
        this.name = name;
        this.url = url;
        this.imageUrl = imageUrl;
    }
}
