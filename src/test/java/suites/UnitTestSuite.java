package suites;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import application.view.helper.EnableCompontentOnTableSelectionListenerTest;
import domain.BookTest;
import domain.CopyTest;
import domain.CustomerTest;
import domain.LibraryTest;
import domain.LoanTest;
import domain.validator.BookValidatorTest;

@RunWith(Suite.class)
@SuiteClasses({
//
        EnableCompontentOnTableSelectionListenerTest.class, //
        BookTest.class, //
        CopyTest.class, //
        CustomerTest.class, //
        LibraryTest.class, //
        LoanTest.class, //
        BookValidatorTest.class //
})
public class UnitTestSuite {

}
