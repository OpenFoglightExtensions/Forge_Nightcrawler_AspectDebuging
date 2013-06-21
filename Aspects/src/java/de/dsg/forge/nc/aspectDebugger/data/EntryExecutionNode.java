package de.dsg.forge.nc.aspectDebugger.data;

import com.quest.forge.ui.web.queue.Entry;

import java.sql.Timestamp;
import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: stefan
 * Date: 21.06.13
 * Time: 12:13
 * To change this template use File | Settings | File Templates.
 */
public class EntryExecutionNode {
    private final LinkedList<EntryExecutionNode> _children;
    private Entry _entry;
    private long _start = 0L;
    private long _stop = 0L;
    private boolean _done = false;
    private String _entryDetailedInfos ="";
    private String _entryInfo = "";
    private EntryExecutionNode _parent;

    public EntryExecutionNode(Entry entry) {
        _entry = entry;
        _entryInfo = entry.toString();
        _children = new LinkedList<EntryExecutionNode>();
    }


    public int getChildrenCount() {
        return _children.size();
    }


    public void startExecution() {
        _start = System.currentTimeMillis();
    }

    public Timestamp getStartTimestamp() {
        return new Timestamp(_start);
    }
    public Timestamp getStopTimestamp() {
        return new Timestamp(_start);
    }

    public void stopExecution() {
        _stop = System.currentTimeMillis();
        _done = true;
        _entry = null;
    }

    public boolean isDone() {return _done ;}

    public long getDuration () {
        return (isDone()?(_stop-_start):-1);
    }



    public void setEntryDetails(String entryInfoString) {
        _entryDetailedInfos = entryInfoString;
    }

    @Override
    public String toString() {

        StringBuffer sb = new StringBuffer(_entryInfo);

        sb.append("\n")
                .append(_entryDetailedInfos)
                .append("\n Execution: ")
                .append(isDone()?"done":"waiting")
                .append("\n")
                .append("Children :")
                .append(getChildrenCount())
                .append("  Tree Children :")
                .append(getTreeCount())
                .append("\n")
                .append(getDuration())
                .append(getStartTimestamp().toString())
                .append("-->")
                .append(getStopTimestamp());
        return sb.toString();

    }

    private int getTreeCount() {
        int count = 1;
        for (EntryExecutionNode n : _children) {
            count += n.getTreeCount();
        }

        return count;
    }

    public synchronized void linkChildNode(EntryExecutionNode n) {
        n.setParent(this);
        _children.add(n);
    }

    public EntryExecutionNode getParent() {
        return _parent;
    }

    public synchronized void  setParent(EntryExecutionNode parent) {
        this._parent = parent;
    }

}