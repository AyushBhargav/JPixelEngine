package util;

import lombok.AllArgsConstructor;
import lombok.Data;

public class Time {

    public static float timeStarted = System.nanoTime();

    public static float getTime() {
        return (float) ((System.nanoTime() - timeStarted) * 1E-9);
    }

    @Data
    @AllArgsConstructor
    public static class TimeInfo {
        float beginTime, endTime, dt;

        public void tick() {
            endTime = Time.getTime();
            dt = endTime - beginTime;
            beginTime = endTime;
        }
    }
}
