package com.example.tdd.bank;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * @author xuning
 * @date 2023/10/4 18:26
 */
public class AccountWithdrawTest {

    private static final int ORIGINAL_BALANCE = 10000;
    private Transactions transactions;
    private Account account;

    @BeforeEach
    void setUp() {
        account = new Account();
        transactions = mock(Transactions.class);
        account.setTransactions(transactions);
        account.deposit(ORIGINAL_BALANCE);
    }

    /**
     * 账户状态正常，取款金额小于当前余额时取款成功
     */
    @Test
    public void testShouldSuccess() {
        int amountOfWithdraw = 2000;
        account.withdraw(amountOfWithdraw);
        assertThat(account.getBalance()).isEqualTo(ORIGINAL_BALANCE - amountOfWithdraw);
        verify(transactions).add(account, TransactionType.CREDIT, amountOfWithdraw);
    }

    /**
     * 测试提取全部余额也是正常的
     */
    @Test
    public void testShouldSuccessWithdrawAll() {
        account.withdraw(ORIGINAL_BALANCE);
        assertThat(account.getBalance()).isEqualTo(0);
        verify(transactions).add(account, TransactionType.CREDIT, ORIGINAL_BALANCE);
    }

    /**
     * 账户锁定时应该失败
     */
    @Test
    public void testShouldFailWhenAccountLocked() {
        int withdrawAmount = 2000;
        account.lock();
        assertThrows(AccountLockedException.class, () -> account.withdraw(withdrawAmount));
        assertThat(account.getBalance()).isEqualTo(ORIGINAL_BALANCE);
        verify(transactions, never()).add(account, TransactionType.CREDIT, withdrawAmount);
    }

    /**
     * 取款金额是负数是, 取款应当失败
     */
    @Test
    public void testShouldFailWhenWithdrawAmountLessThanZero() {
        int withdrawAmount = -100;
        assertThrows(InvalidAmountException.class, () -> account.withdraw(withdrawAmount));
        assertThat(account.getBalance()).isEqualTo(ORIGINAL_BALANCE);
        verify(transactions, never()).add(account, TransactionType.CREDIT, withdrawAmount);
    }

    /**
     * 取款金额为0是,应该报错
     */
    @Test
    public void testShouldFailWhenWithdrawAmountEqZero() {
        int withdrawAmount = 0;
        assertThrows(InvalidAmountException.class, () -> account.withdraw(withdrawAmount));
        assertThat(account.getBalance()).isEqualTo(ORIGINAL_BALANCE);
        verify(transactions, never()).add(account, TransactionType.CREDIT, withdrawAmount);
    }

    /**
     * 余额不足时, 当当失败
     */
    @Test
    public void testShouldFailWhenBalanceInsufficient() {
        int withdrawAmount = ORIGINAL_BALANCE + 1;
        assertThrows(BalanceInsufficientException.class, () -> account.withdraw(withdrawAmount));
        verify(transactions, never()).add(account, TransactionType.CREDIT, withdrawAmount);
    }
}
