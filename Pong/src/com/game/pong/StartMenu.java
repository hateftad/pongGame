package com.game.pong;



import com.example.pong.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;

public class StartMenu extends Activity implements View.OnClickListener {

	ImageButton m_1Player;
	ImageButton m_2Player;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.start_menu);
		m_1Player = (ImageButton) findViewById(R.id.player1);
		m_2Player = (ImageButton) findViewById(R.id.player2);
		m_1Player.setOnClickListener(this);
		m_2Player.setOnClickListener(this);
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.player1:
			Intent playervscp = new Intent(StartMenu.this, Game.class);
			playervscp.putExtra("players", false);
			startActivity(playervscp);
			break;
		case R.id.player2:
			Intent playervsplayer = new Intent(StartMenu.this, Game.class);
			playervsplayer.putExtra("players", true);
			startActivity(playervsplayer);
			break;
		default:
			break;
		}
		
	}
}
