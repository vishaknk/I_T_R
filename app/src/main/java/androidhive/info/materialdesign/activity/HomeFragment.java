package androidhive.info.materialdesign.activity;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidhive.info.materialdesign.adapter.LandingAdapter;
import androidhive.info.materialdesign.model.Landing;
import androidhive.info.materialdesign.volley.AppController;
import androidhive.info.materialdesign.R;


public class HomeFragment extends Fragment {

    private ListView listView;
    private LandingAdapter adapter;
    private List<Landing> landingList = new ArrayList<Landing>();
    private AutoCompleteTextView searchText;
    private ArrayList<String> region_;
    private ArrayAdapter<String> region_adapter;
    private int preLast = 0;
    // Animation
    Animation animSlideup,animSlidedown;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.landing_listview, container, false);

        String url = "http://stage.itraveller.com/backend/api/v1/region";
        region_ = new ArrayList<String>();
        listView = (ListView) rootView.findViewById(R.id.list);

        searchText = (AutoCompleteTextView) rootView.findViewById(R.id.searchtext);
        region_adapter = new ArrayAdapter<String>(getActivity() ,android.R.layout.simple_list_item_1, region_);
        searchText.setThreshold(1);//will start working from first character
        searchText.setAdapter(region_adapter);//setting the adapter data into the AutoCompleteTextView
        searchText.setTextColor(Color.RED);

        adapter = new LandingAdapter(getActivity(), landingList);
        listView.setAdapter(adapter);

        // load the animation
        animSlidedown = AnimationUtils.loadAnimation(getActivity(),
                R.anim.slide_down);
        animSlideup = AnimationUtils.loadAnimation(getActivity(),
                R.anim.slide_up);

        // set animation listener
        animSlideup.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                Log.i("iTraveller","First Visible item: "+firstVisibleItem + " Visible item Count: " +visibleItemCount + " Total :" +totalItemCount);
                int position = firstVisibleItem+visibleItemCount;
                int limit = totalItemCount;

                //Check if top has been reached
                if(firstVisibleItem == 0)
                {
                    if(preLast == 0) {
                        preLast=1;
                        Log.i("123", "First123");
                        searchText.startAnimation(animSlidedown);
                        //searchText.setVisibility(View.VISIBLE);
                    }
                }
                else
                {
                    if(preLast == 1) {
                        searchText.startAnimation(animSlideup);
                        //searchText.setVisibility(View.GONE);
                        preLast = 0;
                    }
                }
                // Check if bottom has been reached
                if (position >= limit && totalItemCount > 0) {
                    //scroll end reached
                    //Write your code here
                }

            }
        });

        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();

        JsonObjectRequest strReq = new JsonObjectRequest(Request.Method.GET,
                url, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    //Log.d("Boolean", ""+response.getBoolean("success"));
                    //Log.d("Error", ""+response.getJSONObject("error"));
                    // Log.d("Payload", ""+response.getJSONObject("payload"));

                    JSONObject jsonobj = response.getJSONObject("payload");
                    // Parsing json
                    for (int i = 0; i < jsonobj.getJSONArray("international").length(); i++) {
                       JSONObject jsonarr = jsonobj.getJSONArray("international").getJSONObject(i);
                       Landing landing_model = new Landing();

                     if(jsonarr.getString("Popular").equalsIgnoreCase("1")) {
                           landing_model.setRegion_Id(jsonarr.getInt("Region_Id"));
                           landing_model.setRegion_Name(jsonarr.getString("Region_Name"));
                           landing_model.setEnable_Flag(jsonarr.getString("Enable_Flag"));
                           landing_model.setAlias(jsonarr.getString("Alias"));
                           landing_model.setSlider(jsonarr.getInt("Slider"));
                           landing_model.setHome_Page(jsonarr.getInt("Home_Page"));
                           landing_model.setTourism_Story(jsonarr.getString("Tourism_Story"));
                           landing_model.setRegion_Story(jsonarr.getString("Region_Story"));
                           landing_model.setLeft_Alias(jsonarr.getString("Left_Alias"));
                           landing_model.setPlaces_To_Visit(jsonarr.getString("Places_To_Visit"));
                           landing_model.setPage_Title(jsonarr.getString("Page_Title"));
                           landing_model.setPage_Description(jsonarr.getString("Page_Description"));
                           landing_model.setPage_Heading(jsonarr.getString("Page_Heading"));
                           landing_model.setAdvance(jsonarr.getInt("Advance"));
                           landing_model.setIntermediate_Payment(jsonarr.getInt("Intermediate_Payment"));
                           landing_model.setDate(jsonarr.getString("Date"));
                           landing_model.setAdmin_Id(jsonarr.getString("admin_Id"));
                           landing_model.setPopular(jsonarr.getInt("Popular"));
                           region_.add(jsonarr.getString("Region_Name"));
                           landingList.add(landing_model);
                      }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                pDialog.hide();
                region_adapter.notifyDataSetChanged();
                adapter.notifyDataSetChanged();
                //searchText.startAnimation(animFadein);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Volley Error", "Error: " + error.getMessage());
                pDialog.hide();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq);
        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
