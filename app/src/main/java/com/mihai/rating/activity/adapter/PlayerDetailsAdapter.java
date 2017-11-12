package com.mihai.rating.activity.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.mihai.rating.R;
import com.mihai.rating.model.Player;
import com.mihai.rating.model.WinsLossesResult;

import java.util.List;

public class PlayerDetailsAdapter extends BaseExpandableListAdapter {

    private List<String> playersHeader;
    private Player playersData;

    public PlayerDetailsAdapter(List<String> playersHeader,
                                Player playersData) {
        this.playersHeader = playersHeader;
        this.playersData = playersData;
    }

    @Override
    public int getGroupCount() {
        return playersHeader.size();
    }

    @Override
    public int getChildrenCount(int parentPos) {
        if (parentPos == 0) {
            return playersData.getWinsLossesResults().size();
        } else {
            return playersData.getSuggestedMatches().size();
        }
    }

    @Override
    public Object getGroup(int parentPos) {
        return playersHeader.get(parentPos);
    }

    @Override
    public Object getChild(int parentPos, int childPos) {
        if (parentPos == 0) {
            return playersData.getWinsLossesResults().get(childPos);
        } else {
            return playersData.getSuggestedMatches().get(childPos);
        }
    }

    @Override
    public long getGroupId(int parentPos) {
        return parentPos;
    }

    @Override
    public long getChildId(int parentPos, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean b, View convertView, ViewGroup viewGroup) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            convertView = inflater.inflate(R.layout.group_header, viewGroup, false);
        }

        TextView groupTitle = convertView.findViewById(R.id.group_header_title);
        groupTitle.setText(headerTitle);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean b, View convertView, ViewGroup viewGroup) {
        Context context = viewGroup.getContext();

        if (groupPosition == 0) {
            return getChildViewResults(childPosition, context, convertView, viewGroup);
        }
        return getChildViewSuggestions(childPosition, context, convertView, viewGroup);
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }

    private View getChildViewResults(int childPosition, Context context, View convertView, ViewGroup viewGroup) {
        MatchesViewHolder matchesViewHolder;
        if (convertView == null || convertView.getTag() instanceof SuggestionsViewHolder) {
            matchesViewHolder = new MatchesViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.results_against_element, viewGroup, false);

            matchesViewHolder.nameView = convertView.findViewById(R.id.against_name);
            matchesViewHolder.winsView = convertView.findViewById(R.id.against_wins);
            matchesViewHolder.lossesView = convertView.findViewById(R.id.against_losses);
            convertView.setTag(matchesViewHolder);
        } else {
            matchesViewHolder = (MatchesViewHolder) convertView.getTag();
        }

        TextView name = matchesViewHolder.nameView;
        TextView wins = matchesViewHolder.winsView;
        TextView losses = matchesViewHolder.lossesView;

        List<WinsLossesResult> winsLossesResults = playersData.getWinsLossesResults();

        name.setText(winsLossesResults.get(childPosition).getOpponent());
        wins.setText(String.format(context.getResources().getString(R.string.wins_short),
                winsLossesResults.get(childPosition).getWins()));
        losses.setText(String.format(context.getResources().getString(R.string.losses_short),
                winsLossesResults.get(childPosition).getLosses()));
        return convertView;
    }

    private View getChildViewSuggestions(int childPosition, Context context, View convertView, ViewGroup viewGroup) {
        SuggestionsViewHolder suggestionsViewHolder;
        if (convertView == null || convertView.getTag() instanceof MatchesViewHolder) {
            suggestionsViewHolder = new SuggestionsViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.potential_matches_element, viewGroup, false);

            suggestionsViewHolder.nameView = convertView.findViewById(R.id.potential_match_text);
            convertView.setTag(suggestionsViewHolder);
        } else {
            suggestionsViewHolder = (SuggestionsViewHolder) convertView.getTag();
        }

        TextView name = suggestionsViewHolder.nameView;

        Player suggestion = playersData.getSuggestedMatches().get(childPosition);

        name.setText(String.format(context.getResources().getString(R.string.name_with_score),
                suggestion.getName(), suggestion.getScore()));
        return convertView;
    }

    private static final class MatchesViewHolder {
        TextView nameView;
        TextView winsView;
        TextView lossesView;
    }

    private static final class SuggestionsViewHolder {
        TextView nameView;
    }
}
