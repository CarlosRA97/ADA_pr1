import java.time.Instant;
import java.time.ZoneOffset;

class Analyser {

    private Testing testcfg;


    Analyser(Testing testcfg) {
        this.testcfg = testcfg;
    }

    private String closer(double ratio) {
        if (ratio < 0.2f) {
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


    void start() {
        testcfg.writeLog("Started", Testing.logType.SYSTEM);

    }

    // https://www.codejava.net/java-core/concurrency/java-concurrency-executing-value-returning-tasks-with-callable-and-future


}
