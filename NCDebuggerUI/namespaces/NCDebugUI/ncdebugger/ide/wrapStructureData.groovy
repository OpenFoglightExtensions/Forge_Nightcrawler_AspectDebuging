package ncdebugger.ide;

def props = src.type.getAllProperties()


def type = com.quest.forge.data.NameRegistry.getObject("ncdebugger.ide.ViewDataWrapper");
props.collect{prop ->
   try{
	def data = com.quest.forge.datasupport.util.ObjectFactory.createObject(type);
	   data.set("propName",prop.name)
	   data.set("value",src.get(prop))
	   data.set("valueType",prop.getValueType())
	   data.set("typeHelper","PLAIN")
	
	  def v = src.get(prop)
	  if (v instanceof Collection) data.set("typeHelper","LIST")
	    else if (v instanceof com.quest.forge.data.Structure)    data.set("typeHelper","STRUCT")
	return data
} catch (Throwable t) {

return null}
}.findAll { it != null } 
