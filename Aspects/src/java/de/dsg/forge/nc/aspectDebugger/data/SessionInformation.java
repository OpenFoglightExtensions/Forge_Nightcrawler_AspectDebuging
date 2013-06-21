package de.dsg.forge.nc.aspectDebugger.data;

import java.sql.Timestamp;

/**
 * Created with IntelliJ IDEA.
 * User: stefan
 * Date: 20.06.13
 * Time: 23:19
 * To change this template use File | Settings | File Templates.
 */
public class SessionInformation {
    private final Timestamp _createdTimestamp;
    private final String _sessionId;
    private final String _mainViewId;
    private final String _username;

    public Timestamp getCreatedTimestamp() {
        return _createdTimestamp;
    }

    public String getSessionId() {
        return _sessionId;
    }

    public String getMainViewId() {
        return _mainViewId;
    }

    public String getUsername() {
        return _username;
    }

    public SessionInformation(String sessionID, String viewID, String username, long createTime) {

        _sessionId = sessionID;
        _mainViewId = viewID;
        _username=username;
        _createdTimestamp = new Timestamp(createTime);



    }

    @Override
    public String toString() {
        StringBuffer erg = new StringBuffer();

        erg.append("Session ID:"+_sessionId+"\n");
        erg.append("User: "+_username +" on View "+_mainViewId+"\n");
        erg.append("Created :"+_createdTimestamp+"\n");

        return erg.toString();

    }
}
