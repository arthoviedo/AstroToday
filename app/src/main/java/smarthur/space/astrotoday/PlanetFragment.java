package smarthur.space.astrotoday;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class PlanetFragment extends Fragment implements UpdatableFragment {
    Map<Planets, PlanetRowView> planetMap = new HashMap<>();


    private final static String COOKIE_INFO =
        "-33.86785%7C151.20732%7CSydney+%28AU%29%7CAustralia%2FSydney%7C0";

    @Override
    public View onCreateView(
        LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_planets, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        initPlanets(view);
        super.onViewCreated(view, savedInstanceState);
    }



    public void initPlanets(View view) {
        planetMap.put(Planets.MERCURY, (PlanetRowView) view.findViewById(R.id.mercuryRow));
        planetMap.put(Planets.VENUS, (PlanetRowView) view.findViewById(R.id.venusRow));
        planetMap.put(Planets.MARS, (PlanetRowView) view.findViewById(R.id.marsRow));
        planetMap.put(Planets.JUPITER, (PlanetRowView) view.findViewById(R.id.jupiterRow));
        planetMap.put(Planets.SATURN, (PlanetRowView) view.findViewById(R.id.saturnRow));
        planetMap.put(Planets.URANUS, (PlanetRowView) view.findViewById(R.id.uranusRow));
        planetMap.put(Planets.NEPTUNE, (PlanetRowView) view.findViewById(R.id.neptuneRow));

        for (Map.Entry<Planets, PlanetRowView> planetEntry : planetMap.entrySet()) {
            planetEntry.getValue().nameLabel.setText(planetEntry.getKey().name);
        }
    }

    public void update() {
        for (Map.Entry<Planets, PlanetRowView> entry : planetMap.entrySet()) {
            fetchPlanetInfo(
                entry.getKey(),
                entry.getValue());
        }

    }



    public void fetchPlanetInfo(
        final Planets planet,
        final PlanetRowView view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document doc = getDocument(planet.url);
                    Elements parent = doc.select("a[name=\"brightness\"]");
                    Elements temp1 = parent.next(); //<h1> Planet Brightness
                    Elements temp2 = temp1.next();          //<p>
                    Element div1 = temp2.next().first();          //<div>
                    Element magnitude = div1.child(1); // AR
                    Element div2 = temp2.next().next().first();
                    Element size = div2.child(1); //Diameter
                    updatePlanetVisibility(
                        planet,
                        magnitude.text(),
                        size.text(),
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

    public void updatePlanetVisibility(
        final Planets planet,
        final String magnitude,
        final String size,
        final PlanetRowView view) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                view.planetMagnitude.setText(
                    Html.fromHtml(String.format(
                        Locale.US,
                        "Magn.:(%.1f/<b>%.1f</b>/%.1f)",
                        planet.minMagnitude,
                        Float.parseFloat(magnitude),
                        planet.maxMagnitude)));
                view.planetSize.setText(
                    Html.fromHtml(String.format("Size:(%s\"/<b>%s</b>/%s\")",
                        planet.minSize,
                        size,
                        planet.maxSize)));
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

    enum Planets {
        MERCURY("Mercury", -2.48f, 7.25f, 4.5f, 13f),
        VENUS("Venus", -4.92f, -2.98f, 9.7f, 66.0f),
        MARS("Mars", -2.94f,1.86f,3.5f,25.1f),
        JUPITER("Jupiter", -2.94f, 1.66f, 29.8f, 50.1f),
        SATURN("Saturn", -0.55f, 1.17f, 14.5f, 20.1f),
        URANUS("Uranus", 5.38f, 6.03f, 3.3f, 4.1f),
        NEPTUNE("Neptune", 7.67f, 8.00f, 2.2f, 2.4f);

        String name;
        String url;
        float minMagnitude;
        float maxMagnitude;
        float minSize;
        float maxSize;
        TransitInfo transitInfo;

        Planets(String name, float minMagnitude, float maxMagnitude, float minSize, float maxSize) {
            this.name = name;
            this.minMagnitude = minMagnitude;
            this.maxMagnitude = maxMagnitude;
            this.minSize = minSize;
            this.maxSize = maxSize;
            this.url = String.format("https://theskylive.com/%s-info", name.toLowerCase());
        }
    }
}