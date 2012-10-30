package application.controller;

import application.core.Repository;

/**
 * Base class for all Controllers.
 */
public abstract class AbstractController {

    private final Repository repository;

    public AbstractController() {
        repository = Repository.getInstance();
    }

    public Repository getRepository() {
        return repository;
    }

}
