public class Analizador {
    public static void main(String[] arg) {
        Config testcfg = new Config(arg);
        Analyser analyser = new Analyser(testcfg);

        analyser.start();
    }
}
