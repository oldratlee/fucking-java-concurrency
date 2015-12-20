fucking-java-concurrency
==========================

:point_right: 通过Demo演示出`Java`中并发问题。

**整理Demo的原因：**

- 可以观察到的实际现象 :see_no_evil: 比 说说的并发原则 :speak_no_evil: 更直观更可信。
- `Java`语言标准库支持线程，语言本身（如`GC`）以及应用（服务器端`The Server side`）中会重度使用多线程。
- 并发程度设计在分析和实现中，复杂度大大增加。
    如果不系统理解和充分分析并发逻辑，随意写代码，这样的程序用 **『碰巧』** 能运行出正确结果 来形容一点都不为过。

这里的Demo没有给出解释和讨论，要深入了解请参见[并发方面的系统的资料](ConcurrencyMaterial.md)。

你在开发中碰到的并发问题的例子，欢迎提供（[提交Issue](https://github.com/oldratlee/fucking-java-concurrency/issues))和分享（[Fork后提交代码](https://github.com/oldratlee/fucking-java-concurrency/fork)）！ :kissing_heart:

:beer: 无同步的修改在另一个线程中会读不到
----------------------------------

Demo类[`com.oldratlee.fucking.concurrency.NoPublishDemo`](src/main/java/com/oldratlee/fucking/concurrency/NoPublishDemo.java)。

### Demo说明

主线程中设置属性`stop`为`true`，以控制在`main`启动的任务线程退出。

### 问题说明

在主线程属性`stop`为`true`后，但任务线程持续运行，即任务线程中一直没有读到新值。

### 快速运行

```bash
mvn compile exec:java -Dexec.mainClass=com.oldratlee.fucking.concurrency.NoPublishDemo
```

:beer: `HashMap`的死循环
----------------------------------

这个问题在[疫苗：Java HashMap的死循环](http://coolshell.cn/articles/9606.html)等多个地方都有讲解。    
Demo类[`com.oldratlee.fucking.concurrency.HashMapHangDemo`](src/main/java/com/oldratlee/fucking/concurrency/HashMapHangDemo.java)，可以复现这个问题。

### Demo说明

主线程中开启2个任务线程执行`HashMap`的`put`操作。主线程做`get`操作。

### 问题说明

通过没有持续的输出判定主线程`Block`，即`HashMap`的出现死循环。

### 快速运行

```bash
mvn compile exec:java -Dexec.mainClass=com.oldratlee.fucking.concurrency.HashMapHangDemo
```

:beer: 组合状态读到无效组合
----------------------------------

程序设计时，会需要多个状态记录（状态可以是个`POJO`对象或是`int`等等）。常看到多状态读写没有同步的代码，并且写的同学会很自然地就忽略了线程安全的问题。

无效组合 是指 从来没有设置过的组合。

### Demo说明

主线程修改多个状态，为了方便检查，每次写入有个固定的关系：第2个状态是第1个状态值的2倍。在任务线程中读取多个状态。  
Demo类[`com.oldratlee.fucking.concurrency.InvalidCombinationStatDemo`](src/main/java/com/oldratlee/fucking/concurrency/InvalidCombinationStatDemo.java)。

### 问题说明

任务线程中读到了 第2个状态不是第1个状态值2倍的值，即是无效值。

### 快速运行

```bash
mvn compile exec:java -Dexec.mainClass=com.oldratlee.fucking.concurrency.InvalidCombinationStatDemo
```

:beer: `long`变量读到无效值
----------------------------------

无效值 是指 从来没有设置过的值。

`long`变量读写不是原子的，会分为2次4字节操作。     
Demo类[`com.oldratlee.fucking.concurrency.InvalidLongDemo`](src/main/java/com/oldratlee/fucking/concurrency/InvalidLongDemo.java)。

### Demo说明

主线程修改`long`变量，为了方便检查，每次写入的`long`值的高4字节和低4字节是一样的。在任务线程中读取`long`变量。

### 问题说明

任务线程中读到了高4字节和低4字节不一样的`long`变量，即是无效值。

### 快速运行

```bash
mvn compile exec:java -Dexec.mainClass=com.oldratlee.fucking.concurrency.InvalidLongDemo
```

:beer: 无同步的并发计数结果不对
----------------------------------

Demo类[`com.oldratlee.fucking.concurrency.WrongCounterDemo`](src/main/java/com/oldratlee/fucking/concurrency/WrongCounterDemo.java)。

### Demo说明

主线程中开启2个任务线程执行并发递增计数。主线程最终结果检查。

### 问题说明

计数值不对。

### 快速运行

```bash
mvn compile exec:java -Dexec.mainClass=com.oldratlee.fucking.concurrency.WrongCounterDemo
```

:beer: 在易变域上的同步
-------------------------

常看到在易变域上的同步代码，并且写的同学会很自然觉得这样是安全和正确的。      
\# 问题分析见文章链接：[在易变域上的同步](http://www.ibm.com/developerworks/cn/java/j-concurrencybugpatterns/#N100DA)，对应的英文文章：[Synchronization on mutable fields](http://www.ibm.com/developerworks/library/j-concurrencybugpatterns/#N100E7)    
Demo类[`com.oldratlee.fucking.concurrency.SynchronizationOnMutableFieldDemo`](src/main/java/com/oldratlee/fucking/concurrency/SynchronizationOnMutableFieldDemo.java)。

### Demo说明

主线程中开启2个任务线程执行`addListener`。主线程最终结果检查。

### 问题说明

最终`Listener`的个数不对。

### 快速运行

```bash
mvn compile exec:java -Dexec.mainClass=com.oldratlee.fucking.concurrency.SynchronizationOnMutableFieldDemo
```

:beer: 对称锁死锁
-------------------------

\# 问题分析见文章链接：[对称锁死锁](http://www.ibm.com/developerworks/cn/java/j-concurrencybugpatterns/#N101B4)，对应的英文文章：[Synchronization on mutable fields](http://www.ibm.com/developerworks/library/j-concurrencybugpatterns/#N101C1)    
Demo类[`com.oldratlee.fucking.concurrency.SymmetricLockDeadlockDemo`](src/main/java/com/oldratlee/fucking/concurrency/SymmetricLockDeadlockDemo.java)。

### Demo说明

主线程中开启2个任务线程执行。

### 问题说明

任务线程死锁。

### 快速运行

```bash
mvn compile exec:java -Dexec.mainClass=com.oldratlee.fucking.concurrency.SymmetricLockDeadlockDemo
```

一些并发的问题讨论和资料
-------------------------

- [ibm developerworks - 多核系统上的`Java`并发缺陷模式（`bug patterns`）](http://www.ibm.com/developerworks/cn/java/j-concurrencybugpatterns/)
- [stackoverflow - What is the most frequent concurrency issue you've encountered in Java?](http://stackoverflow.com/questions/461896/what-is-the-most-frequent-concurrency-issue-youve-encountered-in-java)
- [Java Concurrency Gotchas](http://www.slideshare.net/alexmiller/java-concurrency-gotchas-3666977)
- [Common problems of concurrency (Multi-Threading) in Java](http://www.somanyword.com/2014/03/common-problems-of-concurrency-multi-threading-in-java/)
- [java tutorial - Concurrency](http://docs.oracle.com/javase/tutorial/essential/concurrency/index.html)

要深入了解请参见[并发方面的系统的资料](ConcurrencyMaterial.md)。
