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

import adammcneilly.capturethetag.Utilities.PlayerUtility;

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
            }
        }

        notifyDataSetChanged();
    }

    public void insertTeam(Team team){
        mTeams.add(team);
        mTeamPlayers.put(team, new ArrayList<Player>());
    }

    public void removePlayer(Team playerTeam, Player player){
        for(Team team : mTeamPlayers.keySet()){
            if(team.getName().equals(playerTeam.getName())){
                for(Player pl : mTeamPlayers.get(team)) {
                    if (pl.getName().equals(player.getName())) {
                        mTeamPlayers.get(team).remove(pl);
                    }
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

        // If current player has a team name,and it's not equal to the current row, hide button.
        if(!Global.currentPlayer.getTeamName().isEmpty() && !Global.currentPlayer.getTeamName().equals(team.getName())){
            viewHolder.joinTeam.setVisibility(View.INVISIBLE);
            viewHolder.joinTeam.setClickable(false);
        } else{
            viewHolder.joinTeam.setVisibility(View.VISIBLE);
            viewHolder.joinTeam.setClickable(true);
            // If player's team name is not empty, and it's equal to this team, show leave.
            // Otherwise, show join.
            viewHolder.joinTeam.setText((!Global.currentPlayer.getTeamName().isEmpty() && Global.currentPlayer.getTeamName().equals(team.getName())) ? mContext.getResources().getString(R.string.leave) : mContext.getResources().getString(R.string.join));
        }

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
        public final Button joinTeam;

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
            if(joinTeam.getText().toString().equals(mContext.getResources().getString(R.string.join))){
                boolean isCaptain = getChildrenCount(teamPosition) == 0;
                Global.currentPlayer.setTeamName(teamName);
                Global.currentPlayer.setIsCaptain(isCaptain);
                if(isCaptain){
                    new PlayerUtility().AddPlayerAsCaptain(mGameName, Global.currentPlayer.getTeamName(), Global.currentPlayer.getName());
                } else{
                    new PlayerUtility().AddPlayer(mGameName, Global.currentPlayer.getTeamName(), Global.currentPlayer.getName());
                }
            } else{
                if (getChildrenCount(teamPosition) == 1) // If its the last player in the team
                    new PlayerUtility().RemoveLastPlayer(mGameName, teamName, Global.currentPlayer.getName());
                else
                    new PlayerUtility().RemovePlayer(mGameName, teamName, Global.currentPlayer.getName());
                Global.currentPlayer.setTeamName("");
            }
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
