fucking-java-concurrency
=========================

通过`Demo`演示出`Java`中并发问题。

可以观察到的现象 比 说说的并发原则 更直观更可信。

无同步的修改在另一个线程中读不到
----------------------------------

`Demo`类[`com.oldratlee.fucking.concurrency.NoPublishDemo`](src/main/java/com/oldratlee/fucking/concurrency/NoPublishDemo.java)。

### `Demo`说明

主线程中设置属性`stop`为`true`，以控制`main`启动的任务线程退出。

### 问题说明

在主线程属性`stop`为`true`后，但任务线程持续运行，任务线程中读不到新值。

### 快速运行

```bash
mvn compile exec:java -Dexec.mainClass=com.oldratlee.fucking.concurrency.NoPublishDemo
```

`long`变量读到无效值
----------------------------------

`long`变量读写不是原子的，会分为2次4字节操作。     
`Demo`类[`com.oldratlee.fucking.concurrency.InvalidLongDemo`](src/main/java/com/oldratlee/fucking/concurrency/InvalidLongDemo.java)。

### `Demo`说明

主线程修改`long`变量，每次写的值的高4字节和低4字节是一样的。在任务线程中读取`long`变量。

### 问题说明

任务线程中读到了高4字节和低4字节不一样的`long`变量，即是无效值（从来没有设置过的值）。

### 快速运行

```bash
mvn compile exec:java -Dexec.mainClass=com.oldratlee.fucking.concurrency.InvalidLongDemo
```
