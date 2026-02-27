package com.jdolphin.ricksportalgun.common.customization.type;

import com.jdolphin.ricksportalgun.common.entity.PortalEntity;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class DefaultPortalType extends TypeWithoutShape {

    public DefaultPortalType(ResourceLocation id) {
        super(id);
    }

    @Override
    public void tick(PortalEntity portal) {
        super.tick(portal);

        Level level = portal.level();
        if (level.isClientSide) {
            Vec3 pos = portal.position();
            RandomSource random = level.random;
            int color = portal.getColor();
            float red = FastColor.ARGB32.red(color) / 255f;
            float green = FastColor.ARGB32.green(color) / 255f;
            float blue = FastColor.ARGB32.blue(color) / 255f;

                double xOffset = pos.x - 0.4 + random.nextDouble();
                double yOffset = pos.y - 0.8 + random.nextDouble();
                double zOffset = pos.z - 0.4 + random.nextDouble();
                level.addParticle(new DustParticleOptions(new Vector3f(red, green, blue), 1), xOffset, yOffset, zOffset, 0, 2, 0);

        }
    }
}
