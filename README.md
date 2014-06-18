fucking-java-concurrency
==========================

:point_right: 通过Demo演示出`Java`中并发问题。     
可以观察到的实际现象 :see_no_evil: 比 说说的并发原则 :speak_no_evil: 更直观更可信。  
\# 这里给Demo没有解释和讨论，可以参见[并发方面的系统的资料](ConcurrencyMaterial.md)。

:beer: 无同步的修改在另一个线程中读不到
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

:beer: `long`变量读到无效值
----------------------------------

`long`变量读写不是原子的，会分为2次4字节操作。     
Demo类[`com.oldratlee.fucking.concurrency.InvalidLongDemo`](src/main/java/com/oldratlee/fucking/concurrency/InvalidLongDemo.java)。

### Demo说明

主线程修改`long`变量，每次写入的`long`值的高4字节和低4字节是一样的。在任务线程中读取`long`变量。

### 问题说明

任务线程中读到了高4字节和低4字节不一样的`long`变量，即是无效值（从来没有设置过的值）。

### 快速运行

```bash
mvn compile exec:java -Dexec.mainClass=com.oldratlee.fucking.concurrency.InvalidLongDemo
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
