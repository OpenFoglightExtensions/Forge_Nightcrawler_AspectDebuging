package de.dsg.forge.nc.aspectDebugger;


import com.quest.forge.data.Type;
import com.quest.forge.ui.core.data.ConstructedType;
import com.quest.forge.ui.core.entities.View;
import com.quest.forge.ui.core.module.entitydefinitions.PropertyExpression;
import com.quest.forge.ui.core.module.viewdefinitions.ChildSpec;
import com.quest.forge.ui.core.module.viewdefinitions.CustomViewDefinition;
import com.quest.forge.ui.views.atomic.iterator.Iterator;
import com.quest.forge.ui.views.atomic.switchables.BaseSwitchable;
import com.quest.forge.ui.views.base.BaseView;
import com.quest.forge.ui.views.custom.BaseCustomView;
import com.quest.forge.ui.web.Session;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: stefan
 * Date: 26.06.13
 * Time: 14:06
 * To change this template use File | Settings | File Templates.
 */

@Aspect
public class ViewTreeAccessor {
    public static interface ViewsConnection {
        public Collection<View> getChildViews();
        public void setXXXRootView(View v);

        void setXXXCurrentView(View v);

        void setXXXCurrentIteratorMap(Map<String, View> vMap);

    }

    public static class ViewsConnectionImpl implements ViewsConnection {

        private View _view;
        private View _currentSwitchView;
        private Map<String, View> _iteratorMap;

        @Override
        public Collection<View> getChildViews() {

            if (_view instanceof Iterator) return getIteratorChildViews() ;
            if (_view instanceof BaseSwitchable) return getChildViewsSwitch();
            if (_view instanceof BaseCustomView) return getChildViewsCustom((BaseCustomView) _view);

            return getChildViews(_view);
        }

        private Collection<View> getIteratorChildViews() {
            LinkedList<View> erg = new LinkedList<View>();
            if (_iteratorMap != null) erg.addAll(_iteratorMap.values());
            return erg;  //To change body of created methods use File | Settings | File Templates.
        }

        private Collection<View> getChildViewsSwitch() {
            LinkedList<View> erg = new LinkedList<View>();
            if (_currentSwitchView != null) erg.add(_currentSwitchView);
            return erg;  //To change body of created methods use File | Settings | File Templates.
        }

        @Override
        public void setXXXRootView(View v) {
             _view = v;
        }

        @Override
        public void setXXXCurrentView(View v) {

            _currentSwitchView = v;
        }

        @Override
        public void setXXXCurrentIteratorMap(Map<String, View> vMap) {
            _iteratorMap = vMap;
        }



        private Collection<View> getChildViews(BaseView view) {
            return new LinkedList<View>();
        }

        private Collection<View> getChildViews(Object view) {
            return new LinkedList<View>();
        }


        //for (ChildSpec spec : definition.getChildSpecs()) {
//        View child = children.get(spec.getName());

        private Collection<View> getChildViewsCustom( BaseCustomView view) {

            LinkedList<View> erg = new LinkedList<View>();


            Type type = view.getType();
            CustomViewDefinition definition = (CustomViewDefinition)
                    (type instanceof ConstructedType ? type.getSuperType() : type);

            for (ChildSpec spec : definition.getChildSpecs()) {
                View child = view.getChild(spec.getName());
                erg.add(child);

            }

            return erg ;
        }



    }


    // the field type must be the introduced interface. It can't be a class.
    @DeclareParents(value="com.quest.forge.ui.views.base.BaseView",defaultImpl=ViewsConnectionImpl.class)
    private ViewsConnection implementedInterface;

    @After("execution (com.quest.forge.ui.views.base.BaseView.new(..)) && this(v)")
    public void registerView(View v){
        ((ViewsConnection)v).setXXXRootView(v);
    }

    @After("set(* com.quest.forge.ui.views.atomic.switchables.BaseSwitchable.current)&& args(vTarget)&& this (v)")
    public void registerCurrentView(View vTarget, View v){
        ((ViewsConnection)v).setXXXCurrentView(vTarget);

    }

    @After (" set(* com.quest.forge.ui.views.atomic.iterator.Iterator.children) && args(vMap) && this(v)")
    public void registerIteratorMap(Map<String,View> vMap, View v){
        ((ViewsConnection)v).setXXXCurrentIteratorMap(vMap);

    }

    @Around("call (* org.apache.commons.logging.impl.Log4JLogger.warn(..))")
    public void warnWrapper(ProceedingJoinPoint thisJoinPoint) {


        try {
            thisJoinPoint.proceed();
        }  catch (Throwable t ) {
            System.out.println("Catching  Log Error in VIEW"+t.getMessage());
            t.printStackTrace();

        }
    }

}

