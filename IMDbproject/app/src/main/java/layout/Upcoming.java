package layout;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.assignment.acadgild.zeeshan.imdbproject.ConnectivityReceiver;
import com.android.assignment.acadgild.zeeshan.imdbproject.DownloadJSON;
import com.android.assignment.acadgild.zeeshan.imdbproject.R;


public class Upcoming extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    public Upcoming() {
    }
    SwipeRefreshLayout swipeRefreshLayout_Upcoming;
    View view_upComing;
    RecyclerView lv_upcoming;
    String URL_UC = "http://api.themoviedb.org/3/movie/upcoming?api_key=8496be0b2149805afa458ab8ec27560c";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onPause() {
        setRetainInstance(true);
        super.onPause();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view_upComing = inflater.inflate(R.layout.fragment_upcoming, container, false);
        lv_upcoming = (RecyclerView)view_upComing.findViewById(R.id.recyclerView_upComing);
        if(checkConnection()){
            loadUpcomingData();
        }else{
            Toast.makeText(getActivity().getApplicationContext(), "Check Internet connection and Pull down to refresh", Toast.LENGTH_SHORT).show();
        }
        swipeRefreshLayout_Upcoming = (SwipeRefreshLayout)view_upComing.findViewById(R.id.swipeRefreshLayout_upComing);
        swipeRefreshLayout_Upcoming.setOnRefreshListener(this);
        lv_upcoming.smoothScrollToPosition(0);
        return view_upComing;
    }
    private boolean checkConnection() {
        return ConnectivityReceiver.isConnected();
    }
    private void loadUpcomingData(){
        DownloadJSON downloadJSON = new DownloadJSON(getActivity().getApplication().getApplicationContext(), lv_upcoming);
        downloadJSON.getJSON(URL_UC);
    }
    @Override
    public void onRefresh() {
        swipeRefreshLayout_Upcoming.setRefreshing(true);
        Log.d("Swipe", "Refreshing Upcoming");
        ( new Handler()).postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout_Upcoming.setRefreshing(false);
                if(checkConnection()){
                    loadUpcomingData();
                }else{
                    Toast.makeText(getActivity().getApplicationContext(), "Check Internet connection and Pull down to refresh", Toast.LENGTH_SHORT).show();
                }
            }
        }, 500);
    }

}


