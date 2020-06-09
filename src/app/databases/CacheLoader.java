package app.databases;

import annotations.Initializer;
import annotations.InitializerMethod;

@Initializer
public class CacheLoader {

    @InitializerMethod
    public void loadCache() {
        System.out.println("Loading data from cache");
    }

    public void reloadCache() {
        System.out.println("Reload cache");
    }
}
