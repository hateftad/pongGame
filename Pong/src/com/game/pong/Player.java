package com.game.pong;

import java.util.Random;

import com.game.math.Vector2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class Player {

	Random rn = new Random();
	
	//the rectangle for collision
	private Rect m_rectangle = new Rect();
	
	//touchbox to control the player
	private Rect m_handle = new Rect();
	
	//the position of the player
	private Vector2 m_position;
	
	//the dimensions of the touchbox
	private Vector2 m_handleDimensions;
	
	//the texture of the player
	private Bitmap m_texture;
	
	//the AI movespeed
	private int m_moveSpeed = 10;
	
	//player score
	private int m_score;
	
	// the canvas witdh for boundcheccking
	private int m_canvasWidth;
	
	//boolean to check if touch is within bounds
	private boolean m_selected;
	
	//boolean to check if human or not
	private boolean m_isPlayer;

	public Player(Context context, Vector2 pos, int resourceId, int canvasWidth, boolean player1)
	{
		m_selected = false;
		m_canvasWidth = canvasWidth;
		m_score = 0;
		m_texture = BitmapFactory.decodeResource(context.getResources(), resourceId);
		if (player1) {
			m_handleDimensions = new Vector2(m_texture.getHeight() * 10, 0);
		}
		else
		{
			m_handleDimensions = new Vector2(0, m_texture.getHeight() * 10);
		}

		setPosition(pos);
	}

	public void addPoints()
	{
		this.m_score++;
	}

	public void resetPlayer()
	{
		this.m_score = 0;
		setPosition(new Vector2(m_canvasWidth/2, getPosition().y));
	}
	public int getPoints()
	{
		return m_score;
	}

	public float leftSide()
	{
		return m_position.x + (m_texture.getWidth() /3);
	}

	public float rightSide()
	{
		return m_rectangle.right - (m_texture.getWidth() /3); 
	}

	public Rect getRectangle()
	{
		return m_rectangle;
	}

	public Rect getHandle()
	{
		return m_handle;
	}

	public Vector2 getPosition()
	{
		return m_position;
	}

	
	public Bitmap getTexture()
	{
		return m_texture;
	}
	
	
	public void setPosition(Vector2 pos)
	{
		this.m_position = pos;
		update();
	}
	
	
	public void setPosition(int x, int y)
	{
		this.m_position.x = x;
		this.m_position.y = y;
		update();
	}
	
	public void update()
	{
		this.m_rectangle.set(m_position.x, m_position.y, ( m_position.x + m_texture.getWidth()),(m_position.y + m_texture.getHeight()));
		this.m_handle.set(m_position.x, (m_position.y - (m_handleDimensions.y)), ( m_position.x + m_texture.getWidth()),( m_position.y + m_handleDimensions.x));
	}

	public void move(int x)
	{

		if ((m_position.x + m_texture.getWidth()) > m_canvasWidth) {
			m_position.x = m_canvasWidth - m_texture.getWidth();
		}
		else if(m_position.x < 0)
		{
			m_position.x = 0;
		}

		m_position.x += x;

		update();

	}

	public void draw(Canvas canvas, Paint paint)
	{

		canvas.drawBitmap(m_texture, m_position.x, m_position.y, paint);
		
		if(!isPlayer()){
			paint.setTextSize(12);
			paint.setColor(Color.WHITE);
			canvas.drawText("Touch to select!", getPosition().x, getHandle().centerY(), paint);
		}
	}

	public int getSpeed() {
		return m_moveSpeed;
	}

	public void setSpeed(int m_moveSpeed) {

		this.m_moveSpeed = m_moveSpeed;
	}

	public void randomizeSpeed()
	{
		int randomNum = (int) ((Math.random() * (10 - 6 + 1) ) + 6);
		this.m_moveSpeed = randomNum;
	}

	public void setSelected(boolean isit)
	{
		this.m_selected = isit;
	}

	public boolean isSelected()
	{
		return m_selected;
	}

	public boolean isPlayer() {
		return m_isPlayer;
	}

	public void isPlayer(boolean isPlayer) {
		this.m_isPlayer = isPlayer;
	}

}

