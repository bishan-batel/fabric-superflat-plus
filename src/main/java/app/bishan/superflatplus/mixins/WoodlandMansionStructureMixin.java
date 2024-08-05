package app.bishan.superflatplus.mixins;

import app.bishan.superflatplus.SuperflatPlus;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.structure.Structure;
import net.minecraft.world.gen.structure.WoodlandMansionStructure;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(WoodlandMansionStructure.class)
public abstract class WoodlandMansionStructureMixin extends Structure {
    @Unique
    private static final int MINIMUM_Y_POS = 60;

    @ModifyConstant(method = "getStructurePosition", constant = @Constant(intValue = 60))
    private int injected(@SuppressWarnings("unused") int _value, @Local(argsOnly = true) Structure.Context ctx) {
        return Integer.MIN_VALUE;
    }

    @Redirect(
            method = "getStructurePosition",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/gen/structure/WoodlandMansionStructure;getShiftedPos(Lnet/minecraft/world/gen/structure/Structure$Context;Lnet/minecraft/util/BlockRotation;)Lnet/minecraft/util/math/BlockPos;"
            )
    )
    private BlockPos getStructurePosition(WoodlandMansionStructure instance, Context context, BlockRotation blockRotation) {
        // use of deprecated method bc Mojang does it bruh
        @SuppressWarnings("deprecation") var pos = getShiftedPos(context, blockRotation);

        // Snap position to y60 to pass structure placement check,
        // This shouldn't have many consequences in normal worlds because
        // roofed forest biomes in minecraft never go down to y4, but this
        // is still a solution I'm comfortable with (TODO find something a bit less jank)
        return pos.getY() == SuperflatPlus.STRUCTURE_MIN_CORNER_HEIGHT ? pos.withY(MINIMUM_Y_POS) : pos;
    }

    protected WoodlandMansionStructureMixin(Config config) {
        super(config);
    }
}
