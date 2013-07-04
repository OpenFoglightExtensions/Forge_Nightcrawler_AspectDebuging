package de.dsg.forge.nc.aspectDebugger;

import com.quest.forge.ui.web.queue.SessionQueue;

import de.dsg.forge.nc.aspectDebugger.data.ActiveSession;
import de.dsg.forge.nc.aspectDebugger.interfaces.iActiveSession;
import de.dsg.forge.nc.aspectDebugger.interfaces.iSessionRegistry;


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


    private HashMap<SessionQueue, String> _queueMapping = new HashMap<SessionQueue, String>();

    public static SessionRegistry getSingleton() {
        if (_instance == null) {
            _instance = new SessionRegistry();




        }
        return _instance;  //To change body of created methods use File | Settings | File Templates.
    }



    public void addSession(ActiveSession activeSession) {
        if (!_sessions.containsKey(activeSession.getInfo().getSessionId()))  {
            _sessions.put(activeSession.getInfo().getSessionId(),activeSession);
        }

    }

    public void removeSession(String id) {
        if (_sessions.containsKey(id)) {
            _sessions.remove(id);
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

            return null;
        } else return _sessions.get(sessionID);
    }
}
