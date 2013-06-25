package de.dsg.forge.nc.aspectDebugger.data;

import de.dsg.forge.nc.aspectDebugger.interfaces.iActiveSession;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: stefan
 * Date: 21.06.13
 * Time: 11:17
 * To change this template use File | Settings | File Templates.
 */
public class ActiveSession implements iActiveSession {
    private final SessionInformation _info;
    private LinkedList<EntryExecutionNode> _executions = new LinkedList<EntryExecutionNode>();

    public SessionInformation getInfo() {
        return _info;
    }

    public Timestamp getCreationTimestamp() {
        return getInfo().getCreatedTimestamp()  ;
    }

    public ActiveSession(SessionInformation sessionInfo) {
        _info = sessionInfo;


    }

    public void addExecutionNode(EntryExecutionNode node) {
        _executions.add(node);
        checkMaxExecutions();
    }

    private void checkMaxExecutions() {
        int max = Config.getSessionExecutionLimit();
       while (_executions.size() >= max) {
           _executions.removeFirst();
       }
    }

    public List<EntryExecutionNode> getExecutions() {
       return Collections.unmodifiableList(_executions);
    }

    public int getExecutionsCount() {
        return _executions.size();
    }


    public void clearExecutions() {
        _executions.clear();
    }

    public boolean hasExecution(EntryExecutionNode root) {
        return _executions.contains(root);
    }
}
