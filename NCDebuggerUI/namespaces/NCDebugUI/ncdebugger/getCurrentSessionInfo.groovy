package ncdebugger;

while (!view.class.name.contains("com.quest.forge.ui.web.Session")) {
view = view.getViewContainer();
}


def f = com.quest.forge.data.NameRegistry.getObject("ncdebugger.SessionInfo");

def queue = view.queue
def session = queue.getSessionsRegistry().mapQueueToSession(queue)




def si = session.info

def sessionInfo = com.quest.forge.datasupport.util.ObjectFactory.createObject(f);
sessionInfo.set("username",si.getUsername())
sessionInfo.set("sessionId",si.getSessionId())
sessionInfo.set("createdTimestamp",si.getCreatedTimestamp())
sessionInfo.set("mainView",si.getMainViewId() )
sessionInfo.set("session",si.getSession())
sessionInfo.set("activeSession",session)

sessionInfo.set("lastExecution",null)
if (session.executions.size() >0)  sessionInfo.set("lastExecution",session.executions.last().getStartTimestamp())

return sessionInfo 
