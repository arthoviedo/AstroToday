package smarthur.space.astrotoday;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class PlanetaryNebulaFragment extends Fragment implements UpdatableFragment {
    Map<PlanetaryNebula, PlanetaryNebulaRowView> planetaryNebulaMap = new HashMap<>();


    private final static String COOKIE_INFO =
            "-33.86785%7C151.20732%7CSydney+%28AU%29%7CAustralia%2FSydney%7C0";
    private final static String SKY_LIVE_INFO_PREFIX = "https://theskylive.com/sky/deepsky/";
    private final static String WIKIMEDIA_PREFIX = "https://upload.wikimedia.org/wikipedia/commons";


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_planetary_nabulae, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        initPlanetaryNebulae();
        super.onViewCreated(view, savedInstanceState);
    }

    public void initPlanetaryNebulae() {
        planetaryNebulaMap.put(new PlanetaryNebula("Saturn Nebula (NGC 7009)",
                        SKY_LIVE_INFO_PREFIX + "ngc7009-saturn-nebula-object",
                        WIKIMEDIA_PREFIX + "/c/c9/MUSE_image_of_the_Saturn_Nebula.jpg"),
                new PlanetaryNebulaRowView(getContext()));
        planetaryNebulaMap.put(new PlanetaryNebula("Blinking Planetary (NGC 6826)",
                        SKY_LIVE_INFO_PREFIX + "ngc6826-blinking-planetary-object",
                        WIKIMEDIA_PREFIX + "/8/83/NGC_6826HSTFull.jpg"),
                new PlanetaryNebulaRowView(getContext()));
        planetaryNebulaMap.put(new PlanetaryNebula("Sprirograph Nebula (IC 418)",
                        SKY_LIVE_INFO_PREFIX + "ic418-object",
                        WIKIMEDIA_PREFIX + "/6/6b/Spirograph_Nebula_-_Hubble_1999.jpg"),
                new PlanetaryNebulaRowView(getContext()));
        planetaryNebulaMap.put(new PlanetaryNebula("Cat's Eye Nebula (NGC 6543)",
                        SKY_LIVE_INFO_PREFIX + "ngc6543-cats-eye-nebula-object",
                        WIKIMEDIA_PREFIX + "/5/5a/NGC6543.jpg"),
                new PlanetaryNebulaRowView(getContext()));
        planetaryNebulaMap.put(new PlanetaryNebula("Ghost of Jupiter Nebula (NGC 3242)",
                        SKY_LIVE_INFO_PREFIX + "ngc3242-jupiters-ghost-nebula-object",
                        WIKIMEDIA_PREFIX + "/1/14/NGC_3242_%22Ghost_of_Jupiter%22.png"),
                new PlanetaryNebulaRowView(getContext()));
        planetaryNebulaMap.put(new PlanetaryNebula("Ring Nebula (M 57)",
                        SKY_LIVE_INFO_PREFIX + "messier-57-ring-nebula-object",
                        WIKIMEDIA_PREFIX + "/1/13/M57_The_Ring_Nebula.JPG"),
                new PlanetaryNebulaRowView(getContext()));
        planetaryNebulaMap.put(new PlanetaryNebula("Blue Racquetball Nebula (NGC 6572)",
                        SKY_LIVE_INFO_PREFIX + "ngc6572-object",
                        WIKIMEDIA_PREFIX + "/c/cd/NGC_6572.jpg"),
                new PlanetaryNebulaRowView(getContext()));
        planetaryNebulaMap.put(new PlanetaryNebula("Blue Flash Nebula (NGC 6905)",
                        SKY_LIVE_INFO_PREFIX + "ngc6905-blue-flash-nebula-object",
                        WIKIMEDIA_PREFIX + "/7/70/NGC_6905_-_VLT%28FORS2%29_-_RHaBOIII.png"),
                new PlanetaryNebulaRowView(getContext()));
        planetaryNebulaMap.put(new PlanetaryNebula("Little Gem Nebula (NGC 6818)",
                        SKY_LIVE_INFO_PREFIX + "ngc6818-little-gem-nebula-object",
                        WIKIMEDIA_PREFIX + "/f/f6/Ngc6818.jpg"),
                new PlanetaryNebulaRowView(getContext()));
        planetaryNebulaMap.put(new PlanetaryNebula("Helix Nebula (NGC 7293)",
                        SKY_LIVE_INFO_PREFIX + "ngc7293-helix-nebula-object",
                            WIKIMEDIA_PREFIX + "/7/7f/Comets_Kick_up_Dust_in_Helix_Nebula_%28PIA09178%29.jpg"),
                new PlanetaryNebulaRowView(getContext()));
        planetaryNebulaMap.put(new PlanetaryNebula("Copeland's Blue Snowball (NGC 7662)",
                        SKY_LIVE_INFO_PREFIX + "ngc7662-copelands-blue-snowball-object",
                        WIKIMEDIA_PREFIX + "/7/7e/NGC_7662_%22Blue_Snowball%22.jpg"),
                new PlanetaryNebulaRowView(getContext()));
        planetaryNebulaMap.put(new PlanetaryNebula("Eskimo Nebula (NGC 2392)",
                        SKY_LIVE_INFO_PREFIX + "ngc2392-eskimo-nebula-object",
                        WIKIMEDIA_PREFIX + "/1/16/Ngc2392.jpg"),
                new PlanetaryNebulaRowView(getContext()));
        planetaryNebulaMap.put(new PlanetaryNebula("Retina Nebula (IC 4406)",
                        SKY_LIVE_INFO_PREFIX + "ic4406-object",
                        WIKIMEDIA_PREFIX + "/1/1a/Retinanebel.jpg"),
                new PlanetaryNebulaRowView(getContext()));
        planetaryNebulaMap.put(new PlanetaryNebula("Owl Nebula (M97)",
                        SKY_LIVE_INFO_PREFIX + "messier-97-owl-nebula-object",
                        WIKIMEDIA_PREFIX + "/1/10/The_Owl_Nebula_M97_Goran_Nilsson_%26_The_Liverpool_Telescope.jpg"),
                new PlanetaryNebulaRowView(getContext()));
        int count = 0;
        for (final Map.Entry<PlanetaryNebula, PlanetaryNebulaRowView> entry
                : planetaryNebulaMap.entrySet()) {
            entry.getValue().nameLabel.setText(entry.getKey().name);
            ((LinearLayout) getView().findViewById(R.id.planetary_nabulae)).addView(entry.getValue());
            Glide.with(this).load(entry.getKey().imageUrl).into(entry.getValue().image);
            entry.getValue().image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FullScreenImageDialogFragment newFragment =
                            FullScreenImageDialogFragment.newInstance(entry.getKey().imageUrl);
                    FragmentTransaction ft =
                            getParentFragmentManager()
                                    .beginTransaction();
                    ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);;

                    newFragment.show(ft, "dialog");
                }
            });
            if (count % 2 == 0) {
                entry.getValue().setBackgroundColor(getResources().getColor(R.color.lightGrey, null));
            }
            count++;
        }
    }


    public void update() {
        for (Map.Entry<PlanetaryNebula, PlanetaryNebulaRowView> entry : planetaryNebulaMap.entrySet()) {
            fetchPlanetaryNebulaInfo(entry.getKey(), entry.getValue());
        }
    }

    public void fetchPlanetaryNebulaInfo(
            final PlanetaryNebula planetaryNebula,
            final PlanetaryNebulaRowView view
    ) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document doc = getDocument(planetaryNebula.theSkyLiveUrl);
                    Elements visualMagnitude = doc.select(":matchesOwn(Magnitude V)");
                    Elements visualMagnitudeValue = visualMagnitude.next(); //<h1> Planet Brightness
                    Elements majorSize = doc.select(":matchesOwn(Major Angular Size)");
                    Elements majorSizeValue = majorSize.next(); //<h1> Planet Brightness
                    Elements minorSize = doc.select(":matchesOwn(Minor Angular Size)");
                    Elements minorSizeValue = minorSize.next(); //<h1> Planet Brightness
                    updatePlanetaryNebula(
                            visualMagnitudeValue.text(),
                            majorSizeValue.text(),
                            minorSizeValue.text(),
                            view);

                    Elements riseInfo = doc.select("div[class=\"rise\"]");
                    Elements transitInfo = riseInfo.next();
                    Elements setInfo = transitInfo.next();
                    view.transitInfoView.updateTransit(
                            getActivity(),
                            riseInfo.first().child(2).text(),
                            transitInfo.first().child(2).text(),
                            setInfo.first().child(2).text());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    Document getDocument(String url) throws Exception {
        Connection connection = Jsoup.connect(url);
        connection.cookie(
                "localdata_v1",
                COOKIE_INFO);
        return connection.get();
    }

    public void updatePlanetaryNebula(
            final String visualMagnitude,
            final String majorSize,
            final String minorSize,
            final PlanetaryNebulaRowView view) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                view.visualMagnitude.setText(
                        Html.fromHtml(String.format(
                                Locale.US,
                                "Visual Magn.:<b>%.1f</b>",
                                Float.parseFloat(visualMagnitude))));
                view.majorMinorSize.setText(
                        Html.fromHtml(String.format(
                                Locale.US,
                                "Size:%s x %s",
                                majorSize,
                                minorSize)));
            }
        });
    }

    class PlanetaryNebula {
        String name;
        String theSkyLiveUrl;
        String imageUrl;

        public PlanetaryNebula(
                String name,
                String theSkyLiveUrl,
                String imageUrl) {
            this.name = name;
            this.theSkyLiveUrl = theSkyLiveUrl;
            this.imageUrl = imageUrl;
        }

    }
}