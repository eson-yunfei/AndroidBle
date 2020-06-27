package org.eson.liteble.ble.tes;

/**
 * Auth : xiao_yun_fei
 * Date : 2020/6/26 13:18
 * Package name : org.eson.liteble.ble.command
 * Des :
 */
public class RealSport {
    int sportTime;
    int step;


    public int getSportTime() {
        return sportTime;
    }

    public void setSportTime(int sportTime) {
        this.sportTime = sportTime;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    @Override
    public String toString() {
        return "RealSport{" +
                "sportTime=" + sportTime +
                ", step=" + step +
                '}';
    }
}
