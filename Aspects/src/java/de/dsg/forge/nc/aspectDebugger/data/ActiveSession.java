package de.dsg.forge.nc.aspectDebugger.data;

import java.sql.Timestamp;

/**
 * Created with IntelliJ IDEA.
 * User: stefan
 * Date: 21.06.13
 * Time: 11:17
 * To change this template use File | Settings | File Templates.
 */
public class ActiveSession {
    private final SessionInformation _info;

    public SessionInformation getInfo() {
        return _info;
    }

    public Timestamp getCreationTimestamp() {
        return getInfo().getCreatedTimestamp()  ;
    }

    public ActiveSession(SessionInformation sessionInfo) {
        _info = sessionInfo;


    }
}
