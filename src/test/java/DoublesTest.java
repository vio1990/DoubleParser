import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

public class DoublesTest {
    @Test
    public void checkNumbers() {
        Double actual = new Doubles().parse("125");
        assertThat(actual, is(125.0));
    }

    @Test
    public void checkSignNegativeNumbers() {
        Double actual = new Doubles().parse("-125");
        assertThat(actual, is(-125.0));
    }

    @Test
    public void checkSignPostiveNumbers() {
        Double actual = new Doubles().parse("+125");
        assertThat(actual, is(+125.0));
    }

    @Test
    public void checkCommaNumbers() {
        Double actual = new Doubles().parse("+125.0");
        assertThat(actual, is(125.0));
    }

    @Test
    public void checkDecimalPart() {
        Double actual = new Doubles().parse("125.256");
        assertThat(actual, is(125.256));
    }

    @Test
    public void checkOnlyDecimalPart() {
        Double actual = new Doubles().parse(".256");
        assertThat(actual, is(.256));
    }

    @Test
    public void checkOnlyDotInput() {
        Double actual = new Doubles().parse(".");
        assertNull(actual);
    }

    @Test
    public void checkExpPart() {
        Double actual = new Doubles().parse("2.e2");
        assertThat(actual, is(2.e2));
    }

    @Test
    public void checkComplexNumberInput() {
        Double actual = new Doubles().parse("-.56e-2");
        assertThat(actual, is(-.56e-2));
    }
}
