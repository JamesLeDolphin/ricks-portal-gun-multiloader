package com.jdolphin.ricksportalgun.client.screen.widget;

import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractContainerWidget;
import net.minecraft.client.gui.components.events.ContainerEventHandler;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.navigation.ScreenDirection;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Predicate;

public abstract class PGScrollableWidget<E extends PGScrollableWidget.Entry<E>> extends AbstractContainerWidget {
    private static final ResourceLocation MENU_LIST_BACKGROUND = ResourceLocation.withDefaultNamespace("textures/gui/menu_list_background.png");
    private static final ResourceLocation INWORLD_MENU_LIST_BACKGROUND = ResourceLocation.withDefaultNamespace("textures/gui/inworld_menu_list_background.png");
    protected final Minecraft minecraft;
    protected final int itemHeight;
    private final TrackedList children;
    protected boolean centerListVertically;
    protected int headerHeight;
    @Nullable
    private E selected;
    @Nullable
    private E hovered;

    public PGScrollableWidget(Minecraft minecraft, int width, int height, int x, int y, int itemHeight) {
        super(x, y, width, height, CommonComponents.EMPTY);
        this.children = new PGScrollableWidget.TrackedList();
        this.centerListVertically = true;
        this.minecraft = minecraft;
        this.itemHeight = itemHeight;
    }

    @Nullable
    public E getSelected() {
        return this.selected;
    }

    public void setSelectedIndex(int selected) {
        if (selected == -1) {
            this.setSelected((E) null);
        } else if (this.getItemCount() != 0) {
            this.setSelected(this.getEntry(selected));
        }

    }

    public void setSelected(@Nullable E selected) {
        this.selected = selected;
    }

    public E getFirstElement() {
        return this.children.getFirst();
    }

    @Nullable
    public E getFocused() {
        return (E) super.getFocused();
    }

    public final @NotNull List<E> children() {
        return this.children;
    }

    protected void clearEntries() {
        this.children.clear();
        this.selected = null;
    }

    public void replaceEntries(Collection<E> entries) {
        this.clearEntries();
        this.children.addAll(entries);
    }

    protected E getEntry(int index) {
        return (E)(this.children().get(index));
    }

    protected int addEntry(E entry) {
        this.children.add(entry);
        return this.children.size() - 1;
    }

    protected void addEntryToTop(E entry) {
        double d0 = (double)this.maxScrollAmount() - this.scrollAmount();
        this.children.addFirst(entry);
        this.setScrollAmount((double)this.maxScrollAmount() - d0);
    }

    protected boolean removeEntryFromTop(E entry) {
        double d0 = (double)this.maxScrollAmount() - this.scrollAmount();
        boolean flag = this.removeEntry(entry);
        this.setScrollAmount((double)this.maxScrollAmount() - d0);
        return flag;
    }

    protected int getItemCount() {
        return this.children().size();
    }

    protected boolean isSelectedItem(int index) {
        return Objects.equals(this.getSelected(), this.children().get(index));
    }

    @Nullable
    protected final E getEntryAtPosition(double mouseX, double mouseY) {
        int i = this.getRowWidth() / 2;
        int j = this.getX() + this.width / 2;
        int k = j - i;
        int l = j + i;
        int i1 = Mth.floor(mouseY - (double)this.getY()) - this.headerHeight + (int)this.scrollAmount() - 4;
        int j1 = i1 / this.itemHeight;
        return (E)(mouseX >= (double)k && mouseX <= (double)l && j1 >= 0 && i1 >= 0 && j1 < this.getItemCount() ? (PGScrollableWidget.Entry)this.children().get(j1) : null);
    }

    public void updateSize(int width, HeaderAndFooterLayout layout) {
        this.updateSizeAndPosition(width, layout.getContentHeight(), layout.getHeaderHeight());
    }

    public void updateSizeAndPosition(int width, int height, int y) {
        this.setSize(width, height);
        this.setPosition(0, y);
        this.refreshScrollAmount();
    }

    protected int contentHeight() {
        return this.getItemCount() * this.itemHeight + this.headerHeight + 4;
    }

    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        this.hovered = this.isMouseOver(mouseX, mouseY) ? this.getEntryAtPosition(mouseX, mouseY) : null;
        this.enableScissor(graphics);

        this.renderListBackground(graphics);
        this.renderListSeparators(graphics);
        this.renderListItems(graphics, mouseX, mouseY, delta);
        graphics.disableScissor();

        this.renderScrollbar(graphics);
    }

    protected void renderListSeparators(GuiGraphics guiGraphics) {
        ResourceLocation resourcelocation = this.minecraft.level == null ? Screen.HEADER_SEPARATOR : Screen.INWORLD_HEADER_SEPARATOR;
        ResourceLocation resourcelocation1 = this.minecraft.level == null ? Screen.FOOTER_SEPARATOR : Screen.INWORLD_FOOTER_SEPARATOR;
        guiGraphics.blit(RenderType::guiTextured, resourcelocation, this.getX(), this.getY() - 2, 0.0F, 0.0F, this.getWidth(), 2, 32, 2);
        guiGraphics.blit(RenderType::guiTextured, resourcelocation1, this.getX(), this.getBottom(), 0.0F, 0.0F, this.getWidth(), 2, 32, 2);
    }

    protected void renderListBackground(GuiGraphics guiGraphics) {
        ResourceLocation resourcelocation = this.minecraft.level == null ? MENU_LIST_BACKGROUND : INWORLD_MENU_LIST_BACKGROUND;
        guiGraphics.blit(RenderType::guiTextured, resourcelocation, this.getX(), this.getY(), (float)this.getX(), (float)(this.getBottom() + (int)this.scrollAmount()), this.getWidth(), this.getHeight(), 32, 32);
    }

    protected void enableScissor(GuiGraphics guiGraphics) {
        guiGraphics.enableScissor(this.getX(), this.getY(), this.getRight(), this.getBottom());
    }

    protected void centerScrollOn(E entry) {
        this.setScrollAmount(this.children().indexOf(entry) * this.itemHeight + this.itemHeight / 2 - this.height / 2);
    }

    protected void ensureVisible(E entry) {
        int i = this.getRowTop(this.children().indexOf(entry));
        int j = i - this.getY() - 4 - this.itemHeight;
        if (j < 0) {
            this.scroll(j);
        }

        int k = this.getBottom() - i - this.itemHeight - this.itemHeight;
        if (k < 0) {
            this.scroll(-k);
        }

    }

    private void scroll(int scroll) {
        this.setScrollAmount(this.scrollAmount() + (double)scroll);
    }

    protected double scrollRate() {
        return (double)this.itemHeight / (double)2.0F;
    }

    protected int scrollBarX() {
        return this.getRowRight() + 6 + 2;
    }

    public @NotNull Optional<GuiEventListener> getChildAt(double mouseX, double mouseY) {
        return Optional.ofNullable(this.getEntryAtPosition(mouseX, mouseY));
    }

    public void setFocused(@Nullable GuiEventListener listener) {
        E e = this.getFocused();
        if (e != listener && e instanceof ContainerEventHandler containereventhandler) {
            containereventhandler.setFocused(null);
        }

        super.setFocused(listener);
        int i = this.children.indexOf(listener);
        if (i >= 0) {
            E e1 = this.children.get(i);
            this.setSelected(e1);
            if (this.minecraft.getLastInputType().isKeyboard()) {
                this.ensureVisible(e1);
            }
        }

    }

    @Nullable
    protected E nextEntry(ScreenDirection direction) {
        return this.nextEntry(direction, (p_93510_) -> true);
    }

    @Nullable
    protected E nextEntry(ScreenDirection direction, Predicate<E> predicate) {
        return this.nextEntry(direction, predicate, this.getSelected());
    }

    @Nullable
    protected E nextEntry(ScreenDirection direction, Predicate<E> predicate, @Nullable E selected) {

        int i = switch (direction) {
            case RIGHT, LEFT -> 0;
            case UP -> -1;
            case DOWN -> 1;
        };
        if (!this.children().isEmpty() && i != 0) {
            int j;
            if (selected == null) {
                j = i > 0 ? 0 : this.children().size() - 1;
            } else {
                j = this.children().indexOf(selected) + i;
            }

            for(int k = j; k >= 0 && k < this.children.size(); k += i) {
                E e = this.children().get(k);
                if (predicate.test(e)) {
                    return e;
                }
            }
        }

        return null;
    }

    protected void renderListItems(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        int i = this.getRowLeft();
        int j = this.getRowWidth();
        int k = this.itemHeight - 4;
        int l = this.getItemCount();

        for (int i1 = 0; i1 < l; ++i1) {
            int j1 = this.getRowTop(i1);
            int k1 = this.getRowBottom(i1);
            if (k1 >= this.getY() && j1 <= this.getBottom()) {
                this.renderItem(guiGraphics, mouseX, mouseY, partialTick, i1, i, j1, j, k);
            }
        }

    }

    protected void renderItem(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, int index, int left, int top, int width, int height) {
        E e = this.getEntry(index);
        e.renderBack(guiGraphics, index, top, left, width, height, mouseX, mouseY, Objects.equals(this.hovered, e), partialTick);
        if (this.isSelectedItem(index)) {
            int i = this.isFocused() ? -1 : -8355712;
            this.renderSelection(guiGraphics, top, width, height, i, -16777216);
        }

        e.render(guiGraphics, index, top, left, width, height, mouseX, mouseY, Objects.equals(this.hovered, e), partialTick);
    }

    protected void renderSelection(GuiGraphics guiGraphics, int top, int width, int height, int outerColor, int innerColor) {
        int i = this.getX() + (this.width - width) / 2;
        int j = this.getX() + (this.width + width) / 2;
        guiGraphics.fill(i, top - 2, j, top + height + 2, outerColor);
        guiGraphics.fill(i + 1, top - 1, j - 1, top + height + 1, innerColor);
    }

    public int getRowLeft() {
        return this.getX() + this.width / 2 - this.getRowWidth() / 2 + 2;
    }

    public int getRowRight() {
        return this.getRowLeft() + this.getRowWidth();
    }

    public int getRowTop(int index) {
        return this.getY() + 4 - (int)this.scrollAmount() + index * this.itemHeight + this.headerHeight;
    }

    public int getRowBottom(int index) {
        return this.getRowTop(index) + this.itemHeight;
    }

    public int getRowWidth() {
        return 220;
    }

    public NarratableEntry.@NotNull NarrationPriority narrationPriority() {
        if (this.isFocused()) {
            return NarrationPriority.FOCUSED;
        } else {
            return this.hovered != null ? NarrationPriority.HOVERED : NarrationPriority.NONE;
        }
    }

    @Override
    protected void updateWidgetNarration(@NotNull NarrationElementOutput narrationElementOutput) {

    }

    @Nullable
    protected E remove(int index) {
        E e = this.children.get(index);
        return this.removeEntry(this.children.get(index)) ? e : null;
    }

    protected boolean removeEntry(E entry) {
        boolean flag = this.children.remove(entry);
        if (flag && entry == this.getSelected()) {
            this.setSelected((E) null);
        }

        return flag;
    }

    @Nullable
    protected E getHovered() {
        return this.hovered;
    }

    void bindEntryToSelf(PGScrollableWidget.Entry<E> entry) {
        entry.list = this;
    }

    protected void narrateListElementPosition(NarrationElementOutput narrationElementOutput, E entry) {
        List<E> list = this.children();
        if (list.size() > 1) {
            int i = list.indexOf(entry);
            if (i != -1) {
                narrationElementOutput.add(NarratedElementType.POSITION, Component.translatable("narrator.position.list", i + 1, list.size()));
            }
        }

    }

    protected abstract static class Entry<E extends PGScrollableWidget.Entry<E>> implements GuiEventListener {
        /** @deprecated */
        @Deprecated
        protected PGScrollableWidget<E> list;

        protected Entry() {
        }

        public void setFocused(boolean p_265302_) {
        }

        public boolean isFocused() {
            return this.list.getFocused() == this;
        }

        public abstract void render(GuiGraphics var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, boolean var9, float var10);

        public void renderBack(GuiGraphics guiGraphics, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean isMouseOver, float partialTick) {
        }

        public boolean isMouseOver(double mouseX, double mouseY) {
            return Objects.equals(this.list.getEntryAtPosition(mouseX, mouseY), this);
        }
    }

    class TrackedList extends AbstractList<E> {
        private final List<E> delegate = Lists.newArrayList();

        TrackedList() {
        }

        public E get(int index) {
            return this.delegate.get(index);
        }

        public int size() {
            return this.delegate.size();
        }

        public E set(int index, E entry) {
            E e = this.delegate.set(index, entry);
            PGScrollableWidget.this.bindEntryToSelf(entry);
            return e;
        }

        public void add(int index, E entry) {
            this.delegate.add(index, entry);
            PGScrollableWidget.this.bindEntryToSelf(entry);
        }

        public E remove(int index) {
            return this.delegate.remove(index);
        }
    }
}
