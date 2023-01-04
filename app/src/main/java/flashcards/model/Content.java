package flashcards;

public class Content {
    private String dataType;
    private String data;

    public Content(String data, String dataType) {
        this.data = data;
        this.dataType = dataType;
    }

    public String getData() {
        return data;
    }

    public String getDataType() {
        return dataType;
    }

    public void setData(String data) {
        this.data = data;
    }

}
