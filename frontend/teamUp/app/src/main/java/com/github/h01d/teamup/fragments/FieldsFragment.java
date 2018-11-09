package com.github.h01d.teamup.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.h01d.teamup.R;

public class FieldsFragment extends Fragment
{
    private final String TAG = "FieldsFragment";

    private View view;

    public FieldsFragment()
    {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_fields, container, false);

        return view;
    }
}
