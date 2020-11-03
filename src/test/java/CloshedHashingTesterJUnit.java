import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.function.Function;

public class CloshedHashingTesterJUnit {
    Function<Integer, Integer> fn =  (Integer n) -> {return n;};

    //Insertado normalmente
    @Test
    void testGoodInsert(){
        ClosedHashing<Integer,String> ch = new ClosedHashing<>(fn);
        ch.insert(56,  "Test");
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        ch.dump();
        String cleanOutPut = outContent.toString().replaceAll("[\n,\r]", "");
        Assertions.assertEquals("slot 0 is emptyslot 1 contains key=56 value=Testslot 2 is emptyslot 3 is emptyslot 4 is empty",cleanOutPut);
    }

    //Borrado normalmente
    @Test
    void testGoodDelete() {
        ClosedHashing<Integer, String> ch = new ClosedHashing<>(fn);
        ch.insert(45, "Test");
        ch.insert(46, "Test2");
        ch.delete(46);
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        ch.dump();
        String cleanOutPut = outContent.toString().replaceAll("[\n,\r]", "");
        Assertions.assertEquals("slot 0 contains key=45 value=Testslot 1 is emptyslot 2 is emptyslot 3 is emptyslot 4 is empty",cleanOutPut);
    }

    //Borrar un elemento que no esta en su index natural
    @Test
    void testDelete2(){
        ClosedHashing<Integer, String> ch = new ClosedHashing<>(fn);
        ch.insert(45, "Test");
        ch.insert(65, "Test2");
        ch.delete(65);
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        ch.dump();
        String cleanOutPut = outContent.toString().replaceAll("[\n,\r]", "");
        Assertions.assertEquals("slot 0 contains key=45 value=Testslot 1 is emptyslot 2 is emptyslot 3 is emptyslot 4 is empty",cleanOutPut);
    }

    //Insertar varios elementos con la misma key
    @Test
    void InsertionsWithSameKey(){
        ClosedHashing<Integer, String> ch = new ClosedHashing<>(fn);
        ch.insert(45, "Test");
        ch.insert(65, "Test2");
        ch.insert(5, "Test3");
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        ch.dump();
        String cleanOutPut = outContent.toString().replaceAll("[\n,\r]", "");
        Assertions.assertEquals("slot 0 contains key=45 value=Testslot 1 contains key=65 value=Test2slot 2 contains key=5 value=Test3slot 3 is emptyslot 4 is empty",cleanOutPut);
    }

    //Si borro un elemento y hay otro en el index siguiente, se produce borrado logico
    @Test
    void LogicDelete(){
        ClosedHashing<Integer, String> ch = new ClosedHashing<>(fn);
        ch.insert(45, "Test");
        ch.insert(65, "Test2");
        ch.insert(5, "Test3");
        ch.delete(65);
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        ch.dump();
        String cleanOutPut = outContent.toString().replaceAll("[\n,\r]", "");
        Assertions.assertEquals("slot 0 contains key=45 value=Testslot 1 is empty there was a logic deleteslot 2 contains key=5 value=Test3slot 3 is emptyslot 4 is empty",cleanOutPut);
    }

    //Insertar una key que vaya a parar al ultimo lugar de LookUp y este ocupado por otro elemento distinto
    @Test
    void insertAtLastIndexOccupied(){
        ClosedHashing<Integer, String> ch = new ClosedHashing<>(fn);
        ch.insert(44, "Test");
        ch.insert(4, "Test2");
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        ch.dump();
        String cleanOutPut = outContent.toString().replaceAll("[\n,\r]", "");
        Assertions.assertEquals("slot 0 contains key=4 value=Test2slot 1 is emptyslot 2 is emptyslot 3 is emptyslot 4 contains key=44 value=Test",cleanOutPut);
    }

    //Insertar en un index de borrado logico, no en el primero libre
    @Test
    void insertAtIndexWithLogicDelete(){
        ClosedHashing<Integer, String> ch = new ClosedHashing<>(fn);
        ch.insert(44, "Test");
        ch.insert(4, "Test2");
        ch.delete(44);
        ch.insert(54, "Test3");
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        ch.dump();
        String cleanOutPut = outContent.toString().replaceAll("[\n,\r]", "");
        Assertions.assertEquals("slot 0 contains key=4 value=Test2slot 1 is emptyslot 2 is emptyslot 3 is emptyslot 4 contains key=54 value=Test3",cleanOutPut);
    }

}
