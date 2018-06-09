package it.polimi.se2018;
import it.polimi.se2018.model.BoardList;
import org.junit.Test;


public class TestJSON {

    @Test
    public void testJson(){
        BoardList.loadJSONBoards();
    }
}
