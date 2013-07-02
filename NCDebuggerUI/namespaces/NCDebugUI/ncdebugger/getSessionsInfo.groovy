package ncdebugger;

while (!view.class.name.contains("com.quest.forge.ui.web.Session")) {
view = view.getViewContainer();
}



def sr = view.getQueue().getSessionsRegistry()

def f = com.quest.forge.data.NameRegistry.getObject("ncdebugger.SessionInfo");
def list = sr.sessions.info.collect {si ->


def sessionInfo = com.quest.forge.datasupport.util.ObjectFactory.createObject(f);
sessionInfo.set("username",si.getUsername())
sessionInfo.set("sessionId",si.getSessionId())
sessionInfo.set("createdTimestamp",si.getCreatedTimestamp())
sessionInfo.set("mainView",si.getMainViewId() )
sessionInfo.set("session",si.getSession())


return sessionInfo 
}




def l = com.quest.forge.datasupport.util.ObjectFactory.createDataList(f);
l.addAll(list)
return l