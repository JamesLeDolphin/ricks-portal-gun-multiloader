package com.jdolphin.ricksportalgun.client.screen.widget;

import com.google.common.collect.ImmutableList;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;

import java.util.Collection;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Function;

public class PGCycleButton<T> extends AbstractButton {
    private static final List<Boolean> BOOLEAN_OPTIONS;
    public static ResourceLocation ARROW_TEXTURES = PGHelper.id("textures/gui/sprites/icon/arrow.png");
    private int index;
    private T value;
    private final ValueListSupplier<T> values;
    private final Function<T, Component> valueStringifier;
    private final Function<PGCycleButton<T>, MutableComponent> narrationProvider;
    private final OnValueChange<T> onValueChange;
    private final OptionInstance.TooltipSupplier<T> tooltipSupplier;
    private boolean renderBG = true;
    private boolean renderArrows = false;
    private int color = this.active ? 16777215 : 10526880 | Mth.ceil(this.alpha * 255.0F) << 24;

    PGCycleButton(int x, int y, int width, int height, Component message, int index,
                  T value, ValueListSupplier<T> values, Function<T, Component> valueStringifier, Function<PGCycleButton<T>, MutableComponent> narrationProvider,
                  OnValueChange<T> onValueChange, OptionInstance.TooltipSupplier<T> tooltipSupplier) {
        super(x, y, width, height, message);
        this.index = index;
        this.value = value;
        this.values = values;
        this.valueStringifier = valueStringifier;
        this.narrationProvider = narrationProvider;
        this.onValueChange = onValueChange;
        this.tooltipSupplier = tooltipSupplier;
        this.updateTooltip();
    }

    public void setRenderBackground(boolean renderBG) {
        this.renderBG = renderBG;
    }

    public void setRenderArrows(boolean renderArrows) {
        this.renderArrows = renderArrows;
    }

    public void setTextColor(int color) {
        this.color = color;
    }

    public int getTextColor() {
        return color;
    }

    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        if (renderBG) {
            super.renderWidget(graphics, mouseX, mouseY, delta);
        }
        if (renderArrows) {
            float r = FastColor.ARGB32.red(color) / 255f;
            float g = FastColor.ARGB32.green(color) / 255f;
            float b = FastColor.ARGB32.blue(color) / 255f;
            float a = FastColor.ARGB32.alpha(color) / 255f;
            graphics.setColor(r,g, b, a);
            graphics.blit(ARROW_TEXTURES, this.getX() + 1, this.getY() + this.height - 18, 16, 16, 0, 0, 16, 16, 32, 16);
            graphics.blit(ARROW_TEXTURES,
                    this.getX() + this.width - 17, this.getY() + this.height - 18, 16, 16, 16, 0, 16, 16, 32, 16);
            graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        }
        Component component = this.isHovered() ? ComponentUtils.mergeStyles(this.getMessage().copy(), Style.EMPTY.withUnderlined(true)) : this.getMessage();
        renderScrollingString(graphics, Minecraft.getInstance().font, component, this.getX(), this.getY(), this.getX() + this.getWidth(), this.getY() + this.getHeight(), this.color);
    }

    private void updateTooltip() {
        this.setTooltip(this.tooltipSupplier.apply(this.value));
    }

    public void onPress() {
        if (Screen.hasShiftDown()) {
            this.cycleValue(-1);
        } else {
            this.cycleValue(1);
        }

    }

    private void cycleValue(int delta) {
        List<T> list = this.values.getSelectedList();
        this.index = Mth.positiveModulo(this.index + delta, list.size());
        T t = list.get(this.index);
        this.updateValue(t);
        this.onValueChange.onValueChange(this, t);
    }

    private T getCycledValue(int delta) {
        List<T> list = this.values.getSelectedList();
        return list.get(Mth.positiveModulo(this.index + delta, list.size()));
    }

    public boolean mouseScrolled(double p_168885_, double p_168886_, double p_168887_, double p_294881_) {
        if (p_294881_ > (double)0.0F) {
            this.cycleValue(-1);
        } else if (p_294881_ < (double)0.0F) {
            this.cycleValue(1);
        }

        return true;
    }
    public void updateValue(T value) {
        Component component = this.createLabelForValue(value);
        this.setMessage(component);
        this.value = value;
        this.updateTooltip();
    }

    private Component createLabelForValue(T value) {
        return this.valueStringifier.apply(value);
    }

    public T getValue() {
        return this.value;
    }

    protected MutableComponent createNarrationMessage() {
        return this.narrationProvider.apply(this);
    }

    public void updateWidgetNarration(NarrationElementOutput p_168889_) {
        p_168889_.add(NarratedElementType.TITLE, this.createNarrationMessage());
        if (this.active) {
            T t = this.getCycledValue(1);
            Component component = this.createLabelForValue(t);
            if (this.isFocused()) {
                p_168889_.add(NarratedElementType.USAGE, Component.translatable("narration.cycle_button.usage.focused", component));
            } else {
                p_168889_.add(NarratedElementType.USAGE, Component.translatable("narration.cycle_button.usage.hovered", component));
            }
        }

    }

    public MutableComponent createDefaultNarrationMessage() {
        return wrapDefaultNarrationMessage(this.getMessage());
    }

    public static <T> Builder<T> builder(Function<T, Component> valueStringifier) {
        return new Builder<>(valueStringifier);
    }

    public static Builder<Boolean> booleanBuilder(Component componentOn, Component componentOff) {
        return (new Builder<Boolean>((b) -> b ? componentOn : componentOff)).withValues(BOOLEAN_OPTIONS);
    }

    static {
        BOOLEAN_OPTIONS = ImmutableList.of(Boolean.TRUE, Boolean.FALSE);
    }


    public static class Builder<T> {
        private int initialIndex = 0;
        private T initialValue;
        private final Function<T, Component> valueStringifier;
        private OptionInstance.TooltipSupplier<T> tooltipSupplier = (p_168964_) -> null;
        private Function<PGCycleButton<T>, MutableComponent> narrationProvider = PGCycleButton::createDefaultNarrationMessage;
        private ValueListSupplier<T> values = ValueListSupplier.create(ImmutableList.of());

        public Builder(Function<T, Component> valueStringifier) {
            this.valueStringifier = valueStringifier;
        }

        public Builder<T> withValues(Collection<T> values) {
            return this.withValues(ValueListSupplier.create(values));
        }

        @SafeVarargs
        public final Builder<T> withValues(T... values) {
            return this.withValues(ImmutableList.copyOf(values));
        }

        public Builder<T> withValues(ValueListSupplier<T> values) {
            this.values = values;
            return this;
        }

        public Builder<T> withInitialValue(T initialValue) {
            this.initialValue = initialValue;
            int i = this.values.getDefaultList().indexOf(initialValue);
            if (i != -1) {
                this.initialIndex = i;
            }

            return this;
        }

        public PGCycleButton<T> create(Component message, OnValueChange<T> onValueChange) {
            return this.create(0, 0, 150, 20, message, onValueChange);
        }

        public PGCycleButton<T> create(int x, int y, int width, int height, Component name) {
            return this.create(x, y, width, height, name, (p_168946_, p_168947_) -> {
            });
        }

        public PGCycleButton<T> create(int x, int y, int width, int height, Component name, OnValueChange<T> onValueChange) {
            List<T> list = this.values.getDefaultList();
            if (list.isEmpty()) {
                throw new IllegalStateException("No values for cycle button");
            } else {
                T t = this.initialValue != null ? this.initialValue : list.get(this.initialIndex);
                Component component = this.valueStringifier.apply(t);
                return new PGCycleButton<T>(x, y, width, height, component, this.initialIndex, t, this.values, this.valueStringifier, this.narrationProvider, onValueChange, this.tooltipSupplier);
            }
        }
    }

    public interface ValueListSupplier<T> {
        List<T> getSelectedList();

        List<T> getDefaultList();

        static <T> ValueListSupplier<T> create(Collection<T> values) {
            final List<T> list = ImmutableList.copyOf(values);
            return new ValueListSupplier<T>() {
                public List<T> getSelectedList() {
                    return list;
                }

                public List<T> getDefaultList() {
                    return list;
                }
            };
        }

        static <T> ValueListSupplier<T> create(final BooleanSupplier altListSelector, List<T> defaultList, List<T> selectedList) {
            final List<T> list = ImmutableList.copyOf(defaultList);
            final List<T> list1 = ImmutableList.copyOf(selectedList);
            return new ValueListSupplier<T>() {
                public List<T> getSelectedList() {
                    return altListSelector.getAsBoolean() ? list1 : list;
                }

                public List<T> getDefaultList() {
                    return list;
                }
            };
        }
    }

    public interface OnValueChange<T> {
        void onValueChange(PGCycleButton<T> var1, T var2);
    }
}
