package com.example.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = "Wynn")
public class Settings implements ConfigData {

    public boolean deleteStatusBars = false;

    public int hideHotbarSeconds = 5;
    public int movehotbar = 0;
    public int moveActionBar = 0;
}
