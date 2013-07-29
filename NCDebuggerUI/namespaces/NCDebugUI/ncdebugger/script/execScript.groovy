package ncdebugger.script;

import java.io.PrintWriter;
import java.io.StringWriter;

try {

def ss = server.ScriptingService
def sd = ss.createScriptDefinition(src)
sd.name = "plainScript"
def script = sd.prepare().invocation()

script.addVariable("view",view)
//script.addVariable("ns",ns)

return ss.invoke(script)

} catch (Throwable t) {
def errorMessage = "ERROR !!!!\n------------------\n"
 StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            t.printStackTrace(pw);
errorMessage += sw.toString() +"\n"



}


