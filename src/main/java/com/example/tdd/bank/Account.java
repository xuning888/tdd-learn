package com.example.tdd.bank;


/**
 * @author xuning
 * @date 2023/10/4 18:12
 */
public class Account {

    /**
     * 内部状态: 账户是否被冻结
     */
    private boolean locked = false;

    /**
     * 内部状态: 当前余额
     */
    private int balance = 0;

    /**
     * 外部依赖(协作者): 记录每一笔收支
     */
    private Transactions transactions;

    public boolean isLocked() {
        return this.locked;
    }

    public int getBalance() {
        return this.balance;
    }

    public void setTransactions(Transactions transactions) {
        this.transactions = transactions;
    }

    /**
     * 存款工作单元
     *
     * @param amount 存款金额
     */
    public void deposit(int amount) {
        // 失败路径1: 账户被冻结时不允许存款
        if (this.isLocked()) {
            throw new AccountLockedException();
        }

        // 失败路径2: 存款金额不是证书时不允许存款
        if (amount <= 0) {
            throw new InvalidAmountException();
        }
        // 成功(快乐)路径
        // 存款成功后改变内部状态
        balance += amount;
        // 存款成功后调用外部协作者
        this.transactions.add(this, TransactionType.DEBIT, amount);
    }

    public void withdraw(int amount) {
        // 失败路径1: 账户冻结是不允许取款
        if (this.isLocked()) {
            throw new AccountLockedException();
        }
        // 失败路径2: 取款金额不是证书时不允许取款
        if (amount <= 0) {
            throw new InvalidAmountException();
        }
        // 失败路径3: 取款金额超过余额时不允许取款
        if (amount > this.balance) {
            throw new BalanceInsufficientException();
        }
        // 成功(快乐)路径
        // 取款成功后改变内部状态
        this.balance -= amount;
        // 取款成功后调用外部协作者
        this.transactions.add(this, TransactionType.CREDIT, amount);
    }

    /**
     * 冻结账户的工作单元
     */
    public void lock() {
        this.locked = true;
    }

    /**
     * 解冻账户的工作单元
     */
    public void unlock() {
        this.locked = false;
    }
}

