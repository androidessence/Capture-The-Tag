package adammcneilly.capturethetag;

import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by adammcneilly on 9/12/15.
 */
public class TeamPlayerAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private List<Team> mTeams;
    private HashMap<Team, List<Player>> mTeamPlayers = new HashMap<>();
    private String mGameName;

    public TeamPlayerAdapter(Context context, List<Team> teams, String gameName){
        this.mContext = context;
        this.mTeams = teams;
        for(Team team : teams){
            mTeamPlayers.put(team, new ArrayList<Player>());
            mTeamPlayers.put(team, new ArrayList<Player>());
        }
        this.mGameName = gameName;
    }

    public void insertPlayer(Team playerTeam, Player player){
        // get key from value
        for(Team team : mTeamPlayers.keySet()){
            if(team.getName().equals(playerTeam.getName())){
                mTeamPlayers.get(team).add(player);
                if (mTeamPlayers.get(team).size() == 1)
                {
                    // TODO: Add crown to player item (on left hand side)
                }
            }
        }

        notifyDataSetChanged();
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
        viewHolder.setTeamName(team.getName());
        viewHolder.setTeamPosition(groupPosition);
        viewHolder.teamNameTextView.setText(viewHolder.getTeamName() + " (" + getChildrenCount(groupPosition) + " players)");

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
        if(player.isCaptain()){
            viewHolder.captainImage.setVisibility(View.VISIBLE);
        } else{
            viewHolder.captainImage.setVisibility(View.GONE);
        }

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    public class TeamViewHolder implements View.OnClickListener{
        public final TextView teamNameTextView;
        private String teamName = "";
        private int teamPosition;
        private final Button joinTeam;

        public TeamViewHolder(View view){
            teamNameTextView = (TextView) view.findViewById(R.id.team_name);
            joinTeam = (Button) view.findViewById(R.id.join_team);
            joinTeam.setOnClickListener(this);
        }

        public void setTeamName(String teamName){
            this.teamName = teamName;
        }

        public void setTeamPosition(int position){
            this.teamPosition = position;
        }

        public String getTeamName(){
            return teamName;
        }

        @Override
        public void onClick(View v) {
            boolean isCaptain = getChildrenCount(teamPosition) == 0;
            DialogFragment enterNameDialog = EnterNameDialog.NewInstance(getTeamName(), mGameName, isCaptain);
            enterNameDialog.show(((AppCompatActivity)mContext).getSupportFragmentManager(), "enterName");
        }
    }

    public class PlayerViewHolder{
        public final TextView playerName;
        public final ImageView captainImage;

        public PlayerViewHolder(View view){
            playerName = (TextView) view.findViewById(R.id.player_name);
            captainImage = (ImageView) view.findViewById(R.id.captain_image);
        }
    }
}
