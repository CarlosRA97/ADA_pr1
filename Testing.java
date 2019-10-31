import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

class Testing {

    private String[] args;

    private List<String> log;

    // Config
    private boolean _l = false;
    private boolean _t = false;

    Testing(String[] args) {
        this.args = args;
        log = new LinkedList<>();

        loadSettings();
    }

    /*
        Argumentos:
            -l : Muestra el log en la consola
            -t : Ejecuta todas las pruebas de todos los algoritmos conocidos

     */
    private void loadSettings() {
        for (int i = 0; i < args.length; i++) {
            switch (args[i].toLowerCase()) {
                case "-l":
                    _l = true;
                    break;
                case "-t":
                    _t = true;
                    break;
            }
        }
    }


    void writeLog(String line) {
        writeLog(line, logType.INFO);
    }

    void writeLog(String line, logType type) {
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("[dd/MM/yy HH:mm:ss,S] - ");
        String text = sdf.format(now) + line;
        log.add(text);

        if (_l && (type == logType.INFO || type == logType.ERROR)) {
            System.out.println(text);
        }
    }

    String readLog() {
        StringBuilder sb = new StringBuilder();
        for (String line : log) {
            sb.append(line).append("\n");
        }
        return sb.toString();
    }

    public enum logType {INFO, ERROR, SYSTEM}


}
