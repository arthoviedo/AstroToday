package smarthur.space.astrotoday;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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

    public void initPlanetaryNebulae(){
        planetaryNebulaMap.put(
            new PlanetaryNebula(
                "Sprirograph Nebula (IC 418)",
                "https://theskylive.com/sky/deepsky/ic418-object",
                "https://upload.wikimedia.org/wikipedia/commons/6/6b/Spirograph_Nebula_-_Hubble_1999.jpg"),
            new PlanetaryNebulaRowView(getContext()));
        planetaryNebulaMap.put(
            new PlanetaryNebula(
                "Ghost of Jupiter Nebula (NGC 3242)",
                "https://theskylive.com/sky/deepsky/ngc3242-jupiters-ghost-nebula-object",
                "https://en.wikipedia.org/wiki/NGC_3242#/media/File:NGC_3242_%22Ghost_of_Jupiter%22.png"),
            new PlanetaryNebulaRowView(getContext()));
        planetaryNebulaMap.put(
                new PlanetaryNebula(
                "Ring Nebula (M 57)",
                "https://theskylive.com/sky/deepsky/messier-57-ring-nebula-object",
                "https://upload.wikimedia.org/wikipedia/commons/1/13/M57_The_Ring_Nebula.JPG"),
                new PlanetaryNebulaRowView(getContext()));
        planetaryNebulaMap.put(
                new PlanetaryNebula(
                "Blue Racquetball Nebula (NGC 6572)",
                "https://theskylive.com/sky/deepsky/ngc6572-object",
                "https://upload.wikimedia.org/wikipedia/commons/c/cd/NGC_6572.jpg"),
                new PlanetaryNebulaRowView(getContext()));
        planetaryNebulaMap.put(
                new PlanetaryNebula(
                "Blue Flash Nebula (NGC 6905)",
                "https://theskylive.com/sky/deepsky/ngc6905-blue-flash-nebula-object",
                "https://upload.wikimedia.org/wikipedia/commons/7/70/NGC_6905_-_VLT%28FORS2%29_-_RHaBOIII.png"),
                new PlanetaryNebulaRowView(getContext()));
        planetaryNebulaMap.put(
                new PlanetaryNebula(
                        "Little Gem Nebula (NGC 6818)",
                        "https://theskylive.com/sky/deepsky/ngc6818-little-gem-nebula-object",
                        "https://upload.wikimedia.org/wikipedia/commons/f/f6/Ngc6818.jpg"),
                new PlanetaryNebulaRowView(getContext()));

        for(Map.Entry<PlanetaryNebula,  PlanetaryNebulaRowView> entry
            : planetaryNebulaMap.entrySet()) {
            entry.getValue().nameLabel.setText(entry.getKey().name);
            ((LinearLayout)getView().findViewById(R.id.planetary_nabulae)).addView(entry.getValue());
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
                        planetaryNebula,
                        visualMagnitudeValue.text(),
                        majorSizeValue.text(),
                        minorSizeValue.text(),
                        view);

                    Elements riseInfo = doc.select("div[class=\"rise\"]");
                    Elements transitInfo = riseInfo.next();
                    Elements setInfo = transitInfo.next();
                    updateTransit(
                        riseInfo.first().child(2).text(),
                        transitInfo.first().child(2).text(),
                        setInfo.first().child(2).text(),
                        view.transitInfoView);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    Document getDocument(String url) throws Exception{
        Connection connection = Jsoup.connect(url);
        connection.cookie(
            "localdata_v1",
            COOKIE_INFO);
        return connection.get();
    }

    public void updatePlanetaryNebula(
            final PlanetaryNebula planetaryNebula,
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

    public void updateTransit(
        final String riseTime,
        final String transitTime,
        final String setTime,
        final TransitInfoView transitInfoView) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                transitInfoView.riseTime.setText("Rise:" + riseTime);
                transitInfoView.transitTime.setText("Transit:" + transitTime);
                transitInfoView.setTime.setText("Set:" + setTime);
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