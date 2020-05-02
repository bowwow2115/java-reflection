package configloader;

import data.GameConfig;
import data.UserInterfaceConfig;

import java.io.IOException;
import java.lang.reflect.*;
import java.nio.file.Path;
import java.util.Scanner;

public class Main {
    private static final Path GAME_CONFIG_PATH = Path.of("resources/game-properties.cfg");
    private static final Path UI_CONFIG_PATH = Path.of("resources/user-interface.cfg");

    public static void main(String[] args) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, IOException {
        GameConfig config = createConfigObject(GameConfig.class, GAME_CONFIG_PATH);
        System.out.println(config);
    }

    public static <T> T createConfigObject(Class<T> clazz, Path filePath) throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Scanner scanner = new Scanner(filePath);

        Constructor<?> constructor = clazz.getDeclaredConstructor();
        constructor.setAccessible(true);

        T configInstance = (T) constructor.newInstance();

        while (scanner.hasNextLine()) {
            String configLine = scanner.nextLine();

            String[] nameValuePair = configLine.split("=");
            String propertyName = nameValuePair[0];
            String propertyValue = nameValuePair[1];

            Field field;
            try {
                field = clazz.getDeclaredField(propertyName);
                field.setAccessible(true);
            } catch (NoSuchFieldException e) {
                System.out.println(String.format("property name :%s is unsupported", propertyName));
                continue;
            }
            Object parseValue;
            if (field.getType().isArray()) {
                parseValue = parseArrayValue(field.getType().getComponentType(), propertyValue);

            } else {
                parseValue = parseValue(field.getType(), propertyValue);
            }

            field.set(configInstance, parseValue);
        }

        return configInstance;
    }

    private static Object parseArrayValue(Class<?> componentType, String value) {
        String[] elementValues = value.split(",");
        Object arrayObject = Array.newInstance(componentType, elementValues.length);

        for (int i = 0; i < elementValues.length; i++) {
            Array.set(arrayObject, i, parseValue(componentType, elementValues[i]));
        }
        return arrayObject;
    }

    private static Object parseValue(Class<?> type, String value) {
        if (type.equals(int.class)) {
            return Integer.parseInt(value);
        } else if (type.equals(short.class)) {
            return Short.parseShort(value);
        } else if (type.equals(long.class)) {
            return Long.parseLong(value);
        } else if (type.equals(double.class)) {
            return Double.parseDouble(value);
        } else if (type.equals(float.class)) {
            return Float.parseFloat(value);
        } else {
            return value;
        }
    }
}
