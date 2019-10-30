public class Algorithm2N implements IAlgorithm {

    private long l = 0L;

    public String getName() {
        return "2^n";
    }

    public long getL() {
        return l;
    }

    public void f(int n) {
        l = aux(n);
    }

    private int aux(int n) {
        return (n < 2) ? n : aux(n - 1) + aux(n - 2);
    }
}