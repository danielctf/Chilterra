/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cl.a2r.custom;

import java.util.HashMap;
import java.util.List;

import cl.a2r.sip.model.Ganado;
import cl.a2r.sip.model.InyeccionTB;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class ArrayAdapterInyeccionTB extends ArrayAdapter<InyeccionTB> {

    HashMap<Integer, InyeccionTB> mIdMap = new HashMap<Integer, InyeccionTB>();
    View.OnTouchListener mTouchListener;

    public ArrayAdapterInyeccionTB(Context context, int textViewResourceId,
            List<InyeccionTB> list, View.OnTouchListener listener) {
        super(context, textViewResourceId, list);
        mTouchListener = listener;
        for (InyeccionTB tb : list){
        	mIdMap.put(tb.getGanadoID(), tb);
        }
    }

    @Override
    public long getItemId(int position) {
    	Integer item = getItem(position).getGanadoID();
        return item;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        if (view != convertView) {
            // Add touch listener to every new view to track swipe motion
            view.setOnTouchListener(mTouchListener);
        }
        return view;
    }

}
