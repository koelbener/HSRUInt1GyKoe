package application.data;

import domain.Library;

public interface DataLoder {

    Library loadLibrary() throws Exception;

}
