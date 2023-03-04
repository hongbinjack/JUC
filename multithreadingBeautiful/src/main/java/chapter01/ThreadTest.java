package chapter01;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class ThreadTest {

    //继承Thread类并重写run方法,此方式多个线程执行同一个任务会产生多个线程
    public static class MyThread extends Thread{

        @Override
        public void run() {
            System.out.println("I an child thread");
        }
    }


    //Runnable接口的run方法实现，此方式执行同一个任务会使用同一个线程
    public static class RunnableTask implements Runnable {
        @Override
        public void run() {
            System.out.println("I am a Runnable thread");
        }
    }


    //有返回值的线程
    public static class CallerTask implements Callable<String>{
        @Override
        public String call() {
            return "hello";
        }
    }

  public  static void main(String[] args) {
        //1.创建线程
        MyThread thread = new MyThread();
        //启动线程
        thread.start();


        //2.Runnable
      RunnableTask task = new RunnableTask();
      new Thread(task).start();
      new Thread(task).start();


      //3.Callable
      //创建异步任务
      FutureTask<String> futureTask = new FutureTask<>(new CallerTask());
      //启动线程
      new Thread(futureTask).start();
      try {
          //等待任务执行完毕，并返回结果
          String result = futureTask.get();
          System.out.println(result);
      } catch (Exception e) {
          e.printStackTrace();
      }
  }
}
