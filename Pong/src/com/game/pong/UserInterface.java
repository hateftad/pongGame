package com.game.pong;

import com.example.pong.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

public class UserInterface {

	private Bitmap m_background;
	private Bitmap m_scoreStar;
	private Bitmap m_splashScreen;
	private int m_canvasHeight;
	private int m_canvasWidth;
	private int m_p1Score;
	private int m_p2Score;
	private boolean m_showSplash;
	
	
	public UserInterface(Context context, int width, int height)
	{
		m_canvasHeight = height;
		m_canvasWidth = width;
		
		m_background = BitmapFactory.decodeResource(context.getResources(), R.drawable.background);
		m_scoreStar = BitmapFactory.decodeResource(context.getResources(), R.drawable.star);
		m_splashScreen = BitmapFactory.decodeResource(context.getResources(), R.drawable.splashscreen);
		m_showSplash = false;
		m_p1Score = 0;
		m_p2Score = 0;
	}
	
	public void updateScore(boolean player1)
	{
		if(player1)
			m_p1Score++;
		else
			m_p2Score++;
	}
	
	public void draw(Canvas canvas, Paint paint)
	{
		canvas.drawBitmap(m_background, 0, 0, paint);
		for (int i = 0; i < m_p1Score; i++) {
			canvas.drawBitmap(m_scoreStar, i * m_scoreStar.getWidth(), m_canvasHeight - m_scoreStar.getHeight() * 2, paint);
		}
		
		for (int i = 0; i < m_p2Score; i++) {
			canvas.drawBitmap(m_scoreStar, (m_canvasWidth - m_scoreStar.getWidth()) - (i * m_scoreStar.getWidth()), m_scoreStar.getHeight(), paint);
		}
		if (m_showSplash) {
			canvas.drawBitmap(m_splashScreen, (m_canvasWidth/4), m_canvasHeight /2 , paint);
		}
	}
	
	public void resetScore()
	{
		m_p1Score = 0;
		m_p2Score = 0; 
	}
	
	public void showSplashScreen(boolean show)
	{
		m_showSplash = show;
	}
	
}
