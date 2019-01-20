package events;


import org.junit.Assert;

import java.time.LocalDateTime;

public class EventTest {
    @org.junit.Test
    public void multiDateBuilderTest() {
        String[] input;
        LocalDateTime[] multiDate;

        input = new String[]{"1", "2", "3"};
        multiDate = Event.multiDateBuilder(input);
        Assert.assertNotNull(multiDate);
        Assert.assertEquals(multiDate.length, 1);

        input = new String[]{"1", "2", "3", "4", "5", "6"};
        multiDate = Event.multiDateBuilder(input);
        Assert.assertNotNull(multiDate);
        Assert.assertEquals(multiDate.length, 2);

        input = new String[]{"1", "2"};
        multiDate = Event.multiDateBuilder(input);
        Assert.assertNull(multiDate);

        input = new String[]{"1", "2", "3", "4", "5"};
        multiDate = Event.multiDateBuilder(input);
        Assert.assertNull(multiDate);

        input = new String[]{"", "2", "3"};
        multiDate = Event.multiDateBuilder(input);
        Assert.assertNull(multiDate);


        input = new String[]{"a 1", "2", "3"};
        multiDate = Event.multiDateBuilder(input);
        Assert.assertNotNull(multiDate);
        Assert.assertEquals(multiDate.length, 1);


        input = new String[]{"a\t\t  @1", "\n2", "                  3"};
        multiDate = Event.multiDateBuilder(input);
        Assert.assertNotNull(multiDate);
        Assert.assertEquals(multiDate.length, 1);
    }
}
