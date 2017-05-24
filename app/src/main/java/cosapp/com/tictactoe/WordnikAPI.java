package cosapp.com.tictactoe;

import java.util.List;

import cosapp.com.tictactoe.Model.Definition;
import cosapp.com.tictactoe.Model.Word;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by kkoza on 30.03.2017.
 */

public interface WordnikAPI {

    @GET("/v4/words.json/randomWord")
    Call<Word> getRandomWord(@Query("api_key") String api_key, @Query("maxLength") int max_length);

    @GET("/v4/word.json/{word}/definitions")
    Call<List<Definition>> getDefinitionOfGivenWord(@Path("word") String word, @Query("api_key") String api_key);
}
