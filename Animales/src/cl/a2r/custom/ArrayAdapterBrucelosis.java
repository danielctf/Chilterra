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

import cl.a2r.sip.model.Brucelosis;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class ArrayAdapterBrucelosis extends ArrayAdapter<Brucelosis> {

    HashMap<Integer, Brucelosis> mIdMap = new HashMap<Integer, Brucelosis>();
    View.OnTouchListener mTouchListener;

    public ArrayAdapterBrucelosis(Context context, int textViewResourceId,
            List<Brucelosis> list, View.OnTouchListener listener) {
        super(context, textViewResourceId, list);
        mTouchListener = listener;
        for (Brucelosis b : list){
        	mIdMap.put(b.getGanado().getId(), b);
        }
    }

    @Override
    public long getItemId(int position) {
    	Integer item = getItem(position).getGanado().getId();
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
