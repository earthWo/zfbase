package whitelife.win.library.util;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * Created by wuzefeng on 2017/9/22.
 */

public class SyncUtils {

    private SyncUtils(){}




    public synchronized static void syncRound(){

        int i=0;
        while(i<10){
            i++;
            LogUtils.d("sync",Thread.currentThread().getName()+"**position"+i);
        }

    }


    public static void testSync(int threadCount){

        for(int i=0;i<threadCount;i++){
            Thread thread=new Thread(new Runnable() {
                @Override
                public void run() {
                    testBlock();
                }
            });
            thread.setName("thread"+i);
            thread.start();
        }
    }

    public static int testBlock(){
        FutureTask<Integer> task=testSyncGetNumber();
        try {
            int block=task.get();
            LogUtils.d("block",Thread.currentThread().getName()+"block"+block);
            return block;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return 0;
    }


    /**
     * 模拟耗时操作
     * @return
     */
    private static int getTestNumber(){
        int i=0;
        while(i<100000){
            i++;
        }
        return i;
    }



    private static FutureTask<Integer> testSyncGetNumber(){
        Callable callable=new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return getTestNumber();
            }
        };
        FutureTask<Integer> task=new FutureTask<>(callable);
        Thread thread=new Thread(task);
        thread.start();
        return task;
    }











}
