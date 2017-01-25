package drma.washington.edu.lifecounter;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MainActivity extends Activity {

    // Array of textviews and their data
    final int INITIAL_NUMBER_OF_PLAYERS = 4;
    final int INITIAL_PLAYER_LIFE = 20;
    final int MINIMUM_PLAYERS = 2;
    final int MAXIMUM_PLAYERS = 8;
    ArrayList<Integer> playerData;
    TextView[] playerDisplays;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playerData = new ArrayList<Integer>();
        for(int i = 0; i < INITIAL_NUMBER_OF_PLAYERS; i++){
            playerData.add(INITIAL_PLAYER_LIFE);
        }

        Button addPlayer = (Button) findViewById(R.id.btnAddPlayer);
        Button removePlayer = (Button) findViewById(R.id.btnRemPlayer);

        addPlayer.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(playerData.size() < MAXIMUM_PLAYERS){
                    playerData.add(INITIAL_PLAYER_LIFE);
                    updatePlayerDisplay();
                }else{
                    Log.w("MainActivity", "The maximum amount of players allowed is " + MAXIMUM_PLAYERS);
                }
            }
        });

        removePlayer.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if((playerData.size()) > MINIMUM_PLAYERS) {
                    playerData.remove(playerData.size() - 1);
                    updatePlayerDisplay();
                }else{
                    Log.w("MainActivity", "There must be a minimum of " + MINIMUM_PLAYERS + " players");
                }
            }
        });

        updatePlayerDisplay();
    }

    // Draws display of the current number of players
    private void updatePlayerDisplay(){
        Log.i("MainActivity", "Updating player display");

        LinearLayout playerContainer = (LinearLayout) findViewById(R.id.playerContainer);
        playerContainer.removeAllViews();

        // Used so we can more easily update specific players life totals
        playerDisplays = new TextView[playerData.size()];

        for(int i = 0; i < playerData.size(); i++){
            Log.i("MainActivity", "Creating player #" + (i + 1));
            LinearLayout player = new LinearLayout(this);
            player.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            player.setOrientation(LinearLayout.HORIZONTAL);

            LinearLayout.LayoutParams rowLayout = new LinearLayout.LayoutParams(0,
                    LinearLayout.LayoutParams.WRAP_CONTENT, 0.2f);

            Button[] lifeButtons = new Button[4];
            for(int j = 0; j < 4; j++){
                lifeButtons[j] = new Button(this);
                lifeButtons[j].setLayoutParams(rowLayout);
                lifeButtons[j].setTag((i * playerData.size()) + j);
                lifeButtons[j].setOnClickListener(new MyListener());
            }
            lifeButtons[0].setText("-5");
            lifeButtons[1].setText("-1");
            lifeButtons[2].setText("+1");
            lifeButtons[3].setText("+5");

            LinearLayout centerData = new LinearLayout(this);
            centerData.setLayoutParams(rowLayout);
            centerData.setOrientation(LinearLayout.VERTICAL);
            centerData.setPadding(0, 6, 0, 0);

            LinearLayout.LayoutParams centerDataLayout = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            TextView playerName = new TextView(this);
            playerName.setLayoutParams(centerDataLayout);
            playerName.setGravity(Gravity.CENTER);
            playerName.setText("Player " + (i + 1));
            playerDisplays[i] = new TextView(this);
            playerDisplays[i].setLayoutParams(centerDataLayout);
            playerDisplays[i].setGravity(Gravity.CENTER);
            playerDisplays[i].setTextSize(18);

            centerData.addView(playerName);
            centerData.addView(playerDisplays[i]);

            player.addView(lifeButtons[0]);
            player.addView(lifeButtons[1]);
            player.addView(centerData);
            player.addView(lifeButtons[2]);
            player.addView(lifeButtons[3]);

            playerContainer.addView(player);
        }

        updateLifeDisplay();
    }

    // Updates current display with current life totals
    private void updateLifeDisplay(){
        for(int i = 0; i < playerData.size(); i++){
            String playerLife = playerData.get(i) + "";
            playerDisplays[i].setText(playerLife);
        }
    }

    // Button Listener
    public class MyListener implements View.OnClickListener{

        // constructor to set tip
        public MyListener(){

        }

        @Override
        public void onClick(View v) {
            if(v.getTag() != null) {
                int buttonTag = (int) v.getTag();
                int playerIndex = buttonTag / playerData.size();
                int previousLife = playerData.get(playerIndex);
                int newLife = previousLife;
                Log.i("drma_MainActivity", "Button number " + buttonTag + " Pressed");
                // I know this is kinda weird, but it's the first solution I found and it works
                if (buttonTag % playerData.size() == 0) {
                    newLife = previousLife - 5;
                } else if (buttonTag % playerData.size() == 1) {
                    newLife = previousLife - 1;
                } else if (buttonTag % playerData.size() == 2) {
                    newLife = previousLife + 1;
                } else if (buttonTag % playerData.size() == 3) {
                    newLife = previousLife + 5;
                } else {
                    Log.e("MainActivity", "This button doesn't exist, how did you press it?");
                }
                if(newLife <= 0){
                    playerLose(playerIndex);
                    newLife = 0;
                }
                playerData.set(playerIndex, newLife);
                updateLifeDisplay();
            }else{
                Log.i("drma_MainActivity", "This button doesn't have a tag so I don't care about it");
            }
        }

        private void playerLose(int playerIndex){
            final TextView messageCenter = (TextView) findViewById(R.id.txtMessageArea);
            messageCenter.setText("Player " + (playerIndex + 1) + " looses!");
            messageCenter.postDelayed(new Runnable() {
                public void run() {
                    messageCenter.setText("");
                }
            }, 8000);
        }
    }
}
