package com.example.mixin;

import com.example.Main;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.player.PlayerEntity;
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

    @Unique private int previousSelectedSlot = -1;
    @Unique private long hotbarSwitchTimestamp = 0L;

    @Inject(at = @At("HEAD"), method = "renderStatusBars", cancellable = true)
    private void deleteHealthMana(DrawContext context, CallbackInfo ci) {
        if (Main.settings.deleteStatusBars) ci.cancel();
    }

    @Inject(at = @At("HEAD"), method = "renderHotbar", cancellable = true)
    private void cancelHotbar(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        updateHotbarSwitchState();
        if (!HotbarIsActive()) ci.cancel();
    }

    @ModifyArg(method = "renderHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Lnet/minecraft/util/Identifier;IIII)V"), index = 2)
    public int adjustHotbarY(int y) {
        return y + Main.settings.movehotbar;
    }

    @ModifyArg(method = "renderHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderHotbarItem(Lnet/minecraft/client/gui/DrawContext;IILnet/minecraft/client/render/RenderTickCounter;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/item/ItemStack;I)V"), index = 2)
    public int adjustHotbarItemY(int y) {
        return y + Main.settings.movehotbar;
    }

    @ModifyArg(method = "renderHeldItemTooltip", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTextWithBackground(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;IIII)I"), index = 3)
    private int adjustHeldItemTooltipY(int y) {
        return !HotbarIsActive() ? y + Main.settings.moveActionBar : y + Main.settings.movehotbar;
    }

    @ModifyArg(method = "renderOverlayMessage", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTextWithBackground(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;IIII)I"), index = 3)
    private int adjustOverlayMessageY(int y) {
        return !HotbarIsActive() ? y + Main.settings.moveActionBar : y + Main.settings.movehotbar;
    }

    @ModifyArg(method = "renderExperienceBar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Lnet/minecraft/util/Identifier;IIII)V"),index = 2)
    private int adjustExperienceBarHeight(int y) {
        return !HotbarIsActive() ? y + Main.settings.moveActionBar : y + Main.settings.movehotbar;
    }

    @ModifyArg(method = "renderExperienceBar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Lnet/minecraft/util/Identifier;IIIIIIII)V"),index = 6)
    private int adjustExperienceBarHeight2(int y) {
        return !HotbarIsActive() ? y + Main.settings.moveActionBar : y + Main.settings.movehotbar;
    }
    @ModifyArg(method = "renderMountJumpBar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Lnet/minecraft/util/Identifier;IIII)V"),index = 2)
    private int adjustMountBarHeight(int y) {
        return !HotbarIsActive() ? y + Main.settings.moveActionBar : y + Main.settings.movehotbar;
    }

    @ModifyArg(method = "renderMountJumpBar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Lnet/minecraft/util/Identifier;IIIIIIII)V"),index = 6)
    private int adjustfilledMountBarHeight(int y) {
        return !HotbarIsActive() ? y + Main.settings.moveActionBar : y + Main.settings.movehotbar;
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
        return (System.currentTimeMillis() - hotbarSwitchTimestamp) <= (long) 1000 * Main.settings.hideHotbarSeconds;
    }
}