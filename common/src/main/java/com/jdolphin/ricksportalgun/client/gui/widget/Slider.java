package com.jdolphin.ricksportalgun.client.gui.widget;


import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;

public class Slider extends AbstractSliderButton {
    private float minValue;
    private float maxValue;
    public Slider(int x, int y, int width, int height, MutableComponent text, double value, int minValue, int maxValue) {
        super(x, y, width, height, text, value);
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    public Slider(int x, int y, int width, int height, MutableComponent text, double value, float minValue, float maxValue) {
        this(x, y, width, height, text, value, ((int) minValue), ((int) maxValue));
    }

    public Slider(int x, int y, int width, int height, MutableComponent text, double value, double minValue, double maxValue) {
        this(x, y, width, height, text, value, ((int) minValue), ((int) maxValue));
    }

    @Override
    protected void updateMessage() {

    }

    public float getValue() {
        return (float) this.value;
    }

    public void setValue(float v) {
        this.value = v;
    }

    @Override
    protected void applyValue() {
        this.value = Mth.clamp(this.value, this.minValue, this.maxValue);
    }
}