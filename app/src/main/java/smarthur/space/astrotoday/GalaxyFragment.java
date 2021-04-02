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

public class GalaxyFragment extends Fragment implements UpdatableFragment {
    Map<Galaxy, GalaxyRowView> galaxyMap = new HashMap<>();


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
        return inflater.inflate(R.layout.fragment_galaxy, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        initGalaxies();
        super.onViewCreated(view, savedInstanceState);
    }

    public void initGalaxies() {
        galaxyMap.put(new Galaxy("Pinwheel Galaxy (M101)",
                        SKY_LIVE_INFO_PREFIX + "messier-101-the-pinwheel-galaxy-object",
                        WIKIMEDIA_PREFIX + "/thumb/c/c5/M101_hires_STScI-PRC2006-10a.jpg/1280px-M101_hires_STScI-PRC2006-10a.jpg"),
                new GalaxyRowView(getContext()));
        galaxyMap.put(new Galaxy("Bode's Galaxy (M81)",
                        SKY_LIVE_INFO_PREFIX + "messier-81-bodes-galaxy-object",
                        WIKIMEDIA_PREFIX + "/thumb/6/63/Messier_81_HST.jpg/1280px-Messier_81_HST.jpg"),
                new GalaxyRowView(getContext()));
        galaxyMap.put(new Galaxy("Southern Pinwheel Galaxy (M83)",
                        SKY_LIVE_INFO_PREFIX + "messier-83-southern-pinwheel-galaxy-object",
                        WIKIMEDIA_PREFIX + "/thumb/d/d5/Hubble_view_of_barred_spiral_galaxy_Messier_83.jpg/1280px-Hubble_view_of_barred_spiral_galaxy_Messier_83.jpg"),
                new GalaxyRowView(getContext()));
        galaxyMap.put(new Galaxy("Triangulum Galaxy (M33)",
                        SKY_LIVE_INFO_PREFIX + "messier-33-triangulum-galaxy-object",
                        WIKIMEDIA_PREFIX + "/thumb/6/64/VST_snaps_a_very_detailed_view_of_the_Triangulum_Galaxy.jpg/859px-VST_snaps_a_very_detailed_view_of_the_Triangulum_Galaxy.jpg"),
                new GalaxyRowView(getContext()));
        galaxyMap.put(new Galaxy("Sculptor Galaxy (NGC 253)",
                        SKY_LIVE_INFO_PREFIX + "ngc253-sculptor-filament-object",
                        WIKIMEDIA_PREFIX + "/thumb/0/08/Sculptor_Galaxy_by_VISTA.jpg/1280px-Sculptor_Galaxy_by_VISTA.jpg"),
                new GalaxyRowView(getContext()));
        galaxyMap.put(new Galaxy("Croc's Eye Galaxy (M94)",
                        SKY_LIVE_INFO_PREFIX + "messier-94-crocs-eye-galaxy-object",
                        WIKIMEDIA_PREFIX + "/1/1c/Messier_94.jpg"),
                new GalaxyRowView(getContext()));
        galaxyMap.put(new Galaxy("Vacuum Cleaner Galaxy (M109)",
                        SKY_LIVE_INFO_PREFIX + "messier-109-object",
                        WIKIMEDIA_PREFIX + "/3/3d/Messier109_-_SDSS_DR14_(panorama).jpg"),
                new GalaxyRowView(getContext()));
        galaxyMap.put(new Galaxy("NGC 2997",
                        SKY_LIVE_INFO_PREFIX + "ngc2997-object",
                        WIKIMEDIA_PREFIX + "/4/4c/N2997s.jpg"),
                new GalaxyRowView(getContext()));
        int count = 0;
        for (final Map.Entry<Galaxy, GalaxyRowView> entry
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
        for (Map.Entry<Galaxy, GalaxyRowView> entry : galaxyMap.entrySet()) {
            fetchGalaxyInfo(entry.getKey(), entry.getValue());
        }
    }

    public void fetchGalaxyInfo(
            final Galaxy galaxy,
            final GalaxyRowView view
    ) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document doc = getDocument(galaxy.theSkyLiveUrl);
                    Elements visualMagnitude = doc.select(":matchesOwn(Magnitude V)");
                    Elements visualMagnitudeValue = visualMagnitude.next(); //<h1> Planet Brightness
                    Elements majorSize = doc.select(":matchesOwn(Major Angular Size)");
                    Elements majorSizeValue = majorSize.next(); //<h1> Planet Brightness
                    Elements minorSize = doc.select(":matchesOwn(Minor Angular Size)");
                    Elements minorSizeValue = minorSize.next(); //<h1> Planet Brightness
                    updateGalaxy(
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

    class Galaxy {
        String name;
        String theSkyLiveUrl;
        String imageUrl;

        public Galaxy(
                String name,
                String theSkyLiveUrl,
                String imageUrl) {
            this.name = name;
            this.theSkyLiveUrl = theSkyLiveUrl;
            this.imageUrl = imageUrl;
        }

    }
}