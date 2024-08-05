package app.bishan.superflatplus.mixins;

import app.bishan.superflatplus.SuperflatPlus;
import com.google.common.collect.UnmodifiableIterator;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(FluidBlock.class)
public class FluidBlockMixin {

    @Inject(
            method = "receiveNeighborFluids",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/fluid/FluidState;isStill()Z"
            ),
            locals = LocalCapture.CAPTURE_FAILHARD,
            cancellable = true
    )
    @SuppressWarnings("rawtypes")
    private void receiveNeighborFluids(
            World world,
            BlockPos pos,
            BlockState state,
            CallbackInfoReturnable<Boolean> cir,
            boolean bl,
            UnmodifiableIterator unused,
            Direction direction,
            BlockPos blockPos
    ) {

        // check if superflat world
        if (!SuperflatPlus.isSuperflat(world)) return;

        // has to be flowing water
        if (!world.getFluidState(pos).isStill()) return;

        // below y0
        if (pos.getY() >= 0) return;

        world.setBlockState(pos, Blocks.COBBLED_DEEPSLATE.getDefaultState());
        cir.setReturnValue(false);
        cir.cancel();
    }
}
