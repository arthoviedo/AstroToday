package smarthur.space.astrotoday;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

public class PlanetRowView extends ConstraintLayout {
    TextView planetMagnitude;
    TextView planetSize;
    TextView nameLabel;
    TextView seeingCondition;

    public PlanetRowView(Context context) {
        super(context);
        init();
    }

    public PlanetRowView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    void init() {
        inflate(getContext(), R.layout.planetary_row, this);
        planetMagnitude = findViewById(R.id.planet_magnitude);
        planetSize = findViewById(R.id.planet_size);
        nameLabel = findViewById(R.id.name_label);
    }


}
