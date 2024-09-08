package com.fin.wynnutilities.mixin;

import com.fin.wynnutilities.Main;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.LightmapTextureManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ParticleManager.class)
public class MixinParticleManager {
    @Inject(at = @At("HEAD"), method = "renderParticles", cancellable = true)
    private void deleteParticles (LightmapTextureManager lightmapTextureManager, Camera camera, float tickDelta, CallbackInfo ci) {
        if (Main.settings.extraSettings.deleteParticles) ci.cancel();
    }
    @Inject(at = @At("HEAD"), method = "tick", cancellable = true)
    private void deleteParticleTicks (CallbackInfo ci) {
        if (Main.settings.extraSettings.deleteParticles) ci.cancel();
    }
}
