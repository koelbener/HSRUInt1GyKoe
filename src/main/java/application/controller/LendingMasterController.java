package application.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LendingMasterController extends ControllerBase {
    private static final Logger logger = LoggerFactory.getLogger(LendingMasterController.class);

    public void searchBooks(String searchText) {
        getRepository().getLoansPMod().setSearchString(searchText);

    }

}
