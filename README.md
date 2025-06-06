# <div align="center"> 🎏 fucking-java-concurrency</div>

<p align="center">
<a href="https://github.com/oldratlee/fucking-java-concurrency/actions/workflows/ci.yaml"><img src="https://img.shields.io/github/actions/workflow/status/oldratlee/fucking-java-concurrency/ci.yaml?branch=master&logo=github&logoColor=white" alt="Github Workflow Build Status"></a>
<a href="https://openjdk.java.net/"><img src="https://img.shields.io/badge/Java-8+-339933?logo=openjdk&logoColor=white" alt="Java support"></a>
<a href="https://www.apache.org/licenses/LICENSE-2.0.html"><img src="https://img.shields.io/github/license/oldratlee/fucking-java-concurrency?color=4D7A97&logo=apache" alt="License"></a>
<a href="https://github.com/oldratlee/fucking-java-concurrency/stargazers"><img src="https://img.shields.io/github/stars/oldratlee/fucking-java-concurrency?style=flat" alt="GitHub Stars"></a>
<a href="https://github.com/oldratlee/fucking-java-concurrency/fork"><img src="https://img.shields.io/github/forks/oldratlee/fucking-java-concurrency?style=flat" alt="GitHub Forks"></a>
<a href="https://github.com/oldratlee/fucking-java-concurrency/graphs/contributors"><img src="https://img.shields.io/github/contributors/oldratlee/fucking-java-concurrency" alt="GitHub Contributors"></a>
<a href="https://github.com/oldratlee/fucking-java-concurrency"><img src="https://img.shields.io/github/repo-size/oldratlee/fucking-java-concurrency" alt="GitHub repo size"></a>
<a href="https://gitpod.io/#https://github.com/oldratlee/fucking-java-concurrency"><img src="https://img.shields.io/badge/Gitpod-ready to code-339933?label=gitpod&logo=gitpod&logoColor=white" alt="gitpod: Ready to Code"></a>
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
- [🍺 Livelock caused by reentrant locks](#-livelock-caused-by-reentrant-locks)
    - [Demo description](#demo-description-7)
    - [Problem statement](#problem-statement-7)
    - [Quickly run](#quickly-run-7)
- [🍺 Instruction reordering causes non-final field variable read error](#-instruction-reordering-causes-non-final-field-variable-read-error)
    - [Demo description](#demo-description-8)
    - [Problem statement](#problem-statement-8)
    - [Quickly run](#quickly-run-8)
- [🍺 Cyclic Thread Pool Deadlock](#-cyclic-thread-pool-deadlock)
  - [Demo Description](#demo-description-9)
  - [Problem Description](#problem-description-9)
  - [Quick Run](#quick-run-9)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

----------------------------------------

## 🍺 Update without synchronization cannot be read in another thread

Demo class [`NoPublishDemo`](src/main/java/fucking/concurrency/demo/NoPublishDemo.java).

### Demo description

Set the field `stop` to `true` in the `main` thread to control the exit of the task thread started in `main`.

### Problem statement

After the `main` thread field `stop` is `true`, the task thread continues to run, that is, no new value has been read in the task thread.

### Quickly run

```bash
./mvnw compile exec:java -Dexec.mainClass=fucking.concurrency.demo.NoPublishDemo
```

## 🍺 Infinite loop of `HashMap`

This problem has been explained in many places.

The Demo class [`HashMapHangDemo`](src/main/java/fucking/concurrency/demo/HashMapHangDemo.java) can reproduce this problem.

### Demo description

Two task threads are opened in the main thread to perform the put operation of `HashMap`. The main thread does the get operation.

### Problem statement

The main thread Block is determined by no continuous output, that is, the endless loop of `HashMap` appears.

### Quickly run

```bash
./mvnw compile exec:java -Dexec.mainClass=fucking.concurrency.demo.HashMapHangDemo
```

## 🍺 Combined state read invalid combination

When programming, multiple state records will be required (state can be a `POJO` object or `int`s, etc.).

It is often seen that the multi-state read and write code is not synchronized, and the person who write it will naturally ignore the issue of thread safety.

Invalid combinations are combinations that have never been set.

### Demo description

The main thread modifies multiple states. For the convenience of checking, each write has a fixed relationship: the second state is twice the value of the first state. Read multiple states in a task thread.
Demo class [`InvalidCombinationStateDemo`](src/main/java/fucking/concurrency/demo/InvalidCombinationStateDemo.java).

### Problem statement

The second state read in the task thread is not twice the value of the first state, that is, an invalid value.

### Quickly run

```bash
./mvnw compile exec:java -Dexec.mainClass=fucking.concurrency.demo.InvalidCombinationStateDemo
```

## 🍺 `long` variable read invalid value

An invalid value is a value that has never been set.

Reading and writing of `long` variables is not atomic and will be divided into two 4-byte operations.

Demo class [`InvalidLongDemo`](src/main/java/fucking/concurrency/demo/InvalidLongDemo.java).

### Demo description

The main thread modifies the long variable. For the convenience of checking, the upper 4 bytes and the lower 4 bytes of the long value written each time are the same. Read the long variable in the task thread.

### Problem statement

In the task thread, a long variable whose upper 4 bytes and lower 4 bytes are different is read, which is an invalid value.

### Quickly run

```bash
./mvnw compile exec:java -Dexec.mainClass=fucking.concurrency.demo.InvalidLongDemo
```

## 🍺 the result of concurrency count without synchronization is wrong

Demo class [`WrongCounterDemo`](src/main/java/fucking/concurrency/demo/WrongCounterDemo.java).

### Demo description

Two task threads are opened in the main thread to execute concurrent incrementing counts. Main thread final result check.

### Problem statement

The count value is incorrect.

### Quickly run

```bash
./mvnw compile exec:java -Dexec.mainClass=fucking.concurrency.demo.WrongCounterDemo
```

## 🍺 Synchronization on mutable fields

It is common to see synchronization code on a volatile field, and the person who write it will naturally feel that this is safe and correct.  
\# For problem analysis, see the article [Synchronization on mutable fields](http://www.ibm.com/developerworks/library/j-concurrencybugpatterns/#N100E7).

Demo class [`SynchronizationOnMutableFieldDemo`](src/main/java/fucking/concurrency/demo/SynchronizationOnMutableFieldDemo.java).

### Demo description

Two task threads are opened in the main thread to execute `addListener`. Main thread final result check.

### Problem statement

The final count of Listeners is incorrect.

### Quickly run

```bash
./mvnw compile exec:java -Dexec.mainClass=fucking.concurrency.demo.SynchronizationOnMutableFieldDemo
```

## 🍺 Deadlock caused by the symmetric locks

\# For problem analysis, see the article [Synchronization on mutable fields](http://www.ibm.com/developerworks/library/j-concurrencybugpatterns/#N101C1)

Demo class [`SymmetricLockDeadlockDemo`](src/main/java/fucking/concurrency/demo/SymmetricLockDeadlockDemo.java).

### Demo description

Two task threads are opened in the main thread for execution.

### Problem statement

Task thread deadlocked.

### Quickly run

```bash
./mvnw compile exec:java -Dexec.mainClass=fucking.concurrency.demo.SymmetricLockDeadlockDemo
```

## 🍺 Livelock caused by reentrant locks

\# For a problem description, see the paragraph about livelocks [in this article](https://www.baeldung.com/cs/deadlock-livelock-starvation#livelock)

Demo class [`ReentrantLockLivelockDemo`](src/main/java/fucking/concurrency/demo/ReentrantLockLivelockDemo.java).

### Demo description

Two task threads are trying to acquire a lock that the other thread holds while holding their own lock.

### Problem statement

While the threads are releasing their own lock constantly, they are also re-locking it immediately, denying the other thread
a chance to acquire both locks. Since both threads are not blocked from executing but blocked from doing meaningful work,
this is a livelock.

### Quickly run

```bash
./mvnw compile exec:java -Dexec.mainClass=fucking.concurrency.demo.ReentrantLockLivelockDemo
```

## 🍺 Instruction reordering causes non-final field variable read error

Demo class [`FinalInitialDemo`](src/main/java/fucking/concurrency/demo/FinalInitialDemo.java).

### Demo description

The writer thread calls the constructor of the class, and the reader thread obtains the member variables of the non-final domain of the class.

### Problem statement

When calling the constructor, instruction reordering may occur, placing non-final domain variables outside the constructor,
causing the writer and reader threads to obtain the default initial values of the variables.(Instruction ordering does not necessarily occur
and requires specific hardware and JVM environments).

### Quickly run

```bash
./mvnw compile exec:java -Dexec.mainClass=fucking.concurrency.demo.FinalInitialDemo
```

## 🍺 Cyclic Thread Pool Deadlock

Demo class [`CyclicThreadPoolDeadLockDemo`](../../src/main/java/fucking/concurrency/demo/CyclicThreadPoolDeadLockDemo.java).

### Demo Description

This example demonstrates the issue of deadlock caused by cyclic dependencies between tasks when using thread pools, 
and how to avoid this situation using `CompletableFuture`.

### Problem Description

In the `badCase`, two thread pools, `pool1` and `pool2`, submit tasks to each other, forming a cyclic dependency.
When the thread pool's threads are exhausted, all executing tasks wait for other tasks to complete, leading to a deadlock.
The `goodCase` resolves the deadlock issue by using asynchronous chained calls with `CompletableFuture`, thus avoiding thread pool blocking.

### Quick Run

```bash
./mvnw compile exec:java -Dexec.mainClass=fucking.concurrency.demo.CyclicThreadPoolDeadLockDemo
```
