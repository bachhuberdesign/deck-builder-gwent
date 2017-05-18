package com.bachhuberdesign.deckbuildergwent.features.stattrack.addmatchdialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bachhuberdesign.deckbuildergwent.features.deckbuild.Deck;

import java.util.List;

/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
public class DeckSpinnerArrayAdapter extends ArrayAdapter<Deck> {

    public DeckSpinnerArrayAdapter(@NonNull Context context, int resource, @NonNull List<Deck> decks) {
        super(context, resource, decks);
        this.decks = decks;
    }

    private List<Deck> decks;
    private View view;

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        this.view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(android.R.layout.simple_spinner_item, null);
        }

        TextView textView = (TextView) view.findViewById(android.R.id.text1);
        textView.setText(decks.get(position).getName());

        return view;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        this.view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(android.R.layout.simple_spinner_dropdown_item, null);
        }

        TextView textView = (TextView) view.findViewById(android.R.id.text1);
        textView.setText(decks.get(position).getName());
        textView.setPadding(10, 10, 10, 10);

        return view;
    }

}
