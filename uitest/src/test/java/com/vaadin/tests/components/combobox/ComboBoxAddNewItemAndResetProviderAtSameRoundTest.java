package com.vaadin.tests.components.combobox;

import org.junit.Test;
import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Actions;

import com.vaadin.testbench.By;
import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.CheckBoxElement;
import com.vaadin.testbench.elements.ComboBoxElement;
import com.vaadin.testbench.elements.LabelElement;
import com.vaadin.tests.tb3.SingleBrowserTest;

import static org.junit.Assert.assertTrue;

public class ComboBoxAddNewItemAndResetProviderAtSameRoundTest
        extends SingleBrowserTest {

    protected enum SelectionType {
        ENTER, TAB, CLICK_OUT;
    }

    private ComboBoxElement comboBoxElement;
    private LabelElement resetLabelElement;
    private LabelElement valueLabelElement;
    private String inputValue = "000";
    private Boolean delay = false;

    @Override
    public void setup() throws Exception {
        super.setup();
        openTestURL();
        waitForElementPresent(By.className("v-filterselect"));
        comboBoxElement = $(ComboBoxElement.class).first();
        resetLabelElement = $(LabelElement.class).id("reset-label");
        valueLabelElement = $(LabelElement.class).id("value-label");
    }

    /**
     * Scenario: add new item and reset the data provider in the same round, then
     * add the same value again with ENTER
     */
    @Test
    public void addNewItemAndReset_reAddWithEnter() {
        itemHandling(SelectionType.ENTER, inputValue);
    }

    /**
     * Scenario: add new item and reset the data provider in the same round, then
     * add the same value again with TAB
     */
    @Test
    public void addNewItemAndReset_reAddWithTab() {
        itemHandling(SelectionType.TAB, inputValue);
    }

    /**
     * Scenario: add new item and reset the data provider in the same round, then
     * add the same value again with clicking out side of the CB
     */
    @Test
    public void addNewItemAndReset_reAddWithClickOut() {
        itemHandling(SelectionType.CLICK_OUT, inputValue);
    }

    /**
     * Scenario: add new item and reset the data provider in the same round with
     * 2 seconds delay, then add the same value again with ENTER
     */
    @Test
    public void slowAddNewItemAndReset_reAddWithEnter() {
        delay(true);
        itemHandling(SelectionType.ENTER, inputValue);
    }

    /**
     * Scenario: add new item and reset the data provider in the same round with
     * 2 seconds delay, then add the same value again with TAB
     */
    @Test
    public void slowAddNewItemAndReset_reAddWithTab() {
        delay(true);
        itemHandling(SelectionType.TAB, inputValue);
    }

    /**
     * Scenario: add new item and reset the data provider in the same round with
     * 2 seconds delay, then add the same value again with clicking out side
     */
    @Test
    public void slowAddNewItemAndReset_reAddWithClickOut() {
        delay(true);
        itemHandling(SelectionType.CLICK_OUT, inputValue);
    }

    private void itemHandling(SelectionType selectionType, String input) {
        assertValueLabelText("Value Label");
        sendKeysToInput(input);
        if (delay) {
            sleep(2000);
        }
        assertResetLabelText("Reset Label");

        // reset the dataProvider
        reset();
        sleep(200);
        assertResetLabelText("Reset");
        assertValueLabelText("Value is reset");

        // re-add the same value and select
        sendKeysToInput(input);
        performSelect(selectionType);
        if (delay) {
            sleep(2000);
        } else {
            sleep(200);
        }
        assertValueLabelText(input);
    }

    private void assertResetLabelText(String text) {
        resetLabelElement = $(LabelElement.class).id("reset-label");
        String resetLabel = resetLabelElement.getText();
        assertTrue("Data Provider should have been reset.",
                text.equals(resetLabel));
    }

    private void sendKeysToInput(CharSequence... keys) {
        new Actions(getDriver()).moveToElement(comboBoxElement).perform();
        comboBoxElement.sendKeys(keys);
    }

    private void performSelect(SelectionType selectionType) {
        switch (selectionType) {
        case ENTER:
            sendKeysToInput(Keys.ENTER);
            break;
        case TAB:
            sendKeysToInput(Keys.TAB);
            break;
        case CLICK_OUT:
            $(ButtonElement.class).id("button-for-click").click();
            break;
        }
    }

    private void assertValueLabelText(String value) {
        valueLabelElement = $(LabelElement.class).id("value-label");
        String valueLabel = valueLabelElement.getText();
        assertTrue(
                String.format("Selected combobox item should be : %s , but was %s"
                        , value, valueLabel), value.equals(valueLabel));
    }

    private void delay(boolean delay) {
        CheckBoxElement checkBox = $(CheckBoxElement.class).id("delay");
        if (delay != checkBox.isChecked()) {
            checkBox.click();
        }
        this.delay = delay;
    }

    private void reset() {
        $(ButtonElement.class).id("reset").click();
    }
}
