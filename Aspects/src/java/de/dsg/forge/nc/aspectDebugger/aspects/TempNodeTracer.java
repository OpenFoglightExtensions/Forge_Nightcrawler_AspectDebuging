package de.dsg.forge.nc.aspectDebugger.aspects;

import com.quest.forge.ui.web.queue.Entry;
import de.dsg.forge.nc.aspectDebugger.data.EntryExecutionNode;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: stefan
 * Date: 21.06.13
 * Time: 12:09
 * To change this template use File | Settings | File Templates.
 */
public  class TempNodeTracer {
    private HashMap<Entry,EntryExecutionNode> _nodes = new HashMap<Entry, EntryExecutionNode>();

    private static final Log LOG = LogFactory.getLog(EntryTrace.class);


    public synchronized EntryExecutionNode prepareNode(Entry entry) {
        EntryExecutionNode n = new EntryExecutionNode(entry);
        _nodes.put(entry,n);

        if(entry.getOriginatingEntry() != null) {
            EntryExecutionNode parentNode = retrieve(entry.getOriginatingEntry());

            if (parentNode == null) {
                LOG.error("Couldn't find Parent entry : "+parentNode);
            } else parentNode.linkChildNode(n);


        }
        return n;
    }

    public synchronized void dropNode(Entry entry) {
        _nodes.remove(entry);

    }

    public synchronized EntryExecutionNode retrieve(Entry entry) {
        if (_nodes.containsKey(entry))  return _nodes.get(entry);
        else {
            return null;
        }
    }
}