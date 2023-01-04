package flashcards;

import org.junit.jupiter.api.Test;

import flashcards.model.Content;

import static org.junit.jupiter.api.Assertions.*;

public class ContentTest {

    @Test
    void TestContent() {
        Content content = new Content("Hello", "TEXT");
        assertEquals("Hello", content.getData());
        assertEquals("TEXT", content.getDataType());
        content.setData("World");
        assertEquals("World", content.getData());
    }
}
