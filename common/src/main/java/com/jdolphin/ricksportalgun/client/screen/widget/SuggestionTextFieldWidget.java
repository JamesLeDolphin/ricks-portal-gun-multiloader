package com.jdolphin.ricksportalgun.client.screen.widget;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
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
    private List<String> suggestions;
    private final SuggestionList suggestionListWidget;

    public SuggestionTextFieldWidget(int x, int y, int width, int height, MutableComponent text, List<String> suggestions) {
        super(Minecraft.getInstance().font, x, y, width, height, text);
        this.suggestions = suggestions;
        int i = Math.min(suggestions.size(), 3);
        this.suggestionListWidget = new SuggestionList(Minecraft.getInstance(),
                width, height * i, x, y + height, 14, this);
    }

    public SuggestionList getSuggestionList() {
        return this.suggestionListWidget;
    }

    public List<String> getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(List<String> suggestions) {
        this.suggestions = suggestions;
        this.update();
    }

    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        if (!this.isFocused()) {
            this.suggestionListWidget.visible = false;
            this.suggestionListWidget.active = false;
        } else {
            this.suggestionListWidget.visible = true;
            this.suggestionListWidget.active = true;
            this.suggestionListWidget.render(graphics, mouseX, mouseY, delta);
        }
        super.renderWidget(graphics, mouseX, mouseY, delta);
    }

    public void update() {
        List<String> suggestions = this.sortSuggestions(this.suggestions);
        this.suggestionListWidget.setSuggestions(suggestions);
        this.suggestionListWidget.setScrollAmount(0);
        this.suggestionListWidget.setHeight(this.height * Math.min(3, suggestions.size()));
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

            for (Matcher matcher = Pattern.compile("(\\s+)").matcher(input); matcher.find(); i = matcher.end()) {}

            return i;
        }
    }


    public static class SuggestionList extends PGScrollableWidget<SuggestionList.SuggestionEntry> {
        private final SuggestionTextFieldWidget widget;
        private int borderColor = Color.WHITE.getRGB();
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
            if (this.active && this.visible) {
                SuggestionEntry hoveredEntry = this.isMouseOver(mouseX, mouseY) ? this.getEntryAtPosition(mouseX, mouseY) : null;
                if (hoveredEntry == null) {
                    this.widget.setFocused(false);
                }
                return super.mouseClicked(mouseX, mouseY, button);
            } return false;
        }

        @Override
        protected void updateWidgetNarration(@NotNull NarrationElementOutput output) {
            output.add(NarratedElementType.USAGE, Component.translatable("narration.selection.usage"));
        }

        public void setBorderColor(int color) {
            this.borderColor = color;
        }

        public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
            int x1 = this.getX();
            int y1 = this.getY();
            int x2 = this.getRight();
            int y2 = this.getBottom();
            graphics.enableScissor(x1, y1, x2, y2);
            graphics.fill(x1, y1, x2, y2, -805306368);
            graphics.disableScissor();
            graphics.renderOutline(x1, y1, this.getWidth(), this.getHeight(), borderColor);
            super.renderWidget(graphics, mouseX, mouseY, delta);
        }

        public int getRowWidth() {
            return this.width - 10;
        }

        @Override
        public int getRowRight() {
            return super.getRowRight() - 6;
        }

        public static class SuggestionEntry extends PGScrollableWidget.Entry<SuggestionEntry> {

            private final String string;
            private final PGTextButton button;
            protected SuggestionList list;
            private final SuggestionTextFieldWidget widget;

            SuggestionEntry(String suggestion, SuggestionList list) {
                this.string = suggestion;
                this.list = list;
                widget = list.widget;

                this.button =  new PGTextButton(0, 0, this.list.width - 6, list.itemHeight, Component.literal(suggestion),
                        (pButton -> {
                            widget.setValue(suggestion);
                            widget.setFocused(false);
                        }), Minecraft.getInstance().font);
            }

            @Override
            public void render(@NotNull GuiGraphics context, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean hovered, float tickDelta) {
                SuggestionList wpList = this.list;
                if (top > wpList.headerHeight ) {
                    this.button.setX(wpList.getX() + 2);
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