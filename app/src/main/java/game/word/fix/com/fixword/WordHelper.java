package game.word.fix.com.fixword;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

class WordHelper {

    public static String getRandomWord(Context context,int level) {
        DbHelperWord dbHelperWord = new DbHelperWord(context);
        return dbHelperWord.getRandomWord(level);
    }

    public static String shuffleWord(String input){
        List<Character> characters = new ArrayList<Character>();
        for(char c:input.toCharArray()){
            characters.add(c);
        }
        StringBuilder output = new StringBuilder(input.length());
        while(characters.size()!=0){
            int randPicker = (int)(Math.random()*characters.size());
            output.append(characters.remove(randPicker));
        }
        return output.toString();
    }

}