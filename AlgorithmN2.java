public class AlgorithmN2 implements IAlgorithm {

    private long l = 0L;

    public String getName() {
        return "n^2";
    }

    public long getL() {
        return l;
    }

    public void f(int n) {
        for (long j = 0; j < n; j++) {
            for (long k = 0; k < n; k++) {
                l += j * k;
            }
        }
    }
}
