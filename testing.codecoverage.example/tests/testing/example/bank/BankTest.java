package testing.example.bank;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.Before;
import org.junit.Test;

public class BankTest {

	private static final int AMOUNT = 5;

	private static final int INITIAL_BALANCE = 10;

	private Bank bank;

	// the collaborator of Bank that we manually instrument and inspect
	private List<BankAccount> bankAccounts;

	@Before
	public void setup() {
		bankAccounts = new ArrayList<BankAccount>();
		bank = new Bank(bankAccounts);
	}

	@Test
	public void testOpenNewAccountShouldReturnAPositiveIdAndStoreTheAccount() {
		int newAccountId = bank.openNewBankAccount(0);
		assertTrue("Unexpected non positive id: " + newAccountId, newAccountId > 0);
		assertEquals(newAccountId, bankAccounts.get(0).getId());
	}

	@Test
	public void testDepositWhenAccountIsNotFoundShouldThrow() {
		NoSuchElementException thrown = assertThrows(NoSuchElementException.class,
				() -> bank.deposit(1, INITIAL_BALANCE));
		assertEquals("No account found with id: 1", thrown.getMessage());
	}

	@Test
	public void testDepositWhenAccountIsFoundShouldIncrementBalance() {
		// setup
		BankAccount toBeFound = createTestAccount(INITIAL_BALANCE);
		bankAccounts.add(toBeFound);
		BankAccount another = createTestAccount(INITIAL_BALANCE);
		bankAccounts.add(another);
		// exercise
		bank.deposit(toBeFound.getId(), AMOUNT);
		// verify
		assertEquals(INITIAL_BALANCE + AMOUNT, toBeFound.getBalance(), 0);
	}

	@Test
	public void testWithdrawWhenAccountIsNotFoundShouldThrow() {
		NoSuchElementException thrown = assertThrows(NoSuchElementException.class,
				() -> bank.withdraw(1, AMOUNT));
		assertEquals("No account found with id: 1", thrown.getMessage());
	}

	@Test
	public void testWithdrawWhenAccountIsFoundShouldDecrementBalance() {
		// setup
		BankAccount testAccount = createTestAccount(INITIAL_BALANCE);
		bankAccounts.add(testAccount);
		// exercise
		bank.withdraw(testAccount.getId(), AMOUNT);
		// verify
		assertEquals(INITIAL_BALANCE-AMOUNT, testAccount.getBalance(), 0);
	}

	/**
	 * Utility method for creating a BankAccount for testing.
	 */
	private BankAccount createTestAccount(double initialBalance) {
		BankAccount bankAccount = new BankAccount();
		bankAccount.setBalance(initialBalance);
		return bankAccount;
	}
}
