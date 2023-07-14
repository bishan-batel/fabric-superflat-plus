package app.bishan.superflatplus.mixins;

import app.bishan.superflatplus.SuperflatPlus;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.MultiNoiseBiomeSourceParameterList;
import net.minecraft.world.biome.source.MultiNoiseBiomeSourceParameterLists;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(MultiNoiseBiomeSourceParameterLists.class)
public class MultiNoiseBiomeSourceParameterListMixin {

    @Inject(method = "bootstrap", at = @At("TAIL"), locals = LocalCapture.CAPTURE_FAILHARD)
    private static void bootstrap(
            Registerable<MultiNoiseBiomeSourceParameterList> registry,
            CallbackInfo ci,
            RegistryEntryLookup<Biome> registryEntryLookup
    ) {
        SuperflatPlus.INSTANCE.getLogger().info("Injecting / register");

        registry.register(
                SuperflatPlus.INSTANCE.getFlatBiomeSourceKey(),
                new MultiNoiseBiomeSourceParameterList(SuperflatPlus.INSTANCE.getFlatPreset(), registryEntryLookup)
        );
    }
}
