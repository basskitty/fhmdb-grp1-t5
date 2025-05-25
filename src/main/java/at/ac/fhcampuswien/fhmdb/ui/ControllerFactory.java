package at.ac.fhcampuswien.fhmdb.ui;

import javafx.util.Callback;
import java.util.HashMap;
import java.util.Map;

public class ControllerFactory implements Callback<Class<?>, Object>{
    private static ControllerFactory instance;
    private final Map<Class<?>, Object> singletons = new HashMap<>();

    private ControllerFactory() {};

    public static ControllerFactory getInstance() {
        if (instance == null) {
            instance = new ControllerFactory();
        }
        return instance;
    }

    @Override
    public Object call(Class<?> controllerClass) {
        return singletons.computeIfAbsent(controllerClass, cls -> {
            try {
                return cls.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        });
    }
}
