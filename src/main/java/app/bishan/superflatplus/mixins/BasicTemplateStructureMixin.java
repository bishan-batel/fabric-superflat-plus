package app.bishan.superflatplus.mixins;

import net.minecraft.structure.TrailRuinsGenerator;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.structure.BasicTempleStructure;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BasicTempleStructure.class)
public class BasicTemplateStructureMixin {
    @Redirect(
            method = "getStructurePosition",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/gen/chunk/ChunkGenerator;getSeaLevel()I"
            ))
    int huh(ChunkGenerator instance) {
        return Integer.MIN_VALUE;
    }
}
