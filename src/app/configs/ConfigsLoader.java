package app.configs;

import annotations.Initializer;
import annotations.InitializerMethod;

@Initializer
public class ConfigsLoader {

    @InitializerMethod
    public void loadAllConfigs() {
        System.out.println("Loading all configuration files");
    }
}
