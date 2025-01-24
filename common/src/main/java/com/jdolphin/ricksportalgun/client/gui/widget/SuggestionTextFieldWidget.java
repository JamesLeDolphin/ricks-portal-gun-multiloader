package com.jdolphin.ricksportalgun.client.gui.widget;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.jdolphin.ricksportalgun.client.gui.AbstractBaseScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSelectionList;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.PlainTextButton;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.apache.logging.log4j.LogManager;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SuggestionTextFieldWidget extends EditBox {
    private final List<String> suggestions;
    private final SuggestionList suggestionListWidget;

    public SuggestionTextFieldWidget(AbstractBaseScreen screen, int x, int y, int width, int height, MutableComponent text, List<String> suggestions) {
        super(screen.getFont(), x, y, width, height, text);
        this.suggestions = suggestions;
        this.suggestionListWidget = screen.addRenderableWidget(new SuggestionList(Minecraft.getInstance(),
                screen.width / 2 + x / 2 + width * 2 - 24, y / 2 - 6, x, height + y, 14, this));
    }

    public List<String> getSuggestions() {
        return suggestions;
    }

    public void renderWidget(GuiGraphics context, int mouseX, int mouseY, float delta) {
        if (this.isActive()) {
            this.suggestionListWidget.active = true;
            this.suggestionListWidget.render(context, mouseX, mouseY, delta);
        } else {
            this.suggestionListWidget.active = false;
        }
        super.renderWidget(context, mouseX, mouseY, delta);
    }

    public void update() {
        List<String> suggestions = this.sortSuggestions(this.suggestions);
        this.suggestionListWidget.setSuggestions(suggestions);
        this.suggestionListWidget.setScrollAmount(0);
    }

    private List<String> sortSuggestions(List<String> suggestions) {
        String string = this.getValue().substring(0, this.getCursorPosition());
        int i = getStartOfCurrentWord(string);
        String string2 = string.substring(i).toLowerCase(Locale.ROOT);
        List<String> list = Lists.newArrayList();
        List<String> list2 = Lists.newArrayList();

        for(String suggestion : suggestions) {
            String s = suggestion.substring(suggestion.indexOf(":") + 1);
            if (s.startsWith(string2)) {
                list.add(suggestion);
            }
            if (!suggestion.startsWith(string2)) {
                list2.add(suggestion);
            } else if (!list.contains(suggestion)) {
                list.add(suggestion);
            }
        }

        list.addAll(list2);
        return list;
    }

    private static int getStartOfCurrentWord(String input) {
        if (Strings.isNullOrEmpty(input)) {
            return 0;
        } else {
            int i = 0;

            for(Matcher matcher = Pattern.compile("(\\s+)").matcher(input); matcher.find(); i = matcher.end()) {}

            return i;
        }
    }


    public static class SuggestionList extends ScrollableList<SuggestionList.SuggestionEntry> {
        private final SuggestionTextFieldWidget widget;

        public SuggestionList(Minecraft minecraft, int width, int height, int x, int y, int itemHeight, SuggestionTextFieldWidget widget) {
            super(minecraft, width, height, x, y, itemHeight);
            this.widget = widget;

            List<String> suggestions = widget.suggestions;
            setSuggestions(suggestions);
        }

        public void setSuggestions(List<String> suggestions) {
            this.clearEntries();

            for (String s : suggestions) {
                if (s != null && !s.isEmpty()) {
                    SuggestionEntry entry = new SuggestionEntry(s, this);
                    if (!this.children().contains(entry)) {
                        this.addEntry(entry);
                    }
                } else
                    LogManager.getLogger().warn("Failed to get suggestion: {}", s);
            }
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            SuggestionEntry hoveredEntry = this.isMouseOver(mouseX, mouseY) ? this.getEntryAtPosition(mouseX, mouseY) : null;
            if (hoveredEntry == null) {
                this.widget.setFocused(false);
            }
            return super.mouseClicked(mouseX, mouseY, button);
        }

        @Override
        protected void updateWidgetNarration(NarrationElementOutput output) {
            output.add(NarratedElementType.USAGE, Component.translatable("narration.selection.usage"));
        }

        public void renderWidget(GuiGraphics context, int mouseX, int mouseY, float delta) {
            Screen screen = Minecraft.getInstance().screen;
            int x1 = this.widget.getX();
            int y1 = this.widget.getY() + this.widget.height;
            int x2 = this.widget.width;
            int y2 = this.height + 2;
            context.enableScissor(x1, y1, x1 + x2, y1 + y2);
            context.fill(0, 0, screen.width, screen.height, -805306368);
            context.disableScissor();
            context.renderOutline(x1, y1, x2, y2, Color.WHITE.getRGB());
            super.renderWidget(context, mouseX, mouseY, delta);
        }

        protected void renderHeader(GuiGraphics context, int x, int y) {}

        protected void renderDecorations(GuiGraphics context, int mouseX, int mouseY) {}

        protected void drawHeaderAndFooterSeparators(GuiGraphics context) {}

        protected void drawMenuListBackground(GuiGraphics context) {}

        public int getRowWidth() {
            return super.getRowWidth() - 136;
        }

        public static class SuggestionEntry extends ScrollableList.Entry<SuggestionEntry> {

            private final String string;
            private final Button button;
            protected SuggestionList list;
            private final SuggestionTextFieldWidget widget;

            SuggestionEntry(String suggestion, SuggestionList list) {
                this.string = suggestion;
                this.list = list;
                widget = list.widget;

                this.button =  new PlainTextButton(0, 0, widget.width, list.itemHeight, Component.literal(suggestion),
                        (pButton -> {
                            widget.setValue(suggestion);
                            widget.setFocused(false);
                        }), Minecraft.getInstance().font);
            }

            @Override
            public void render(@NotNull GuiGraphics context, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean hovered, float tickDelta) {
                SuggestionList wpList = this.list;
                if (top > wpList.headerHeight ) {
                    this.button.setX(wpList.width / 2 - 64);
                    this.button.setY(top);
                    this.button.setMessage(Component.literal(this.string));
                    button.render(context, mouseX, mouseY, tickDelta);
                }
            }

            public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
                return this.button.mouseClicked(pMouseX, pMouseY, pButton);
            }

            public boolean mouseReleased(double pMouseX, double pMouseY, int pButton) {
                return this.button.mouseReleased(pMouseX, pMouseY, pButton);
            }
        }
    }
}