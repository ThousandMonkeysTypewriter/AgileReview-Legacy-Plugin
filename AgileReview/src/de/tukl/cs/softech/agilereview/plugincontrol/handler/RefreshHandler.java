package de.tukl.cs.softech.agilereview.plugincontrol.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import de.tukl.cs.softech.agilereview.dataaccess.ReviewAccess;
import de.tukl.cs.softech.agilereview.tools.PluginLogger;

/**
 * Handler for the "refresh" (F5) command for our review
 */
public class RefreshHandler extends AbstractHandler {
    
    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        PluginLogger.log(RefreshHandler.class.toString(), "execute", "Refresh triggered");
        ReviewAccess.getInstance().doGlobalRefresh();
        // Return must be null (see API)
        return null;
    }
    
}