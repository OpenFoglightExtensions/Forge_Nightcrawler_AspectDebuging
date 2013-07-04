package ncdebugger;


def f = com.quest.forge.data.NameRegistry.getObject("ncdebugger.ExecutionEntry");

def executionsList = []

def exIt =  execution.executionNode.iterateChildren()

while (exIt.hasNext()){
	def node = exIt.next()

def n2= com.quest.forge.datasupport.util.ObjectFactory.createObject(f);

n2.set("startTimestamp",node.getStartTimestamp());
n2.set("stopTimestamp",node.getStopTimestamp());
n2.set("totalStopTimestamp",node.getTreeStopTime());
n2.set("duration",node.getDuration());
n2.set("childrenCount",node.getChildrenCount());

def treeDuration = node.getTreeStopTime().getTime() - node.getStartTimestamp().getTime();

n2.set("totalChildrenCount",node.getTreeCount());
n2.set("totalDuration",treeDuration);
n2.set("executionNode",node);
n2.set("info",node.toInfoString());
n2.set("longInfo",node.toString());

	executionsList += n2
}

return executionsList

