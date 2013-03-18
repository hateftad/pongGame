package com.example.pong;

import java.util.Random;

import com.example.math.Vector2;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class Player {

	Random rn = new Random();
	private Rect m_rectangle = new Rect();
	private Rect m_handle = new Rect();
	private Vector2 m_position;
	private Vector2 m_dimensions;
	private Vector2 m_handleDimensions;
	private int m_color;
	private int m_moveSpeed = 10;
	private int m_score;
	private int m_canvasWidth;
	private boolean m_selected;
	private boolean m_isPlayer;

	public Player(int width, int height, Vector2 pos, int color, int canvasWidth, boolean player1)
	{
		m_selected = false;
		m_canvasWidth = canvasWidth;
		m_score = 0;
		m_color = color;
		m_dimensions = new Vector2(width, height);
		if (player1) {
			m_handleDimensions = new Vector2(height * 10, 0);
		}
		else
		{
			m_handleDimensions = new Vector2(0, height * 10);
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
		setPosition(m_canvasWidth/2, getPosition().y);
	}
	public int getPoints()
	{
		return m_score;
	}

	public float leftSide()
	{
		return m_position.x + (m_dimensions.x /3);
	}

	public float rightSide()
	{
		return m_rectangle.right - (m_dimensions.x /3); 
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

	public Vector2 getDimensions()
	{
		return m_dimensions;
	}

	public void setPosition(Vector2 pos)
	{
		this.m_rectangle.set(pos.x, pos.y, ( pos.x + m_dimensions.x),(pos.y + m_dimensions.y));
		this.m_handle.set(pos.x, (pos.y - (m_handleDimensions.y)), ( pos.x + m_dimensions.x),( pos.y + m_handleDimensions.x));
		this.m_position = pos;
	}

	public void setPosition(int x, int y)
	{
		this.m_rectangle.set(x, y, ( x + m_dimensions.x),(y + m_dimensions.y));
		this.m_handle.set(x, ( y - (m_handleDimensions.y)), ( x + m_dimensions.x), ( y + m_handleDimensions.x));
		this.m_position.x = x;
		this.m_position.y = y;
	}

	public void move(int x)
	{

		if ((m_position.x + m_dimensions.x) > m_canvasWidth) {
			m_position.x = m_canvasWidth - m_dimensions.x;
		}
		else if(m_position.x < 0)
		{
			m_position.x = 0;
		}

		m_position.x += x;

		setPosition(m_position);

	}

	public void draw(Canvas canvas, Paint paint)
	{
		paint.setColor(m_color);
		canvas.drawRect(getRectangle(), paint);

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

