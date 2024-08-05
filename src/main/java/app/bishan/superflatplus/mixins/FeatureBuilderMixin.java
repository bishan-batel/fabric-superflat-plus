package app.bishan.superflatplus.mixins;

import app.bishan.superflatplus.SuperflatPlus;
import net.minecraft.resource.featuretoggle.FeatureFlag;
import net.minecraft.resource.featuretoggle.FeatureManager;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FeatureManager.Builder.class)
public abstract class FeatureBuilderMixin {

    @Shadow
    public abstract FeatureFlag addFlag(Identifier feature);

    @Inject(
            method = "<init>",
            at = @At("TAIL")
    )
    void init(String universe, CallbackInfo ci) {
        SuperflatPlus.setFeature(addFlag(SuperflatPlus.id("superflat_plus")));
    }
}
