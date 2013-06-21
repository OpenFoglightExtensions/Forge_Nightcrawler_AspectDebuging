package de.dsg.forge.nc.aspectDebugger.aspects;

import com.quest.forge.ui.web.queue.Entry;
import com.quest.forge.ui.web.queue.SessionQueue;
import de.dsg.forge.nc.aspectDebugger.SessionRegistry;
import de.dsg.forge.nc.aspectDebugger.data.EntryExecutionNode;
import de.dsg.forge.nc.aspectDebugger.data.EntryInfo;
import de.dsg.forge.nc.aspectDebugger.data.WebActionEntryInfo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: stefan
 * Date: 21.06.13
 * Time: 08:29
 * To change this template use File | Settings | File Templates.
 */
@Aspect
public class EntryTrace {
    private static final Log LOG = LogFactory.getLog(EntryTrace.class);
    private HashMap<Entry, EntryInfo> _entryInfos = new HashMap<Entry, EntryInfo>();
    private TempNodeTracer _tracedNodes = new TempNodeTracer();

    @Pointcut("call (* com.quest.forge.ui..WorkerThread.executeEntry(..)) && args (entry)")
    public void pc_executeEntry(Entry entry){}

    @Pointcut("execution (* com.quest.forge.ui..*Entry.execute()) && this (entry)")
    public void pc_executeEntry2(Entry entry){}


    @Around( "pc_executeEntry2(entry)")
    public Object traceEntryExecution(ProceedingJoinPoint thisJoinPoint ,Entry entry) {
        // pre call

        EntryExecutionNode node;
        node = _tracedNodes.retrieve(entry);

        if (node == null) {
            LOG.error("Node not TRACED (or not found) ... creating one now");
            node = _tracedNodes.prepareNode(entry);
        } else   {
            node.startExecution();
        }


        Object result = null;
        try {
           result = thisJoinPoint.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


        //post Call

        if (node != null){
            node.stopExecution();


            // TODO trace tree and fill in (if parent is available)
            // TODO make sur eto delete nodes prevent memleaks



            // Add Entry Information
            node.setEntryDetails(getEntryInfoString(entry));


             if (entry.getOriginatingEntry() == null) {
                 System.out.println("RootNode:"+node);
             }



        }

        // Finish Execution
        if (_entryInfos.containsKey(entry)) {
            _entryInfos.remove(entry);
        }


        return result;

    }

    private String getEntryInfoString(Entry entry) {

        String parent = (entry.getOriginatingEntry() != null)?entry.getOriginatingEntry().toString():"root";

        StringBuffer dumpTxt = new StringBuffer(parent);
        dumpTxt.append("  -->  ")
                .append(entry.toString())
                .append("\n");

        if (_entryInfos.containsKey(entry)) {
            dumpTxt.append(" Info:")
                    .append(_entryInfos)
                    .append("\n");
        }

        return dumpTxt.toString();

    }




    @Pointcut ("call (*  com.quest.forge.ui.web.SocketConnection.handleAction(..)) && args(map)")
    void actionHandle(Map<?, ?> map){}
    // fill in some Details to by-pass access limitations
    @Before( "cflowbelow ( actionHandle(map) ) &&  execution (com.quest.forge.ui.web.ActionEntry.new(..)) && this(entry) && args(method,child,a,env)")
    public void fillDetails(  Entry entry,
                             java.lang.reflect.Method method,
                             Object child,  Object a,
                             com.quest.forge.ui.web.queue.QueuedEnvironment env,
                             Map<?,?> map) {


        WebActionEntryInfo info = new WebActionEntryInfo((String) map.get("path"),method);
        _entryInfos.put(entry,info);
    }

    @After ("!within (com.quest.forge.ui.web.BaseEntry)" +
            "&&execution(com.quest.forge.ui.*..*Entry.new(..)) && this(entry)")
    public void entryCreated  (JoinPoint p, Entry entry) {

        _tracedNodes.prepareNode(entry);
    }

    @Pointcut( "execution (* com.quest.forge.ui.web.queue.SessionQueue.append(..))")
    void pcSessionQueueAppend(){}

    @Pointcut("call( * java.util.LinkedList.add(..)) && args(arg)")
    void pcListAdd(Entry arg) { }

    @Pointcut("call( * java.util.LinkedList.remove(..)) && args(arg)")
    void pcListRemove(Entry arg) { }


//    doesn't catch all node, switch to constructor bounds
//    @Before("withincode(* com.quest.forge.ui.web.queue.SessionQueue.append(..)) && pcListAdd(arg) && this(queue)")
//    public void entryAdded(SessionQueue queue,Entry arg) {
//
//        String sessionId = SessionRegistry.getSingleton().mapQueueToSessionId(queue);
//        if (sessionId.equals("-1")) {
//            LOG.error("No Session found. aborting");
//            return;
//        }
//       _tracedNodes.prepareNode(sessionId,arg);
//    }

    @Before("withincode(* com.quest.forge.ui.web.queue.SessionQueue.append(..)) && pcListRemove(arg) && this(queue)")
    public void entryRemoved(SessionQueue queue,Entry arg) {
        _tracedNodes.dropNode(arg);
    }

    //TODO: Trace Global Nodes

    //TODO: add details to Execution Tracer
    //TODO: use global Registry to track SessionNodes (within Session Details, implement a max number of Sessions and allow pinned Nodes)




}