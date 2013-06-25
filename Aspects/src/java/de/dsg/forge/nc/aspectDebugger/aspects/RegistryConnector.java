package de.dsg.forge.nc.aspectDebugger.aspects;

import de.dsg.forge.nc.aspectDebugger.SessionRegistry;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.DeclareParents;

/**
 * Created with IntelliJ IDEA.
 * User: stefan
 * Date: 24.06.13
 * Time: 19:59
 * To change this template use File | Settings | File Templates.
 */

@Aspect
public class RegistryConnector {


    public static interface SessionConnection {
        public SessionRegistry getSessionsRegistry();
    }

    public static class SessionConnectionImpl implements SessionConnection {

        @Override
        public SessionRegistry getSessionsRegistry() {
            return SessionRegistry.getSingleton();
        }
    }


    // the field type must be the introduced interface. It can't be a class.
    @DeclareParents(value="com.quest.forge.ui.web.queue.SessionQueue",defaultImpl=SessionConnectionImpl.class)
    private SessionConnection implementedInterface;
}
