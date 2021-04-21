package smarthur.space.astrotoday;

import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;

import java.util.Comparator;
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

    private MenuItem actionSortBySize;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_planetary_nabulae, container, false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_menu, menu);
        actionSortBySize = menu.findItem(R.id.action_sort_by_size);
        super.onCreateOptionsMenu(menu,inflater);
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
                if (actionSortBySize != null) {
                    actionSortBySize.setEnabled(true);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sort_by_size:
                // User chose the "Settings" item, show the app settings UI...
                sortBySize();

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    private void sortBySize() {
        ((List<PlanetaryNebulaViewModel>) model.getSkyObjectsList().getValue()).sort(
                new Comparator<PlanetaryNebulaViewModel>() {
            @Override
            public int compare(PlanetaryNebulaViewModel o1, PlanetaryNebulaViewModel o2) {
                float majorSize1 = TextUtils.isEmpty(o1.majorSize) ? 0 : Float.parseFloat(o1.majorSize.replace("arcmin", "").trim());
                float minorSize1 = TextUtils.isEmpty(o1.minorSize) ? 0 : Float.parseFloat(o1.minorSize.replace("arcmin", "").trim());
                float majorSize2 = TextUtils.isEmpty(o2.majorSize) ? 0 : Float.parseFloat(o2.majorSize.replace("arcmin", "").trim());
                float minorSize2 = TextUtils.isEmpty(o2.minorSize) ? 0 : Float.parseFloat(o2.minorSize.replace("arcmin", "").trim());
                return (majorSize1 == majorSize2 ? 0  : majorSize2 - majorSize1 > 0 ? 1 : -1 );
            }
        });
        LinearLayout planetaryNebulaeList = (LinearLayout) getView().findViewById(R.id.planetary_nabulae);
        planetaryNebulaeList.removeAllViews();
        for (PlanetaryNebulaViewModel viewModel : (List<PlanetaryNebulaViewModel>) model.getSkyObjectsList().getValue()) {
            planetaryNebulaeList.addView(planetaryNebulaMap.get(viewModel.planetaryNebula));
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