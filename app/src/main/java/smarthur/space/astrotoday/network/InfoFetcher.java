package smarthur.space.astrotoday.network;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ThreadPoolExecutor;

import smarthur.space.astrotoday.PlanetRowView;
import smarthur.space.astrotoday.model.PlanetViewModel;
import smarthur.space.astrotoday.model.PlanetsEnum;
import smarthur.space.astrotoday.model.PlanetsViewModel;
import smarthur.space.astrotoday.model.SkyObjectViewModel;

public class InfoFetcher {
    static FutureTask<SkyObjectViewModel> fetchInfo(final Object key, final String url) {
        return new FutureTask<SkyObjectViewModel>(new Callable<SkyObjectViewModel>() {
            @Override
            public SkyObjectViewModel call() throws Exception {
                SkyObjectViewModel result = null;
                if (key instanceof PlanetsEnum) {
                    result = new PlanetViewModel();
                }
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

                    Elements riseInfo = doc.select("div[class=\"rise\"]");
                    Elements transitInfo = riseInfo.next();
                    Elements setInfo = transitInfo.next();

                    String riseInfoText = riseInfo.first().child(2).text();
                    String transitInfoText = transitInfo.first().child(2).text();
                    String setInfoText = setInfo.first().child(2).text();

                    result.transitInfo.riseTime = riseInfoText;
                    result.transitInfo.transitTime = transitInfoText;
                    result.transitInfo.setTime = setInfoText;

                    assert key instanceof PlanetsEnum;
                    ((PlanetViewModel) result).planet = (PlanetsEnum) key;

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return result;
            }
        });
    }


    private final static String COOKIE_INFO =
            "-33.86785%7C151.20732%7CSydney+%28AU%29%7CAustralia%2FSydney%7C0";

   static  Document getDocument(String url) throws Exception {
        Connection connection = Jsoup.connect(url);
        connection.cookie(
                "localdata_v1",
                COOKIE_INFO);
        return connection.get();
    }

    public static void updateData(Set<PlanetsEnum> planetSet, PlanetsViewModel model) {
       ExecutorService executorService = Executors.newFixedThreadPool(50);
       List<PlanetViewModel> planetsViewModels = new ArrayList<>();
       for (PlanetsEnum planet : planetSet) {
           try {
               FutureTask<SkyObjectViewModel> fetchTask = fetchInfo(planet, planet.url);
               executorService.execute(fetchTask);
               planetsViewModels.add((PlanetViewModel) fetchTask.get());

           } catch (Exception e) {
               e.printStackTrace();
           }
       }
       model.getSkyObjectsList().setValue(planetsViewModels);
    }
}
