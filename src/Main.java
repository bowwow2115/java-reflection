import annotations.Initializer;
import annotations.InitializerMethod;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws Throwable {
        initialize("init", "init.configs", "init.databases", "init.http");
        // application logic
    }

    public static void initialize(String... packageNames) throws Throwable {

        List<Class<?>> classes = getAllClasses(packageNames);

        for (Class<?> clazz : classes) {
            if (clazz.getAnnotation(Initializer.class) == null) {
                continue;
            }

            List<Method> methods = getAllInitializingMethods(clazz);

            Object instance = clazz.getDeclaredConstructor().newInstance();

            for (Method method : methods) {
                method.invoke(instance);
            }
        }
    }

    private static List<Method> getAllInitializingMethods(Class<?> clazz) {
        List<Method> initializingMethods = new ArrayList<>();
        for (Method method : clazz.getDeclaredMethods()) {
            InitializerMethod initializerMethodAnnotation = method.getAnnotation(InitializerMethod.class);

            if (initializerMethodAnnotation != null) {
                initializingMethods.add(method);
            }
        }
        return initializingMethods;
    }


    public static List<Class<?>> getAllClasses(String... packageNames)
            throws ClassNotFoundException, IOException, URISyntaxException {
        List<Class<?>> classes = new ArrayList<>();
        for (String packageName : packageNames) {
            String packageRelativePath = packageName.replace('.', '/');

            URI packageUri = Thread.currentThread().getContextClassLoader().getResource(packageRelativePath).toURI();

            if (packageUri.getScheme().equals("jar")) {
                FileSystem fileSystem = FileSystems.newFileSystem(packageUri, Collections.emptyMap());

                Path packageFullPathInJar = fileSystem.getPath(packageRelativePath);
                classes.addAll(getAllPackageClasses(packageFullPathInJar, packageName));

                fileSystem.close();
            } else if (packageUri.getScheme().equals("file")) {
                Path packageFullPath = Paths.get(packageUri);
                classes.addAll(getAllPackageClasses(packageFullPath, packageName));
            }
        }
        return classes;
    }

    private static List<Class<?>> getAllPackageClasses(Path packagePath, String packageName) throws ClassNotFoundException, IOException {
        if (!Files.exists(packagePath)) {
            return Collections.emptyList();
        }

        List<Path> files = Files.list(packagePath)
                .filter(Files::isRegularFile)
                .collect(Collectors.toList());

        List<Class<?>> classes = new ArrayList<>();

        for (Path filePath : files) {
            String fileName = filePath.getFileName().toString();

            if (fileName.endsWith(".class")) {
                String classFullName = packageName + '.' + fileName.replaceFirst("\\.class$", "");
                classes.add(Class.forName(classFullName));
            }
        }
        return classes;
    }
}
