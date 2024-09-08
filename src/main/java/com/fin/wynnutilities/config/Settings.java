package com.fin.wynnutilities.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(name = "wynnutilities")
public class Settings implements ConfigData {

    @ConfigEntry.Gui.CollapsibleObject
    public DynamicHUDSettings dynamicHUDSettings = new DynamicHUDSettings();
    public static class DynamicHUDSettings {
        public boolean enableDynamicHud = true;

        public int hideHotbarSeconds = 5;

        @Comment("Similar to Raised mod, move your hotbar on the Y-axis")
        public int movehotbar = 0;

        @Comment("move the Wynn' HUD when the hotbar is hidden.")
        public int moveActionBar = 0;
    }

    @Comment("Disable HUD that wynncraft already tried to disable")
    @ConfigEntry.Gui.CollapsibleObject
    public WynncraftHUDSettings wynncraftHUDSettings = new WynncraftHUDSettings();
    public static class WynncraftHUDSettings {
        @Comment("Delete Health and Mana if they were present in your hotbar.")
        public boolean deleteStatusBars = false;

        public boolean deleteMountHealth = false;

        @Comment("Delete the Wynncraft HUD shadow.")
        public boolean deleteOverlayMessageShadow = false;
    }

    @Comment("Delete other stuff to juice some performance out of massive fights.")
    @ConfigEntry.Gui.CollapsibleObject
    public ExtraSettings extraSettings = new ExtraSettings();
    public static class ExtraSettings {
        public boolean deletePlayerHeldItem = false;
        public boolean deleteAllHeldItem = false;
        public boolean deletePlayerArmor = false;
        public boolean deleteParticles = false;
    }
    @Comment("Added culling boxes for display entities. increasing Framerates significantly on Wynncraft.")
    public boolean modifyEntityDisplayWidthHeight = false;
}
