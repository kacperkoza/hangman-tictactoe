package cosapp.com.tictactoe;

import android.util.Log;

import java.util.List;

import cosapp.com.tictactoe.Model.Definition;
import cosapp.com.tictactoe.Model.Word;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by kkoza on 31.03.2017.
 */

public abstract class Controller implements Callback<List<Definition>> {
    private final static String API_KEY = "a2a73e7b926c924fad7001ca3111acd55af2ffabf50eb4ae5";

    private final static String BASE_URL = "http://api.wordnik.com";

    public int maxLength;

    public Controller(int maxLength) {
        this.maxLength = maxLength;
    }

    public void getDefinition() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WordnikAPI wordnikAPI = retrofit.create(WordnikAPI.class);

        Callback<Word> callback = new Callback<Word>() {
            @Override
            public void onResponse(Call<Word> call, Response<Word> response) {
                if (response.isSuccessful()) {
                    String word = response.body().getWord();
                    Log.d("word", word);
                    queryForDefinition(word);
                }
            }

            @Override
            public void onFailure(Call<Word> call, Throwable t) {
                t.printStackTrace();
            }
        };

        Call<Word> word = wordnikAPI.getRandomWord(API_KEY, maxLength);
        word.enqueue(callback);
    }

    private void queryForDefinition(String word) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WordnikAPI wordnik = retrofit.create(WordnikAPI.class);

        Call<List<Definition>> call = wordnik.getDefinitionOfGivenWord(word, API_KEY);
        call.enqueue(this);
    }

    @Override
    public abstract void onResponse(Call<List<Definition>> call, Response<List<Definition>> response);

    @Override
    public abstract void onFailure(Call<List<Definition>> call, Throwable t);
}
