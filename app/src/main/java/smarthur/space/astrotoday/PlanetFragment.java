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

import smarthur.space.astrotoday.model.PlanetsEnum;
import smarthur.space.astrotoday.util.Constants;

public class PlanetFragment extends Fragment implements UpdatableFragment {
    Map<PlanetsEnum, PlanetRowView> planetMap = new HashMap<>();

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
        planetMap.put(PlanetsEnum.MERCURY, (PlanetRowView) view.findViewById(R.id.mercuryRow));
        planetMap.put(PlanetsEnum.VENUS, (PlanetRowView) view.findViewById(R.id.venusRow));
        planetMap.put(PlanetsEnum.MARS, (PlanetRowView) view.findViewById(R.id.marsRow));
        planetMap.put(PlanetsEnum.JUPITER, (PlanetRowView) view.findViewById(R.id.jupiterRow));
        planetMap.put(PlanetsEnum.SATURN, (PlanetRowView) view.findViewById(R.id.saturnRow));
        planetMap.put(PlanetsEnum.URANUS, (PlanetRowView) view.findViewById(R.id.uranusRow));
        planetMap.put(PlanetsEnum.NEPTUNE, (PlanetRowView) view.findViewById(R.id.neptuneRow));

        for (Map.Entry<PlanetsEnum, PlanetRowView> planetEntry : planetMap.entrySet()) {
            planetEntry.getValue().nameLabel.setText(planetEntry.getKey().name);
        }
    }

    public void update() {
        for (Map.Entry<PlanetsEnum, PlanetRowView> entry : planetMap.entrySet()) {
            fetchPlanetInfo(
                    entry.getKey(),
                    entry.getValue());
        }

    }

    public void fetchPlanetInfo(
            final PlanetsEnum planet,
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
                Constants.COOKIE_INFO);
        return connection.get();
    }

    public void updatePlanetVisibility(
            final PlanetsEnum planet,
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


}