package smarthur.space.astrotoday;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

public class PlanetaryNebulaRowView extends ConstraintLayout {
    TextView nameLabel;
    TextView visualMagnitude;
    TextView majorSize;
    TextView minorSize;
    TransitInfoView transitInfoView;

    public PlanetaryNebulaRowView(Context context) {
        super(context);
        init();
    }

    public PlanetaryNebulaRowView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    void init() {
        inflate(getContext(), R.layout.planetary_row, this);
        nameLabel = findViewById(R.id.name_label);
        visualMagnitude = findViewById(R.id.planet_magnitude);
        majorSize = findViewById(R.id.planet_size);
        minorSize = findViewById(R.id.planet_size);
        transitInfoView = findViewById(R.id.transit_info);
    }


}
