package com.fin.wynnutilities.mixin;

import com.fin.wynnutilities.Main;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class MixinInGameHud {

    @Shadow protected abstract PlayerEntity getCameraPlayer();

    @Shadow public abstract TextRenderer getTextRenderer();

    @Shadow @Nullable private Text overlayMessage;
    @Shadow private boolean overlayTinted;
    @Shadow private int overlayRemaining;
    @Shadow @Final private MinecraftClient client;
    @Unique private int previousSelectedSlot = -1;
    @Unique private long hotbarSwitchTimestamp = 0L;

    @Inject(at = @At("HEAD"), method = "renderStatusBars", cancellable = true)
    private void deleteHealthMana(DrawContext context, CallbackInfo ci) {
        if (Main.settings.wynncraftHUDSettings.deleteStatusBars) ci.cancel();
    }

    @Inject(at = @At("HEAD"), method = "renderMountHealth", cancellable = true)
    private void deleteMountHealth(DrawContext context, CallbackInfo ci) {
        if (Main.settings.wynncraftHUDSettings.deleteMountHealth) ci.cancel();
    }

    @Inject(at = @At("HEAD"), method = "renderHotbar", cancellable = true)
    private void cancelHotbar(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        updateHotbarSwitchState();
        if (!HotbarIsActive()) ci.cancel();
    }

    @ModifyArg(method = "renderHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Lnet/minecraft/util/Identifier;IIII)V"), index = 2)
    public int adjustHotbarY(int y) {
        return y + Main.settings.dynamicHUDSettings.movehotbar;
    }

    @ModifyArg(method = "renderHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderHotbarItem(Lnet/minecraft/client/gui/DrawContext;IILnet/minecraft/client/render/RenderTickCounter;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/item/ItemStack;I)V"), index = 2)
    public int adjustHotbarItemY(int y) {
        return y + Main.settings.dynamicHUDSettings.movehotbar;
    }

    @ModifyArg(method = "renderHeldItemTooltip", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTextWithBackground(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;IIII)I"), index = 3)
    private int adjustHeldItemTooltipY(int y) {
        return !HotbarIsActive() ? y + Main.settings.dynamicHUDSettings.moveActionBar : y + Main.settings.dynamicHUDSettings.movehotbar;
    }

    @ModifyArg(method = "renderOverlayMessage", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTextWithBackground(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;IIII)I"), index = 3)
    private int adjustOverlayMessageY(int y) {
        return !HotbarIsActive() ? y + Main.settings.dynamicHUDSettings.moveActionBar : y + Main.settings.dynamicHUDSettings.movehotbar;
    }

    @ModifyArg(method = "renderExperienceBar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Lnet/minecraft/util/Identifier;IIII)V"),index = 2)
    private int adjustExperienceBarHeight(int y) {
        return !HotbarIsActive() ? y + Main.settings.dynamicHUDSettings.moveActionBar : y + Main.settings.dynamicHUDSettings.movehotbar;
    }

    @ModifyArg(method = "renderExperienceBar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Lnet/minecraft/util/Identifier;IIIIIIII)V"),index = 6)
    private int adjustExperienceBarHeight2(int y) {
        return !HotbarIsActive() ? y + Main.settings.dynamicHUDSettings.moveActionBar : y + Main.settings.dynamicHUDSettings.movehotbar;
    }
    @ModifyArg(method = "renderMountJumpBar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Lnet/minecraft/util/Identifier;IIII)V"),index = 2)
    private int adjustMountBarHeight(int y) {
        return !HotbarIsActive() ? y + Main.settings.dynamicHUDSettings.moveActionBar : y + Main.settings.dynamicHUDSettings.movehotbar;
    }

    //seriously why the fuck does minecraft use drawTextWithBackground and not drawText with shadow enabled so that it can be injected easier what the fuck??
    @Inject(method = "renderOverlayMessage", at = @At("HEAD"), cancellable = true)
    private void deleteOverlayMessageBackground(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        if (!Main.settings.wynncraftHUDSettings.deleteOverlayMessageShadow) {return;}

        TextRenderer textRenderer = this.getTextRenderer();
        if (this.overlayMessage != null && this.overlayRemaining > 0) {
            this.client.getProfiler().push("overlayMessage");
            float f = (float)this.overlayRemaining - tickCounter.getTickDelta(false);
            int i = (int)(f * 255.0F / 20.0F);
            if (i > 255) {
                i = 255;
            }

            if (i > 8) {
                context.getMatrices().push();
                context.getMatrices().translate((float)(context.getScaledWindowWidth() / 2), (float)(context.getScaledWindowHeight() - 68), 0.0F);
                int j;
                if (this.overlayTinted) {
                    j = MathHelper.hsvToArgb(f / 50.0F, 0.7F, 0.6F, i);
                } else {
                    j = ColorHelper.Argb.withAlpha(i, -1);
                }


                int moveDynamic = !HotbarIsActive() ? Main.settings.dynamicHUDSettings.moveActionBar : Main.settings.dynamicHUDSettings.movehotbar;
                int textWidth = textRenderer.getWidth(this.overlayMessage);
                context.drawText(textRenderer, this.overlayMessage, -textWidth / 2, -4 + moveDynamic, j, false);
                context.getMatrices().pop();
            }

            this.client.getProfiler().pop();
        }

        ci.cancel();
    }

    @ModifyArg(method = "renderMountJumpBar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Lnet/minecraft/util/Identifier;IIIIIIII)V"),index = 6)
    private int adjustfilledMountBarHeight(int y) {
        return !HotbarIsActive() ? y + Main.settings.dynamicHUDSettings.moveActionBar : y + Main.settings.dynamicHUDSettings.movehotbar;
    }

    @Unique
    private void updateHotbarSwitchState() {
        PlayerEntity player = getCameraPlayer();

        int currentSelectedSlot = player.getInventory().selectedSlot;
        if (currentSelectedSlot != previousSelectedSlot) {
            previousSelectedSlot = currentSelectedSlot;
            hotbarSwitchTimestamp = System.currentTimeMillis();
        }
    }

    @Unique
    private boolean HotbarIsActive() {
        if (!Main.settings.dynamicHUDSettings.enableDynamicHud) {return true;}
        return (System.currentTimeMillis() - hotbarSwitchTimestamp) <= (long) 1000 * Main.settings.dynamicHUDSettings.hideHotbarSeconds;
    }
}