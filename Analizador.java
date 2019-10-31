public class Analizador {
    public static void main(String[] arg) {
        Testing testcfg = new Testing(arg);
        Analyser analyser = new Analyser(testcfg);

        analyser.start();
    }
}
