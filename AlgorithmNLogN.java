public class AlgorithmNLogN implements IAlgorithm {

    private long l = 0L;

    public String getName() {
        return "nlogn";
    }

    public long getL() {
        return l;
    }

    public void f(int n) {
        for (long i = 1; i <= n; i++) {
            for (long j = 1; j < 8; j *= 2) {
                l += i + j;
            }
        }
    }
}
