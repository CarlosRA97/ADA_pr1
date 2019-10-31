import java.util.LinkedList;
import java.util.List;

abstract class Algorithm implements IAlgorithm {
    private double ratio;

    static List<String> toStringList(List<IAlgorithm> list) {
        List<String> newList = new LinkedList<>();
        for (IAlgorithm alg : list) {
            newList.add(alg.toString());
        }
        return newList;
    }

    static List<IAlgorithm> intersection(List<String> algorithmsNames, List<IAlgorithm> algorithms) {
        List<IAlgorithm> list = new LinkedList<>();

        for (IAlgorithm algorithm : algorithms) {
            if (algorithmsNames.contains(algorithm.getName())) {
                list.add(algorithm);
            }
        }

        return list;
    }

    public double getRatio() {
        return ratio;
    }

    public void setRatio(double ratio) {
        this.ratio = ratio;
    }

    public String toString() {
        return getName();
    }
}
