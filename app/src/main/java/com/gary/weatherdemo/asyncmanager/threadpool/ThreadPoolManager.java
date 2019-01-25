package com.gary.weatherdemo.asyncmanager.threadpool;

import com.gary.weatherdemo.exception.WtException;

import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by GaryCao on 2018/10/25.
 * 线程管理类，管理线程池，一个应用中有多个线程池，每个线程池做自己相关的业务(concurrent tasks manager)
 */
public class ThreadPoolManager {
    //普通线程池
    private static ThreadPoolProxy mNormalPool =
            new ThreadPoolProxy(1, 3, 5 * 1000);
    //下载专用线程池
    private static ThreadPoolProxy mDownloadPool =
            new ThreadPoolProxy(3, 3, 5 * 1000);

    public static ThreadPoolProxy getNormalPool() {
        return mNormalPool;
    }

    public static ThreadPoolProxy getDownloadPool() {
        return mDownloadPool;
    }

    /**
     * 线程池代理类
     */
    public static class ThreadPoolProxy {
        /**
         * para1:corePoolSize核心线程池大小
         * 如果设置allowCoreThreadTimeOut为false的情况下：
         * 即使当线程池中的线程处于空闲状态，这些线程也不会被线程池中移除。
         * 如果设置了allowCoreThreadTimeOut为true,
         * 那么当核心线程在空闲了一段时间后依旧没有用于工作，那么将会从线程池中移除。
         * 注意:(allowCoreThreadTimeOut默认为false，通常情况下也无需做修改)
         */
        private final int mCorePoolSize;

        /**
         * para2:maximumPoolSize:线程池中所允许创建最大线程数量
         */
        private final int mMaximumPoolSize;

        /**
         * para3:keepAliveTime:当线程池中的线程数量大于核心线程数，
         * 如果这些多出的线程在经过了keepAliveTime时间后，
         * 依然处于空闲状态，那么这些多出的空闲线程将会被结束其生命周期。
         */
        private final long mKeepAliveTime;

        /**
         * para4:unit:keepAliveTime的时间单位
         */
        private final TimeUnit unit = TimeUnit.MILLISECONDS;

        private ThreadPoolExecutor mPool;

        public ThreadPoolProxy(int corePoolSize, int maximumPoolSize, long keepAliveTime) {
            this.mCorePoolSize = corePoolSize;
            this.mMaximumPoolSize = maximumPoolSize;
            this.mKeepAliveTime = keepAliveTime;
        }

        private void initPool() {
            if (mPool == null || mPool.isShutdown()) {
                /**
                 * para5:workQueue线程池的缓存队列
                 * 提交的任务将被存储在workQueue进行缓冲。
                 * 该队列只能存放通过execute方法提交的Runnable任务。
                 */
                BlockingQueue<Runnable> workQueue = null;

                /**缓存队列workQueue type1: FIFO,大小有限制*/
                workQueue = new ArrayBlockingQueue<Runnable>(4);
                /**缓存队列workQueue type2: FIFO,大小有限制*/
                //workQueue = new LinkedBlockingQueue();
                /**缓存队列workQueue type3: FIFO,大小有限制*/
                //workQueue = new PriorityBlockingQueue();

                /**
                 * para6:threadFactory:线程池中用于创建线程的工厂
                 * 在这里使用线程工厂的目的也是为了解耦,将创建的实现细节通过工厂进行封装，
                 * 而不是直接将创建的方式固化在ThreadPoolExecutor本身的代码中。
                 * Thread newThread(Runnable r)
                 */
                ThreadFactory threadFactory = Executors.defaultThreadFactory();//线程工厂

                /**
                 * para7:RejectedExecutionHandler:线程池对拒绝任务的处理策略.
                 * 当线程池中的线程数量达到最大并且阻塞队列也已经满了无法再添加任务时，线程池所采取的处理策略。
                 */
                RejectedExecutionHandler handler = null;//异常捕获器

                /**拒绝任务处理策略 type1: 去掉队列中首个任务，将新加入的放到队列中去*/
                //handler = new ThreadPoolExecutor.DiscardOldestPolicy();
                /**拒绝任务处理策略 type2: 触发异常*/
                //handler = new ThreadPoolExecutor.AbortPolicy();
                /**拒绝任务处理策略 type3: 不做任何处理*/
                handler = new ThreadPoolExecutor.DiscardPolicy();
                /**拒绝任务处理策略 type4: 直接执行，不归线程池控制,在调用线程中执行*/
                //handler = new ThreadPoolExecutor.CallerRunsPolicy();

                //new Thread(task).start();

                mPool = new ThreadPoolExecutor(
                        mCorePoolSize,//para1:核心线程池大小
                        mMaximumPoolSize,//para2:最大线程池大小
                        mKeepAliveTime,//para3:保持存活的时间
                        unit,//para4:保持存活時間单位
                        workQueue,//para5:线程池的缓存队列
                        threadFactory,//para6:线程工厂
                        handler);//para7:异常捕获器
            }
        }

        /**
         * 执行任务
         */
        public void execute(Runnable task) {
            initPool();
            mPool.execute(task);
        }

        /**
         * 提交任务?
         */
        public Future<?> submit(Runnable task) {
            initPool();
            return mPool.submit(task);
        }

        /**
         * 取消任务
         */
        public void remove(Runnable task) {
            if (mPool != null && !mPool.isShutdown()) {
                mPool.getQueue().remove(task);
            }
        }
    }

    //===================================================================================================
    //for test
    public void startNormalTask(Runnable task) {
        ThreadPoolManager.getNormalPool().execute(task);
    }

    public void startDownloadTask(Runnable task) {
        ThreadPoolManager.getDownloadPool().execute(task);
    }

    public void removeDownloadTask(Runnable task) {
        ThreadPoolManager.getDownloadPool().remove(task);//移除任务(停止线程池任务执行)
    }
}