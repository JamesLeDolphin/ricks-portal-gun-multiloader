package com.jdolphin.ricksportalgun.common.packet.serverbound;

import com.jdolphin.ricksportalgun.common.entity.MeeseeksEntity;
import com.jdolphin.ricksportalgun.common.init.PGMeeseeksCommands;
import com.jdolphin.ricksportalgun.common.meeseeks.base.AbstractMeeseeksArgument;
import com.jdolphin.ricksportalgun.common.meeseeks.base.AbstractMeeseeksCommand;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import com.jdolphin.ricksportalgun.common.util.network.PGServerPayload;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public record SBRunMeeseeksCommandPacket(UUID uuid, String cmd) implements PGServerPayload {

    public void handle(ServerPlayer player) {
        MinecraftServer server = player.server;
        ServerLevel level = player.serverLevel();
        server.executeIfPossible(() -> {
            Entity entity = level.getEntity(uuid);
            if (entity instanceof MeeseeksEntity meeseeks) {
                List<String> chopped = List.of(cmd.split("\\|"));
                String root = chopped.get(0);

                Optional<AbstractMeeseeksCommand> optional = PGMeeseeksCommands.COMMANDS.stream()
                        .filter(command ->
                                command.getName().equals(root)).findFirst();

                if (optional.isPresent()) {
                    AbstractMeeseeksCommand command = optional.get();
                    List<String> args = chopped.subList(1, chopped.size());

                    for (AbstractMeeseeksCommand child : command.getChildren()) {
                        runCommand(child, args, 0, meeseeks, player);
                    }
                }
            }
        });
    }

    private boolean runCommand(AbstractMeeseeksCommand command, List<String> args, int index, MeeseeksEntity meeseeks, Player player) {
        if (index == args.size() - 1) {
            if (command.completesCommand()) {
                String arg = args.get(index);
                boolean valid;
                if (command instanceof AbstractMeeseeksArgument<?> abstractArg) {
                    Object parsed = abstractArg.fromString(arg);
                    valid = abstractArg.getArgumentClass().isInstance(parsed);
                } else {
                    valid = command.getName().equals(arg);
                }
                if (valid) {
                    command.getResult()
                            .ifPresentOrElse(
                                    r -> r.runCommand(meeseeks, player),
                                    () -> meeseeks.sayToPlayer(player, "A")
                            );
                    return true;
                }
                return false;
            }
        }
        if (index + 1 != args.size()) {
            String nextArg = args.get(index + 1);

            for (AbstractMeeseeksCommand child : command.getChildren()) {
                if (child instanceof AbstractMeeseeksArgument<?> argumentCommand) {
                    Object parsed = argumentCommand.fromString(nextArg);
                    if (argumentCommand.getArgumentClass().isInstance(parsed)) {
                        if (runCommand(child, args, index + 1, meeseeks, player)) {
                            return true;
                        }
                    }
                } else if (child.getName().equals(nextArg)) {
                    if (runCommand(child, args, index + 1, meeseeks, player)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static SBRunMeeseeksCommandPacket decode(FriendlyByteBuf buf) {
        return new SBRunMeeseeksCommandPacket(buf.readUUID(), buf.readUtf());
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeUUID(uuid());
        buf.writeUtf(cmd());
    }

    @Override
    public ResourceLocation getId() {
        return getID();
    }

    public static ResourceLocation getID() {
        return PGHelper.id("meeseeks_execute");
    }

}
