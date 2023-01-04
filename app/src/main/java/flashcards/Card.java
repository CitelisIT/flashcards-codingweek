package flashcards;

import java.util.ArrayList;

public class Card {
   private ArrayList<Content> question = new ArrayList<Content>();
   private ArrayList<Content> answer = new ArrayList<Content>();
   private int apperence_count = 0;
   private int rhight_count = 0;

   public int getApperence_count() {
       return apperence_count;
   }
   public int getRhight_count() {
       return rhight_count;
   }
   public Content getAnswer(int i) {
       return answer.get(i);
   }
   public Content getQuestion(int i) {
       return question.get(i);
   }
   public void addQuestionContentText(String contentText) {
        question.add(new Content());
   }

   public float get_score() {
        return (float)getRhight_count()/getApperence_count();
   }
}
