package com.example.tdd.bank;

/**
 * @author xuning
 * @date 2023/10/4 18:14
 */
public interface Transactions {

    void add(Account account, TransactionType transactionType, int amount);
}
