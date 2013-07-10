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

def namespace = cs.getNamespaces(cartridgeName) as List


def type = com.quest.forge.data.NameRegistry.getObject("ncdebugger.NamespaceWrapper");
namespace.collect{ n->
def ns  = com.quest.forge.datasupport.util.ObjectFactory.createObject(type);
  ns.set("name",n.getResolvableName())
  ns.set("path",n.getResolvablePath())

  return ns
}
