public class AlgorithmN extends Algorithm {

    private long l = 0L;

    public String getName() {
        return "n";
    }

    public long getL() {
        return l;
    }

    public void f(long n) {
        for (long i = 0; i < n; i++) {
            l += i;
        }
    }

}
