package flashcards.model;

public class Content {
    private String dataType;
    private String data;

    public Content(String data, String dataType) {
        this.data = data;
        this.dataType = dataType;
    }

    public String getData() {
        return this.data;
    }

    public String getDataType() {
        return this.dataType;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

}
