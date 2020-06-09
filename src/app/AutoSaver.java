package app;

import annotations.Initializer;
import annotations.InitializerMethod;


@Initializer
public class AutoSaver {

    @InitializerMethod
    public void startAutoSavingThreads() {
        System.out.println("Start automatic data saving to disk");
    }
}
