package com.example.eatdrinkrepeat;

import java.util.Random;
import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

import android.animation.Animator;
import android.os.Bundle;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    final Random rand = new Random();
    final String[] foods={"food1n", "food2n", "food3n"};
    final String[] drinks={"drink1n", "drink2n", "drink3n"};
    int[] state;
    int[][] win;
    boolean gameOver;
    boolean enableInput=true;
    int imF;
    int imD;
    char item='F';

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        reset(findViewById(R.id.resButton));
    }

    public void reset(View view){
        try {
            TextView resultTxt=findViewById(R.id.resultTxt);
            resultTxt.setVisibility(View.INVISIBLE);
            state = new int[]{2, 2, 2, 2, 2, 2, 2, 2, 2};
            win = new int[][]{{0, 1, 2}, {3, 4, 5}, {6, 7, 8}, {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, {0, 4, 8}, {2, 4, 6}};
            gameOver = false;
            shuffleImageFood();
            shuffleImageDrink();
            GridLayout gL1=findViewById(R.id.gL);
            for(int i=0; i<9; i++){
                ImageView img= (ImageView) gL1.getChildAt(i);
                img.setImageDrawable(null);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void shuffleImageFood(){
        int rndInt = rand.nextInt(3);
        int resID = getResources().getIdentifier(foods[rndInt], "drawable",  getPackageName());
        imF=resID;
        ImageView img=findViewById(R.id.ImgOutFood);
        img.setImageResource(imF);
    }

    public void shuffleImageDrink(){
        int rndInt=rand.nextInt(3);
        int resID = getResources().getIdentifier(drinks[rndInt], "drawable",  getPackageName());
        imD=resID;
        ImageView img=findViewById(R.id.ImgOutDrink);
        img.setImageResource(imD);
    }

    public void bringIn(View view){
        ImageView iv=(ImageView) view;
        final char c=item;
        String tag=iv.getTag().toString();
        if(enableInput && !gameOver && state[Integer.parseInt(tag)]==2){
            enableInput=false;
            iv.setTranslationY(-1500);
            ViewPropertyAnimator anim=iv.animate().rotation(360).translationYBy(1500).setDuration(200);
            anim.setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {}
                @Override
                public void onAnimationEnd(Animator animation) {
                    checkForWin(c);
                    enableInput=true;
                }
                @Override
                public void onAnimationCancel(Animator animation) {}
                @Override
                public void onAnimationRepeat(Animator animation) {}
            });
            if(item=='F'){
                iv.setImageResource(imF);
                state[Integer.parseInt(tag)]=0;
                item='D';
            }else{
                iv.setImageResource(imD);
                state[Integer.parseInt(tag)]=1;
                item='F';
            }
            iv.setRotationY(0);

        }
    }

    public void checkForWin(char c) {
        TextView resultTxt=findViewById(R.id.resultTxt);
        for(int[] winner: win){
            if(state[winner[0]]==state[winner[1]] && state[winner[1]]==state[winner[2]] && state[winner[0]]!=2){
                //not working (rotation animation on win)
                gameOver=true;
                for(int i=0; i<3; i++) {
                    String view = "iv" + winner[i];
                    int resID = getResources().getIdentifier(view, "id", getPackageName());
                    ImageView img = findViewById(resID);
                    img.animate().rotationY(1080).setDuration(1000);
                }
                String out="";
                if(c=='F') //Works opposite
                    out="Eat!";
                else
                    out="Drink!";

                resultTxt.setText(out);
                resultTxt.setVisibility(View.VISIBLE);
                break;
            }
        }
        boolean all2=true;
        for(int i:state){
            if(i==2) {
                all2=false;
                break;
            }
        }
        if(all2 && !gameOver) {
            resultTxt.setText("Draw!");
            for(int i=0; i<9; i++){
                String view = "iv" + i;
                int resID = getResources().getIdentifier(view, "id", getPackageName());
                ImageView img = findViewById(resID);
                img.animate().rotationY(1080).setDuration(1000);
            }
            resultTxt.setVisibility(View.VISIBLE);
        }
    }
}
