package com.quest.forge.ui.master.b;

/**
 * Created with IntelliJ IDEA.
 * User: stefan
 * Date: 20.06.13
 * Time: 15:26
 * To change this template use File | Settings | File Templates.
 */
public class WorkerThread {
    public static void main(String[] args) {
        new WorkerThread().executeEntry("TESTER");
    }

    private void executeEntry(String tester) {
        System.out.println("NOP:"+tester);
    }
}
