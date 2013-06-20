package de.dsg.forge.nc.aspectDebugger.aspects;

import com.quest.forge.ui.web.queue.Entry;
import com.quest.wcf.core.component.Session;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;


/**
 * Created with IntelliJ IDEA.
 * User: stefan
 * Date: 20.06.13
 * Time: 10:32
 * To change this template use File | Settings | File Templates.
 */
@Aspect
public class PlainTracer {

    @Pointcut ("within(com.quest.wcf.core..*) || within(com.quest.forge.ui..*)")
    void wcfCoreClasses() {}


    @Pointcut ("call (* com.quest.forge.ui..WorkerThread.executeEntry(..)) && args (entry)")
    public void pc_executeEntry(Entry entry){}


    @Around( "pc_executeEntry(entry)")
    public Object wrapEntryExecution(ProceedingJoinPoint thisJoinPoint ,Entry entry) {
        System.out.println("Caught simple ExecuteEntry!!! "+thisJoinPoint.getSignature());
        System.out.println(":"+thisJoinPoint.getSourceLocation());
        System.out.println(":"+thisJoinPoint.getStaticPart().getSignature());
        System.out.println(":"+thisJoinPoint.getTarget());
        System.out.println("C:"+entry.getClass().getName());
        System.out.println("E:"+entry);
        try {
            return thisJoinPoint.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        }
    }


}
