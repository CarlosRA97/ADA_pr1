public class AlgorithmNLogN extends Algorithm {

    private long l = 0L;

    public String getName() {
        return "nlogn";
    }

    public long getL() {
        return l;
    }

    public void f(long n) {
        for (long i = 1; i <= n; i++) {
            for (long j = 1; j < n; j *= 2) {
                l += i + j;
            }
        }
    }

}
