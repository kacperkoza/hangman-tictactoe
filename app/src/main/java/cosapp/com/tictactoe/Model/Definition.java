package cosapp.com.tictactoe.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by kkoza on 30.03.2017.
 */

public class Definition implements Serializable{

    @SerializedName("word")
    private String word;

    @SerializedName("attributionText")
    private String originOfDefinition;

    @SerializedName("text")
    private String text;

    public Definition(String word, String originOfDefinition, String text) {
        this.word = word;
        this.originOfDefinition = originOfDefinition;
        this.text = text;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getOriginOfDefinition() {
        return originOfDefinition;
    }

    public void setOriginOfDefinition(String originOfDefinition) {
        this.originOfDefinition = originOfDefinition;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
