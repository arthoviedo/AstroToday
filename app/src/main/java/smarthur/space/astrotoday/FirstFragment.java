package smarthur.space.astrotoday;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.List;
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
                    entry.getKey().url,
                    entry.getValue().planetMagnitude,
                    entry.getValue().planetSize);
        }
    }

    public void fetchPlanetInfo(
            final String url,
            final TextView magnitudeView,
            final TextView sizeView) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document doc = Jsoup.connect(url).get();
                    Elements parent = doc.select("a[name=\"brightness\"]");
                    Elements temp1 = parent.next(); //<h1> Planet Brightness
                    Elements temp2 = temp1.next();          //<p>
                    Element div1 = temp2.next().first();          //<div>
                    Element magnitude = div1.child(1); // AR
                    Element div2 = temp2.next().next().first();
                    Element size = div2.child(1); //Diameter
                    updateUi(magnitude.text(), size.text(), magnitudeView, sizeView);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void updateUi(
            final String magnitude,
            final String size,
            final TextView magnitudeView,
            final TextView sizeView) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                magnitudeView.setText(magnitude);
                sizeView.setText(size);
            }
        });
    }

    private void processPlanetInfo(String response) {

    }

    enum Planets {
        MERCURY("Mercury"),
        VENUS("Venus"),
        MARS("Mars"),
        JUPITER("Jupiter"),
        SATURN("Saturn"),
        URANUS("Uranus"),
        NEPTUNE("Neptune");

        String name;
        String url;

        Planets(String name) {
            this.name = name;
            this.url = String.format("https://theskylive.com/%s-info", name.toLowerCase());
        }
    }
}