# <div align="center"> 🎏 fucking-java-concurrency</div>

<p align="center">
<a href="https://github.com/oldratlee/fucking-java-concurrency/actions/workflows/ci.yaml"><img src="https://img.shields.io/github/workflow/status/oldratlee/fucking-java-concurrency/CI/master?logo=github&logoColor=white" alt="Github Workflow Build Status"></a>
<a href="https://openjdk.java.net/"><img src="https://img.shields.io/badge/Java-8+-green?logo=openjdk&logoColor=white" alt="JDK support"></a>
<a href="https://www.apache.org/licenses/LICENSE-2.0.html"><img src="https://img.shields.io/github/license/oldratlee/fucking-java-concurrency?color=4D7A97&logo=apache" alt="License"></a>
<a href="https://github.com/oldratlee/fucking-java-concurrency/stargazers"><img src="https://img.shields.io/github/stars/oldratlee/fucking-java-concurrency" alt="GitHub Stars"></a>
<a href="https://github.com/oldratlee/fucking-java-concurrency/fork"><img src="https://img.shields.io/github/forks/oldratlee/fucking-java-concurrency" alt="GitHub Forks"></a>
<a href="https://github.com/oldratlee/fucking-java-concurrency/graphs/contributors"><img src="https://img.shields.io/github/contributors/oldratlee/fucking-java-concurrency" alt="GitHub Contributors"></a>
<a href="https://github.com/oldratlee/fucking-java-concurrency"><img src="https://img.shields.io/github/repo-size/oldratlee/fucking-java-concurrency" alt="GitHub repo size"></a>
</p>

----------------------------------------

📖 English Documentation | [📖 中文文档](docs/zh-CN/README.md)

Simple showcases of `Java` concurrency problems, seeing 🙈 is believing 🐵.

## 🍎 Reasons to organize Demo

- The actual phenomenon that can be observed 🙈 is more intuitive and more trustworthy than the concurrency principle mentioned 🙊.
- The `Java` language standard library supports threads, multithreading is heavily used in the language itself (such as `GC`) and applications (the server side).
- Concurrency program design, in the analysis and implementation, the complexity is greatly increased. If you do not fully understand and systematically analyze the concurrent logic, and write code at will, it is not an exaggeration to describe such a program as "**accidentally**" running with the correct result.  
    - The demos here do not give explanations and discussions, and they are all entry-level :neckbeard: . For more information, please loop up the concurrency materials by yourself.

Examples of concurrency problems you encountered in development are welcome to provide ([submit Issue](https://github.com/oldratlee/fucking-java-concurrency/issues)) or share ([pull request after Fork](https://github.com/oldratlee/fucking-java-concurrency/fork))! 😘

----------------------------------------

<img src="docs/dining-philosophers-problem.jpg" width="30%" align="right" />

<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->

- [🍺 Update without synchronization cannot be read in another thread](#-update-without-synchronization-cannot-be-read-in-another-thread)
    - [Demo description](#demo-description)
    - [Problem statement](#problem-statement)
    - [Quickly run](#quickly-run)
- [🍺 Infinite loop of `HashMap`](#-infinite-loop-of-hashmap)
    - [Demo description](#demo-description-1)
    - [Problem statement](#problem-statement-1)
    - [Quickly run](#quickly-run-1)
- [🍺 Combined state read invalid combination](#-combined-state-read-invalid-combination)
    - [Demo description](#demo-description-2)
    - [Problem statement](#problem-statement-2)
    - [Quickly run](#quickly-run-2)
- [🍺 `long` variable read invalid value](#-long-variable-read-invalid-value)
    - [Demo description](#demo-description-3)
    - [Problem statement](#problem-statement-3)
    - [Quickly run](#quickly-run-3)
- [🍺 the result of concurrency count without synchronization is wrong](#-the-result-of-concurrency-count-without-synchronization-is-wrong)
    - [Demo description](#demo-description-4)
    - [Problem statement](#problem-statement-4)
    - [Quickly run](#quickly-run-4)
- [🍺 Synchronization on mutable fields](#-synchronization-on-mutable-fields)
    - [Demo description](#demo-description-5)
    - [Problem statement](#problem-statement-5)
    - [Quickly run](#quickly-run-5)
- [🍺 Deadlock caused by the symmetric locks](#-deadlock-caused-by-the-symmetric-locks)
    - [Demo description](#demo-description-6)
    - [Problem statement](#problem-statement-6)
    - [Quickly run](#quickly-run-6)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

----------------------------------------

## 🍺 Update without synchronization cannot be read in another thread

Demo class [`com.oldratlee.fucking.concurrency.NoPublishDemo`](src/main/java/com/oldratlee/fucking/concurrency/NoPublishDemo.java).

### Demo description

Set the field `stop` to `true` in the `main` thread to control the exit of the task thread started in `main`.

### Problem statement

After the `main` thread field `stop` is `true`, the task thread continues to run, that is, no new value has been read in the task thread.

### Quickly run

```bash
mvn compile exec:java -Dexec.mainClass=com.oldratlee.fucking.concurrency.NoPublishDemo
```

## 🍺 Infinite loop of `HashMap`

This problem has been explained in many places.

The Demo class [`com.oldratlee.fucking.concurrency.HashMapHangDemo`](src/main/java/com/oldratlee/fucking/concurrency/HashMapHangDemo.java) can reproduce this problem.

### Demo description

Two task threads are opened in the main thread to perform the put operation of `HashMap`. The main thread does the get operation.

### Problem statement

The main thread Block is determined by no continuous output, that is, the endless loop of `HashMap` appears.

### Quickly run

```bash
mvn compile exec:java -Dexec.mainClass=com.oldratlee.fucking.concurrency.HashMapHangDemo
```

## 🍺 Combined state read invalid combination

When programming, multiple state records will be required (state can be a `POJO` object or `int`s, etc.).

It is often seen that the multi-state read and write code is not synchronized, and the person who write it will naturally ignore the issue of thread safety.

Invalid combinations are combinations that have never been set.

### Demo description

The main thread modifies multiple states. For the convenience of checking, each write has a fixed relationship: the second state is twice the value of the first state. Read multiple states in a task thread.
Demo class com.oldratlee.fucking.concurrency.InvalidCombinationStateDemo.

### Problem statement

The second state read in the task thread is not twice the value of the first state, that is, an invalid value.

### Quickly run

```bash
mvn compile exec:java -Dexec.mainClass=com.oldratlee.fucking.concurrency.InvalidCombinationStateDemo
```

## 🍺 `long` variable read invalid value

An invalid value is a value that has never been set.

Reading and writing of `long` variables is not atomic and will be divided into two 4-byte operations.

Demo class [`com.oldratlee.fucking.concurrency.InvalidLongDemo`](src/main/java/com/oldratlee/fucking/concurrency/InvalidLongDemo.java).

### Demo description

The main thread modifies the long variable. For the convenience of checking, the upper 4 bytes and the lower 4 bytes of the long value written each time are the same. Read the long variable in the task thread.

### Problem statement

In the task thread, a long variable whose upper 4 bytes and lower 4 bytes are different is read, which is an invalid value.

### Quickly run

```bash
mvn compile exec:java -Dexec.mainClass=com.oldratlee.fucking.concurrency.InvalidLongDemo
```

## 🍺 the result of concurrency count without synchronization is wrong

Demo class [`com.oldratlee.fucking.concurrency.WrongCounterDemo`](src/main/java/com/oldratlee/fucking/concurrency/WrongCounterDemo.java).

### Demo description

Two task threads are opened in the main thread to execute concurrent incrementing counts. Main thread final result check.

### Problem statement

The count value is incorrect.

### Quickly run

```bash
mvn compile exec:java -Dexec.mainClass=com.oldratlee.fucking.concurrency.WrongCounterDemo
```

## 🍺 Synchronization on mutable fields

It is common to see synchronization code on a volatile field, and the person who write it will naturally feel that this is safe and correct.  
\# For problem analysis, see the article [Synchronization on mutable fields](http://www.ibm.com/developerworks/library/j-concurrencybugpatterns/#N100E7).

Demo class [`com.oldratlee.fucking.concurrency.SynchronizationOnMutableFieldDemo`](src/main/java/com/oldratlee/fucking/concurrency/SynchronizationOnMutableFieldDemo.java).

### Demo description

Two task threads are opened in the main thread to execute `addListener`. Main thread final result check.

### Problem statement

The final count of Listeners is incorrect.

### Quickly run

```bash
mvn compile exec:java -Dexec.mainClass=com.oldratlee.fucking.concurrency.SynchronizationOnMutableFieldDemo
```

## 🍺 Deadlock caused by the symmetric locks

\# For problem analysis, see the article [Synchronization on mutable fields](http://www.ibm.com/developerworks/library/j-concurrencybugpatterns/#N101C1)

Demo class [`com.oldratlee.fucking.concurrency.SymmetricLockDeadlockDemo`](src/main/java/com/oldratlee/fucking/concurrency/SymmetricLockDeadlockDemo.java).

### Demo description

Two task threads are opened in the main thread for execution.

### Problem statement

Task thread deadlocked.

### Quickly run

```bash
mvn compile exec:java -Dexec.mainClass=com.oldratlee.fucking.concurrency.SymmetricLockDeadlockDemo
```
