package b_Money;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AccountTest {
	Currency SEK, DKK;
	Bank Nordea;
	Bank DanskeBank;
	Bank SweBank;
	Account testAccount;

	@Before
	public void setUp() throws Exception {
		SEK = new Currency("SEK", 0.15);
		SweBank = new Bank("SweBank", SEK);
		SweBank.openAccount("Alice");
		testAccount = new Account("Hans", SEK);
		testAccount.deposit(new Money(10000000, SEK));

		SweBank.deposit("Alice", new Money(1000000, SEK));
	}

	@Test
	public void testAddRemoveTimedPayment() {
		assertFalse("Cant pay before", testAccount.timedPaymentExists("payment"));
		testAccount.addTimedPayment("payment", 1, 1, new Money(1234_00, SEK), SweBank, "Alice");
		assertTrue("Payment already exist", testAccount.timedPaymentExists("payment"));
		testAccount.removeTimedPayment("payment");
		assertFalse("Cant pay again", testAccount.timedPaymentExists("payment"));
	}

	@Test
	public void testTimedPayment() throws AccountDoesNotExistException {
		testAccount.addTimedPayment("payment", 1, 1, new Money(123_00, SEK), SweBank, "Alice");
		assertEquals("0H", Integer.valueOf(10000000), testAccount.getBalance().getAmount());//hans
		assertEquals("0A", Integer.valueOf(1000000), SweBank.getBalance("Alice"));//alice
		testAccount.tick();
		assertEquals("1H", Integer.valueOf(10000000), testAccount.getBalance().getAmount());
		assertEquals("1A", Integer.valueOf(1000000), SweBank.getBalance("Alice"));
		testAccount.tick();
		assertEquals("2H", Integer.valueOf(99877_00), testAccount.getBalance().getAmount());//-123
		assertEquals("2A", Integer.valueOf(10123_00), SweBank.getBalance("Alice"));//+123
		testAccount.tick();
	}

	@Test
	public void testAddWithdraw() {
		testAccount.deposit(new Money(100, SEK));
		assertEquals("After Deposit the amount doesnt match", Integer.valueOf(100001_00), testAccount.getBalance().getAmount());
		testAccount.withdraw(new Money(100, SEK));
		assertEquals("After withdraw the amount doesnt match", Integer.valueOf(100000_00), testAccount.getBalance().getAmount());
	}

	@Test
	public void testGetBalance() {
		assertEquals("Test account amount doesnt match", Integer.valueOf(10000000), testAccount.getBalance().getAmount());
		try {
			assertEquals("Balance doesnt match", Integer.valueOf(1000000), SweBank.getBalance("Alice"));
		} catch (AccountDoesNotExistException e) {
			e.printStackTrace();
		}
	}
}
