package com.fin.wynndynamichud.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(name = "wynndynamichud")
public class Settings implements ConfigData {

    public boolean enableDynamicHud = true;

    @Comment("Delete Health and Mana if they were present in your hotbar.")
    public boolean deleteStatusBars = false;

    @Comment("Delete the Wynncraft HUD shadow.")
    public boolean deleteOverlayMessageShadow = false;

    @Comment("Added culling boxes for display entities. increasing Framerates significantly on Wynncraft.")
    public boolean modifyEntityDisplayWidthHeight = false;

    public int hideHotbarSeconds = 5;

    @Comment("Similar to Raised mod, move your hotbar on the Y-axis")
    public int movehotbar = 0;

    @Comment("move the Wynn' HUD when the hotbar is hidden.")
    public int moveActionBar = 0;

}
