import java.util.LinkedList;
import java.util.List;

class Analyser {

    private Config cfg;

    Analyser(Config testcfg) {
        this.cfg = testcfg;
    }

    private String closer(double ratio) {
        if (ratio < 0.2) {
            return "1";
        } else if (ratio >= 0.2 && ratio < 0.55) {
            return "LOGN";
        } else if (ratio >= 0.55 && ratio < 1.65) {
            return "N";
        } else if (ratio >= 1.65 && ratio < 2) {
            return "NLOGN";
        } else if (ratio >= 2 && ratio < 4.1) {
            return "N2";
        } else if (ratio >= 4.1 && ratio < 8.2) {
            return "N3";
        } else if (ratio >= 8.2 && ratio < 520) {
            return "2N";
        } else {
            return "NF";
        }
    }

    private List<IAlgorithm> loadAlgorithms() {
        List<IAlgorithm> list = new LinkedList<>();
        if (cfg.enabled("-t") || cfg.enabled("-1")) {
            list.add(new Algorithm1());
            list.add(new AlgorithmLogN());
            list.add(new AlgorithmN());
            list.add(new AlgorithmNLogN());
            list.add(new AlgorithmN2());
            list.add(new AlgorithmN3());
            list.add(new Algorithm2N());
            list.add(new AlgorithmNF());
        }

        if (cfg.enabled("-1")) {
            list = Algorithm.intersection(cfg.arguments("-1"), list);
        } else {
            list.add(new AlgorithmUnknown());
        }

        return list;
    }

    void start() {
        cfg.writeLog("Started", Config.logType.SYSTEM);
        List<IAlgorithm> algorithms = loadAlgorithms();

        cfg.writeLog("Loaded algorithms: \n", Config.logType.SYSTEM, Algorithm.toStringList(algorithms));

        for (IAlgorithm algorithm : algorithms) {
            algorithm.setRatio(calculate(algorithm));
            cfg.writeLog("[" + algorithm.toString() + "] ratio: " + algorithm.getRatio());
        }

//
//        System.out.println(cfg.readFullLog());
    }

    private double calculate(IAlgorithm algorithm) {




        return 3;
    }


    // https://www.codejava.net/java-core/concurrency/java-concurrency-executing-value-returning-tasks-with-callable-and-future


}
