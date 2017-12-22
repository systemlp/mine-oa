package com.mine.oa.service;

import com.google.common.collect.Lists;
import org.junit.Test;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/***
 *
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author liupeng
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class AlgorithmTest {

    /***
     * 顺序查找对比二分查找（必须基于排序集）
     */
    @Test
    public void testDichotomy() {
        int[] nums = new int[10000];
        for (int i = 0; i < 10000; i++) {
            nums[i] = i;
        }
        int num = -1;
        long time = System.nanoTime();
        for (int num1 : nums) {
            if (num1 == num) {
                System.out.println("顺序查找耗时：" + (System.nanoTime() - time));
                break;
            }
        }
        time = System.nanoTime();
        int start = 0;
        int end = nums.length;
        do {
            int index = (end - start) / 2 + start;
            if (nums[index] == num) {
                break;
            }
            if (nums[index] > num) {
                end = index;
                continue;
            }
            if (nums[index] < num) {
                start = index;
            }
        } while (start < end);
        System.out.println("二分查找耗时：" + (System.nanoTime() - time));
    }

    /***
     * 选择排揎，每次找到数组中最大或最小数字与第i个数字交换位置
     */
    @Test
    public void testSelectSort() throws InterruptedException {
        int[] nums = new int[10000];
        Random rand = new Random();
        for (int i = 0; i < 10000; i++) {
            nums[i] = rand.nextInt(1000);
            // Thread.sleep(1);
        }
        long time = System.nanoTime();
        int totalCount = 0;
        int moveCount = 0;
        for (int i = 0; i < nums.length - 1; i++) {
            int maxIndex = i;
            for (int j = i + 1; j < nums.length; j++) {
                totalCount++;
                if (nums[j] > nums[maxIndex]) {
                    maxIndex = j;
                }
            }
            if (maxIndex != i) {
                moveCount++;
                nums[i] = nums[i] ^ nums[maxIndex];
                nums[maxIndex] = nums[i] ^ nums[maxIndex];
                nums[i] = nums[i] ^ nums[maxIndex];
            }
        }
        System.out.println(String.format("遍历次数%s，数字交换次数%s,耗时%s纳秒", totalCount, moveCount, (System.nanoTime() - time)));
        System.out.println(Arrays.toString(nums));
    }

    /***
     * 快速排序
     */
    @Test
    public void testQuickSort() throws InterruptedException {
        List<Integer> nums = Lists.newArrayList();
        Random rand = new Random();
        for (int i = 0; i < 100000; i++) {
            nums.add(rand.nextInt(1000));
            // Thread.sleep(1);
        }
        long time = System.nanoTime();
        List<Integer> sortNums = quickSort(nums);
        System.out.println(String.format("耗时%s纳秒", (System.nanoTime() - time)));
        System.out.println(Arrays.toString(sortNums.toArray()));
        time = System.nanoTime();
        Collections.sort(nums);
        System.out.println(String.format("耗时%s纳秒", (System.nanoTime() - time)));
    }

    private List<Integer> quickSort(List<Integer> nums) {
        if (CollectionUtils.isEmpty(nums) || nums.size() < 2) {
            return nums;
        }
        if (nums.size() < 7) {
            int num, num1;
            for (int i = 0; i < nums.size(); i++) {
                for (int j = i; j > 0 && nums.get(j) < nums.get(j - 1); j--) {
                    num = nums.get(j);
                    num1 = nums.get(j - 1);
                    nums.set(j - 1, num);
                    nums.set(j, num1);
                }
            }
            return nums;
        }
        List<Integer> firstNums = Lists.newArrayList(nums.get(0)), lessNums = Lists.newArrayList(),
                greaterNums = Lists.newArrayList();
        for (int i = 1; i < nums.size(); i++) {
            if (nums.get(i) < firstNums.get(0)) {
                lessNums.add(nums.get(i));
            } else if (nums.get(i).equals(firstNums.get(0))) {
                firstNums.add(nums.get(i));
            } else {
                greaterNums.add(nums.get(i));
            }
        }
        List<Integer> sortNums = quickSort(lessNums);
        sortNums.addAll(firstNums);
        sortNums.addAll(quickSort(greaterNums));
        return sortNums;
    }

}
