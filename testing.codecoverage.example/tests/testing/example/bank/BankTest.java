package testing.example.bank;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class BankTest {

	private static final int AMOUNT = 5;

	private static final int INITIAL_BALANCE = 10;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

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
		thrown.expect(NoSuchElementException.class);
		thrown.expectMessage("No account found with id: 1");
		bank.deposit(1, INITIAL_BALANCE);
	}

	@Test
	public void testDepositWhenAccountIsFoundShouldIncrementBalance() {
		// setup
		BankAccount testAccount = createTestAccount(INITIAL_BALANCE);
		bankAccounts.add(testAccount);
		// exercise
		bank.deposit(testAccount.getId(), AMOUNT);
		// verify
		assertEquals(INITIAL_BALANCE + AMOUNT, testAccount.getBalance(), 0);
	}

	@Test
	public void testWithdrawWhenAccountIsNotFoundShouldThrow() {
		thrown.expect(NoSuchElementException.class);
		thrown.expectMessage("No account found with id: 1");
		bank.withdraw(1, AMOUNT);
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
