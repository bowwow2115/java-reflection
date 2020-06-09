package app.http;

import annotations.Initializer;
import annotations.InitializerMethod;

@Initializer
public class ServiceRegistry {

    @InitializerMethod
    public void registerService() {
        System.out.println("Service successfully registered");
    }
}
