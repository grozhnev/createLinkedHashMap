import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class TestFooBar {


    Map<Integer, String> hashMap;
    Bar<Integer, String> fooBar;

    @Before public void init(){
        hashMap = new HashMap<>();
        fooBar = new FooBar<Integer, String>();

        fillHashMap(hashMap);
        fillFooBar(fooBar);
    }

    void fillHashMap(Map<Integer, String> collection){
        collection.put(70, "gvozd");
        collection.put(30, "tyumen");
        collection.put(4, "alabama");
        collection.put(16, "filtr");
        collection.put(9, "sii");
        collection.put(144, "as");
        collection.put(0, "klyrtope");
        collection.put(2, "zaoper");
        collection.put(45, "lyee");
        collection.put(16, "goof");
        collection.put(27, "foo");
        collection.put(90, "doo");
        collection.put(99, "rembo");
        collection.put(17, "palcebo");
        collection.put(73, "ALI");
        collection.put(88, "zed");
        collection.put(55, "juko");
        collection.put(100, "book");
        collection.put(2, "sally");
        collection.put(80, "kilo");
    }
    void fillFooBar(Bar<Integer, String> collection){
        collection.put(70, "gvozd");
        collection.put(30, "tyumen");
        collection.put(4, "alabama");
        collection.put(16, "filtr");
        collection.put(9, "sii");
        collection.put(144, "as");
        collection.put(0, "klyrtope");
        collection.put(2, "zaoper");
        collection.put(45, "lyee");
        collection.put(16, "goof");
        collection.put(27, "foo");
        collection.put(90, "doo");
        collection.put(99, "rembo");
        collection.put(17, "palcebo");
        collection.put(73, "ALI");
        collection.put(88, "zed");
        collection.put(55, "juko");
        collection.put(100, "book");
        collection.put(2, "sally");
        collection.put(80, "kilo");
    }

    @Test
    public void testGet(){
        Assert.assertEquals(hashMap.get(30), fooBar.get(30));
    }

    @Test
    public void testPut(){
        hashMap.put(1,"aaa");
        fooBar.put(1, "aaa");

        Assert.assertEquals(hashMap.get(1), fooBar.get(1));
    }

    @Test
    public void testDelete(){
        fooBar.remove(1);
        hashMap.remove(1);

        Assert.assertEquals(fooBar.get(1), hashMap.get(1));
    }
}
