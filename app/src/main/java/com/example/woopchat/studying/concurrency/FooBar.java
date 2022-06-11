package com.example.woopchat.studying.concurrency;

class FooBar {
    private int n;

    public FooBar(int n) {
        this.n = n;
    }

    private volatile boolean isFoo = true;

    public void foo(Runnable printFoo) throws InterruptedException {
        for (int i = 0; i < n; i++) {
            synchronized (this) {
                if (!isFoo) wait();
                printFoo.run();
                isFoo = false;
                notify();
            }
        }
    }

    public void bar(Runnable printBar) throws InterruptedException {
        for (int i = 0; i < n; i++) {
            synchronized (this) {
                if (isFoo) wait();
                printBar.run();
                isFoo = true;
                notify();
            }
        }
    }
}