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

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import smarthur.space.astrotoday.model.GalaxyEnum;
import smarthur.space.astrotoday.model.GalaxyViewModel;
import smarthur.space.astrotoday.model.SkyObjectViewModel;
import smarthur.space.astrotoday.model.SkyObjectsListViewModel;
import smarthur.space.astrotoday.network.InfoFetcher;
import smarthur.space.astrotoday.util.Constants;

public class GalaxyFragment2 extends Fragment implements UpdatableFragment {
    Map<GalaxyEnum, GalaxyRowView> galaxyMap = new HashMap<>();
    private SkyObjectsListViewModel model;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_galaxy, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        initGalaxies();
        super.onViewCreated(view, savedInstanceState);

        model = new ViewModelProvider(this).get(SkyObjectsListViewModel.class);

        Observer<? extends List<? extends SkyObjectViewModel>> galaxiesObserver = new Observer<List<GalaxyViewModel>>() {
            @Override
            public void onChanged(List<GalaxyViewModel> newGalaxyList) {
                for (GalaxyViewModel galaxyViewModel : newGalaxyList) {
                    GalaxyRowView galaxyRowView = galaxyMap.get(galaxyViewModel.galaxy);
                    updateGalaxy(
                            galaxyViewModel.magnitude,
                            galaxyViewModel.majorSize,
                            galaxyViewModel.minorSize,
                            galaxyRowView);
                    galaxyRowView.transitInfoView.updateTransit(
                            getActivity(),
                            galaxyViewModel.transitInfo.riseTime,
                            galaxyViewModel.transitInfo.transitTime,
                            galaxyViewModel.transitInfo.setTime);
                }
            }
        };

        model.getSkyObjectsList().observe(
                getViewLifecycleOwner(),
                (Observer<? super List<? extends SkyObjectViewModel>>) galaxiesObserver
        );
    }

    public void initGalaxies() {
        for(GalaxyEnum galaxy : GalaxyEnum.values()) {
            galaxyMap.put(galaxy, new GalaxyRowView(getContext()));
        }

        int count = 0;
        for (final Map.Entry<GalaxyEnum, GalaxyRowView> entry
                : galaxyMap.entrySet()) {
            entry.getValue().nameLabel.setText(entry.getKey().name);
            ((LinearLayout) getView().findViewById(R.id.galaxies)).addView(entry.getValue());
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
        InfoFetcher.updateData(galaxyMap.keySet(), model);
    }

    public void updateGalaxy(
            final String visualMagnitude,
            final String majorSize,
            final String minorSize,
            final GalaxyRowView view) {
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