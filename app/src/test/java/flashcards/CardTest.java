package flashcards;

import org.junit.jupiter.api.Test;

import flashcards.model.Card;

import static org.junit.jupiter.api.Assertions.*;

public class CardTest {

    @Test
    void TestCard() {
        Card card = new Card();
        assertEquals(0, card.getApperenceCount());
        assertEquals(0, card.getRightCount());
        assertEquals("Answer", card.getAnswerContent(0).getData());
        assertEquals("Question", card.getQuestionContent(0).getData());
        card.incrApperenceCount();
        card.incrRightCount();
        assertEquals(1, card.getApperenceCount());
        assertEquals(1, card.getRightCount());
        card.setQuestionContent(0, "What is the capital of France?");
        card.setAnswerContent(0, "Paris");
        assertEquals("Paris", card.getAnswerContent(0).getData());
        assertEquals("What is the capital of France?", card.getQuestionContent(0).getData());
        assertEquals(1, card.getScore());

        card.addQuestionContentText("What is the capital of Germany?");
        card.addAnswerContentText("Berlin");
        assertEquals("Berlin", card.getAnswerContent(1).getData());
        assertEquals("What is the capital of Germany?", card.getQuestionContent(1).getData());

        card.addQuestionContentMultimedia("images/question.png", "IMAGE");
        card.addAnswerContentMultimedia("images/answer.mp4", "VIDEO");
        assertEquals("images/answer.mp4", card.getAnswerContent(2).getData());
        assertEquals("VIDEO", card.getAnswerContent(2).getDataType());
        assertEquals("images/question.png", card.getQuestionContent(2).getData());
        assertEquals("IMAGE", card.getQuestionContent(2).getDataType());

    }
}
