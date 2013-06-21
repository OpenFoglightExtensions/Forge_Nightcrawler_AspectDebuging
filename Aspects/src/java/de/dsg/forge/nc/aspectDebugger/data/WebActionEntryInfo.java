package de.dsg.forge.nc.aspectDebugger.data;

import java.lang.reflect.Method;

/**
 * Created with IntelliJ IDEA.
 * User: stefan
 * Date: 21.06.13
 * Time: 09:54
 * To change this template use File | Settings | File Templates.
 */
public class WebActionEntryInfo extends EntryInfo {
    private final String _path  ;
    private final Method _method   ;

    public WebActionEntryInfo(String path, Method method) {
        super();
        _path = path;
        _method = method;

    }

    @Override
    public String toString() {

        return "Calling "+  _method.getName() + " on "+_method.getDeclaringClass().getName()  +" : called from "+_path;

    }
}
