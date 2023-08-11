package app.bishan.superflatplus.mixins;

import app.bishan.superflatplus.SuperflatPlus;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.structure.BuriedTreasureGenerator;
import net.minecraft.structure.StructurePiece;
import net.minecraft.structure.StructurePieceType;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(BuriedTreasureGenerator.Piece.class)
public abstract class BuriedTreasureGenerator$PieceMixin extends StructurePiece {

	protected BuriedTreasureGenerator$PieceMixin(StructurePieceType type, int length, BlockBox boundingBox) {
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
