package suites;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import application.view.mainView.BookDetailMainViewTest;
import application.view.mainView.BookDetailMainViewTestForIntegrity;
import application.view.subView.BookMasterSubViewTest;
import application.view.subView.LendingMasterSubViewTest;

@RunWith(Suite.class)
@SuiteClasses({
//
        BookDetailMainViewTest.class, //
        BookMasterSubViewTest.class, //
        LendingMasterSubViewTest.class, //
        BookDetailMainViewTestForIntegrity.class //
})
public class SwingTestSuite {

}
