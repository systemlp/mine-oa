package com.mine.concurrency;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

/***
 *
 * 〈一句话功能简述〉<br> 
 * 〈功能详细描述〉
 *
 * @author liupeng
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class TestAtomic {

    interface A{
        default void hello(){
            System.out.println("A");
        }
    }

    interface B extends A{
        default void hello(){
            System.out.println("B");
        }
    }
    interface C extends A{
    }

    class D implements C,B{

    }

    @Test
    public void testAetAndAddInt() {
        D d = new D();
        d.hello();
        AtomicInteger atomicInteger = new AtomicInteger(0);
        atomicInteger.getAndIncrement();
    }

}
