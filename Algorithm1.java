public class Algorithm1 implements IAlgorithm {

    private long l = 0L;

    public String getName() {
        return "1";
    }

    public long getL() {
        return l;
    }

    public void f(int n) {
        l = n;
    }
}
