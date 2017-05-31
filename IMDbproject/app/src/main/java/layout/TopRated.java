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


public class TopRated extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    public TopRated() {
    }
    SwipeRefreshLayout swipeRefreshLayout_TopRated;
    View view_topRated;
    RecyclerView lv_topRated;
    String URL_TR = "http://api.themoviedb.org/3/movie/top_rated?api_key=8496be0b2149805afa458ab8ec27560c";

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
        view_topRated = inflater.inflate(R.layout.fragment_top_rated, container, false);
        lv_topRated = (RecyclerView) view_topRated.findViewById(R.id.recyclerView_topRated);
        if(checkConnection()){
            loadTopRatedData();
        }else{
            Toast.makeText(getActivity().getApplicationContext(), "Check Internet connection and Pull down to refresh", Toast.LENGTH_SHORT).show();
        }
        lv_topRated.smoothScrollToPosition(0);
        swipeRefreshLayout_TopRated = (SwipeRefreshLayout)view_topRated.findViewById(R.id.swipeRefreshLayout_TopRated);
        swipeRefreshLayout_TopRated.setOnRefreshListener(this);
        return view_topRated;
    }

    private boolean checkConnection() {
        return ConnectivityReceiver.isConnected();
    }

    private void loadTopRatedData(){
        DownloadJSON downloadJSON = new DownloadJSON(getActivity().getApplication().getApplicationContext(), lv_topRated);
        downloadJSON.getJSON(URL_TR);
    }
    @Override
    public void onRefresh() {
        swipeRefreshLayout_TopRated.setRefreshing(true);
        Log.d("Swipe", "Refreshing TopRated");
        ( new Handler()).postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout_TopRated.setRefreshing(false);
                if(checkConnection()){
                    loadTopRatedData();
                }else{
                    Toast.makeText(getActivity().getApplicationContext(), "Check Internet connection and Pull down to refresh", Toast.LENGTH_SHORT).show();
                }
            }
        }, 500);
    }
}


