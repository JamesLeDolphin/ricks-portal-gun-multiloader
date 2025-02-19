package com.jdolphin.ricksportalgun.client.screen.widget;

import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

import java.text.DecimalFormat;

public class Slider extends AbstractSliderButton {
    protected Component msg;
    protected double minValue;
    protected double maxValue;
    protected double stepSize;
    protected boolean drawString;
    private final DecimalFormat format;

    public Slider(int x, int y, int width, int height, Component message, double currentValue, double minValue, double maxValue, double stepSize, int precision, boolean drawString) {
        super(x, y, width, height, Component.empty(), 0.0F);
        this.msg = message;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.stepSize = Math.abs(stepSize);
        this.value = this.snapToNearest((currentValue - minValue) / (maxValue - minValue));
        this.drawString = drawString;
        if (stepSize == (double)0.0F) {
            precision = Math.min(precision, 4);
            StringBuilder builder = new StringBuilder("0");
            if (precision > 0) {
                builder.append('.');
            }

            while(precision-- > 0) {
                builder.append('0');
            }

            this.format = new DecimalFormat(builder.toString());
        } else if (Mth.equal(this.stepSize, Math.floor(this.stepSize))) {
            this.format = new DecimalFormat("0");
        } else {
            this.format = new DecimalFormat(Double.toString(this.stepSize).replaceAll("\\d", "0"));
        }

        this.updateMessage();
    }

    public Slider(int x, int y, int width, int height, Component message, double currentValue, double minValue, double maxValue, boolean drawString) {
        this(x, y, width, height, message, currentValue, minValue, maxValue, 0.1F, 0, drawString);
    }

    public double getValue() {
        return this.value * (this.maxValue - this.minValue) + this.minValue;
    }

    public long getValueLong() {
        return Math.round(this.getValue());
    }

    public int getValueInt() {
        return (int)this.getValueLong();
    }

    public void setValue(double value) {
        this.value = this.snapToNearest((value - this.minValue) / (this.maxValue - this.minValue));
        this.updateMessage();
    }

    public String getValueString() {
        this.format.applyPattern("#.#");
        return this.format.format(this.getValue());
    }

    public void onClick(double mouseX, double mouseY) {
        this.setValueFromMouse(mouseX);
    }

    protected void onDrag(double mouseX, double mouseY, double dragX, double dragY) {
        super.onDrag(mouseX, mouseY, dragX, dragY);
        this.setValueFromMouse(mouseX);
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        boolean flag = keyCode == 263;
        if (flag || keyCode == 262) {
            if (this.minValue > this.maxValue) {
                flag = !flag;
            }

            float f = flag ? -1.0F : 1.0F;
            if (this.stepSize <= (double)0.0F) {
                this.setSliderValue(this.value + (double)(f / (float)(this.width - 8)));
            } else {
                this.setValue(this.getValue() + (double)f * this.stepSize);
            }
        }

        return false;
    }

    private void setValueFromMouse(double mouseX) {
        this.setSliderValue((mouseX - (double)(this.getX() + 4)) / (double)(this.width - 8));
    }

    private void setSliderValue(double value) {
        double oldValue = this.value;
        this.value = this.snapToNearest(value);
        if (!Mth.equal(oldValue, this.value)) {
            this.applyValue();
        }

        this.updateMessage();
    }

    private double snapToNearest(double value) {
        if (this.stepSize <= (double)0.0F) {
            return Mth.clamp(value, 0.0F, 1.0F);
        } else {
            value = Mth.lerp(Mth.clamp(value, 0.0F, 1.0F), this.minValue, this.maxValue);
            value = this.stepSize * (double)Math.round(value / this.stepSize);
            if (this.minValue > this.maxValue) {
                value = Mth.clamp(value, this.maxValue, this.minValue);
            } else {
                value = Mth.clamp(value, this.minValue, this.maxValue);
            }

            return Mth.map(value, this.minValue, this.maxValue, 0.0F, 1.0F);
        }
    }

    protected void updateMessage() {
        if (this.drawString) {
            this.setMessage(Component.empty().append(this.msg).append(this.getValueString()));
        } else {
            this.setMessage(Component.empty());
        }

    }

    protected void applyValue() {
    }
}