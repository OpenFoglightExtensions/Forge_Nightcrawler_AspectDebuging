package ncdebugger;

def f = com.quest.forge.data.NameRegistry.getObject("ncdebugger.ViewWrapper");


def wv = com.quest.forge.datasupport.util.ObjectFactory.createObject(f);
wv.set("name",view.type.toString())
wv.set("id",view.id)
wv.set("view",view)

return wv


