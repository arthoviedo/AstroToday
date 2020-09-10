package smarthur.space.astrotoday;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;

public class PlanetaryNebulaRowView extends ConstraintLayout {
    AppCompatTextView nameLabel;
    AppCompatTextView visualMagnitude;
    AppCompatTextView majorMinorSize;
    AppCompatImageView image;
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
        inflate(getContext(), R.layout.planetary_nebula_row, this);
        nameLabel = findViewById(R.id.name_label);
        visualMagnitude = findViewById(R.id.visual_magnitude);
        majorMinorSize = findViewById(R.id.major_minor_size);
        transitInfoView = findViewById(R.id.transit_info);
        image = findViewById(R.id.image);
    }


}
