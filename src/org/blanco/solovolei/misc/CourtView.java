/**
 * The MIT License
 * 
 * Copyright (c) 2012 Alexandro Blanco <ti3r.bubblenet@gmail.com>
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.blanco.solovolei.misc;

import android.R;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.RelativeLayout;

public class CourtView extends RelativeLayout 
	implements OnTouchListener{

	Point points[] = new Point[2];
	Paint paint = null;
	VoleiAction action = null;
	
	public CourtView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setBackgroundColor(R.color.white);
		setOnTouchListener(this);
		//The default action is spike. Volei standards says you
		//should start receiving the ball and make a spike.
		action = VoleiAction.SPIKE;
				
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setColor(action.getColor());
		paint.setStrokeWidth(3.5f);
		
	}

	/**
	 * Sets the current action that has happened on the court
	 * @param action The VoleiAction to relate to the court
	 */
	public void setAction(VoleiAction action){
		this.action = action;
		//update paint color
		paint.setColor(action.getColor());
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		Point p = new Point();
		if (event.getAction() == MotionEvent.ACTION_UP){
			p.set((int)event.getX(), (int)event.getY());
			points[(points[0] == null)?0:1] = p;
			v.invalidate();
		}
		return true;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		boolean drawSomething = false;
		//Draw the points 
		if (points[0] != null && points[1] != null){
			//Draw a line
			canvas.drawLine(points[0].x, points[0].y, points[1].x, points[1].y, paint);
			//clear the sequence
			points[0] = null; points[1] = null;
			drawSomething = true;
		}else if (points[0] != null){
			//Draw the first point of the sequence
			canvas.drawPoint(points[0].x, points[0].y, paint);
			drawSomething = true;
		}
		//else draw nothing
		if (drawSomething){
			//TODO launch a time out to know if the first point was error or not
			//this will clear the existing points and will start over
		}
	}

}
