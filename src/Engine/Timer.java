package Engine;
public class Timer {
    
    public static double getTime() {
        return System.nanoTime() / 1000000000.0;
    }
}
