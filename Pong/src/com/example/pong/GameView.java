package com.example.pong;

import java.lang.ref.WeakReference;

import com.example.math.Vector2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

public class GameView extends View {


	private final Paint m_paint = new Paint();
	private Player m_player1;
	private Player m_player2;

	static final int PADDING = 100;
	private Ball m_ball;
	private Vector2 m_window;

	private RefreshHandler m_redrawHandler = new RefreshHandler(this);
	private int m_framesPerSecond = 30;


	public GameView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

	}
	@SuppressLint("NewApi")
	public GameView(Context context, AttributeSet attrs) {
		super(context, attrs);
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);

		m_window = new Vector2(size.x, size.y);
		setUp();

	}

	private void setUp()
	{

		m_ball = new Ball(new Vector2(m_window.x /2, m_window.y/2), 5.0f);
		m_ball.setVelocity(new Vector2(0.1f, 8.0f));

		m_player1 = new Player(90, 10, new Vector2(m_window.x/2, m_window.y - PADDING));
		m_player2 = new Player(90, 10, new Vector2(m_window.x/2, PADDING));

	}

	@Override
	public void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		m_paint.setStyle(Style.FILL);
		m_paint.setColor(Color.RED);

		canvas.drawRect(m_player1.getRectangle(), m_paint);
		m_paint.setStyle(Style.FILL);
		m_paint.setColor(Color.GREEN);

		canvas.drawRect(m_player2.getRectangle(), m_paint);

		m_paint.setStyle(Style.FILL);
		m_paint.setColor(Color.WHITE);

		canvas.drawCircle(m_ball.getPosition().x, m_ball.getPosition().y, m_ball.getRadius(), m_paint);

	}

	public void update()
	{
		long now = System.currentTimeMillis();

		m_ball.update();

		if (m_ball.getPosition().x > m_window.x) {
			m_ball.getPosition().x = m_window.x;
			m_ball.inverseX();
		}
		else if (m_ball.getPosition().x < 0)
		{
			m_ball.getPosition().x = 0;
			m_ball.inverseX();
		}

		if (m_ball.getPosition().y < 0) {
			m_ball.getPosition().y = 0;
			m_ball.inverseY();
		}
		else if(m_ball.getPosition().y > m_window.y)
		{
			m_ball.getPosition().y = m_window.y;
			m_ball.inverseY();
		}

		if (collided(m_player1.getRectangle())) {
			if (m_ball.getPosition().x < (m_player1.getRectangle().centerX() - 20)) {
				//m_ball.inverseY();
				m_ball.setVelocity(-10, -m_ball.getVelocity().y);
			}
			if (m_ball.getPosition().x > (m_player1.getRectangle().centerX() + 20)){
				//m_ball.inverseY();
				m_ball.setVelocity(10, -m_ball.getVelocity().y);
			}
			else {
				m_ball.inverseY();
			}
			
		}
		if (collided(m_player2.getRectangle())) {
			if (m_ball.getPosition().x < (m_player1.getRectangle().centerX())) {
				//m_ball.inverseY();
				m_ball.setVelocity(-10, -m_ball.getVelocity().y);
			}
			if (m_ball.getPosition().x > (m_player1.getRectangle().centerX())){
				//m_ball.inverseY();
				m_ball.setVelocity(10, -m_ball.getVelocity().y);
			}
			
		}
		
		doAI(m_player2);

		long diff = System.currentTimeMillis() - now;
		m_redrawHandler.sleep(Math.max(0, (1000 / m_framesPerSecond) - diff) );

	}
	private float speed = 6;
	private void doAI(Player ai)
	{
		float middle = m_player2.getRectangle().exactCenterX();
		
		if (m_ball.getVelocity().y > 0) {
	
			if (middle < (m_window.x / 2) - 20) {
				m_player2.move(speed, m_window.x);
			}
			else if (middle > (m_window.x / 2) + 20){
				m_player2.move(-speed, m_window.x);
			}
		}
		else if(m_ball.getVelocity().y < 0)
		{
			if (middle != m_ball.getPosition().x) {
				if (m_ball.getPosition().x < middle) {
					m_player2.move(-speed, m_window.x);
				}
				else if(m_ball.getPosition().x > middle)
				{
					m_player2.move(speed, m_window.x);
				}
			}
		}
	}

	private boolean collided(Rect rectangle)
	{
		float bx = m_ball.getPosition().x; 
		float by = m_ball.getPosition().y;
		float radius = m_ball.getRadius();
		float rl = rectangle.left;
		float rr = rectangle.right;
		float rt = rectangle.top;
		float rb = rectangle.bottom;

		if (by + radius > rt &&
			by + radius < rb &&
			bx < rr && 
			bx > rl ) {
			return true;
		}

		return false;
	}

	private boolean inside = false;

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		int x = (int) event.getX();
		int y = (int) event.getY();


		switch (event.getAction())
		{
		case MotionEvent.ACTION_DOWN:
			if(m_player1.getHandle().contains(x, y))
			{
				inside = true;
			}
			break;
		case MotionEvent.ACTION_MOVE:
			if (inside) {
				m_player1.setPosition(new Vector2(x - (m_player1.getDimensions().x/2), m_player1.getPosition().y));
			}
			break;
		case MotionEvent.ACTION_UP:
			inside = false;
			break;

		}

		return true;

	}
	/*
	@Override
	public boolean onTouchEvent(MotionEvent event) {

		switch (event.getAction())
		{
		case MotionEvent.ACTION_DOWN:
			m_offset = event.getX();
			break;
		case MotionEvent.ACTION_MOVE:

			m_x = (int) (event.getX() - m_offset);
			break;
		case MotionEvent.ACTION_UP:
			m_x = 0;
			break;

		}
		return true;

	}
	 */

	static class RefreshHandler extends Handler {

		private final WeakReference<GameView> m_service;

		public RefreshHandler(GameView view) {
			m_service = new WeakReference<GameView>(view);
		}

		@Override
		public void handleMessage(Message msg) {
			GameView v = m_service.get();
			if (v != null) {
				v.update();
				v.invalidate();
			}

		}

		public void sleep(long delay) {
			this.removeMessages(0);
			this.sendMessageDelayed(obtainMessage(0), delay);
		}
	}

}
