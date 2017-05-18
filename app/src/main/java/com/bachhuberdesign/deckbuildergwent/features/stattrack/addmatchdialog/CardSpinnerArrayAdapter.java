package com.bachhuberdesign.deckbuildergwent.features.stattrack.addmatchdialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bachhuberdesign.deckbuildergwent.features.shared.model.Card;

import java.util.List;

/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
public class CardSpinnerArrayAdapter extends ArrayAdapter<Card> {

    public CardSpinnerArrayAdapter(@NonNull Context context, int resource, @NonNull List<Card> cards) {
        super(context, resource, cards);
        this.cards = cards;
    }

    private List<Card> cards;
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
        textView.setText(cards.get(position).getName());

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
        textView.setText(cards.get(position).getName());
        textView.setPadding(10, 10, 10, 10);

        return view;
    }

}
