package frc.lightning.testing;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.PriorityQueue;

import frc.lightning.testing.SystemTest.Priority;

public class SystemTestEngine {

    private List<SystemTest> tests;
    private boolean isAllDone;

    public SystemTestEngine(){
        tests = new ArrayList<SystemTest>();
        isAllDone = false;
    }

    public static SystemTestEngine makeEngine(){
        return new SystemTestEngine();
    }

    public void registerTest(SystemTest test){
        tests.add(test);
    }

    public void runTests(){
        tests = organizeTests(tests);
        for(SystemTest test : tests) if(test != null) test.test();
        isAllDone = true;
    }

    public boolean isAllDone(){
        return isAllDone;
    }

    private List<SystemTest> organizeTests(List<SystemTest> tests) {
        List<SystemTest> prioritizedTests = new ArrayList<>();
        for(SystemTest.Priority p : SystemTest.Priority.values()){
            for(int i = 0 ; i < tests.size() ; i++){
                if(tests.get(i).getPriority() == p){
                    prioritizedTests.add(tests.get(i));
                    tests.remove(i);
        }}}   
        return prioritizedTests;
    }



    /*
    private PriorityQueue<SystemTest> tests = new PriorityQueue<>((t1, t2) -> t1.getPriority() - t2.getPriority() );
    private Iterator<SystemTest> itor;
    private SystemTest current;

    private SystemTestEngine() {
    }

    static SystemTestEngine getEngine() {
        return new SystemTestEngine();
    }

    static void registerTest(SystemTest test) {
        getEngine().addTest(test);
    }

    public void addTest(SystemTest test) {
        tests.add(test);
    }

    public void initialize() {
        itor = tests.iterator();
        current = null;
    }

    public boolean run() {
        if (current == null) {
            current = itor.next();

            if (current != null) {
                current.setup();
            }
        }

        if (current != null) {
            if (current.test()) {
                current.tearDown();
                current = null;
                return itor.hasNext();
            }

            return true;
        }

        return false;
    }
*/
}