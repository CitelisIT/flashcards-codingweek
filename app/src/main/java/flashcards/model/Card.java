package flashcards.model;

import java.util.ArrayList;

public class Card extends Observable {
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

    public void setQuestionContentType(int i, String dataType) {
        this.question.get(i).setDataType(dataType);
    }

    public int getApperenceCount() {
        return apperenceCount;
    }

    public int getRightCount() {
        return rightCount;
    }

    public Content getAnswerContent(int i) {
        return answer.get(i);
    }

    public Content getQuestionContent(int i) {
        return question.get(i);
    }

    public void addQuestionContentText(String contentText) {
        question.add(new Content(contentText, "TEXT"));
    }

    public void addAnswerContentText(String contentText) {
        answer.add(new Content(contentText, "TEXT"));
    }

    public void addQuestionContentMultimedia(String data, String dataType) {
        question.add(new Content(data, dataType));
    }

    public void addAnswerContentMultimedia(String data, String dataType) {
        answer.add(new Content(data, dataType));
    }

    public float getScore() {
        if (getApperenceCount() != 0) {
            return (float) getRightCount() / getApperenceCount();
        } else {
            return 0;
        }
    }

    public ArrayList<Content> getQuestion() {
        return question;
    }

    public ArrayList<Content> getAnswer() {
        return answer;
    }

    public void setAnswerContentType(int i, String dataType) {
        this.answer.get(i).setDataType(dataType);

    }
}
