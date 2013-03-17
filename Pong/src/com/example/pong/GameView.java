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
	private Ball m_ball;

	static final int PADDING = 100;
	private Vector2 m_window;
	private int m_textPos = 0;

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

		m_ball = new Ball(new Vector2(m_window.x /2, m_window.y/2), 5.0f, Color.WHITE);
		m_ball.setVelocity(new Vector2(0.1f, 8.0f));

		m_player1 = new Player(90, 10, new Vector2(m_window.x/2, m_window.y - PADDING), Color.RED, m_window.x, true);
		m_player1.isPlayer(true);
		m_player2 = new Player(90, 10, new Vector2(m_window.x/2, PADDING), Color.BLUE, m_window.x, false);
		m_textPos = (int) m_player2.getDimensions().x /3;
	}

	@Override
	public void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		m_paint.setStyle(Style.FILL);

		m_player1.draw(canvas, m_paint);
		m_player2.draw(canvas, m_paint);
		m_ball.draw(canvas, m_paint);

		m_paint.setTextSize(25);
		canvas.drawText(m_player1.getPoints()+"", 0, m_window.y, m_paint);
		canvas.drawText(m_player2.getPoints()+"", m_window.x - m_textPos, m_textPos, m_paint);
		
		
		
		
	}

	public void update()
	{
		long now = System.currentTimeMillis();

		m_ball.update();

		if (m_ball.getPosition().x > m_window.x - m_ball.getRadius()*4 || 
				m_ball.getPosition().x < 0 + m_ball.getRadius() * 4) {
			m_ball.inverseX();
		}

		if (m_ball.getPosition().y < 0) {
			m_player1.addPoints();
			m_ball.resetBall(m_window);
		}
		else if(m_ball.getPosition().y > m_window.y)
		{
			m_player2.addPoints();
			m_ball.resetBall(m_window);
		}

		if (collided(m_player1.getRectangle())) {
			if (m_ball.getPosition().x < m_player1.leftSide()) {
				m_ball.setVelocity(-m_ball.getVelocity().y, -m_ball.getVelocity().y );
			}
			if (m_ball.getPosition().x > m_player1.rightSide()){

				m_ball.setVelocity(m_ball.getVelocity().y, -m_ball.getVelocity().y );
			}
			if (m_ball.getPosition().x <= m_player1.rightSide() &&
					m_ball.getPosition().x >= m_player1.leftSide()) {

				m_ball.inverseY();
			}
		}
		if (collided(m_player2.getRectangle())) {

			if (m_ball.getPosition().x < m_player2.leftSide()) {
				m_ball.setVelocity(m_ball.getVelocity().y, -m_ball.getVelocity().y );
			}
			if (m_ball.getPosition().x > m_player2.rightSide()){
				m_ball.setVelocity(-m_ball.getVelocity().y, -m_ball.getVelocity().y );
			}
			if (m_ball.getPosition().x <= m_player2.rightSide() &&
					m_ball.getPosition().x >= m_player2.leftSide()) {
				
				m_ball.inverseY();
			}
			m_player2.randomizeSpeed();
		}

		if (!m_player2.isPlayer()) {
			m_player2 = doAI(m_player2);
		}
		
		long diff = System.currentTimeMillis() - now;
		m_redrawHandler.sleep(Math.max(0, (1000 / m_framesPerSecond) - diff) );

	}

	private Player doAI(Player ai)
	{
		float middle = ai.getRectangle().centerX();
		float minRange = (m_window.x / 2);
		float maxRange = (m_window.x / 2) - (ai.getDimensions().x);
		float width = (ai.getDimensions().x);


		if(m_ball.getPosition().y < m_window.y/2)
		{
			if (m_ball.getVelocity().y > 0) {

				if (middle < minRange - width) {
					ai.move(ai.getSpeed());
				}
				else if (middle > maxRange + width){
					ai.move(-ai.getSpeed());
				}
			}
			else if(m_ball.getVelocity().y < 0)
			{
				if (middle != m_ball.getPosition().x) {

					if (m_ball.getPosition().x < middle - 10) {
						ai.move(-ai.getSpeed());

					}
					else if(m_ball.getPosition().x > middle + 10)
					{
						ai.move(ai.getSpeed());
					}

				}

			}
		}
		return ai;
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

		if (by + radius>= rt &&
				by <= rb &&
				bx <= rr && 
				bx >= rl ) {
			return true;
		}

		return false;
	}

	private boolean inside = false;

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		//int x = (int) event.getX();
		//int y = (int) event.getY();
		
		InputHandler ih = InputHandler.getInstance();

		for(int i = 0; i < ih.getTouchCount(event); i++) {
			
			int x = (int) ih.getX(event, i);
            int y = (int) ih.getY(event, i);
            
			switch (event.getAction())
			{
			case MotionEvent.ACTION_DOWN:
				
				if(m_player1.getHandle().contains(x, y))
				{
					m_player1.setSelected(true);
				}
				
				if(m_player2.getHandle().contains(x, y))
				{
					m_player2.setSelected(true);
					m_player2.isPlayer(true);
				}
				
				break;
			case MotionEvent.ACTION_MOVE:
				if (m_player1.isSelected()) {
					m_player1.setPosition(x - (m_player1.getDimensions().x/2), m_player1.getPosition().y);
				}
				if (m_player2.isSelected()) {
					m_player2.setPosition(x - (m_player2.getDimensions().x/2), m_player2.getPosition().y);
				}
				break;
			case MotionEvent.ACTION_UP:
				m_player1.setSelected(false);
				m_player2.setSelected(false);
				break;
	
			}
		}
		return true;

	}

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
