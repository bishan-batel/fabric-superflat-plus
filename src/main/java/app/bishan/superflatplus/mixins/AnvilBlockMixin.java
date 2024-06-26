package app.bishan.superflatplus.mixins;

import app.bishan.superflatplus.SuperflatPlus;
import app.bishan.superflatplus.resource.CustomRecipeManager;
import app.bishan.superflatplus.resource.recipe.AnvilDropInventory;
import app.bishan.superflatplus.resource.recipe.AnvilDropRecipe;
import net.minecraft.block.AnvilBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(AnvilBlock.class)
public class AnvilBlockMixin {

    @Inject(method = "onLanding", at = @At("TAIL"))
    public void onLanding(World world, BlockPos pos, BlockState fallingBlockState, BlockState currentStateInPos, FallingBlockEntity fallingBlockEntity, CallbackInfo ci) {
        if (!SuperflatPlus.isSuperflat(world)) return;
        AnvilDropRecipe.mixinOnLanding(world, pos);
    }

}
