package chapter01;


public class SharedVariableLockTest {

   private static volatile Object resourceA = new Object();
   private static volatile Object resourceB = new Object();
    public static void main(String[] args) throws InterruptedException {

        //创建线程
        Thread threadA = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    //获取resourceA共享资源的监视器锁
                    synchronized (resourceA){
                        System.out.println("threadA get resourceA lock");


                    //获取resourceB共享资源的监视器锁
                        synchronized (resourceB){
                            System.out.println("threadA get resourceB lock");

                            //线程A阻塞，并释放获取到的resourceA锁
                            System.out.println("threadA release resourceA lock");
                            resourceA.wait();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        //创建线程
        Thread threadB = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    //线程B休眠2s

                    Thread.sleep(2000);
                    System.out.println("线程B已经等待2秒，继续执行");

                    //获取resourceA共享资源的监视器锁
                    synchronized (resourceA){
                        System.out.println("threadB get resourceA lock");

                        System.out.println("threadB try get resourceB lock...");


                        /*
                           获取resourceB共享资源的监视器锁，此时线程A只释放了resourceA，并没有释放获取到的resourceB锁
                           所以此时，该锁一直请求resourceB资源，但是久久不能回应。所有里面的输出语句不能够执行。该程序就会
                           一直处于等待状态不能结束进程。
                         */
                        synchronized (resourceB){
                            System.out.println("threadB get resourceB lock");

                            //线程B阻塞，并释放获取到的resourceA的锁
                            System.out.println("threadB release resourceA lock");
                            resourceA.wait();
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        //启动线程
        threadA.start();
        threadB.start();

        //等待两个线程结束
        threadA.join();
        threadB.join();
        System.out.println("main over");//block comments可知该输出语句永远执行不了了
    }
    /**
     * 如上代码中，在main 函数里面启动了线程A和线程B，为了让线程A先获取到锁,这里让线程B先休眠了2s，线程A先后
     * 获取到共享变量 resourceA和共享变量 resourceB上的锁，然后调用了resourceA的wait()方法阻塞自己，阻
     * 塞自己后线程A释放掉获取的resourceA上的锁。
     * 线程B休眠结束后会首先尝试获取resourceA上的锁，如果当时线程A还没有调用wait()方法释放该锁，那么线程B会被阻塞，
     * 当线程A释放了resourceA上的锁后，线程B就会获取到resourceA 上的锁，然后尝试获取resourceB上的锁。由于线程A
     * 调用的是resourceA 上的 wait()方法,所以线程A挂起自己后并没有释放获取到的resourceB上的锁,所以线程B尝试获取
     * resourceB上的锁时会被阻塞。
     * 这就证明了当线程调用共享对象的wait()方法时，当前线程只会释放当前共享对象的锁，当前线程持有的其他共享对象的监视器锁并不会被释放
     */
}
