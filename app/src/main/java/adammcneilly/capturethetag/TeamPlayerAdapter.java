package adammcneilly.capturethetag;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

/**
 * Created by adammcneilly on 9/12/15.
 */
public class TeamPlayerAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private List<Team> mTeams;
    private HashMap<Team, List<Player>> mTeamPlayers;

    public TeamPlayerAdapter(Context context, List<Team> teams, HashMap<Team, List<Player>> teamPlayers){
        this.mContext = context;
        this.mTeams = teams;
        this.mTeamPlayers = teamPlayers;
    }

    @Override
    public int getGroupCount() {
        return mTeams.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        // get key from value
        for(Team team : mTeamPlayers.keySet()){
            if(team.equals(mTeams.get(groupPosition))){
                return mTeamPlayers.get(team).size();
            }
        }

        return 0;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mTeams.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        // Get key from value
        for(Team team : mTeamPlayers.keySet()){
            if(team.equals(mTeams.get(groupPosition))){
                return mTeamPlayers.get(team).get(childPosition);
            }
        }

        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        TeamViewHolder viewHolder;

        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_team, parent, false);
            viewHolder = new TeamViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else{
            viewHolder = (TeamViewHolder) convertView.getTag();
        }

        Team team = (Team) getGroup(groupPosition);
        viewHolder.teamName.setText(team.getName());

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        PlayerViewHolder viewHolder;

        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_player, parent, false);
            viewHolder = new PlayerViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else{
            viewHolder = (PlayerViewHolder) convertView.getTag();
        }

        Player player = (Player) getChild(groupPosition, childPosition);
        viewHolder.playerName.setText(player.getName());

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    public class TeamViewHolder implements View.OnClickListener{
        public final TextView teamName;
        private final Button joinTeam;

        public TeamViewHolder(View view){
            teamName = (TextView) view.findViewById(R.id.team_name);
            joinTeam = (Button) view.findViewById(R.id.join_team);
            joinTeam.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            //TODO: Remove hard coded game name.
            DialogFragment enterNameDialog = EnterNameDialog.NewInstance(teamName.getText().toString(), "MHacks");
            enterNameDialog.show(((AppCompatActivity)mContext).getSupportFragmentManager(), "enterName");
        }
    }

    public class PlayerViewHolder{
        public final TextView playerName;

        public PlayerViewHolder(View view){
            playerName = (TextView) view.findViewById(R.id.player_name);
        }
    }
}
