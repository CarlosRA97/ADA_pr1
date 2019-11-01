public class AlgorithmN3 extends Algorithm {

    private long l = 0L;

    public String getName() {
        return "n^3";
    }

    public long getL() {
        return l;
    }

    public void f(long n) {
        for (long j = 0; j < n; j++) {
            for (long k = 0; k < n; k++) {
                for (long i = 0; i < n; i++) {
                    l += j * k / (i + 1);
                }
            }
        }
    }

}
