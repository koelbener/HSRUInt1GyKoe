package application.controller;

import application.core.Repository;

/**
 * Base class for all Controllers.
 */
public abstract class ControllerBase {

    private final Repository repository;

    public ControllerBase() {
        repository = Repository.getInstance();
    }

    public Repository getRepository() {
        return repository;
    }

}
