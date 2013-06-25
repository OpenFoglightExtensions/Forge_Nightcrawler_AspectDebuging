package de.dsg.forge.nc.aspectDebugger.interfaces;



import java.util.Collection;

/**
 * Created with IntelliJ IDEA.
 * User: stefan
 * Date: 24.06.13
 * Time: 16:54
 * To change this template use File | Settings | File Templates.
 */
public interface iSessionRegistry {
    public Collection<iActiveSession> getSessions();

    public  iActiveSession getSession(String sessionID);

}
