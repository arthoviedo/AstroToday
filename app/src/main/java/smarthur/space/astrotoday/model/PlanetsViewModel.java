package smarthur.space.astrotoday.model;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class PlanetsViewModel extends ViewModel {

    private MutableLiveData<List<PlanetViewModel>> skyObjectsList;

    public MutableLiveData<List<PlanetViewModel>> getSkyObjectsList() {
        if (skyObjectsList == null) {
            skyObjectsList = new MutableLiveData<>();
        }
        return skyObjectsList;
    }
}
