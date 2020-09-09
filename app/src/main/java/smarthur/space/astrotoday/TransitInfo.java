package smarthur.space.astrotoday;

import java.util.Calendar;

class TransitInfo {
    Calendar riseTime;
    Calendar transitTime;
    Calendar setTime;

    TransitInfo(Calendar riseTime, Calendar transitTime, Calendar setTime) {
        this.riseTime = riseTime;
        this.transitTime = transitTime;
        this.setTime = setTime;
    }
}