package it.polimi.se2018;
import it.polimi.se2018.model.BoardList;
import org.junit.Test;

/**
 * Json loading test
 * @author  Alessio
 */
public class TestJSON {

    @Test
    public void testJson(){
        BoardList.loadJSONBoards();
    }
}
