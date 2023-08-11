package app.bishan.superflatplus.mixins;

import app.bishan.superflatplus.SuperflatPlus;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.structure.Structure;
import net.minecraft.world.gen.structure.WoodlandMansionStructure;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(WoodlandMansionStructure.class)
public abstract class WoodlandMansionStructureMixin extends Structure {
	@Unique
	private static final int MINIMUM_Y_POS = 60;

	@Redirect(
			method = "getStructurePosition",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/gen/structure/WoodlandMansionStructure;getShiftedPos(Lnet/minecraft/world/gen/structure/Structure$Context;Lnet/minecraft/util/BlockRotation;)Lnet/minecraft/util/math/BlockPos;"
			)
	)
	private BlockPos getStructurePosition(
			WoodlandMansionStructure instance,
			Context context,
			BlockRotation blockRotation
	) {
		// use of deprecated method bc Mojang does it bruh
		@SuppressWarnings("deprecation") var pos = this.getShiftedPos(context, blockRotation);

		// Snap position to y60 to pass structure placement check,
		// This shouldn't have many consequences in normal worlds because
		// roofed forest biomes in minecraft never go down to y4, but this
		// is still a solution I'm comfortable with (TODO find something a bit less jank)
		return pos.getY() == SuperflatPlus.STRUCTURE_MIN_CORNER_HEIGHT
				? pos.withY(MINIMUM_Y_POS)
				: pos;
	}

	protected WoodlandMansionStructureMixin(Config config) {
		super(config);
	}
}
