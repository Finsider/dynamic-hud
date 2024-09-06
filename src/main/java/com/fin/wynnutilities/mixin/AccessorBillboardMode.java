package com.fin.wynnutilities.mixin;

import net.minecraft.entity.decoration.DisplayEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(DisplayEntity.BillboardMode.class)
public interface AccessorBillboardMode {
    @Accessor("index")
    byte getIndex();
}
