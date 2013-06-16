package com.game.math;


public class Vector2 {
	public int x;
	public int y;

	public static final Vector2 ZERO = new Vector2(0, 0);

	public Vector2() {
	}

	public Vector2(int x, int y) {
		set(x, y);
	}

	public Vector2(Vector2 other) {
		set(other);
	}

	public final void add(Vector2 other) {
		x += other.x;
		y += other.y;
	}

	public final void add(float otherX, float otherY) {
		x += otherX;
		y += otherY;
	}

	public final void subtract(Vector2 other) {
		x -= other.x;
		y -= other.y;
	}

	public final void multiply(float magnitude) {
		x *= magnitude;
		y *= magnitude;
	}

	public final void multiply(Vector2 other) {
		x *= other.x;
		y *= other.y;
	}

	public final void divide(float magnitude) {
		if (magnitude != 0.0f) {
			x /= magnitude;
			y /= magnitude;
		}
	}

	public final void set(Vector2 other) {
		x = other.x;
		y = other.y;
	}

	public final void set(int xVal, int yVal) {
		x = xVal;
		y = yVal;
	}

	public final float dot(Vector2 other) {
		return (x * other.x) + (y * other.y);
	}

	public final float length() {
		return (float) Math.sqrt(length2());
	}

	public final float length2() {
		return (x * x) + (y * y);
	}

	public final float distance2(Vector2 other) {
		float dx = x - other.x;
		float dy = y - other.y;
		return (dx * dx) + (dy * dy);
	}

	public final float normalize() {
		final float magnitude = length();

		if (magnitude != 0.0f) {
			x /= magnitude;
			y /= magnitude;
		}

		return magnitude;
	}

	public final void zero() {
		set(0, 0);
	}

}
