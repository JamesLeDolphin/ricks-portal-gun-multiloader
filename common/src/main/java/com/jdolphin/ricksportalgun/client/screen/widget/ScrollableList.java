//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.jdolphin.ricksportalgun.client.screen.widget;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.AbstractContainerEventHandler;
import net.minecraft.client.gui.components.events.ContainerEventHandler;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.navigation.ScreenDirection;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

import javax.annotation.Nullable;
import java.util.AbstractList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public abstract class ScrollableList<E extends ScrollableList.Entry<E>> extends AbstractContainerEventHandler implements Renderable, NarratableEntry {
    protected final Minecraft minecraft;
    protected final int itemHeight;
    private final List<E> children;
    protected int width;
    protected int height;
    protected int y0;
    protected int y1;
    protected int x1;
    protected int x0;
    public boolean visible = true;
    private double scrollAmount;
    protected int headerHeight;
    private boolean scrolling;
    @Nullable
    private E selected;
    @Nullable
    private E hovered;
    private boolean renderScrollbar = true;

    public ScrollableList(Minecraft pMinecraft, int pWidth, int pHeight, int x0, int pY0, int pItemHeight) {
        this.children = new TrackedList();
        this.minecraft = pMinecraft;
        this.width = pWidth;
        this.height = pHeight;
        this.y0 = pY0;
        this.y1 = pY0 + pHeight;
        this.itemHeight = pItemHeight;
        this.x0 = x0;
        this.x1 = x0 + pWidth;
    }

    public void setRenderScrollbar(boolean renderScrollbar) {
        this.renderScrollbar = renderScrollbar;
    }

    public boolean renderScrollbar() {
        return renderScrollbar;
    }

    public int getRowWidth() {
        return 110;
    }

    @Nullable
    public E getSelected() {
        return this.selected;
    }

    public void setSelected(@Nullable E pSelected) {
        this.selected = pSelected;
    }

    public E getFirstElement() {
        return this.children.get(0);
    }

    @Nullable
    public E getFocused() {
        return (E) super.getFocused();
    }

    public final List<E> children() {
        return this.children;
    }

    protected void clearEntries() {
        this.children.clear();
        this.selected = null;
    }

    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        this.setScrollAmount(this.getScrollAmount() - scrollY * (double)this.itemHeight / (double)2.0F);
        return true;
    }

    protected void replaceEntries(Collection<E> pEntries) {
        this.clearEntries();
        this.children.addAll(pEntries);
    }

    protected E getEntry(int pIndex) {
        return this.children().get(pIndex);
    }

    protected int addEntry(E pEntry) {
        this.children.add(pEntry);
        return this.children.size() - 1;
    }

    protected void addEntryToTop(E pEntry) {
        double d0 = (double)this.getMaxScroll() - this.getScrollAmount();
        this.children.add(0, pEntry);
        this.setScrollAmount((double)this.getMaxScroll() - d0);
    }

    protected boolean removeEntryFromTop(E pEntry) {
        double d0 = (double)this.getMaxScroll() - this.getScrollAmount();
        boolean flag = this.removeEntry(pEntry);
        this.setScrollAmount((double)this.getMaxScroll() - d0);
        return flag;
    }

    protected int getItemCount() {
        return this.children().size();
    }

    protected boolean isSelectedItem(int pIndex) {
        return Objects.equals(this.getSelected(), this.children().get(pIndex));
    }

    public double scrollAmount() {
        return this.scrollAmount;
    }

    @Nullable
    public final E getEntryAtPosition(double mouseX, double mouseY) {
        int i = this.getRowWidth() / 2;
        int j = this.getLeft() + this.width / 2;
        int k = j - i;
        int l = j + i;
        int i1 = Mth.floor(mouseY - (double)this.getTop()) - this.headerHeight + (int)this.scrollAmount() - 4;
        int j1 = i1 / this.itemHeight;
        return (E)(mouseX >= (double)k && mouseX <= (double)l && j1 >= 0 && i1 >= 0 && j1 < this.getItemCount() ? (Entry)this.children().get(j1) : null);
    }

    public void updateSize(int pWidth, int pHeight, int x1, int pY0, int pY1) {
        this.width = pWidth;
        this.height = pHeight;
        this.y0 = pY0;
        this.y1 = pY1;
        this.x0 = x1;
        this.x1 = x0 + pWidth;
    }

    public void setLeftPos(int pX0) {
        this.x0 = pX0;
        this.x1 = pX0 + this.width;
    }

    protected int getMaxPosition() {
        return this.getItemCount() * this.itemHeight + this.headerHeight;
    }

    protected void clickedHeader(int pMouseX, int pMouseY) {
    }

    protected void renderBackground(GuiGraphics pGuiGraphics) {
    }

    protected void renderDecorations(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {
    }

    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        if (visible) {
            this.renderBackground(pGuiGraphics);
            int i = this.getScrollbarPosition();
            int j = i + 6;
            this.hovered = this.isMouseOver(pMouseX, pMouseY) ? this.getEntryAtPosition(pMouseX, pMouseY) : null;

            this.enableScissor(pGuiGraphics);


            this.renderList(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
            pGuiGraphics.disableScissor();

            if (this.renderScrollbar) {
            int i2 = this.getMaxScroll();
            if (i2 > 0) {
                int j2 = (int) ((float) ((this.y1 - this.y0) * (this.y1 - this.y0)) / (float) this.getMaxPosition());
                j2 = Mth.clamp(j2, 32, this.y1 - this.y0 - 8);
                int k1 = (int) this.getScrollAmount() * (this.y1 - this.y0 - j2) / i2 + this.y0;
                if (k1 < this.y0) {
                    k1 = this.y0;
                }

               pGuiGraphics.fill(i, this.y0, j, this.y1, -16777216);
               pGuiGraphics.fill(i, k1, j, k1 + j2, -8355712);
               pGuiGraphics.fill(i, k1, j - 1, k1 + j2 - 1, -4144960);
            }

            this.renderDecorations(pGuiGraphics, pMouseX, pMouseY);
            RenderSystem.disableBlend();
        }
            }
    }

    protected void enableScissor(GuiGraphics pGuiGraphics) {
        pGuiGraphics.enableScissor(this.x0, this.y0, this.x1, this.y1);
    }

    protected void centerScrollOn(E pEntry) {
        this.setScrollAmount(this.children().indexOf(pEntry) * this.itemHeight + (double) this.itemHeight / 2 - (double) (this.y1 - this.y0) / 2);
    }

    protected void ensureVisible(E pEntry) {
        int i = this.getRowTop(this.children().indexOf(pEntry));
        int j = i - this.y0 - 4 - this.itemHeight;
        if (j < 0) {
            this.scroll(j);
        }

        int k = this.y1 - i - this.itemHeight - this.itemHeight;
        if (k < 0) {
            this.scroll(-k);
        }

    }

    private void scroll(int pScroll) {
        this.setScrollAmount(this.getScrollAmount() + (double)pScroll);
    }

    public double getScrollAmount() {
        return this.scrollAmount;
    }

    public void setScrollAmount(double pScroll) {
        this.scrollAmount = Mth.clamp(pScroll, 0.0F, this.getMaxScroll());
    }

    public int getMaxScroll() {
        return Math.max(0, this.getMaxPosition() - (this.y1 - this.y0 - 4));
    }

    public int getScrollBottom() {
        return (int)this.getScrollAmount() - this.height - this.headerHeight;
    }

    protected void updateScrollingState(double pMouseX, double pMouseY, int pButton) {
        this.scrolling = pButton == 0 && pMouseX >= (double)this.getScrollbarPosition() && pMouseX < (double)(this.getScrollbarPosition() + 6);
    }

    protected int getScrollbarPosition() {
        return this.width / 2 + 124;
    }

    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        if (this.visible) {
            this.updateScrollingState(pMouseX, pMouseY, pButton);
            if (!this.isMouseOver(pMouseX, pMouseY)) {
                return false;
            } else {
                E e = this.getEntryAtPosition(pMouseX, pMouseY);
                if (e != null) {
                    if (e.mouseClicked(pMouseX, pMouseY, pButton)) {
                        this.setFocused(e);
                        this.setDragging(true);
                        return true;
                    }
                } else if (pButton == 0) {
                    this.clickedHeader((int) (pMouseX - (double) (this.x0 + this.width / 2 - this.getRowWidth() / 2)), (int) (pMouseY - (double) this.y0) + (int) this.getScrollAmount() - 4);
                    return true;
                }

                return this.scrolling;
            }
        } else return false;
    }

    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        this.setScrollAmount(this.getScrollAmount() - delta * (double)this.itemHeight / (double)2.0F);
        return true;
    }

    public boolean mouseReleased(double pMouseX, double pMouseY, int pButton) {
        if (this.getFocused() != null) {
            this.getFocused().mouseReleased(pMouseX, pMouseY, pButton);
        }

        return false;
    }

    public boolean mouseDragged(double pMouseX, double pMouseY, int pButton, double pDragX, double pDragY) {
        if (super.mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY)) {
            return true;
        } else if (pButton == 0 && this.scrolling) {
            if (pMouseY < (double)this.y0) {
                this.setScrollAmount(0.0F);
            } else if (pMouseY > (double)this.y1) {
                this.setScrollAmount(this.getMaxScroll());
            } else {
                double d0 = Math.max(1, this.getMaxScroll());
                int i = this.y1 - this.y0;
                int j = Mth.clamp((int)((float)(i * i) / (float)this.getMaxPosition()), 32, i - 8);
                double d1 = Math.max(1.0F, d0 / (double)(i - j));
                this.setScrollAmount(this.getScrollAmount() + pDragY * d1);
            }

            return true;
        } else {
            return false;
        }
    }

    public void setFocused(@Nullable GuiEventListener pListener) {
        super.setFocused(pListener);
        int i = this.children.indexOf(pListener);
        if (i >= 0) {
            E e = this.children.get(i);
            this.setSelected(e);
            if (this.minecraft.getLastInputType().isKeyboard()) {
                this.ensureVisible(e);
            }
        }

    }

    @Nullable
    protected E nextEntry(ScreenDirection pDirection) {
        return this.nextEntry(pDirection, (e) -> true);
    }

    @Nullable
    protected E nextEntry(ScreenDirection pDirection, Predicate<E> pPredicate) {
        return this.nextEntry(pDirection, pPredicate, this.getSelected());
    }

    @Nullable
    protected E nextEntry(ScreenDirection pDirection, Predicate<E> pPredicate, @Nullable E pSelected) {
        byte b0 = switch (pDirection) {
            case RIGHT, LEFT -> 0;
            case UP -> -1;
            case DOWN -> 1;
        };

        if (!this.children().isEmpty() && b0 != 0) {
            int j;
            if (pSelected == null) {
                j = b0 > 0 ? 0 : this.children().size() - 1;
            } else {
                j = this.children().indexOf(pSelected) + b0;
            }

            for(int k = j; k >= 0 && k < this.children.size(); k += b0) {
                E e = this.children().get(k);
                if (pPredicate.test(e)) {
                    return e;
                }
            }
        }

        return null;
    }

    public boolean isMouseOver(double pMouseX, double pMouseY) {
        return pMouseY >= (double)this.y0 && pMouseY <= (double)this.y1 && pMouseX >= (double)this.x0 && pMouseX <= (double)this.x1;
    }

    protected void renderList(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        int i = this.getRowLeft();
        int j = this.getRowWidth();
        int k = this.itemHeight - 4;
        int l = this.getItemCount();

        for(int i1 = 0; i1 < l; ++i1) {
            int j1 = this.getRowTop(i1);
            int k1 = this.getRowBottom(i1);
            if (k1 >= this.y0 && j1 <= this.y1) {
                this.renderItem(pGuiGraphics, pMouseX, pMouseY, pPartialTick, i1, i, j1, j, k);
            }
        }

    }

    protected void renderItem(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick, int pIndex, int pLeft, int pTop, int pWidth, int pHeight) {
        E e = this.getEntry(pIndex);
        e.renderBack(pGuiGraphics, pIndex, pTop, pLeft, pWidth, pHeight, pMouseX, pMouseY, Objects.equals(this.hovered, e), pPartialTick);
        if (this.isSelectedItem(pIndex)) {
            int i = this.isFocused() ? -1 : -8355712;
            this.renderSelection(pGuiGraphics, pTop, pWidth, pHeight, i, -16777216);
        }

        e.render(pGuiGraphics, pIndex, pTop, pLeft, pWidth, pHeight, pMouseX, pMouseY, Objects.equals(this.hovered, e), pPartialTick);
    }

    protected void renderSelection(GuiGraphics pGuiGraphics, int pTop, int pWidth, int pHeight, int pOuterColor, int pInnerColor) {
        int i = this.x0 + (this.width - pWidth) / 2;
        int j = this.x0 + (this.width + pWidth) / 2;
        pGuiGraphics.fill(i, pTop - 2, j, pTop + pHeight + 2, pOuterColor);
        pGuiGraphics.fill(i + 1, pTop - 1, j - 1, pTop + pHeight + 1, pInnerColor);
    }

    public int getRowLeft() {
        return this.x0 + this.width / 2 - this.getRowWidth() / 2 + 2;
    }

    public int getRowRight() {
        return this.getRowLeft() + this.getRowWidth();
    }

    protected int getRowTop(int pIndex) {
        return this.y0 + 4 - (int)this.getScrollAmount() + pIndex * this.itemHeight + this.headerHeight;
    }

    protected int getRowBottom(int pIndex) {
        return this.getRowTop(pIndex) + this.itemHeight;
    }

    public NarrationPriority narrationPriority() {
        if (this.isFocused()) {
            return NarrationPriority.FOCUSED;
        } else {
            return this.hovered != null ? NarrationPriority.HOVERED : NarrationPriority.NONE;
        }
    }

    @Nullable
    protected E remove(int pIndex) {
        E e = this.children.get(pIndex);
        return this.removeEntry(this.children.get(pIndex)) ? e : null;
    }

    protected boolean removeEntry(E pEntry) {
        boolean flag = this.children.remove(pEntry);
        if (flag && pEntry == this.getSelected()) {
            this.setSelected(null);
        }

        return flag;
    }

    @Nullable
    protected E getHovered() {
        return this.hovered;
    }

    void bindEntryToSelf(Entry<E> pEntry) {
        pEntry.list = this;
    }

    protected void narrateListElementPosition(NarrationElementOutput pNarrationElementOutput, E pEntry) {
        List<E> list = this.children();
        if (list.size() > 1) {
            int i = list.indexOf(pEntry);
            if (i != -1) {
                pNarrationElementOutput.add(NarratedElementType.POSITION, Component.translatable("narrator.position.list", i + 1, list.size()));
            }
        }

    }

    public ScreenRectangle getRectangle() {
        return new ScreenRectangle(this.x0, this.y0, this.x1 - this.x0, this.y1 - this.y0);
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public int getTop() {
        return this.y0;
    }

    public int getBottom() {
        return this.y1;
    }

    public int getLeft() {
        return this.x0;
    }

    public int getRight() {
        return this.x1;
    }

    protected abstract static class Entry<E extends Entry<E>> implements GuiEventListener, ContainerEventHandler {
        @Nullable
        private GuiEventListener focused;
        private boolean dragging;

        /** @deprecated */
        @Deprecated
        protected ScrollableList<E> list;

        protected Entry() {
        }

        public boolean isDragging() {
            return this.dragging;
        }

        public void setDragging(boolean pDragging) {
            this.dragging = pDragging;
        }


        public void setFocused(@Nullable GuiEventListener pListener) {
            if (this.focused != null) {
                this.focused.setFocused(false);
            }

            if (pListener != null) {
                pListener.setFocused(true);
            }

            this.focused = pListener;
        }

        @Nullable
        public GuiEventListener getFocused() {
            return this.focused;
        }

        public boolean isFocused() {
            return this.list.getFocused() == this;
        }

        public abstract void render(GuiGraphics var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, boolean var9, float var10);

        public void renderBack(GuiGraphics pGuiGraphics, int pIndex, int pTop, int pLeft, int pWidth, int pHeight, int pMouseX, int pMouseY, boolean pIsMouseOver, float pPartialTick) {
        }

        public boolean isMouseOver(double pMouseX, double pMouseY) {
            return Objects.equals(this.list.getEntryAtPosition(pMouseX, pMouseY), this);
        }
    }

    class TrackedList extends AbstractList<E> {
        private final List<E> delegate = Lists.newArrayList();

        TrackedList() {
        }

        public E get(int pIndex) {
            return this.delegate.get(pIndex);
        }

        public int size() {
            return this.delegate.size();
        }

        public E set(int pIndex, E pEntry) {
            E e = this.delegate.set(pIndex, pEntry);
            ScrollableList.this.bindEntryToSelf(pEntry);
            return e;
        }

        public void add(int pIndex, E pEntry) {
            this.delegate.add(pIndex, pEntry);
            ScrollableList.this.bindEntryToSelf(pEntry);
        }

        public E remove(int pIndex) {
            return this.delegate.remove(pIndex);
        }
    }
}