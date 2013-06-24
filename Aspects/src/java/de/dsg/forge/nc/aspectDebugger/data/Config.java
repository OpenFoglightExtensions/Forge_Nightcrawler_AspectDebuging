package de.dsg.forge.nc.aspectDebugger.data;

/**
 * Created with IntelliJ IDEA.
 * User: stefan
 * Date: 24.06.13
 * Time: 11:47
 * To change this template use File | Settings | File Templates.
 */
public class Config {
    public static int getSessionExecutionLimit() {

        return convertToInt(System.getProperty("de.dsg.forge.sessionTracer.maxTracedExecutions","200"),200);
    }

    private static int convertToInt(String property, int i) {
        int value = i;
        try {
            value = Integer.parseInt(property);

        } catch (NumberFormatException ex) {}

        return value;
    }

}
