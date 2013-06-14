package com.example.pong;

import com.example.math.Vector2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.Style;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    
	
	class GameThread extends Thread {
 
		
        public static final int STATE_LOSE = 1;
        public static final int STATE_PAUSE = 2;
        public static final int STATE_READY = 3;
        public static final int STATE_RUNNING = 4;
        public static final int STATE_WIN = 5;
        
        static final int PADDING = 100;
    	static final int WINPOINT = 3;
    	private int m_textPos = 0;

    	private Paint m_paint;
    	private Player m_player1;
    	private Player m_player2;
    	private Ball m_ball;
        private Bitmap m_background;
       
        private int m_canvasWidth;
        private int m_canvasHeight;

        private long m_lastTime;

        private Handler m_handler;

         /** The state of the game. One of READY, RUNNING, PAUSE, LOSE, or WIN */
        private int m_mode;
        /** Indicate whether the surface has been created & is ready to draw */
        private boolean m_run = false;
        /** Handle to the surface manager object we interact with */
        private SurfaceHolder m_surfaceHolder;

        public GameThread(SurfaceHolder surfaceHolder, Context context,
                Handler handler) {
            // get handles to some important objects
            m_surfaceHolder = surfaceHolder;
            m_handler = handler;
            m_context = context;
        	
            DisplayMetrics metrics = context.getResources().getDisplayMetrics();
            m_canvasWidth = metrics.widthPixels;
            m_canvasHeight = metrics.heightPixels;
            
    		m_paint = new Paint();
    		m_ball = new Ball(new Vector2(m_canvasWidth /2, m_canvasHeight/2), 5.0f, Color.WHITE);
    		m_ball.setVelocity(new Vector2(0, 8));

    		m_player1 = new Player(context, new Vector2(90, 10), new Vector2(m_canvasWidth/2, m_canvasHeight - PADDING), R.drawable.paddle, m_canvasWidth, true);
    		m_player1.isPlayer(true);
    		m_player2 = new Player(context, new Vector2(90, 10), new Vector2(m_canvasWidth/2, PADDING), R.drawable.paddle2, m_canvasWidth, false);
    		m_textPos = m_player2.getDimensions().x /3;
    		
            m_background = BitmapFactory.decodeResource(m_context.getResources(), R.drawable.background);
        }

        /**
         * Starts the game, setting parameters for the current difficulty.
         */
        public void doStart() {
            synchronized (m_surfaceHolder) {
           	
                reset();
            	
                m_lastTime = System.currentTimeMillis() + 100;
                setState(STATE_RUNNING);
            }
        }

        /**
         * Pauses the physics update & animation.
         */
        public void pause() {
            synchronized (m_surfaceHolder) {
                if (m_mode == STATE_RUNNING) 
                	setState(STATE_PAUSE);
            }
        }

        @Override
        public void run() {
            while (m_run) {
                Canvas c = null;
                try {
                    c = m_surfaceHolder.lockCanvas(null);
                    synchronized (m_surfaceHolder) {
                        if (m_mode == STATE_RUNNING) 
                        	updateGame();
                        doDraw(c);
                    }
                } finally {

                    if (c != null) {
                        m_surfaceHolder.unlockCanvasAndPost(c);
                    }
                }
            }
            
            
            
        }

    
        public void setRunning(boolean b) {
            m_run = b;
        }

    
        public void setState(int mode) {
            synchronized (m_surfaceHolder) {
                setState(mode, null);
            }
        }

 
        public void setState(int mode, CharSequence message) {
            synchronized (m_surfaceHolder) {
                m_mode = mode;
            }
        }


        public void setSurfaceSize(int width, int height) {
 
            synchronized (m_surfaceHolder) {
                m_canvasWidth = width;
                m_canvasHeight = height;
            }
        }

        
        public void unpause() {
            // Move the real time clock up to now
            synchronized (m_surfaceHolder) {
                m_lastTime = System.currentTimeMillis() + 100;
            }
            setState(STATE_RUNNING);
        }


        private void doDraw(Canvas canvas) {
        	// empty canvas
        	canvas.drawARGB(255, 0, 0, 0);
        	canvas.drawBitmap(m_background, 0, 0, m_paint);
        	if(!foundWinner())
    		{
    			m_paint.setStyle(Style.FILL);

    			m_player1.draw(canvas, m_paint);
    			m_player2.draw(canvas, m_paint);
    			m_ball.draw(canvas, m_paint);

    			m_paint.setTextSize(25);
    			canvas.drawText(m_player1.getPoints()+"", 0, m_canvasHeight, m_paint);
    			canvas.drawText(m_player2.getPoints()+"", m_canvasWidth - m_textPos, m_textPos, m_paint);

    		}
    		else {
    			m_paint.setStyle(Style.FILL);
    			m_paint.setColor(Color.GREEN);
    			canvas.drawText("GAME OVER!", m_canvasWidth/3, m_canvasHeight/3, m_paint);
    			canvas.drawText("Tap to restart!", m_canvasWidth/3, m_canvasHeight/2, m_paint);
    		}
        	
        	
        }


        private void updateGame() {

            long now = System.currentTimeMillis();
  
            if (m_lastTime > now) 
            	return;
            double elapsed = (now - m_lastTime) / 1000.0;
            m_lastTime = now;
 
            m_ball.update();

    		if (m_ball.getPosition().x > m_canvasWidth - m_ball.getRadius() * 3 || 
    				m_ball.getPosition().x < 0 + m_ball.getRadius() * 3) {
    			m_ball.inverseX();
    		}

    		if (m_ball.getPosition().y < 0) {
    			m_player1.addPoints();
    			m_ball.resetBall(new Vector2(m_canvasWidth, m_canvasHeight));
    		}
    		else if(m_ball.getPosition().y > m_canvasHeight)
    		{
    			m_player2.addPoints();
    			m_ball.resetBall(new Vector2(m_canvasWidth, m_canvasHeight));
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

        }
        
        private Player doAI(Player ai)
    	{
    		float middle = ai.getRectangle().centerX();
    		float minRange = (m_canvasWidth / 2);
    		float maxRange = (m_canvasWidth / 2) - (ai.getDimensions().x);
    		float width = (ai.getDimensions().x);


    		if(m_ball.getPosition().y < m_canvasHeight/2)
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
        
        public boolean foundWinner()
    	{
    		return (m_player1.getPoints() > WINPOINT-1 || m_player2.getPoints() > WINPOINT-1);
    	}
        
        public void reset()
    	{
    		m_ball.resetBall(new Vector2(m_canvasWidth, m_canvasHeight));
    		m_player1.resetPlayer();
    		m_player2.resetPlayer();
    		m_player2.isPlayer(m_player2.isPlayer());
    	}
        
        public Player getPlayer(boolean player)
        {
        	if(player)
        		return m_player1;
        	return m_player2;
        }
    }

    /** Handle to the application context, used to e.g. fetch Drawables. */
    private Context m_context;

    /** The thread that actually draws the animation */
    private GameThread thread;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);

  
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);


        thread = new GameThread(holder, context, new Handler() {
            @Override
            public void handleMessage(Message m) {
            	
            }
        });

        setFocusable(true); 
    }

 
    public GameThread getThread() {
        return thread;
    }
  
    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        if (!hasWindowFocus)
        	thread.pause();
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width,
            int height) {
        thread.setSurfaceSize(width, height);
    }

 
    public void surfaceCreated(SurfaceHolder holder) {

        thread.setRunning(true);
        thread.start();
    }

    public void surfaceDestroyed(SurfaceHolder holder) {

        boolean retry = true;
        thread.setRunning(false);
        while (retry) {
            try {
                thread.join();
                retry = false;
            } catch (InterruptedException e) {
            }
        }
    }
    
	@Override
	public boolean onTouchEvent(MotionEvent event) {

		InputHandler ih = InputHandler.getInstance();
		Player m_player1 = getThread().getPlayer(true);
		Player m_player2 = getThread().getPlayer(false);
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
				if (getThread().foundWinner()) {
					getThread().reset();
				}
				break;

			}
		}
		return true;

	}
}
