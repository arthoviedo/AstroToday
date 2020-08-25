package smarthur.space.astrotoday;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class FirstFragment extends Fragment {
    Map<Planets, PlanetRowView> planetRowViewList = new HashMap<>();

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        planetRowViewList.put(Planets.MERCURY, (PlanetRowView) view.findViewById(R.id.mercuryRow));
        planetRowViewList.put(Planets.VENUS, (PlanetRowView) view.findViewById(R.id.venusRow));
        planetRowViewList.put(Planets.MARS, (PlanetRowView) view.findViewById(R.id.marsRow));
        planetRowViewList.put(Planets.JUPITER, (PlanetRowView) view.findViewById(R.id.jupiterRow));
        planetRowViewList.put(Planets.SATURN, (PlanetRowView) view.findViewById(R.id.saturnRow));
        planetRowViewList.put(Planets.URANUS, (PlanetRowView) view.findViewById(R.id.uranusRow));
        planetRowViewList.put(Planets.NEPTUNE, (PlanetRowView) view.findViewById(R.id.neptuneRow));

        for (Map.Entry<Planets, PlanetRowView> planetEntry : planetRowViewList.entrySet()) {
            planetEntry.getValue().nameLabel.setText(planetEntry.getKey().name);
        }
        super.onViewCreated(view, savedInstanceState);
    }

    public void update() {
        for (Map.Entry<Planets, PlanetRowView> entry : planetRowViewList.entrySet()) {
            fetchPlanetInfo(
                    entry.getKey(),
                    entry.getKey().url,
                    entry.getValue().planetMagnitude,
                    entry.getValue().planetSize,
                    entry.getValue().transitInfoView);
        }
    }

    public void fetchPlanetInfo(
            final Planets planet,
            final String url,
            final TextView magnitudeView,
            final TextView sizeView,
            final TransitInfoView transitInfoView) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Connection connection = Jsoup.connect(url);
                    connection.cookie(
                            "localdata_v1",
                            "-33.86785%7C151.20732%7CSydney+%28AU%29%7CAustralia%2FSydney%7C0");
                    Document doc = connection.get();
                    Elements parent = doc.select("a[name=\"brightness\"]");
                    Elements temp1 = parent.next(); //<h1> Planet Brightness
                    Elements temp2 = temp1.next();          //<p>
                    Element div1 = temp2.next().first();          //<div>
                    Element magnitude = div1.child(1); // AR
                    Element div2 = temp2.next().next().first();
                    Element size = div2.child(1); //Diameter
                    updatePlanetVisibility(planet, magnitude.text(), size.text(), magnitudeView, sizeView);

                    Elements riseInfo = doc.select("div[class=\"rise\"]");
                    Elements transitInfo = riseInfo.next();
                    Elements setInfo = transitInfo.next();
                    updatePlanetTransit(
                            riseInfo.first().child(2).text(),
                            transitInfo.first().child(2).text(),
                            setInfo.first().child(2).text(),
                            transitInfoView);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void updatePlanetVisibility(
            final Planets planet,
            final String magnitude,
            final String size,
            final TextView magnitudeView,
            final TextView sizeView) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                magnitudeView.setText(
                        Html.fromHtml(String.format(
                                Locale.US,
                                "Magn.:(%.1f/<b>%.1f</b>/%.1f)",
                                planet.minMagnitude,
                                Float.parseFloat(magnitude),
                                planet.maxMagnitude)));
                sizeView.setText(
                        Html.fromHtml(String.format("Size:(%s\"/<b>%s</b>/%s\")",
                                planet.minSize,
                                size,
                                planet.maxSize)));
            }
        });
    }

    public void updatePlanetTransit(
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

    class TransitInfo {
        Calendar riseTime;
        Calendar transitTime;
        Calendar setTime;

        TransitInfo(Calendar riseTime, Calendar transitTime, Calendar setTime) {
            this.riseTime = riseTime;
            this.transitTime = transitTime;
            this.setTime = setTime;
        }
    }
}