/**
 * Tec ch mun 2011 for Android, is the android application used to 
 *  
 * review all the information that is generated during the event
 * Tec Ch Mun 2011 of the ITESM campus chihuahua.
 * You can use this application as an example of all the technologies
 * used in this app.
 * Copyright (C) 2011  Alexandro Blanco <ti3r.bubblenet@gmail.com>
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Visit http://tec-ch-mun-2011.herokuapps.com
 */
package org.blanco.solovolei.misc;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class ExpandAnimation extends Animation {
        private final View _view;
        private final int _startHeight;
        private final int _finishHeight;

        public ExpandAnimation( View view, int startHeight, int finishHeight ) {
            _view = view;
            _startHeight = startHeight;
        _finishHeight = finishHeight;
        setDuration(220);
    }

    @Override
    protected void applyTransformation( float interpolatedTime, Transformation t ) {
        final int newHeight = (int)((_finishHeight - _startHeight) * interpolatedTime + _startHeight);
        _view.getLayoutParams().height = newHeight;
        _view.requestLayout();
    }

    @Override
    public void initialize( int width, int height, int parentWidth, int parentHeight ) {
        super.initialize(width, height, parentWidth, parentHeight);
    }

    @Override
    public boolean willChangeBounds( ) {
        return true;
    }
}
	