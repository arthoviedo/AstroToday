package smarthur.space.astrotoday;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

public class TransitInfoView extends ConstraintLayout {
    TextView riseTime;
    TextView transitTime;
    TextView setTime;

    public TransitInfoView(Context context) {
        super(context);
        init();
    }

    public TransitInfoView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    void init() {
        inflate(getContext(), R.layout.transit_info_row, this);
        riseTime = findViewById(R.id.rise_time);
        transitTime = findViewById(R.id.transit_time);
        setTime = findViewById(R.id.set_time);
    }

    public void updateTransit(
            Activity activity,
            final String riseTime,
            final String transitTime,
            final String setTime) {

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TransitInfoView.this.riseTime.setText(getContext().getString(R.string.rise_time, riseTime));
                TransitInfoView.this.transitTime.setText(getContext().getString(R.string.transit_time, transitTime));
                TransitInfoView.this.setTime.setText(getContext().getString(R.string.set_time, setTime));
            }
        });
    }
}
