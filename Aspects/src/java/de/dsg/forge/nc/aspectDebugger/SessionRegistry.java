package de.dsg.forge.nc.aspectDebugger;

import com.quest.forge.ui.web.queue.SessionQueue;
import de.dsg.forge.nc.aspectDebugger.data.ActiveSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: stefan
 * Date: 21.06.13
 * Time: 11:24
 * To change this template use File | Settings | File Templates.
 */
public class SessionRegistry {
    private static SessionRegistry _instance ;
    private HashMap<String, ActiveSession> _sessions = new HashMap<String, ActiveSession>();


    private static final Log LOG = LogFactory.getLog(SessionRegistry.class);
    private HashMap<SessionQueue, String> _queueMapping = new HashMap<SessionQueue, String>();

    public static SessionRegistry getSingleton() {

        if (_instance == null) _instance = new SessionRegistry();
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
        ActiveSession s = getSession(sessionID);
        return s;
    }

    public  ActiveSession getSession(String sessionID) {
        if (!_sessions.containsKey(sessionID)) {
            LOG.error("Session not found for key :"+sessionID);
            return null;
        } else return _sessions.get(sessionID);
    }
}
