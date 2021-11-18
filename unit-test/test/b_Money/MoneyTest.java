package b_Money;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class MoneyTest {
	Currency SEK, DKK, NOK, EUR;
	Money SEK100, EUR10, SEK200, EUR20, SEK0, EUR0, SEKn100;

	@Before
	public void setUp() throws Exception {
		SEK = new Currency("SEK", 0.15);
		DKK = new Currency("DKK", 0.20);
		EUR = new Currency("EUR", 1.5);
		SEK100 = new Money(10000, SEK);
		EUR10 = new Money(1000, EUR);
		SEK200 = new Money(20000, SEK);
		EUR20 = new Money(2000, EUR);
		SEK0 = new Money(0, SEK);
		EUR0 = new Money(0, EUR);
		SEKn100 = new Money(-10000, SEK);
	}

	@Test
	public void testGetAmount() {
		assertEquals("Amount doesn't match",Integer.valueOf(10000), SEK100.getAmount());
		assertEquals("Amount doesn't match",Integer.valueOf(1000), EUR10.getAmount());
		assertEquals("Amount doesn't match",Integer.valueOf(20000), SEK200.getAmount());
		assertEquals("Amount doesn't match",Integer.valueOf(2000), EUR20.getAmount());
		assertEquals("Amount doesn't match",Integer.valueOf(0), SEK0.getAmount());
		assertEquals("Amount doesn't match",Integer.valueOf(0), EUR0.getAmount());
		assertEquals("Amount doesn't match",Integer.valueOf(-10000), SEKn100.getAmount());
	}

	@Test
	public void testGetCurrency() {
		assertEquals("Currency doesn't match","SEK", SEK100.getCurrency().getName());
		assertEquals("Currency doesn't match","EUR", EUR10.getCurrency().getName());
		assertEquals("Currency doesn't match","SEK", SEK200.getCurrency().getName());
		assertEquals("Currency doesn't match", "EUR", EUR20.getCurrency().getName());
		assertEquals("Currency doesn't match","SEK", SEK0.getCurrency().getName());
		assertEquals("Currency doesn't match","EUR", EUR0.getCurrency().getName());
		assertEquals("Currency doesn't match","SEK", SEKn100.getCurrency().getName());
	}

	@Test
	public void testToString() {
		assertEquals("To string doesnt match","100.0 SEK", SEK100.toString());
		assertEquals("To string doesnt match","10.0 EUR", EUR10.toString());
		assertEquals("To string doesnt match","200.0 SEK", SEK200.toString());
		assertEquals("To string doesnt match", "20.0 EUR", EUR20.toString());
		assertEquals("To string doesnt match","0 SEK", SEK0.toString());
		assertEquals("To string doesnt match","0 EUR", EUR0.toString());
		assertEquals("To string doesnt match","-100.0 SEK", SEKn100.toString());
	}

	@Test
	public void testGlobalValue() {
		assertEquals("Universal value is not equal", Integer.valueOf(0), EUR0.universalValue());
		assertEquals("Universal value is not equal", Integer.valueOf(1500), SEK100.universalValue());
		assertEquals("Universal value is not equal", Integer.valueOf(-1500), SEKn100.universalValue());
		assertEquals("Universal value is not equal", Integer.valueOf(0), SEK0.universalValue());
	}

	@Test
	public void testEqualsMoney() {
		assertFalse("Not Equal", SEK100.equals(EUR20));
		assertTrue("Not Equal", SEK0.equals(EUR0));
		assertFalse("Not Equal", SEKn100.equals(EUR10));
	}

	@Test
	public void testAdd() {
		assertEquals("Amount not equal after add",Integer.valueOf(30000), SEK100.add(SEK200).getAmount());
		assertEquals("Amount not equal after add",Integer.valueOf(20000), SEK100.add(EUR10).getAmount());
		assertEquals("Amount not equal after add",Integer.valueOf(3000), EUR20.add(SEK100).getAmount());
		assertEquals("Amount not equal after add",Integer.valueOf(0), SEK100.add(SEKn100).getAmount());
		assertEquals("Amount not equal after add",Integer.valueOf(4000), EUR20.add(SEK200).getAmount());

	}

	@Test
	public void testSub() {

		assertEquals("Amount not equal after sub",Integer.valueOf(10000), SEK200.sub(EUR10).getAmount());
		assertEquals("Amount not equal after sub",Integer.valueOf(-10000), SEK100.sub(SEK200).getAmount());
		assertEquals("Amount not equal after sub",Integer.valueOf(-10000), SEK100.sub(EUR20).getAmount());
		assertEquals("Amount not equal after sub",Integer.valueOf(1000), EUR20.sub(SEK100).getAmount());
		assertEquals("Amount not equal after sub",Integer.valueOf(1000), EUR0.sub(SEKn100).getAmount());
		assertEquals("Amount not equal after sub",Integer.valueOf(-2000), EUR0.sub(EUR20).getAmount());
	}

	@Test
	public void testIsZero() {
		assertTrue("not zero",SEK0.isZero());
		assertTrue("not zero",EUR0.isZero());
		assertFalse("zero",EUR10.isZero());
		assertFalse("zero",SEK200.isZero());
	}

	@Test
	public void testNegate() {
		assertEquals("Amount not equal after negate",Integer.valueOf(10000), SEKn100.negate().getAmount());
		assertEquals("Amount not equal after negate",Integer.valueOf(-1000), EUR10.negate().getAmount());
		assertEquals("Amount not equal after negate",Integer.valueOf(0), EUR0.negate().getAmount());
		assertEquals("Amount not equal after negate",Integer.valueOf(0), SEK0.negate().getAmount());
	}

	@Test
	public void testCompareTo() {
		assertTrue("",EUR20.compareTo(SEK100) > 0);
		assertTrue("",SEK200.compareTo(SEK100) > 0);
		assertTrue("",SEKn100.compareTo(EUR0) < 0);
		assertTrue("",EUR10.compareTo(SEKn100) > 0);
		assertEquals("",0, SEK100.compareTo(EUR10));//equals
	}
}
