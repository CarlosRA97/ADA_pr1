public class AlgorithmNF extends Algorithm {

    private long l = 0L;

    public String getName() {
        return "nf";
    }

    public long getL() {
        return l;
    }

    public void f(int n) {
        for (long i = 0; i < n; i++) {
            l++;
            f(n - 1);
        }
    }

}
