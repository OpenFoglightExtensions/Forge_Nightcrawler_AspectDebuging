package de.dsg.forge.nc.aspectDebugger;

import com.quest.forge.ui.web.queue.SessionQueue;

import de.dsg.forge.nc.aspectDebugger.data.ActiveSession;
import de.dsg.forge.nc.aspectDebugger.interfaces.iActiveSession;
import de.dsg.forge.nc.aspectDebugger.interfaces.iSessionRegistry;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: stefan
 * Date: 21.06.13
 * Time: 11:24
 * To change this template use File | Settings | File Templates.
 */
public class SessionRegistry implements iSessionRegistry {
    private static SessionRegistry _instance ;
    private HashMap<String, iActiveSession> _sessions = new HashMap<String, iActiveSession>();

    // TODO Mapp all elements to de.dsg.forge.nc.aspectDebugger.interfaces


    private static final Log LOG = LogFactory.getLog(SessionRegistry.class);
    private HashMap<SessionQueue, String> _queueMapping = new HashMap<SessionQueue, String>();

    public static SessionRegistry getSingleton() {
        LOG.debug("########################### GET SINGLETON #############################");
        if (_instance == null) {
            _instance = new SessionRegistry();
            com.quest.forge.ui.core.services.Registry.getContext().register(_instance);
            LOG.debug("########################### Registered on #############################");
            LOG.debug("###"+com.quest.forge.ui.core.services.Registry.getContext());



        }
        return _instance;  //To change body of created methods use File | Settings | File Templates.
    }



    public void addSession(ActiveSession activeSession) {
        if (_sessions.containsKey(activeSession.getInfo().getSessionId())) {
            LOG.error("Session with ID ("+activeSession.getInfo().getSessionId()+") already registered ! ");
        } else {
            _sessions.put(activeSession.getInfo().getSessionId(),activeSession);
        }

    }

    public void removeSession(String id) {
        if (_sessions.containsKey(id)) {
            _sessions.remove(id);
        } else {
            LOG.error("Session with ID wasn't found during removeSession call. (ID:"+id+")   Remove canceled.");
        }

    }

    public void registerQueue(SessionQueue queue, String sessionID) {

        if (!_queueMapping.containsKey(queue)) _queueMapping.put(queue,sessionID);
    }

    public void deregisterQueue(SessionQueue queue) {
        if (_queueMapping.containsKey(queue)) _queueMapping.remove(queue);
    }

    public String mapQueueToSessionId(SessionQueue queue) {
        if (!_queueMapping.containsKey(queue)) {
            LOG.error("Session not found ("+queue+")");
            return "-1";
        } else return _queueMapping.get(queue);
    }

    public ActiveSession mapQueueToSession(SessionQueue queue) {
        String sessionID = mapQueueToSessionId(queue);
        ActiveSession s = (ActiveSession) getSession(sessionID);
        return s;
    }

    public Collection<iActiveSession> getSessions() {
        return _sessions.values();
    }

    public  iActiveSession getSession(String sessionID) {
        if (!_sessions.containsKey(sessionID)) {
            LOG.error("Session not found for key :"+sessionID);
            return null;
        } else return _sessions.get(sessionID);
    }
}
