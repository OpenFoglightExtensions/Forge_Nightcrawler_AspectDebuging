package de.dsg.forge.nc.aspectDebugger.data;

import com.quest.forge.ui.core.actions.Action;

/**
 * Created with IntelliJ IDEA.
 * User: stefan
 * Date: 23.06.13
 * Time: 19:35
 * To change this template use File | Settings | File Templates.
 */
public class   ActionEntryInfo extends EntryInfo {
    private final Action _action;

    public ActionEntryInfo(Action action) {
      _action = action;
    }

    @Override
    public String toString() {
        return "("+ _action.getClass().getSimpleName()+") ";
    }
}
