package io.github.team2.Utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class Singleton<T> {
    private static final Map<Class<?>, Singleton<?>> instances = new ConcurrentHashMap<>();
    
    protected Singleton() {
    	// prevent external instantiation
    }

    // uses double-checked locking principle
    @SuppressWarnings("unchecked")
    public static <T extends Singleton<T>> T getInstance(Class<T> singleton) {
        if (!instances.containsKey(singleton)) {
            synchronized (singleton) {
                // double-check if the instance is still not created
                if (!instances.containsKey(singleton)) {
                    try {
                        // create the instance and store it in the map
                        instances.put(singleton, singleton.getDeclaredConstructor().newInstance());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        
        return (T) instances.get(singleton);
    }
}
