public class Algorithm2N extends Algorithm {

    private long l = 0L;

    public String getName() {
        return "2^n";
    }

    public long getL() {
        return l;
    }

    public void f(long n) {
        l = aux(n);
    }

    private long aux(long n) {
        return (n < 2) ? n : aux(n - 1) + aux(n - 2);
    }

}
