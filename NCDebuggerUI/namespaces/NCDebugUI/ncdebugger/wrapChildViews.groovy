package ncdebugger;

def f = com.quest.forge.data.NameRegistry.getObject("ncdebugger.ViewWrapper");


def listOfViews = view.childViews.collect{ v->
def wv = com.quest.forge.datasupport.util.ObjectFactory.createObject(f);
wv.set("name",v.type.toString())
wv.set("id",v.id)
wv.set("view",v)
wv.set("viewType",v.type)

return wv
}


return listOfViews ;


