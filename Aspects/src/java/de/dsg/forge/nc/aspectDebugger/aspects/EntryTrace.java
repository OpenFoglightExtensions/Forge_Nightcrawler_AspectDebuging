package de.dsg.forge.nc.aspectDebugger.aspects;

import com.quest.forge.ui.core.actions.Action;

import com.quest.forge.ui.core.entities.View;
import com.quest.forge.ui.web.queue.Entry;
import com.quest.forge.ui.web.queue.SessionQueue;
import de.dsg.forge.nc.aspectDebugger.SessionRegistry;
import de.dsg.forge.nc.aspectDebugger.ViewTreeAccessor;
import de.dsg.forge.nc.aspectDebugger.data.*;

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
    private HashMap<Entry, EntryInfo> _entryInfos = new HashMap<Entry, EntryInfo>();
    private TempNodeTracer _tracedNodes = new TempNodeTracer();

    @Pointcut("execution (* com.quest.forge.ui.web.queue.WorkerThread.executeEntry(..)) && args (entry)")
    public void pc_executeEntry(Entry entry){}

    @Pointcut("execution (* com.quest.forge.ui..*Entry.execute()) && this (entry)")
    public void pc_executeEntry2(Entry entry){}


    @Around( "pc_executeEntry(entry)")
    public Object traceEntryExecution(ProceedingJoinPoint thisJoinPoint ,Entry entry) throws Throwable {
        EntryExecutionNode node = null;
        try {
            node = preProcessEntryExecution(entry);
        }  catch (Throwable t ) {
            System.out.println("Catching  (pre) Aspects Error");

        }




        Object result = null;

        try {
            result = thisJoinPoint.proceed();
        }  catch (Throwable t ) {
            System.out.println("Catching  WCF Error");
            t.printStackTrace();
            throw(t);
        }

        try {
            if (node != null) postProcessEntryExecution(entry, node);
        }  catch (Throwable t ) {
            System.out.println("Catching  (Post) Aspects Error");
            t.printStackTrace();

        }


        return result;

    }

    private EntryExecutionNode preProcessEntryExecution(Entry entry) {
        // pre call

        EntryExecutionNode node;
        node = _tracedNodes.retrieve(entry);

        if (node == null) {
            node = _tracedNodes.prepareNode(entry);

        }

        node.startExecution();
        return node;
    }

    private void postProcessEntryExecution(Entry entry, EntryExecutionNode node) {
        //post Call

        if (node != null){
             node.stopExecution();
             // Add Entry Information
             node.setEntryDetails(getEntryInfoString(entry));

              if (node.isRoot()) {
 //                 System.out.println("RootNode:"+node);
 //                 System.out.println("------------------");
 //                 System.out.println(node.dumpTreeStructure());

                  // This is a root node, we can drop the node now because all childs have been added during execution !!!
                    _tracedNodes.dropNode(entry);
                  SessionRegistry sessionRegistry = SessionRegistry.getSingleton();
                  String sessionId = sessionRegistry.mapQueueToSessionId(entry.getEnvironment().getSessionQueue());

                  // TODO fully implement de.dsg.forge.nc.aspectDebugger.interfaces
                  ActiveSession session = (ActiveSession) sessionRegistry.getSession(sessionId);
                  if (session != null) {
                      if (!session.hasExecution(node)) {
                          session.addExecutionNode(node);
                      }

                  }
              } else {

                  EntryExecutionNode root = node.getRootNode();
                  // no longer need to store the entry relationship. Also unlink the node structure from local cache.
                  // Nodes will still be linked as child nodes in their Root. Which then is linked with the session

                  // TODO Check if root is registered
                  ActiveSession session = SessionRegistry.getSingleton().mapQueueToSession(entry.getEnvironment().getSessionQueue());
                  if (!session.hasExecution(root)) {

                    session.addExecutionNode(root);

                  }

                     _tracedNodes.dropNode(entry);

                  if (root.isTreeDone()) {


                  }
              }



         }

        // Finish Execution
        if (_entryInfos.containsKey(entry)) {
            _entryInfos.remove(entry);
        }
    }



    private String getEntryInfoString(Entry entry) {


        StringBuffer dumpTxt = new StringBuffer(entry.toString())
                .append("\n");

        if (_entryInfos.containsKey(entry)) {
            dumpTxt.append(" Info:")
                    .append(_entryInfos.get(entry))
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

    // && this(entry) && args(action
    @After ("execution (com.quest.forge.ui.web.queue.ActionEntry.new(..))&& this(entry) && args(action,..) ")
    public void fillActionDetails (Entry entry,Action action) {

        ActionEntryInfo info;

        info = new ActionEntryInfo(action);
        _entryInfos.put(entry,info);
    }

    @After ("!within (com.quest.forge.ui.web.BaseEntry)" +
            "&&execution(com.quest.forge.ui.*..*Entry.new(..)) && this(entry)")
    public void entryCreated  (JoinPoint p, Entry entry) {

        _tracedNodes.prepareNode(entry);
    }




    @Pointcut("call( * *..*.remove(..)) && args(arg)")
    void pcListRemove(Entry arg) { }




    @Before("withincode(* com.quest.forge.ui.web.queue.SessionQueue.append(..)) && pcListRemove(arg) && this(queue)")
    public void entryRemoved(SessionQueue queue,Entry arg) {

        try {
            _tracedNodes.dropNode(arg);
        }   catch (Throwable t) {}
    }


    //TODO: add More Details for "queue" Nodes details to Execution Tracer

    @Around("call (* org.apache.commons.logging.impl.Log4JLogger.warn(..))")
    public void warnWrapper(ProceedingJoinPoint thisJoinPoint) {


        try {
            thisJoinPoint.proceed();
        }  catch (Throwable t ) {
            System.out.println("Catching  Log Error in WEB "+t.getMessage());
            t.printStackTrace();

        }
    }



}
