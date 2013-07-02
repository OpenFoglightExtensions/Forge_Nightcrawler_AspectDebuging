package ncdebugger.script;


import com.quest.forge.ui.core.services.Registry;
import com.quest.forge.ui.core.services.ScriptService;
import com.quest.forge.ui.core.services.*
import com.quest.forge.ui.core.data.*
import com.quest.forge.ui.core.module.namespace.*
import com.quest.forge.ui.core.data.editable.*;
import com.quest.forge.ui.core.services.script.*
import javax.script.*;
import java.io.PrintWriter;
import java.io.StringWriter;


def cs = Registry.getInstance(CartridgeService.class)

def namespace = cs.getNamespaces("NCDebugUI").toArray()[0]
ScriptInfo info = new ScriptInfo(namespace, "dynamicScript",
				ScriptOrigin.FUNCTION);


Bindings bindings = new SimpleBindings();
bindings.put("_view",view);
bindings.put("_obj",obj);




// Execute

try {

//return de.dsg.forge.nc.aspectDebugger.SessionRegistry.class.getName()
return Registry.getInstance(ScriptService.class).eval(src, info, bindings);

} catch (Throwable t) {
def errorMessage = "ERROR !!!!\n------------------\n"
 StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            t.printStackTrace(pw);
errorMessage += sw.toString() +"\n"



}


