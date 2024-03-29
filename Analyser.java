import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;


class Analyser {

    private Config cfg;

    Analyser(Config testcfg) {
        this.cfg = testcfg;
    }

    private String closer(double ratio) {
        if (ratio < 0.2) {
            return "1";
        } else if (ratio >= 0.2 && ratio < 0.45) {
            return "LOGN";
        } else if (ratio >= 0.45 && ratio < 1.85) {
            return "N";
        } else if (ratio >= 1.85 && ratio < 2.12) {
            return "NLOGN";
        } else if (ratio >= 2.12 && ratio < 4.1) {
            return "N2";
        } else if (ratio >= 4.1 && ratio < 8.6) {
            return "N3";
        } else if (ratio >= 8.6 && ratio < 520) {
            return "2N";
        } else {
            return "NF";
        }
    }

    private List<Long> SetUpRange(int attempt) {
        List<Long> range = new LinkedList<>();

        if (cfg.enabled("-n")) {
            for (Object argument : cfg.arguments("-n")) {
                range.add(Long.valueOf((String) argument));
            }
            return range;
        } else {
            if (attempt == -1) {
                range.add(40L);
                range.add(100L);
            } else if (attempt == 0) {             // Comprobar rango [1, nlogn]
                range.add(100000L);
                range.add(1000000L);
                range.add(10000000L);
            } else if (attempt == 1) {      // Comprobar rango [n^2, n^3]
                range.add(100L);
                range.add(150L);
                range.add(200L);
            } else if (attempt == 2) {      // Comprobar rango [2^n, nf]
                range.add(10L);
                range.add(15L);
            }

            return range;
        }
    }

    @SuppressWarnings("unchecked")
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
        } else {
            list.add(new AlgorithmUnknown());
        }

        if (cfg.enabled("-1")) {
            list = Algorithm.intersection((List<String>) cfg.arguments("-1"), list);
        }

        cfg.writeLog("Loaded algorithms: \n", Config.logType.SYSTEM, Algorithm.toStringList(list));

        return list;
    }

    void start() {
        cfg.writeLog("Started", Config.logType.SYSTEM);
        List<IAlgorithm> algorithms = loadAlgorithms();


        for (IAlgorithm algorithm : algorithms) {
            algorithm.setRatio(startCalc(algorithm, SetUpRange(algorithm.getAttempts())));
            cfg.writeLog("[" + algorithm.toString() + "] ratio: " + algorithm.getRatio() + " ==> {" + closer(algorithm.getRatio()) + "}");
        }


        if ((!cfg.enabled("-t") && !cfg.enabled("-1")) && !cfg.enabled("-x")) {

            String result = closer(algorithms.get(0).getRatio());

            if (result.equals("NF")) {
                nfSietteParcialFix(algorithms.get(0));
            } else {
                System.out.println(result);
            }
        }

        cfg.writeLog("Finished", Config.logType.SYSTEM);

        if (cfg.enabled("-log")) {
            System.out.println(cfg.readFullLog());
        }
        System.exit(0);
    }


    private double startCalc(IAlgorithm algorithm, List<Long> range) {
        cfg.writeLog("Using n's range of: " + range, Config.logType.SYSTEM);
        cfg.writeLog("Trying to get ratio of : " + algorithm.toString());

        calculate(algorithm, range);

        return algorithm.getRatio();
    }

    private void nfSietteParcialFix(IAlgorithm algorithm) {
//        cfg.disable("-n");

        algorithm.setRatio(0.0);

        algorithm.setAttempts(2);
        List<Long> range = SetUpRange(2);
        cfg.writeLog("LAST ATTEMPT: " + range, Config.logType.SYSTEM);

        calculate(algorithm, range);

        String finalResult = closer(algorithm.getRatio());

        if (finalResult.equals("NF")) {
            finalResult = "2N";
        }

        System.out.println(finalResult);
    }

    private void calculate(IAlgorithm algorithm, List<Long> range) {

        algorithm.setRatio(0);

        List<Double> ratios = new ArrayList<>();
        List<Double> finalRatios = new ArrayList<>();

        double ratio;
        final double error = 0.05;

        boolean timeout = false;

        // Ejecutar varias veces
        for (int i = 0; i < 3; i++) {
            ratios.clear();

            for (long n : range) {

                ExecutorService thread = Executors.newSingleThreadExecutor();
                Future<Double> futureValue = thread.submit(() -> getRatio(n, algorithm));

                Double value = null;
                try {
                    value = futureValue.get(2000, TimeUnit.MILLISECONDS);

                    // Stakoverflowerror porque la pila de java no aguanta la funcion recursiva de 2^n, por lo que lo trato igual que el timeout
                } catch (TimeoutException | ExecutionException e) {
                    futureValue.cancel(true);
                    cfg.writeLog("TIMEOUT", Config.logType.ERROR);
                    timeout = true;
                    break;
                } catch (Exception e) {
                    cfg.writeLog(e.toString(), Config.logType.ERROR);
//                    timeout = true;
                    break;
                }


                thread.shutdown();

                if (value != null && !futureValue.isCancelled()) {
                    ratio = value;

                    ratios.add(ratio);


                    try {
                        // Si no se espera salen valores muy altos
                        Thread.sleep(500);
                    } catch (InterruptedException ignored) {
                    }
                }
            }

            if (timeout) {
                algorithm.incrementAttempts();
                if (algorithm.getAttempts() <= 2) {
                    cfg.writeLog("Attempt: " + (algorithm.getAttempts() + 1));
                    cfg.writeLog("Trying with new range: " + SetUpRange(algorithm.getAttempts()));
                    calculate(algorithm, SetUpRange(algorithm.getAttempts()));
                    break;
                } else if (algorithm.getAttempts() >= 3) {
                    cfg.writeLog("More than 3 attempts", Config.logType.ERROR);
                    algorithm.setRatio(Double.MAX_VALUE);
                    break;
                }

            } else {
                ratio = mean(ratios);
                finalRatios.add(ratio);

                cfg.writeLog("[" + algorithm.toString() + "] [Loop:" + i + "] Ratio: " + ratio, Config.logType.SYSTEM);
            }
        }

        if (!timeout) {
            algorithm.setRatio(mean(finalRatios) - error);
        }


    }

    private double mean(List<Double> list) {
        double sum = 0.0;
        for (double value : list) {
            sum += value;
        }

        return sum / list.size();
    }

    private double getRatio(long n, IAlgorithm algorithm) {
        long t1, t2;
        Temporizador t = new Temporizador(2);


        t.iniciar();
        algorithm.f(n);
        t.parar();
        t1 = t.tiempoPasado();
        t.reiniciar();
        t.iniciar();
        algorithm.f(n * 2);
        t.parar();
        t2 = t.tiempoPasado();
        return (double) t2 / t1;
    }

}
