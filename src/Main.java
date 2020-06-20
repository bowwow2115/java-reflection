import static annotations.Annotations.*;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

public class Main {

    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException {
        SqlQueryBuilder sqlQueryBuilder = new SqlQueryBuilder(Arrays.asList("1" , "2", "3"), 10, "Movies",
                Arrays.asList("Id", "Name"));

        BestGamesFinder bestGamesFinder = new BestGamesFinder();

        System.out.println((List<? extends String>)execute(bestGamesFinder));
    }

    public static <T> T execute(Object instance) throws InvocationTargetException, IllegalAccessException {
        Class<?> clazz = instance.getClass();

        Map<String, Method> operationToMethod = getOperationToMethod(clazz);

        Map<String, Field> inputToField = getInputToField(clazz);

        Method finalResultMethod = findFinalResultMethod(clazz);

        return (T) executeWithDependencies(instance, finalResultMethod, operationToMethod, inputToField);
    }

    private static Object executeWithDependencies(Object instance,
                                                  Method method,
                                                  Map<String, Method> operationToMethod,
                                                  Map<String, Field> inputToField) throws IllegalAccessException, InvocationTargetException {
        List<Object> parameterValues = new ArrayList<>(method.getParameterCount());

        for (Parameter parameter : method.getParameters()) {
            Object value = null;
            if (parameter.isAnnotationPresent(Input.class)) {
                Field field = inputToField.get(parameter.getAnnotation(Input.class).value());

                field.setAccessible(true);

                value = field.get(instance);

            } else if (parameter.isAnnotationPresent(DependsOn.class)) {
                Method dependencyMethod = operationToMethod.get(parameter.getAnnotation(DependsOn.class).value());

                value = executeWithDependencies(instance, dependencyMethod, operationToMethod, inputToField);
            }

            parameterValues.add(value);
        }

        return method.invoke(instance, parameterValues.toArray());
    }

    private static Method findFinalResultMethod(Class<?> clazz) {
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(FinalResult.class)){
                return method;
            }
        }

        throw new RuntimeException("No method found with FinalResult annotation");
    }


    private static Map<String, Method> getOperationToMethod(Class<?> clazz) {
        Map<String, Method> operationNameToMethod = new HashMap<>();

        for (Method method : clazz.getDeclaredMethods()) {
            if (!method.isAnnotationPresent(Operation.class)) {
                continue;
            }

            Operation operation = method.getAnnotation(Operation.class);

            operationNameToMethod.put(operation.value(), method);
        }

        return operationNameToMethod;
    }

    private static Map<String, Field> getInputToField(Class<?> clazz) {
        Map<String, Field> inputToField = new HashMap<>();

        for (Field field : clazz.getDeclaredFields()) {
            if (!field.isAnnotationPresent(Input.class)) {
                continue;
            }

            Input input = field.getAnnotation(Input.class);

            inputToField.put(input.value(), field);
        }
        return inputToField;
    }
}
