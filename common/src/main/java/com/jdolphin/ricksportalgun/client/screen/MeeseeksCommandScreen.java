package com.jdolphin.ricksportalgun.client.screen;

import com.jdolphin.ricksportalgun.client.screen.widget.PGTextButton;
import com.jdolphin.ricksportalgun.common.init.PGMeeseeksCommands;
import com.jdolphin.ricksportalgun.common.meeseeks.base.AbstractArgumentCommand;
import com.jdolphin.ricksportalgun.common.meeseeks.base.AbstractMeeseeksCommand;
import com.jdolphin.ricksportalgun.common.packet.serverbound.SBRunMeeseeksCommandPacket;
import com.jdolphin.ricksportalgun.common.util.helper.GuiHelper;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.FittingMultiLineTextWidget;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class MeeseeksCommandScreen extends AbstractBaseScreen {
    private List<AbstractMeeseeksCommand> commands = PGMeeseeksCommands.COMMANDS;
    private final List<PGTextButton> buttons = new ArrayList<>();
    private final List<AbstractMeeseeksCommand> selected = new ArrayList<>();
    private Button backspace, doneButton;
    private FittingMultiLineTextWidget sentenceWidget;
    private EditBox input;
    private final List<Runnable> tickables = new ArrayList<>();
    private final UUID uuid;

    public MeeseeksCommandScreen(UUID uuid) {
        super(Component.translatable("menu.ricksportalgun.meeseeks"));
        this.uuid = uuid;
    }

    public void init() {
        super.init();

        reloadSentenceWidget();
        reloadCommandButtons();

        this.backspace = this.addRenderableWidget(Button.builder(Component.literal("Backspace"), button -> {
            int index = selected.size() - 1;
            AbstractMeeseeksCommand cmd = getLastSelected();
            Optional<AbstractMeeseeksCommand> optional = cmd.getParent();
            optional.ifPresentOrElse(command -> commands = command.getChildren(),
                    () -> commands = PGMeeseeksCommands.COMMANDS);

            selected.remove(index);

            reloadCommandButtons();
            reloadSentenceWidget();

        }).bounds(this.width / 2 + 132, this.height / 2 - 64, 40,20).build());

        this.doneButton = this.addRenderableWidget(Button.builder(Component.literal("Done"), button -> {
            if (isLastSelectedAnArgument()) {
                AbstractArgumentCommand<?> arg = (AbstractArgumentCommand<?>) getLastSelected();
                int index = selected.indexOf(arg);
                selected.remove(index);
                arg.setValue(input.getValue());
                selected.add(index, arg);
            }

            SBRunMeeseeksCommandPacket packet = new SBRunMeeseeksCommandPacket(uuid, getSentenceAsString());
            PGHelper.sendPacketToServer(packet);

            this.onClose();
        }).bounds(this.width / 2 - 64, this.height / 2 - 24, 128, 20).build());

        this.input = this.addRenderableWidget(new EditBox(this.font, this.width / 2 - 64, this.height / 2 - 48, 128, 20, Component.empty()));

        if (selected.isEmpty()) {
            backspace.active = backspace.visible = false;
            doneButton.active = doneButton.visible = false;
            input.active = input.visible = false;
        }
    }

    private void reloadSentenceWidget() {
        this.removeWidget(sentenceWidget);

        String sentence = getSentenceAsString().replaceAll("\\|", " ");
        sentenceWidget = this.addRenderableWidget(new FittingMultiLineTextWidget(this.width / 2 - 128, this.height / 2 - 110, 256, 44, Component.literal(sentence), this.font));
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

            if (cmd instanceof AbstractArgumentCommand<?> argumentCmd) {
                argumentCmd.setValue(null);
            }

            PGTextButton btn = this.addWidget(new PGTextButton(offsetX, offsetY, 128, 20, Component.literal(cmd.getName()), button -> {
                if (isLastSelectedAnArgument()) {
                    AbstractArgumentCommand<?> arg = (AbstractArgumentCommand<?>) getLastSelected();
                    int index = selected.indexOf(arg);
                    selected.remove(index);
                    arg.setValue(input.getValue());
                    selected.add(index, arg);
                }

                this.commands = cmd.getChildren();
                this.selected.add(cmd);

                input.setValue("");
                reloadCommandButtons();
                reloadSentenceWidget();
            }, this.font));

            tickables.add(() -> {
                if (isLastSelectedAnArgument()) {
                    btn.active = !this.input.getValue().isEmpty();

                    if (input.active && !input.getValue().isEmpty()) {
                        AbstractArgumentCommand<?> argument = (AbstractArgumentCommand<?>) getLastSelected();
                        String txt = input.getValue();
                        var fromString = argument.fromString(txt);
                        btn.active = fromString != null;
                        doneButton.active = fromString != null;
                        if (fromString == null) {
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

            boolean isFinalArg = cmd.completesCommand();
            this.doneButton.visible = isFinalArg;
            this.doneButton.active = isFinalArg;
        }

        tickables.forEach(Runnable::run);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(graphics);

       // if (doneButton.active) doneButton.render(graphics, mouseX, mouseY, partialTick);
       // if (input.active) input.render(graphics, mouseX, mouseY, partialTick);

        buttons.forEach(textButton -> {
            textButton.render(graphics, mouseX, mouseY, partialTick);
            GuiHelper.renderOutline(graphics, textButton, Color.WHITE.getRGB());
        });

        Style style = GuiHelper.getStyle(mouseX, mouseY);
        if (style != null && style.getHoverEvent() != null) {
            this.renderWithTooltip(graphics, mouseX, mouseY, partialTick);
        }
        super.render(graphics, mouseX, mouseY, partialTick);
    }

    private AbstractMeeseeksCommand getLastSelected() {
        if (!selected.isEmpty()) return selected.get(selected.size() - 1);
        else return null;
    }

    private String getSentenceAsString() {
        StringBuilder sb = new StringBuilder();
        for (AbstractMeeseeksCommand cmd : selected) {
            String s = cmd.getName();
            if (cmd instanceof AbstractArgumentCommand<?> arg) {
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
        return cmd instanceof AbstractArgumentCommand<?>;
    }
}
