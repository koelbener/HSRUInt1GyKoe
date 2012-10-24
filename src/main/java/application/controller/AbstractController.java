package application.controller;

import application.core.Repository;

import com.google.common.eventbus.EventBus;

/**
 * Base class for all Controllers.
 */
public abstract class AbstractController {

    private final EventBus eventBus;

    public AbstractController() {
        eventBus = Repository.getInstance().getEventBus();
    }

    protected EventBus getEventBus() {
        return eventBus;
    }

}
