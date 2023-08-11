package app.bishan.superflatplus.mixins;

import app.bishan.superflatplus.SuperflatPlus;
import app.bishan.superflatplus.resource.CustomRecipeManager;
import app.bishan.superflatplus.resource.recipe.AnvilRecipeSerializer;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(RecipeType.class)
public interface RecipeTypeMixin {
	private static <T extends Recipe<?>> void register(CustomRecipeManager.CustomRecipeType<T> type) {
		Registry.register(
				Registries.RECIPE_TYPE,
				new Identifier(SuperflatPlus.ID, type.getId()),
				CustomRecipeManager.ANVIL_DROP
		);
	}

	@Inject(method = "<clinit>", at = @At("TAIL"))
	private static void clInit(CallbackInfo ci) {
		AnvilRecipeSerializer.bootstrap();
		List.<CustomRecipeManager.CustomRecipeType<?>>of(
				CustomRecipeManager.ANVIL_DROP
		).forEach(RecipeTypeMixin::register);
	}
}
