package com.example.pong;

import com.example.math.Vector2;

public class Ball {

	private Vector2 m_position;
	private Vector2 m_velocity;
	private float m_radius;

	
	public Ball(Vector2 pos, float radius)
	{
		setPosition(new Vector2());
		setVelocity(new Vector2());
		setPosition(pos);
		setRadius(radius);
		
	}
	
	public void setPosition(float x, float y)
	{
		m_position.set(x, y);
	}
	
	public void setPosition(Vector2 pos)
	{
		this.m_position = pos;
	}
	
	public Vector2 getPosition()
	{
		return m_position;
	}

	public float getRadius() {
		return m_radius;
	}

	public void setRadius(float m_radius) {
		this.m_radius = m_radius;
	}
	
	public void update()
	{
		m_position.x += m_velocity.x;
		m_position.y += m_velocity.y;
	
	}

	public Vector2 getVelocity() {
		return m_velocity;
	}

	public void inverseX()
	{
		m_velocity.x = -m_velocity.x;
		
	}
	
	public void inverseY()
	{
		m_velocity.y = -m_velocity.y;
	}
	
	public void setVelocity(Vector2 m_velocity) {
		this.m_velocity = m_velocity;
	}
	
	public void setVelocity(float x, float y) {
		this.m_velocity.x = x;
		this.m_velocity.y = y;
	}
	
}
