import java.text.SimpleDateFormat;
import java.util.*;

class Config <T> {

    private String[] args;
    private List<String> log;

    // Config
    private Map<String, CfgArguments> cfg;

    Config(String[] args) {
        this.args = args;
        log = new LinkedList<>();
        cfg = new HashMap<>();

        loadSettings();
    }

    /*
        Argumentos:
            -l : Muestra el log en la consola
            -t : Ejecuta todas las pruebas de todos los algoritmos conocidos
            -n : 'n' que se utiliza para realizar la prueba
            -1 <algoritmo, ...> : Selecciona el/los algoritmos a ejecutar
            -log : Muestra el log completo final al acabar

     */
    private void loadSettings() {
        for (int i = 0; i < args.length; i++) {
            switch (args[i].toLowerCase()) {
                case "-log":
                case "-t":
                    cfg.put(args[i], new CfgArguments(true));
                    break;
                case "-l":
                case "-n":
                case "-1":
                    cfg.put(args[i], new CfgArguments<>(true, getArguments(i)));
                    break;
            }
        }
    }

    private Object[] getArguments(int i) {
        return Arrays.copyOfRange(args, i + 1, auxCheckArg(args, i));
    }

    private int auxCheckArg(String[] args, int index) {
        if (args != null) {
            for (int i = index + 1; i < args.length; i++) {
                if (args[i].contains("-")) {
                    return i;
                }
            }
            return args.length;
        } else {
            return 0;
        }
    }

    boolean enabled(String arg) {
        return cfg.containsKey(arg) && cfg.get(arg).enabled;
    }

    @SuppressWarnings("unchecked")
    List<T> arguments(String arg) {
        if (cfg.containsKey(arg)) {
            return (cfg.get(arg).args);
        } else {
            return null;
        }
    }

    T argument(String arg) {
        if (arguments(arg) == null) {
            return null;
        } else {
            try {
                return arguments(arg).get(0);
            } catch (IndexOutOfBoundsException e) {
                return null;
            }
        }
    }

    void writeLog(String line) {
        writeLog(line, logType.INFO);
    }

    void writeLog(String line, logType type) {
        writeLog(line, type, null);
    }

    void writeLog(String line, List<String> list) {
        writeLog(line, logType.INFO, list);
    }

    void writeLog(String line, logType type, List<String> list) {
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("[dd/MM/yy HH:mm:ss,S]");
        StringBuilder sb = new StringBuilder(sdf.format(now) + "\t[" + type.name() + "]" + (type == logType.SYSTEM ? "" : "\t") + "\t - " + line);

        if (list != null) {
            for (String elem : list) {
                sb.append("\t - ").append(elem);
                if (!elem.equalsIgnoreCase(list.get(list.size()-1))) {
                    sb.append('\n');
                }
            }
        }
        log.add(sb.toString());

        if (enabled("-l") && (type == logType.INFO || (type == logType.ERROR && argument("-l") != null))) {
            System.out.println(sb.toString());
        }
    }

    String readFullLog() {
        StringBuilder sb = new StringBuilder();
        for (String line : log) {
            sb.append(line).append("\n");
        }
        return sb.toString();
    }

    static List<String> longToString(List<Long> list) {
        List<String> newList = new LinkedList<>();
        for (Long n : list) {
            newList.add(String.valueOf(n));
        }
        return newList;
    }

    static List<String> doubleToString(List<Double> list) {
        List<String> newList = new LinkedList<>();
        for (Double n : list) {
            newList.add(String.valueOf(n));
        }
        return newList;
    }

    public enum logType {INFO, ERROR, SYSTEM}

    private static class CfgArguments<T> {
        private boolean enabled;
        private List<T> args = null;

        CfgArguments(boolean enabled) {
            this.enabled = enabled;
        }


        @SafeVarargs
        CfgArguments(boolean enabled, T... args) {
            this(enabled);
            this.args = new LinkedList<>();
            this.args.addAll(Arrays.asList(args));
        }
    }


}
