package smarthur.space.astrotoday.model;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class SkyObjectsListViewModel extends ViewModel {

    private MutableLiveData<List<? extends SkyObjectViewModel>> skyObjectsList;

    public MutableLiveData<List<? extends SkyObjectViewModel>> getSkyObjectsList() {
        if (skyObjectsList == null) {
            skyObjectsList = new MutableLiveData<>();
        }
        return skyObjectsList;
    }
}
