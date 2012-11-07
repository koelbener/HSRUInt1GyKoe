package application.controller;


public class LendingMasterController extends ControllerBase {

    public void searchBooks(String searchText) {
        getRepository().getLoansPMod().setSearchString(searchText);

    }

}
