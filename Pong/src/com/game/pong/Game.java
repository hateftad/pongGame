package com.game.pong;


import com.example.pong.R;
import com.game.pong.GameView.GameThread;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

public class Game extends Activity {

	private static final int MENU_PAUSE = Menu.FIRST;

	private static final int MENU_RESUME = Menu.FIRST + 1;

	private static final int MENU_START = Menu.FIRST + 2;

	private static final int MENU_STOP = Menu.FIRST + 3;

	private GameView m_game;
	
	private GameThread m_gameThread;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		Intent gameIntent = getIntent();
		
		boolean players = gameIntent.getBooleanExtra("players", false);
		
		setContentView(R.layout.game_view);
		m_game = (GameView) findViewById(R.id.pong);
		
		m_gameThread = m_game.getThread();
		m_gameThread.setState(GameThread.STATE_READY);
		m_gameThread.setPlayers(players);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		super.onCreateOptionsMenu(menu);

        menu.add(0, MENU_START, 0, R.string.menu_start);
        menu.add(0, MENU_STOP, 0, R.string.menu_stop);
        menu.add(0, MENU_PAUSE, 0, R.string.menu_pause);
        menu.add(0, MENU_RESUME, 0, R.string.menu_resume);

        return true;

	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_START:
                m_gameThread.doStart();
                return true;
            case MENU_STOP:
            	m_gameThread.setState(GameThread.STATE_LOSE);
                return true;
            case MENU_PAUSE:
            	m_gameThread.pause();
                return true;
            case MENU_RESUME:
            	m_gameThread.unpause();
                return true;
        }

        return false;
    }

	 @Override
	    protected void onPause() {
	        super.onPause();
	        m_game.getThread().pause(); // pause game when Activity pauses
	    }


}
