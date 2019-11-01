public interface IAlgorithm {
    String getName();

    void f(long n);

    long getL();

    double getRatio();

    void setRatio(double ratio);

    int getAttempts();

    void incrementAttempts();

    String toString();
}
