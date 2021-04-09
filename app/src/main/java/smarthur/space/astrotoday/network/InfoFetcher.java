package smarthur.space.astrotoday.network;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import smarthur.space.astrotoday.model.PlanetaryNebulaViewModel;
import smarthur.space.astrotoday.model.TransitInfo;
import smarthur.space.astrotoday.model.enums.GalaxyEnum;
import smarthur.space.astrotoday.model.GalaxyViewModel;
import smarthur.space.astrotoday.model.PlanetViewModel;
import smarthur.space.astrotoday.model.enums.PlanetaryNebulaeEnum;
import smarthur.space.astrotoday.model.enums.PlanetsEnum;
import smarthur.space.astrotoday.model.enums.SkyObjectEnum;
import smarthur.space.astrotoday.model.SkyObjectsListViewModel;
import smarthur.space.astrotoday.model.SkyObjectViewModel;
import smarthur.space.astrotoday.util.Constants;

public class InfoFetcher {
    static FutureTask<SkyObjectViewModel> fetchPlanetInfo(final PlanetsEnum key, final String url) {
        return new FutureTask<>(new Callable<SkyObjectViewModel>() {
            @Override
            public SkyObjectViewModel call() throws Exception {
                PlanetViewModel result = new PlanetViewModel();
                try {
                    Document doc = getDocument(url);
                    Elements parent = doc.select("a[name=\"brightness\"]");
                    Elements temp1 = parent.next(); //<h1> Planet Brightness
                    Elements temp2 = temp1.next();          //<p>
                    Element div1 = temp2.next().first();          //<div>
                    Element magnitude = div1.child(1); // AR
                    Element div2 = temp2.next().next().first();
                    Element size = div2.child(1); //Diameter
                    String magnitudeText = magnitude.text();
                    String sizeText = size.text();

                    result.magnitude = magnitudeText;
                    result.size = sizeText;
                    result.transitInfo = extractTransitInfo(doc);

                    result.planet = key;

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return result;
            }
        });
    }

    static FutureTask<SkyObjectViewModel> fetchGalaxyInfo(
            final GalaxyEnum key,
            final String url) {
        return new FutureTask<>(new Callable<SkyObjectViewModel>() {
            @Override
            public SkyObjectViewModel call() throws Exception {
                GalaxyViewModel result = new GalaxyViewModel();
                result.galaxy = key;
                try {
                    Document doc = getDocument(url);
                    Elements visualMagnitude = doc.select(":matchesOwn(Magnitude V)");
                    Elements visualMagnitudeValue = visualMagnitude.next(); //<h1> Planet Brightness
                    Elements majorSize = doc.select(":matchesOwn(Major Angular Size)");
                    Elements majorSizeValue = majorSize.next(); //<h1> Planet Brightness
                    Elements minorSize = doc.select(":matchesOwn(Minor Angular Size)");
                    Elements minorSizeValue = minorSize.next(); //<h1> Planet Brightness

                    result.magnitude = visualMagnitudeValue.text();
                    result.majorSize = majorSizeValue.text();
                    result.minorSize = minorSizeValue.text();
                    result.transitInfo = extractTransitInfo(doc);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return result;
            }
        });
    }

    static FutureTask<SkyObjectViewModel> fetchPlanetaryNebulaInfo(
            final PlanetaryNebulaeEnum key,
            final String url) {
        return new FutureTask<>(new Callable<SkyObjectViewModel>() {
            @Override
            public SkyObjectViewModel call() throws Exception {
                PlanetaryNebulaViewModel result = new PlanetaryNebulaViewModel();
                result.planetaryNebula = key;
                try {
                    Document doc = getDocument(url);
                    Elements visualMagnitude = doc.select(":matchesOwn(Magnitude V)");
                    Elements visualMagnitudeValue = visualMagnitude.next(); //<h1> Planet Brightness
                    Elements majorSize = doc.select(":matchesOwn(Major Angular Size)");
                    Elements majorSizeValue = majorSize.next(); //<h1> Planet Brightness
                    Elements minorSize = doc.select(":matchesOwn(Minor Angular Size)");
                    Elements minorSizeValue = minorSize.next(); //<h1> Planet Brightness

                    result.magnitude = visualMagnitudeValue.text();
                    result.majorSize = majorSizeValue.text();
                    result.minorSize = minorSizeValue.text();
                    result.transitInfo = extractTransitInfo(doc);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return result;
            }
        });
    }

    static TransitInfo extractTransitInfo(Document doc) {
        Elements riseInfo = doc.select("div[class=\"rise\"]");
        Elements transitInfo = riseInfo.next();
        Elements setInfo = transitInfo.next();
        try {
            return new TransitInfo(
                    riseInfo.first().child(2).text(),
                    transitInfo.first().child(2).text(),
                    setInfo.first().child(2).text());
        } catch (Exception e) {
            return new TransitInfo("N/A", "N/A", "N/A");
        }
    }

   static  Document getDocument(String url) throws Exception {
        Connection connection = Jsoup.connect(url);
        connection.cookie(
                "localdata_v1",
                Constants.COOKIE_INFO);
        return connection.get();
    }

    public static void updateData(Set<? extends SkyObjectEnum> skyObjectEnumSet, SkyObjectsListViewModel model) {
       ExecutorService executorService = Executors.newFixedThreadPool(50);
       List<SkyObjectViewModel> skyObjectsViewModels = new ArrayList<>();
       for (SkyObjectEnum skyObject : skyObjectEnumSet) {
           try {
               FutureTask<SkyObjectViewModel> fetchTask = null;
               if (skyObject instanceof PlanetsEnum) {
                fetchTask = fetchPlanetInfo((PlanetsEnum) skyObject, ((PlanetsEnum) skyObject).url);
               } else if (skyObject instanceof GalaxyEnum) {
                   fetchTask = fetchGalaxyInfo((GalaxyEnum) skyObject, ((GalaxyEnum) skyObject).url);
               }
               else if (skyObject instanceof PlanetaryNebulaeEnum) {
                   fetchTask = fetchPlanetaryNebulaInfo((PlanetaryNebulaeEnum) skyObject, ((PlanetaryNebulaeEnum) skyObject).url);
               }
               if (fetchTask == null) {
                   throw new Exception("Invalid sky object type");
               }
               executorService.execute(fetchTask);
               skyObjectsViewModels.add(fetchTask.get());

           } catch (Exception e) {
               e.printStackTrace();
           }
       }
       model.getSkyObjectsList().setValue(skyObjectsViewModels);
    }
}
