package application.controller;

import application.core.Repository;

import com.google.common.eventbus.EventBus;

/**
 * Base class for all Controllers.
 */
public abstract class AbstractController {

    private final EventBus eventBus;
    private final Repository repository;

    public AbstractController() {
        repository = Repository.getInstance();
        eventBus = repository.getEventBus();
    }

    protected EventBus getEventBus() {
        return eventBus;
    }

    public Repository getRepository() {
        return repository;
    }

}
