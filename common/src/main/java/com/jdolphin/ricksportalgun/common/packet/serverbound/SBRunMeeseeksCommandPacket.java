package com.jdolphin.ricksportalgun.common.packet.serverbound;

import com.jdolphin.ricksportalgun.common.entity.MeeseeksEntity;
import com.jdolphin.ricksportalgun.common.init.PGMeeseeksCommands;
import com.jdolphin.ricksportalgun.common.meeseeks.base.AbstractMeeseeksCommand;
import com.jdolphin.ricksportalgun.common.util.helper.PGHelper;
import com.jdolphin.ricksportalgun.common.util.network.PGServerPayload;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

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

                        int depth = command.getChildDepth();
                        System.out.printf("%s, %s, %s%n", command.getName(), depth, args.size());

                        for (AbstractMeeseeksCommand child : command.getChildren()) {

                            if (depth == args.size()) {
                                for (int i = 0; i < depth; i++) {

                                }
                                if (child.getName().equals(chopped.get(1))) {
                                    if (child.completesCommand()) {
                                        child.getResult().ifPresent(result -> result.runCommand(meeseeks, player));
                                    }
                                }
                            }
                        }
                    }

            } else {
                System.out.println("Smth went wrong");
            }
        });
    }

    private int visit(AbstractMeeseeksCommand command, int i) {

            for (AbstractMeeseeksCommand cmd : command.getChildren()) {
                if (!command.getChildren().isEmpty()) {
                System.out.println(cmd.getName() + " " + i);
                visit(cmd, i++);
            }
        }
        return i;
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
