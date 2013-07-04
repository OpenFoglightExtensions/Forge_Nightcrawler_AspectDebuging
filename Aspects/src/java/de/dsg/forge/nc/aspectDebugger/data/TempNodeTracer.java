package de.dsg.forge.nc.aspectDebugger.data;

import com.quest.forge.ui.web.queue.Entry;
import de.dsg.forge.nc.aspectDebugger.aspects.EntryTrace;
import de.dsg.forge.nc.aspectDebugger.data.EntryExecutionNode;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: stefan
 * Date: 21.06.13
 * Time: 12:09
 * To change this template use File | Settings | File Templates.
 */
public  class TempNodeTracer {
    private HashMap<Entry,EntryExecutionNode> _nodes = new HashMap<Entry, EntryExecutionNode>();



    public synchronized EntryExecutionNode prepareNode(Entry entry) {


        EntryExecutionNode n = new EntryExecutionNode(entry);
        _nodes.put(entry,n);

        if(entry.getOriginatingEntry() != null) {
            EntryExecutionNode parentNode = retrieve(entry.getOriginatingEntry());

            // TODO Maybe we need to link the parent Node to the Session :-)

            if (parentNode == null) {
                parentNode = this.prepareNode(entry.getOriginatingEntry());

            } else parentNode.linkChildNode(n);


        }
        return n;
    }

    public synchronized void dropNode(Entry entry) {
        if(_nodes.containsKey(entry)) _nodes.remove(entry);

    }

    public synchronized EntryExecutionNode retrieve(Entry entry) {
        if (_nodes.containsKey(entry))  return _nodes.get(entry);
        else {
            return null;
        }
    }

    public void dropNode(EntryExecutionNode node) {

        LinkedList<Entry> entriesFound = new LinkedList<Entry>();

        for (Map.Entry<Entry,EntryExecutionNode> e : _nodes.entrySet()) {
            if (e.getValue() == node) {
                entriesFound.add(e.getKey());
            }
        }

        for (Entry e:entriesFound) _nodes.remove(e);

    }
}
