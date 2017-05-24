package cosapp.com.tictactoe.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kkoza on 30.03.2017.
 */

public class Word {
    
    @SerializedName("word")
    private String word;

    public Word(String word) {
        this.word = word;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }
}
