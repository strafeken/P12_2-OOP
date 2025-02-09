package io.github.team2.Utils;

public abstract class Singleton<T> {

    private static Singleton<?> instance;
    
    protected Singleton() {
    	
    }
    
    // uses double-checked locking principle
    @SuppressWarnings("unchecked")
	public static <T extends Singleton<T>> T getInstance(Class<T> singleton) {
        if (instance == null) {
            synchronized (Singleton.class) {
                if (instance == null) {
                    try {
                        instance = singleton.getDeclaredConstructor().newInstance();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return (T) instance;
    }
}
