package com.example.arrietty.demoapp.battery;

/**
 * Created by asus on 2017/12/6.
 */

public class PhoneBatteryInfo {
    //BatteryManager.EXTRA_STATUS
    private int status;
    //EXTRA_HEALTH
    private int health;
   //指示电池是否存在 EXTRA_PRESENT
    private boolean present;
    //EXTRA_LEVEL 当前电量 from 0 to scale
    private int level;
    //EXTRA_SCALE 最大电量等级
    private int scale;
    // EXTRA_ICON_SMALL  smallBatteryIconIndicatorStatus
    private int icon_small;
    //EXTRA_PLUGGED 插入电源类型
    private int plugged;
    //EXTRA_VOLTAGE 电压
    private int voltage;
    // EXTRA_TEMPERATURE 温度
    private float temperature;
    //EXTRA_TECHNOLOGY  Li-ion类似信息
    private String technology;
    //hide EXTRA_INVALID_CHARGER插入不支持的充电器，非0
    private int invalid_charger;
    //hide 最大充电电流，单位为umA  EXTRA_MAX_CHARGING_CURRENT
    private int max_charging_current;
    //hide 最大充电电压，单位为uV  EXTRA_MAX_CHARGING_VOLTAGE
    private int max_charging_voltage;

    //hide 充电计数器 ？？ EXTRA_CHARGE_COUNTER
    private int charge_counter;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public boolean isPresent() {
        return present;
    }

    public void setPresent(boolean present) {
        this.present = present;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public int getIcon_small() {
        return icon_small;
    }

    public void setIcon_small(int icon_small) {
        this.icon_small = icon_small;
    }

    public int getPlugged() {
        return plugged;
    }

    public void setPlugged(int plugged) {
        this.plugged = plugged;
    }

    public int getVoltage() {
        return voltage;
    }

    public void setVoltage(int voltage) {
        this.voltage = voltage;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public String getTechnology() {
        return technology;
    }

    public void setTechnology(String technology) {
        this.technology = technology;
    }

    public int getInvalid_charger() {
        return invalid_charger;
    }

    public void setInvalid_charger(int invalid_charger) {
        this.invalid_charger = invalid_charger;
    }

    public int getMax_charging_current() {
        return max_charging_current;
    }

    public void setMax_charging_current(int max_charging_current) {
        this.max_charging_current = max_charging_current;
    }

    public int getMax_charging_voltage() {
        return max_charging_voltage;
    }

    public void setMax_charging_voltage(int max_charging_voltage) {
        this.max_charging_voltage = max_charging_voltage;
    }

    public int getCharge_counter() {
        return charge_counter;
    }

    public void setCharge_counter(int charge_counter) {
        this.charge_counter = charge_counter;
    }

    @Override
    public String toString() {
        return "PhoneBatteryInfo{" +
                "status=" + status +
                ", health=" + health +
                ", present=" + present +
                ", level=" + level +
                ", scale=" + scale +
                ", icon_small=" + icon_small +
                ", plugged=" + plugged +
                ", voltage=" + voltage +
                ", temperature=" + temperature +
                ", technology='" + technology + '\'' +
                ", invalid_charger=" + invalid_charger +
                ", max_charging_current=" + max_charging_current +
                ", max_charging_voltage=" + max_charging_voltage +
                ", charge_counter=" + charge_counter +
                '}';
    }
}
