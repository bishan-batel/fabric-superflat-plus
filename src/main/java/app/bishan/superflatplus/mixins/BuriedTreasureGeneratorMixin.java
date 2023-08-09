package app.bishan.superflatplus.mixins;

import app.bishan.superflatplus.SuperflatPlus;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.structure.BuriedTreasureGenerator;
import net.minecraft.structure.StructurePiece;
import net.minecraft.structure.StructurePieceType;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BuriedTreasureGenerator.Piece.class)
public abstract class BuriedTreasureGeneratorMixin extends StructurePiece {

	protected BuriedTreasureGeneratorMixin(StructurePieceType type, int length, BlockBox boundingBox) {
		super(type, length, boundingBox);
	}

	@Redirect(
			method = "generate",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/world/StructureWorldAccess;getBlockState(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/BlockState;"),
			slice = @Slice(
					from = @At(value = "INVOKE", target = "Lnet/minecraft/world/StructureWorldAccess;getBottomY()I"),
					to = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;getDefaultState()Lnet/minecraft/block/BlockState;")
			)
	)
	private BlockState generate$getBlockState(StructureWorldAccess instance, BlockPos blockPos) {
		return SuperflatPlus.isSuperflat(instance) ? Blocks.SANDSTONE.getDefaultState() : instance.getBlockState(blockPos);
	}
}
