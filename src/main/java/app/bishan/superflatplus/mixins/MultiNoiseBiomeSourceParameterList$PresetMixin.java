package app.bishan.superflatplus.mixins;

import app.bishan.superflatplus.SuperflatPlus;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.source.MultiNoiseBiomeSourceParameterList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Map;
import java.util.stream.Stream;

@Mixin(MultiNoiseBiomeSourceParameterList.Preset.class)
public abstract class MultiNoiseBiomeSourceParameterList$PresetMixin {
    @Shadow
    @Final
    static Map<Identifier, MultiNoiseBiomeSourceParameterList.Preset> BY_IDENTIFIER;

    @Redirect(method = "<clinit>", at = @At(value = "INVOKE", target = "Ljava/util/stream/Stream;of([Ljava/lang/Object;)Ljava/util/stream/Stream;"))
    private static <T> Stream<MultiNoiseBiomeSourceParameterList.Preset> a(T[] values) {
        SuperflatPlus.INSTANCE.getLogger().info("Adding multi noise biome source parameter list preset ");
        return Stream.concat(
                Stream.of((MultiNoiseBiomeSourceParameterList.Preset[]) values),
                Stream.of(SuperflatPlus.INSTANCE.getFlatPreset())
        );
    }
}
