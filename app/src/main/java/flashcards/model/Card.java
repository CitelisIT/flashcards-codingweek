package flashcards;

import java.util.ArrayList;

public class Card {
    private ArrayList<Content> question = new ArrayList<Content>();
    private ArrayList<Content> answer = new ArrayList<Content>();
    private int apperenceCount = 0;
    private int rightCount = 0;

    public Card() {
        addAnswerContentText("Answer");
        addQuestionContentText("Question");
    }

    public void incrApperenceCount() {
        this.apperenceCount++;
    }

    public void incrRightCount() {
        this.rightCount++;
    }

    public void setAnswerContent(int i, String data) {
        this.answer.get(i).setData(data);
    }

    public void setQuestionContent(int i, String data) {
        this.question.get(i).setData(data);
    }

    public int getApperenceCount() {
        return apperenceCount;
    }

    public int getRightCount() {
        return rightCount;
    }

    public Content getAnswer(int i) {
        return answer.get(i);
    }

    public Content getQuestion(int i) {
        return question.get(i);
    }

    public void addQuestionContentText(String contentText) {
        question.add(new Content(contentText, "TEXT"));
    }

    public void addAnswerContentText(String contentText) {
        question.add(new Content(contentText, "TEXT"));
    }

    public void addQuestionContentMultimedia(String data, String dataType) {
        question.add(new Content(data, dataType));
    }

    public void addAnswerContentMultimedia(String data, String dataType) {
        question.add(new Content(data, dataType));
    }

    public float get_score() {
        return (float) getRightCount() / getApperenceCount();
    }
}
