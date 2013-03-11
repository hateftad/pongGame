package com.example.pong;

import com.example.math.Vector2;

import android.graphics.Rect;

public class Player {

	private Rect m_rectangle = new Rect();
	private Rect m_handle = new Rect();
	private Vector2 m_position;
	private Vector2 m_dimensions;
	private float m_moveSpeed = 10;
	
	public Player(int width, int height, Vector2 pos)
	{
		m_dimensions = new Vector2(width, height);
		setPosition(pos);
		
	}
	
	public Rect getRectangle()
	{
		return m_rectangle;
	}
	
	public Rect getHandle()
	{
		return m_handle;
	}
	
	public void setPosition(Vector2 pos)
	{
		m_rectangle.set((int)pos.x, (int) pos.y, (int)( pos.x + m_dimensions.x),(int)(pos.y + m_dimensions.y));
		m_handle.set((int)pos.x, (int) pos.y, (int)( pos.x + m_dimensions.x), (int)(pos.y + m_dimensions.y * 10));
		m_position = pos;
	}
	
	public Vector2 getPosition()
	{
		return m_position;
	}
	
	public Vector2 getDimensions()
	{
		return m_dimensions;
	}
	
	public void move(float x, float width)
	{
		
		
		if ((m_position.x + m_dimensions.x) > (width)) {
			m_position.x = width - m_dimensions.x;
		}
		else if(m_position.x < 0)
		{
			m_position.x = 0;
		}
		
		
		m_position.x += x;
		
		setPosition(m_position);

	}
	/*
	public void move(int x, float width)
	{
		
		
		if (m_rectangle.right > (width)) {
			m_position.x = ((width) - m_rectangle.right);
		}
		else if(m_position.x < 0)
		{
			m_position.x = 0;
		}
		
		
		m_position.x += x;
		
		setPosition(m_position);

	}
	*/
}
