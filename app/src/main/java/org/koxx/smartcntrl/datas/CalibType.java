package org.koxx.smartcntrl.datas;

public enum CalibType {
    BrakeMaxPressure(0),
    BatMaxVoltage(1),
    BatMinVoltage(2),
    CurrentZero(3),
    BrakeMinPressure(4);

    private int numVal;

    CalibType(int numVal) {
        this.numVal = numVal;
    }

    public int getNumVal() {
        return numVal;
    }
}
