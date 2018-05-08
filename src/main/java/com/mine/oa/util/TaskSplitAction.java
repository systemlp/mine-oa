package com.mine.oa.util;

import java.util.List;
import java.util.concurrent.RecursiveAction;

/***
 *
 * 〈利用Fork/Join将一个大任务拆分为多个子任务并发执行〉<br>
 * 〈功能详细描述〉
 *
 * @author liupeng
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class TaskSplitAction<T> extends RecursiveAction {

    private static final long serialVersionUID = 5726851608325022177L;

    /** 子任务最大执行数量 */
    private int maxTaskMum = 100;
    private List<T> dataList;
    private int start;
    private int end;
    private Callable<T> callable;

    public TaskSplitAction(List<T> dataList, int start, int end, Callable<T> callable) {
        this.dataList = dataList;
        this.start = start;
        this.end = end;
        this.callable = callable;
    }

    /**
     * 说明：会直接对ForkJoin产生的线程数造成影响<br>
     * 线程数计算公司：end/(2^n)<=maxTaskMum时n的值
     */
    @SuppressWarnings("unused")
    public void setMaxTaskMum(int maxTaskMum) {
        this.maxTaskMum = maxTaskMum;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void compute() {
        if ((end - start) <= maxTaskMum) {
            for (int i = start; i < end; i++) {
                callable.call(dataList.get(i));
            }
            return;
        }
        int middle = (start + end) / 2;
        TaskSplitAction left = new TaskSplitAction(dataList, start, middle, callable);
        TaskSplitAction right = new TaskSplitAction(dataList, middle + 1, end, callable);
        invokeAll(left, right);
    }
}
