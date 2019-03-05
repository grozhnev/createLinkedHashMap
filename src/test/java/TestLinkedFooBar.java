import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;


public class TestLinkedFooBar {

    private LinkedHashMap<Integer, String> hashMap;
    private LinkedFooBar<Integer, String> fooBar;

    @Before
    public void init(){
        hashMap = new LinkedHashMap<>();
        fooBar = new LinkedFooBar<>();

        fillHashMap(hashMap);
        fillFooBar(fooBar);
    }

    private void fillHashMap(Map<Integer, String> collection){
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
        collection.put(1089, "kilo");
    }
    private void fillFooBar(LinkedFooBar<Integer, String> collection){
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
        collection.put(1089, "kilo");
    }

    @Test
    public void testGet(){
//        Assert.assertEquals(hashMap.get(30), fooBar.get(30));
        Assert.assertEquals(hashMap.get(70), fooBar.get(70));
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


    @Test
    public void testKeySet(){

        System.out.println(hashMap.keySet());
        System.out.println(fooBar.keySet());

        LinkedList<Integer> hashMapKeys = new LinkedList<>(hashMap.keySet());
        LinkedList<Integer> fooBarKeys = new LinkedList<>(fooBar.keySet());


        Assert.assertEquals(hashMapKeys, fooBarKeys);
    }



    @Test
    public void testLocalDifferentOrder(){

        LinkedHashMap hashMap = new LinkedHashMap();
        LinkedFooBar fooBar = new LinkedFooBar();

        fooBar.put(1, "flea");
        fooBar.put(4, "ZAZA");
        fooBar.put(3, "alll");
        fooBar.put(5, "cha");


        hashMap.put(1, "alll");
        hashMap.put(3, "WWW");
        hashMap.put(4, "BABAB");
        hashMap.put(5,"nope");

        Assert.assertNotEquals(fooBar.keySet().toString(), hashMap.keySet().toString());

        fooBar.remove(4);
        hashMap.remove(4);
        fooBar.put(1, "ida");


        System.out.println(hashMap.keySet().toString());
        System.out.println(fooBar.keySet().toString());

        Assert.assertEquals(fooBar.keySet().toString(), hashMap.keySet().toString());
    }


}
