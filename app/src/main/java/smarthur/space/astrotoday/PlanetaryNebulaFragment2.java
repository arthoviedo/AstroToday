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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import smarthur.space.astrotoday.model.PlanetaryNebulaViewModel;
import smarthur.space.astrotoday.model.SkyObjectViewModel;
import smarthur.space.astrotoday.model.SkyObjectsListViewModel;
import smarthur.space.astrotoday.model.enums.PlanetaryNebulaeEnum;
import smarthur.space.astrotoday.network.InfoFetcher;

public class PlanetaryNebulaFragment2 extends Fragment implements UpdatableFragment {
    Map<PlanetaryNebulaeEnum, PlanetaryNebulaRowView> planetaryNebulaMap = new HashMap<>();
    private SkyObjectsListViewModel model;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_planetary_nabulae, container, false);
    }

    public void onViewCreated(@NonNull final View view, Bundle savedInstanceState) {
        initPlanetaryNebulae();
        super.onViewCreated(view, savedInstanceState);
        model = new ViewModelProvider(this).get(SkyObjectsListViewModel.class);

        Observer<? extends List<? extends SkyObjectViewModel>> planetaryNebulaeObserver = new Observer<List<PlanetaryNebulaViewModel>>() {

            @Override
            public void onChanged(List<PlanetaryNebulaViewModel> viewModels) {
                for (PlanetaryNebulaViewModel viewModel : viewModels) {
                    PlanetaryNebulaRowView rowView  = planetaryNebulaMap.get(viewModel.planetaryNebula);
                    updatePlanetaryNebula(
                            viewModel.magnitude,
                            viewModel.majorSize,
                            viewModel.minorSize,
                            rowView);
                    rowView.transitInfoView.updateTransit(
                            getActivity(),
                            viewModel.transitInfo.riseTime,
                            viewModel.transitInfo.transitTime,
                            viewModel.transitInfo.setTime);
                }
            }
        };
        model.getSkyObjectsList().observe(
                getViewLifecycleOwner(),
                (Observer<? super List<? extends SkyObjectViewModel>>) planetaryNebulaeObserver);
    }

    public void initPlanetaryNebulae() {
        for (PlanetaryNebulaeEnum planetaryNebula : PlanetaryNebulaeEnum.values()) {
            planetaryNebulaMap.put(planetaryNebula, new PlanetaryNebulaRowView(getContext()));
        }
        int count = 0;
        for (final Map.Entry<PlanetaryNebulaeEnum, PlanetaryNebulaRowView> entry
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
        InfoFetcher.updateData(planetaryNebulaMap.keySet(), model);
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
}