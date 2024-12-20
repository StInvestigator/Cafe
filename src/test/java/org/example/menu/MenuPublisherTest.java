package org.example.menu;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;

public class MenuPublisherTest {
    @Test
    void showStringList_ShouldReturnListAfStings_WhenCalledWithStringCollection() {

        List<String> groups = new ArrayList<>();
        groups.add("gr-01");
        groups.add("gr-02");
        groups.add("gr-03");
        groups.add("gr-04");

        String expected = "------------------------------------------------------------\n" +
                "1.  gr-01\n" +
                "2.  gr-02\n" +
                "3.  gr-03\n" +
                "4.  gr-04\n\n";

        String actual = MenuPublisher.showStringList(groups);

        assertEquals(expected, actual);
    }
}
