package smarthur.space.astrotoday;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import smarthur.space.astrotoday.model.PlanetViewModel;
import smarthur.space.astrotoday.model.PlanetsEnum;
import smarthur.space.astrotoday.model.SkyObjectsListViewModel;
import smarthur.space.astrotoday.model.SkyObjectViewModel;
import smarthur.space.astrotoday.network.InfoFetcher;

public class PlanetFragment2 extends Fragment implements UpdatableFragment {
    Map<PlanetsEnum, PlanetRowView> planetMap = new HashMap<>();
    private SkyObjectsListViewModel model;

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

        model = new ViewModelProvider(this).get(SkyObjectsListViewModel.class);
        // Create the observer which updates the UI.

        Observer<? extends List<? extends SkyObjectViewModel>> planetsObserver = new Observer<List<PlanetViewModel>>() {
            @Override
            public void onChanged(List<PlanetViewModel> newPlanetList) {
                // Update the UI, in this case, a TextView.
                for(PlanetViewModel planetViewModel : newPlanetList) {
                    PlanetRowView planetRowView = planetMap.get(planetViewModel.planet);

                    updatePlanetVisibility(
                            planetViewModel.planet,
                            planetViewModel.magnitude,
                            planetViewModel.size,
                            planetRowView);

                    planetRowView.transitInfoView.updateTransit(
                            getActivity(),
                            planetViewModel.transitInfo.riseTime,
                            planetViewModel.transitInfo.transitTime,
                            planetViewModel.transitInfo.setTime);
                }

            }
        };

        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        model.getSkyObjectsList().observe(
                getViewLifecycleOwner(),
                (Observer<? super List<? extends SkyObjectViewModel>>) planetsObserver);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        InfoFetcher.updateData(planetMap.keySet(), model);
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