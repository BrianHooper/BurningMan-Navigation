package events;

//import org.jboss.arquillian.container.test.api.Deployment;
//import org.jboss.arquillian.junit.Arquillian;
//import org.jboss.shrinkwrap.api.ShrinkWrap;
//import org.jboss.shrinkwrap.api.asset.EmptyAsset;
//import org.jboss.shrinkwrap.api.spec.JavaArchive;
//import org.junit.runner.RunWith;

import org.junit.Assert;

import java.time.LocalDateTime;

//@RunWith(Arquillian.class)
public class EventTest {
//    @Deployment
//    public static JavaArchive createDeployment() {
//        return ShrinkWrap.create(JavaArchive.class)
//                .addClass(Event.class)
//                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
//    }

    @org.junit.Test
    public void multiDateBuilder() {
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
