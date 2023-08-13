package app.bishan.superflatplus.mixins;

import app.bishan.superflatplus.SuperflatPlus;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.gui.screen.world.WorldCreator;
import net.minecraft.resource.DataConfiguration;
import net.minecraft.resource.DataPackSettings;
import net.minecraft.resource.ResourcePackManager;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.nio.file.Path;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

@Mixin(CreateWorldScreen.class)
@ApiStatus.Experimental
public abstract class CreateWorldMixin {


	@Shadow
	@Nullable
	protected abstract Pair<Path, ResourcePackManager> getScannedPack(DataConfiguration dataConfiguration);

	@Shadow
	@Final
	WorldCreator worldCreator;

	@Shadow
	protected abstract void applyDataPacks(ResourcePackManager dataPackManager, boolean fromPackScreen, Consumer<DataConfiguration> configurationSetter);

	@Shadow
	@Nullable
	private ResourcePackManager packManager;

	@Shadow
	protected abstract void validateDataPacks(ResourcePackManager dataPackManager, DataConfiguration dataConfiguration, Consumer<DataConfiguration> configurationSetter);

	@Inject(method = "createLevel", at = @At("HEAD"))
	@SuppressWarnings("DataFlowIssue")
	private void createLevel(CallbackInfo ci) {
		if (!SuperflatPlus.WORLD_PRESET_ENABLED) return;

		var preset = worldCreator.getWorldType().preset();

		if (!preset.matchesKey(SuperflatPlus.WORLD_PRESET)) return;

		ResourcePackManager manager = getScannedPack(worldCreator.getGeneratorOptionsHolder().dataConfiguration()).getSecond();

		SuperflatPlus.logger.info("{}", manager.getProfiles());

		if (manager.enable("superflat-plus:superflat-plus-data")) {
			SuperflatPlus.logger.info("worked");
		} else {
			SuperflatPlus.logger.info("not worked");
		}

		var enabledList = ImmutableList.copyOf(manager.getEnabledNames());
		var disabledList = manager.getNames().stream().filter(a -> !manager.getEnabledNames().contains(a)).collect(ImmutableList.toImmutableList());

		var dataConfig = new DataConfiguration(new DataPackSettings(enabledList, disabledList),
//				manager.getRequestedFeatures()
				worldCreator.getGeneratorOptionsHolder().dataConfiguration().enabledFeatures());

		validateDataPacks(manager, dataConfig, (data) -> { /* ignore */ });
	}
}
