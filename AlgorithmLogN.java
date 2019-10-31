public class AlgorithmLogN extends Algorithm {

    private long l = 0L;

    public String getName() {
        return "logn";
    }

    public long getL() {
        return l;
    }

    public void f(int n) {
        for (long i = 1; i < n; i *= 2) {
            l += i;
        }
    }

}
