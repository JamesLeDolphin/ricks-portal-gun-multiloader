package com.jdolphin.ricksportalgun.client.screen;

import com.jdolphin.ricksportalgun.client.screen.widget.PGTextButton;
import com.jdolphin.ricksportalgun.client.screen.widget.SuggestionTextFieldWidget;
import com.jdolphin.ricksportalgun.common.init.PGMeeseeksCommands;
import com.jdolphin.ricksportalgun.common.meeseeks.argument.PlayerArgument;
import com.jdolphin.ricksportalgun.common.meeseeks.base.AbstractMeeseeksArgument;
import com.jdolphin.ricksportalgun.common.meeseeks.base.AbstractMeeseeksCommand;
import com.jdolphin.ricksportalgun.common.packet.serverbound.SBRunMeeseeksCommandPacket;
import com.jdolphin.ricksportalgun.common.util.helper.GuiHelper;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;

import java.awt.*;
import java.util.*;
import java.util.List;

public class MeeseeksCommandScreen extends AbstractBaseScreen {
    private List<AbstractMeeseeksCommand> commands = PGMeeseeksCommands.COMMANDS;
    private final List<PGTextButton> buttons = new ArrayList<>();
    private final List<AbstractMeeseeksCommand> selected = new ArrayList<>();
    private Button backspace, doneButton;
    private SuggestionTextFieldWidget input;
    private final List<Runnable> tickables = new ArrayList<>();
    private final UUID uuid;

    public MeeseeksCommandScreen(UUID uuid) {
        super(Component.translatable("menu.ricksportalgun.meeseeks"));
        this.uuid = uuid;
    }

    public void init() {
        super.init();

        reloadCommandButtons();

        this.input = this.addWidget(new SuggestionTextFieldWidget(this.width / 2 - 64, this.height / 2 - 48, 128, 20, Component.empty(), List.of()));
        input.setResponder(string -> input.update());
        this.addWidget(input.getSuggestionList());

        this.backspace = this.addRenderableWidget(Button.builder(Component.literal("Backspace"), button -> {
            int index = selected.size() - 1;
            AbstractMeeseeksCommand cmd = getLastSelected();
            Optional<AbstractMeeseeksCommand> optional = cmd.getParent();
            optional.ifPresentOrElse(command -> commands = command.getChildren(),
                    () -> commands = PGMeeseeksCommands.COMMANDS);

            int j = Math.max(0, index - 1);
            if (j >= selected.size() - 2) {
                AbstractMeeseeksCommand command = selected.get(j);
                if (command instanceof AbstractMeeseeksArgument<?> arg) {
                    input.setValue(arg.getValue());
                    input.setSuggestions(arg.values());
                    if (arg instanceof PlayerArgument) {
                        input.setSuggestions(minecraft.getConnection().getListedOnlinePlayers().stream().map(playerInfo -> playerInfo.getTabListDisplayName().getString()).toList());
                    }
                } else input.setSuggestions(List.of());
            }

            selected.remove(index);

            reloadCommandButtons();

        }).bounds(this.width / 2 + 132, this.height / 2 - 64, 40,20).build());

        this.doneButton = this.addWidget(Button.builder(Component.literal("Done"), button -> {
            if (isLastSelectedAnArgument()) {
                AbstractMeeseeksArgument<?> arg = (AbstractMeeseeksArgument<?>) getLastSelected();
                int index = selected.indexOf(arg);
                selected.remove(index);
                arg.setValue(input.getValue());
                selected.add(index, arg);
            }

            SBRunMeeseeksCommandPacket packet = new SBRunMeeseeksCommandPacket(uuid, getSentenceAsString());
            PGHelper.sendPacketToServer(packet);

            this.onClose();
        }).bounds(this.width / 2 - 64, this.height / 2 - 24, 128, 20).build());

        if (selected.isEmpty()) {
            backspace.active = backspace.visible = false;
            doneButton.active = doneButton.visible = false;
            input.active = input.visible = false;
        }
    }

    private void reloadCommandButtons() {
        buttons.forEach(this::removeWidget);
        buttons.clear();

        int x = this.width / 2 - 196;
        int y = this.height / 2;

        int i = 0;
        for (AbstractMeeseeksCommand cmd : commands) {
            int offsetX = x + (i % 3) * 132;
            int offsetY = y + (i / 3) * 24;

            if (cmd instanceof AbstractMeeseeksArgument<?> argumentCmd) {
                argumentCmd.setValue(null);
            }

            PGTextButton btn = this.addWidget(new PGTextButton(offsetX, offsetY, 128, 20, Component.literal(cmd.getName()), button -> {
                if (isLastSelectedAnArgument()) {
                    AbstractMeeseeksArgument<?> arg = (AbstractMeeseeksArgument<?>) getLastSelected();
                    int index = selected.indexOf(arg);
                    selected.remove(index);
                    arg.setValue(input.getValue());
                    selected.add(index, arg);
                }

                this.commands = cmd.getChildren();
                this.selected.add(cmd);

                input.setValue("");
                reloadCommandButtons();
            }, this.font));

            tickables.add(() -> {
                if (isLastSelectedAnArgument()) {
                    boolean canProceed = !this.input.getValue().isEmpty();
                    btn.active = canProceed;
                    btn.setTextColour(canProceed ? Color.WHITE.getRGB() : Color.GRAY.getRGB());

                    if (input.visible) {
                        AbstractMeeseeksArgument<?> argument = (AbstractMeeseeksArgument<?>) getLastSelected();
                        String txt = input.getValue();
                        var fromString = argument.fromString(txt);
                        btn.active = fromString != null;
                        doneButton.active = fromString != null;
                        if (fromString == null || input.getValue().isEmpty()) {
                            input.setTextColor(Color.RED.getRGB());
                        } else input.setTextColor(Color.WHITE.getRGB());
                    }
                }
            });
            buttons.add(btn);
            i++;
        }
    }

    @Override
    public void tick() {
        super.tick();

        boolean somethingSelected = !selected.isEmpty();
        backspace.active = somethingSelected;
        backspace.visible = somethingSelected;

        if (somethingSelected) {
            AbstractMeeseeksCommand cmd = getLastSelected();
            this.input.active = isLastSelectedAnArgument();
            this.input.visible = isLastSelectedAnArgument();

            if (input.visible) {
                AbstractMeeseeksArgument<?> arg = (AbstractMeeseeksArgument<?>) cmd;
                if (!(arg instanceof PlayerArgument)) {
                    if (arg.values().isEmpty()) {
                        input.getSuggestionList().visible = false;
                    }
                boolean sameList = !(new HashSet<>(input.getSuggestions()).containsAll(arg.values()));
                if (!arg.values().isEmpty() && sameList) {
                    input.setSuggestions(arg.values());
                    input.getSuggestionList().visible = true;
                }
            } else {
                    if (arg.values().isEmpty()) {
                        List<String> players = minecraft.getConnection().getListedOnlinePlayers().stream().map(playerInfo -> playerInfo.getProfile().getName()).toList();
                        input.setSuggestions(players);
                    }
                }
        }
            boolean isFinalArg = cmd.completesCommand();
            this.doneButton.visible = isFinalArg;
            this.doneButton.active = isFinalArg;
        }

        tickables.forEach(Runnable::run);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(graphics);

        buttons.forEach(textButton -> {
            textButton.render(graphics, mouseX, mouseY, partialTick);
            GuiHelper.renderOutline(graphics, textButton, textButton.getTextColour());
        });
        String sentence = getSentenceAsString().replaceAll("\\|", " ");
        GuiHelper.drawWordWrap(graphics, this.font, Component.literal(sentence), this.width / 2, this.height / 2 - 96, 256, Color.WHITE.getRGB());

        if (input.visible) {
            input.renderWidget(graphics, mouseX, mouseY, partialTick);
        }
        if (!input.getSuggestionList().visible) doneButton.render(graphics, mouseX, mouseY, partialTick);

        Style style = GuiHelper.getStyle(mouseX, mouseY);
        if (style != null && style.getHoverEvent() != null) {
            this.renderWithTooltip(graphics, mouseX, mouseY, partialTick);
        }
        super.render(graphics, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        Optional<GuiEventListener> optional = this.getChildAt(mouseX, mouseY);
        if (optional.isEmpty()) {
            input.setFocused(false);
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    private AbstractMeeseeksCommand getLastSelected() {
        if (!selected.isEmpty()) return selected.get(selected.size() - 1);
        else return null;
    }

    private String getSentenceAsString() {
        StringBuilder sb = new StringBuilder();
        for (AbstractMeeseeksCommand cmd : selected) {
            String s = cmd.getName();
            if (cmd instanceof AbstractMeeseeksArgument<?> arg) {
                if (arg.getValue() == null || arg.getValue().isEmpty()) {
                    s = cmd.getName();
                } else s = arg.getValue();
            }
            sb.append(s).append("|");
        }
        return sb.toString();
    }

    private boolean isLastSelectedAnArgument() {
        AbstractMeeseeksCommand cmd = getLastSelected();
        return cmd instanceof AbstractMeeseeksArgument<?>;
    }
}
