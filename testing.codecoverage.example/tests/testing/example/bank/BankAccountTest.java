package testing.example.bank;

import static org.junit.Assert.*;

import java.util.function.BiConsumer;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class BankAccountTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	private static final int AMOUNT = 3;

	private static final int INITIAL_BALANCE = 10;

	@Test
	public void testIdIsAutomaticallyAssignedAsPositiveNumber() {
		// setup
		BankAccount bankAccount = new BankAccount();
		// verify
		assertTrue("Id should be positive", bankAccount.getId() > 0);
	}

	@Test
	public void testIdsAreIncremental() {
		assertTrue("Ids should be incremental", new BankAccount().getId() < new BankAccount().getId());
	}

	@Test
	public void testDepositWhenAmountIsCorrectShouldIncreaseBalance() {
		// setup
		BankAccount bankAccount = new BankAccount();
		bankAccount.setBalance(INITIAL_BALANCE);
		// exercise
		bankAccount.deposit(AMOUNT);
		// verify
		assertEquals(INITIAL_BALANCE+AMOUNT, bankAccount.getBalance(), 0);
	}

	@Test
	public void testDepositWhenAmountIsNegativeShouldThrow() {
		// setup
		BankAccount bankAccount = new BankAccount();
		try {
			// exercise
			bankAccount.deposit(-1);
			fail("Expected an IllegalArgumentException to be thrown");
		} catch (IllegalArgumentException e) {
			// verify
			assertEquals("Negative amount: -1.0", e.getMessage());
			assertEquals(0, bankAccount.getBalance(), 0);
		}
	}

	@Test
	public void testDepositWhenAmountIsNegativeShouldThrowAlternative() {
		BankAccount bankAccount = new BankAccount();
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Negative amount: -1.0");
		bankAccount.deposit(-1);
		// but we can't perform further assertions...
	}

	@Test
	public void testWithdrawWhenAmountIsNegativeShouldThrow() {
		BankAccount bankAccount = new BankAccount();
		try {
			bankAccount.withdraw(-1);
			fail("Expected an IllegalArgumentException to be thrown");
		} catch (IllegalArgumentException e) {
			assertEquals("Negative amount: -1.0", e.getMessage());
			assertEquals(0, bankAccount.getBalance(), 0);
		}
	}

	@Test
	public void testWithdrawWhenBalanceIsUnsufficientShouldThrow() {
		BankAccount bankAccount = new BankAccount();
		try {
			bankAccount.withdraw(AMOUNT);
			fail("Expected an IllegalArgumentException to be thrown");
		} catch (IllegalArgumentException e) {
			assertEquals("Cannot withdraw 3.0 from 0.0", e.getMessage());
			assertEquals(0, bankAccount.getBalance(), 0);
		}
	}

	@Test
	public void testWithdrawWhenBalanceIsSufficientShouldDecreaseBalance() {
		// setup
		BankAccount bankAccount = new BankAccount();
		bankAccount.setBalance(INITIAL_BALANCE);
		// exercise
		bankAccount.withdraw(AMOUNT); // the method we want to test
		// verify
		assertEquals(INITIAL_BALANCE-AMOUNT, bankAccount.getBalance(), 0);
	}

	@Test
	public void testDepositWhenAmountIsNegativeShouldThrowRefactored() {
		assertActionWithNegativeAmount(BankAccount::deposit);
	}

	@Test
	public void testWithdrawWhenAmountIsNegativeShouldThrowRefactored() {
		assertActionWithNegativeAmount(BankAccount::withdraw);
	}

	private void assertActionWithNegativeAmount(BiConsumer<BankAccount, Double> action) {
		// setup
		BankAccount bankAccount = new BankAccount();
		try {
			// exercise
			action.accept(bankAccount, -1.0);
			fail("Expected an IllegalArgumentException to be thrown");
		} catch (IllegalArgumentException e) {
			// verify
			assertEquals("Negative amount: -1.0", e.getMessage());
			assertEquals(0, bankAccount.getBalance(), 0);
		}
	}
}
