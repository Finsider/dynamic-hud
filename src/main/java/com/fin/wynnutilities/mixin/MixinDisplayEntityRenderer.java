package com.fin.wynnutilities.mixin;

import com.fin.wynnutilities.Main;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.decoration.DisplayEntity;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DisplayEntity.class)
public class MixinDisplayEntityRenderer {

    @Shadow @Final private static TrackedData<Float> WIDTH;

    @Shadow @Final private static TrackedData<Float> HEIGHT;

    @Shadow @Final private static TrackedData<Integer> TELEPORT_DURATION;

    @Shadow @Final private static TrackedData<Integer> START_INTERPOLATION;

    @Shadow @Final private static TrackedData<Integer> INTERPOLATION_DURATION;

    @Shadow @Final private static TrackedData<Vector3f> TRANSLATION;

    @Shadow @Final private static TrackedData<Vector3f> SCALE;

    @Shadow @Final private static TrackedData<Quaternionf> RIGHT_ROTATION;

    @Shadow @Final private static TrackedData<Quaternionf> LEFT_ROTATION;

    @Shadow @Final private static TrackedData<Integer> GLOW_COLOR_OVERRIDE;

    @Shadow @Final private static TrackedData<Float> SHADOW_STRENGTH;

    @Shadow @Final private static TrackedData<Float> SHADOW_RADIUS;

    @Shadow @Final private static TrackedData<Float> VIEW_RANGE;

    @Shadow @Final private static TrackedData<Integer> BRIGHTNESS;

    @Shadow @Final private static TrackedData<Byte> BILLBOARD;

    @Inject(method = "initDataTracker", at = @At("HEAD"), cancellable = true)
    private void modify(DataTracker.Builder builder, CallbackInfo ci) {

        if (!Main.settings.modifyEntityDisplayWidthHeight) {return;}

        byte billboardIndex = ((AccessorBillboardMode) (Object) DisplayEntity.BillboardMode.FIXED).getIndex();

        builder.add(TELEPORT_DURATION, 0);
        builder.add(START_INTERPOLATION, 0);
        builder.add(INTERPOLATION_DURATION, 0);
        builder.add(TRANSLATION, new Vector3f());
        builder.add(SCALE, new Vector3f(1.0F, 1.0F, 1.0F));
        builder.add(RIGHT_ROTATION, new Quaternionf());
        builder.add(LEFT_ROTATION, new Quaternionf());
        builder.add(BILLBOARD, billboardIndex);
        builder.add(BRIGHTNESS, -1);
        builder.add(VIEW_RANGE, 1.0F);
        builder.add(SHADOW_RADIUS, 0.0F);
        builder.add(SHADOW_STRENGTH, 1.0F);
        builder.add(WIDTH, 1.0F);
        builder.add(HEIGHT, 1.0F);
        builder.add(GLOW_COLOR_OVERRIDE, -1);
        ci.cancel();
    }
}
