package app.bishan.superflatplus.mixins;

import app.bishan.superflatplus.SuperflatPlus;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.LavaFluid;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LavaFluid.class)
public class LavaFluidMixin {

	@Redirect(method = "flow", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/WorldAccess;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
	boolean flow$setBlockState(WorldAccess world, BlockPos pos, BlockState state, int fluidState) {
		Block block = Blocks.STONE;

		var biome = world.getBiome(pos).getKey();


		if (biome.isPresent() && SuperflatPlus.isSuperflat(world)) {
			if (pos.getY() < 0)
				block = Blocks.DEEPSLATE;
		}
		return world.setBlockState(pos, block.getDefaultState(), 3);
	}
}
