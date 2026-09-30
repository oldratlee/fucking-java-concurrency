# <div align="center"> 🎏 fucking-java-concurrency</div>

<p align="center">
<a href="https://github.com/oldratlee/fucking-java-concurrency/actions/workflows/ci.yaml">
  <img src="https://img.shields.io/github/actions/workflow/status/oldratlee/fucking-java-concurrency/ci.yaml?branch=master&logo=github&logoColor=white" alt="Github Workflow Build Status"></a>
<a href="https://openjdk.java.net/">
  <img src="https://img.shields.io/badge/Java-8+-339933?logo=openjdk&logoColor=white" alt="Java support"></a>
<a href="https://www.apache.org/licenses/LICENSE-2.0.html">
  <img src="https://img.shields.io/github/license/oldratlee/fucking-java-concurrency?color=4D7A97&logo=apache" alt="License"></a>
<a href="https://github.com/oldratlee/fucking-java-concurrency/stargazers">
  <img src="https://img.shields.io/github/stars/oldratlee/fucking-java-concurrency?style=flat" alt="GitHub Stars"></a>
<a href="https://github.com/oldratlee/fucking-java-concurrency/fork">
  <img src="https://img.shields.io/github/forks/oldratlee/fucking-java-concurrency?style=flat" alt="GitHub Forks"></a>
<a href="https://github.com/oldratlee/fucking-java-concurrency/graphs/contributors">
  <img src="https://img.shields.io/github/contributors/oldratlee/fucking-java-concurrency?style=flat" alt="GitHub Contributors"></a>
<a href="https://github.com/oldratlee/fucking-java-concurrency">
  <img src="https://img.shields.io/github/repo-size/oldratlee/fucking-java-concurrency?style=flat" alt="GitHub repo size"></a>
<a href="https://gitpod.io/#https://github.com/oldratlee/fucking-java-concurrency">
  <img src="https://img.shields.io/badge/Gitpod-ready to code-339933?label=gitpod&logo=gitpod&logoColor=white" alt="gitpod: Ready to Code"></a>
</p>

----------------------------------------

📖 English Documentation | [📖 中文文档](docs/zh-CN/README.md)

Minimal demos of `Java` concurrency problems — seeing 🙈 is believing 🐵.

## 🍎 Why these demos

- An observed symptom 🙈 is more intuitive and convincing than a stated
  concurrency principle 🙊.
- The `Java` standard library supports threads, and multithreading is
  heavily used both in the language itself (e.g. `GC`) and in
  applications (server side).
- Concurrency greatly increases the complexity of program design,
  analysis, and implementation. Unless you fully understand and
  systematically analyze the concurrent logic, writing code at random
  means it is no exaggeration to say that such a program runs
  correctly **by accident**.
    - The demos here come with no explanation or discussion, and all of
      them are entry-level 🤓 . For more information, look
      up concurrency material yourself.

Hit a concurrency problem in your own development? Please share it —
[submit an issue](https://github.com/oldratlee/fucking-java-concurrency/issues)
or [fork the repo](https://github.com/oldratlee/fucking-java-concurrency/fork)
and open a pull request! 😘

----------------------------------------

<img src="docs/dining-philosophers-problem.jpg" width="30%" align="right" />

<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->

- [🍺 Unsynchronized updates are not visible to other threads](#-unsynchronized-updates-are-not-visible-to-other-threads)
    - [Demo description](#demo-description)
    - [Problem statement](#problem-statement)
    - [Quick run](#quick-run)
- [🍺 Infinite loop in `HashMap`](#-infinite-loop-in-hashmap)
    - [Demo description](#demo-description-1)
    - [Problem statement](#problem-statement-1)
    - [Quick run](#quick-run-1)
- [🍺 Reading combined state yields an invalid combination](#-reading-combined-state-yields-an-invalid-combination)
    - [Demo description](#demo-description-2)
    - [Problem statement](#problem-statement-2)
    - [Quick run](#quick-run-2)
- [🍺 Reading a `long` variable yields an invalid value](#-reading-a-long-variable-yields-an-invalid-value)
    - [Demo description](#demo-description-3)
    - [Problem statement](#problem-statement-3)
    - [Quick run](#quick-run-3)
- [🍺 Consecutive reads of the same field see different values](#-consecutive-reads-of-the-same-field-see-different-values)
    - [Demo description](#demo-description-4)
    - [Problem statement](#problem-statement-4)
    - [Quick run](#quick-run-4)
- [🍺 Unsynchronized concurrent counting gives wrong results](#-unsynchronized-concurrent-counting-gives-wrong-results)
    - [Demo description](#demo-description-5)
    - [Problem statement](#problem-statement-5)
    - [Quick run](#quick-run-5)
- [🍺 Synchronization on mutable fields](#-synchronization-on-mutable-fields)
    - [Demo description](#demo-description-6)
    - [Problem statement](#problem-statement-6)
    - [Quick run](#quick-run-6)
- [🍺 Deadlock caused by symmetric locks](#-deadlock-caused-by-symmetric-locks)
    - [Demo description](#demo-description-7)
    - [Problem statement](#problem-statement-7)
    - [Quick run](#quick-run-7)
- [🍺 Livelock caused by reentrant locks](#-livelock-caused-by-reentrant-locks)
    - [Demo description](#demo-description-8)
    - [Problem statement](#problem-statement-8)
    - [Quick run](#quick-run-8)
- [🍺 Instruction reordering causes incorrect reads of non-final fields](#-instruction-reordering-causes-incorrect-reads-of-non-final-fields)
    - [Demo description](#demo-description-9)
    - [Problem statement](#problem-statement-9)
    - [Quick run](#quick-run-9)
- [🍺 Cyclic thread pool deadlock](#-cyclic-thread-pool-deadlock)
    - [Demo description](#demo-description-10)
    - [Problem statement](#problem-statement-10)
    - [Quick run](#quick-run-10)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

----------------------------------------

## 🍺 Unsynchronized updates are not visible to other threads

Demo class [`NoPublishDemo`](src/main/java/fucking/concurrency/demo/NoPublishDemo.java).

### Demo description

The main thread sets the field `stop` to `true` to signal the task
thread (started in `main`) to exit.

### Problem statement

After the main thread sets `stop` to `true`, the task thread keeps
running — i.e. it never sees the new value.

### Quick run

```bash
./mvnw compile exec:java -Dexec.mainClass=fucking.concurrency.demo.NoPublishDemo
```

## 🍺 Infinite loop in `HashMap`

This problem has been explained in many places.

The Demo class [`HashMapHangDemo`](src/main/java/fucking/concurrency/demo/HashMapHangDemo.java)
can reproduce this problem.

### Demo description

The main thread starts two task threads that `put` into the `HashMap`,
while itself repeatedly performing `get`.

### Problem statement

The main thread blocks (output stops), i.e. the `HashMap` has entered
an infinite loop.

### Quick run

```bash
./mvnw compile exec:java -Dexec.mainClass=fucking.concurrency.demo.HashMapHangDemo
```

## 🍺 Reading combined state yields an invalid combination

Programs often need to track several related pieces of state (a
`POJO`, a few `int`s, etc.).

Multi-state read/write code is frequently left unsynchronized, and
whoever writes it naturally overlooks thread safety.

An *invalid combination* is a combination of values that the program
never actually wrote.

### Demo description

The main thread modifies multiple states. For convenience of checking,
every write keeps a fixed relationship: the second state is always
twice the first. A task thread reads the states.
Demo class [`InvalidCombinationStateDemo`](src/main/java/fucking/concurrency/demo/InvalidCombinationStateDemo.java).

### Problem statement

The task thread reads a second state that is not twice the first — a
combination that was never written.

### Quick run

```bash
./mvnw compile exec:java -Dexec.mainClass=fucking.concurrency.demo.InvalidCombinationStateDemo
```

## 🍺 Reading a `long` variable yields an invalid value

An invalid value is one that was never written.

Reads and writes of non-`volatile` `long` and `double` are not guaranteed
to be atomic.

per the Java Language Specification (§17.7), a single
read/write of a 64-bit value may be treated as two separate 32-bit
accesses. On 32-bit JVMs this is necessarily split into two 4-byte
operations; on 64-bit JVMs it is typically done atomically in practice,
but the specification still does not guarantee atomicity.

Demo class [`InvalidLongDemo`](src/main/java/fucking/concurrency/demo/InvalidLongDemo.java).

### Demo description

The main thread modifies the `long` variable. For convenience of
checking, the upper and lower 4 bytes of every value written are
identical. A task thread reads the `long`.

### Problem statement

The task thread reads a `long` whose upper and lower 4 bytes differ —
a value that was never written.

### Quick run

```bash
./mvnw compile exec:java -Dexec.mainClass=fucking.concurrency.demo.InvalidLongDemo
```

## 🍺 Consecutive reads of the same field see different values

Demo class [`InconsistentReadDemo`](src/main/java/fucking/concurrency/demo/InconsistentReadDemo.java).

### Demo description

The main thread increments the plain `count` field in a tight loop.
A task thread performs two consecutive reads of `count` and reports
each time the two values differ.

### Problem statement

Two consecutive reads of the same non-volatile field by the same
thread return different values. For a plain field, each read may
observe a different write, so a thread must not assume that two reads
see the same value.

### Quick run

```bash
./mvnw compile exec:java -Dexec.mainClass=fucking.concurrency.demo.InconsistentReadDemo
```

## 🍺 Unsynchronized concurrent counting gives wrong results

Demo class [`WrongCounterDemo`](src/main/java/fucking/concurrency/demo/WrongCounterDemo.java).

### Demo description

The main thread starts two task threads that increment a shared
counter concurrently, then checks the final result.

### Problem statement

The final count is incorrect.

### Quick run

```bash
./mvnw compile exec:java -Dexec.mainClass=fucking.concurrency.demo.WrongCounterDemo
```

## 🍺 Synchronization on mutable fields

Synchronizing on a `volatile` field is common, and whoever writes it
naturally assumes this is safe and correct.  
\# For problem analysis, see the article
[Synchronization on mutable fields](http://www.ibm.com/developerworks/library/j-concurrencybugpatterns/#N100E7).

Demo class [`SynchronizationOnMutableFieldDemo`](src/main/java/fucking/concurrency/demo/SynchronizationOnMutableFieldDemo.java).

### Demo description

The main thread starts two task threads that call `addListener`, then
checks the final result.

### Problem statement

The final listener count is incorrect.

### Quick run

```bash
./mvnw compile exec:java -Dexec.mainClass=fucking.concurrency.demo.SynchronizationOnMutableFieldDemo
```

## 🍺 Deadlock caused by symmetric locks

\# For problem analysis, see the article
[Synchronization on mutable fields](http://www.ibm.com/developerworks/library/j-concurrencybugpatterns/#N101C1)

Demo class [`SymmetricLockDeadlockDemo`](src/main/java/fucking/concurrency/demo/SymmetricLockDeadlockDemo.java).

### Demo description

The main thread starts two task threads, each of which takes the two
locks in the opposite order from the other.

### Problem statement

The task threads deadlock.

### Quick run

```bash
./mvnw compile exec:java -Dexec.mainClass=fucking.concurrency.demo.SymmetricLockDeadlockDemo
```

## 🍺 Livelock caused by reentrant locks

\# For a problem description, see the paragraph about livelocks in
[the article](https://www.baeldung.com/cs/deadlock-livelock-starvation#livelock)

Demo class [`ReentrantLockLivelockDemo`](src/main/java/fucking/concurrency/demo/ReentrantLockLivelockDemo.java).

### Demo description

Two task threads try to acquire the lock the other thread holds while
holding their own lock.

### Problem statement

The threads release their own lock and immediately re-acquire it,
denying the other thread a chance to acquire both locks. The threads
keep executing: an attempt wastes its work, and overall they make
almost no actual progress. This is a livelock.

### Quick run

```bash
./mvnw compile exec:java -Dexec.mainClass=fucking.concurrency.demo.ReentrantLockLivelockDemo
```

## 🍺 Instruction reordering causes incorrect reads of non-final fields

Demo class [`FinalInitialDemo`](src/main/java/fucking/concurrency/demo/FinalInitialDemo.java).

### Demo description

The writer thread calls the class constructor while the reader thread
reads the class's non-final fields.

### Problem statement

When the constructor runs, instruction reordering can move the stores
of non-final fields outside it, so the reader thread may observe the
fields' default values instead of the values set by the constructor.
(Reordering is not guaranteed; it requires specific hardware and JVM
conditions.)

### Quick run

```bash
./mvnw compile exec:java -Dexec.mainClass=fucking.concurrency.demo.FinalInitialDemo
```

## 🍺 Cyclic thread pool deadlock

Demo class [`CyclicThreadPoolDeadLockDemo`](src/main/java/fucking/concurrency/demo/CyclicThreadPoolDeadLockDemo.java).

### Demo description

This example demonstrates the deadlock caused by cyclic dependencies
between tasks when using thread pools, and how to avoid it with
`CompletableFuture`.

### Problem statement

In the `badCase`, two thread pools, `pool1` and `pool2`, submit tasks
to each other, forming a cyclic dependency.
When the pools' threads are exhausted, every running task waits for
another task to complete, and the pools deadlock.
The `goodCase` replaces this with asynchronous chained calls using
`CompletableFuture`, so no pool thread blocks.

### Quick run

```bash
./mvnw compile exec:java -Dexec.mainClass=fucking.concurrency.demo.CyclicThreadPoolDeadLockDemo
```
