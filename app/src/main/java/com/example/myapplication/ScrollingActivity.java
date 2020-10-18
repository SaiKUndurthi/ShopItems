package com.example.myapplication;

import android.app.Activity;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.myapplication.shopping_service.ShoppingService;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ScrollingActivity extends AppCompatActivity
{
    private int input = 4500;
    private ShopItemsApdapter itemsAdapter;
    private TextView mMessageTextView;
    private ListView itemListView;
    private final ArrayList<ShopData> data = new ArrayList<>();
    private ArrayList<String> transactionHistory = new ArrayList<>();

    public int getInput() {
        return input;
    }

    public  void setInput(int input) {
        this.input = input;
        TextView dialogCash = this.findViewById(R.id.CashText);
        dialogCash.setText( "$" + Integer.toString(input));
    }



    class CustomDialogClass extends Dialog {
        public int m_id;
        public ListView m_view;
        public CustomDialogClass(@NonNull Context context) {
            super(context);
        }
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.shop_dialog_professor);

            mMessageTextView = (TextView) findViewById(R.id.message);

            Button yes = this.findViewById(R.id.buybutton);
            final int dialogCash = getInput();

            yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    setInput(getInput() - data.get(m_id).getCost());
                    transactionHistory.add(data.get(m_id).getName() +" purchased at $" +((Integer) data.get(m_id).getCost()).toString());
                    data.get(m_id).setCount(data.get(m_id).getCount()-1);
                    if(data.get(m_id).getCount()==0) {
                        data.remove(m_id);
                    }
                            itemsAdapter.notifyDataSetChanged();
                    dismiss();
                }
            });
            Button no = this.findViewById(R.id.nobuybutton);
            no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });

        }


        public void SetDetails(int id)
        {
            m_id = id;
            ImageView dialogImage =this.findViewById(R.id.purchaseImage);
            dialogImage.setImageResource(data.get(id).imageResource);

            TextView dialogName = this.findViewById(R.id.titleView);
            dialogName.setText(data.get(id).name);

            TextView dialogDesc = this.findViewById(R.id.descText);
            dialogDesc.setText(data.get(id).description);

            TextView dialogcost = this.findViewById(R.id.priceText);
            dialogcost.setText( "$" + data.get(id).cost);
        }
    }

    public static class ShopData
    {
        public String name;
        public int imageResource;
        public String description;
        public int cost;
        public boolean purchased;
        public String date;
        public int count;
        public int id;


        public String getName() {
            return name;
        }

        public int getCount() {
            return count;
        }
        public void setCount(int count){
            this.count = count;
        }

        public int getId(){
            return id;
        }

        public void setName(String name) {
            this.name = name;
        }
        public String getDate(){ return date;}

        public String getDescription() {
            return description;
        }

        public int getCost() {return cost;}

        public int getImageResource() {
            return imageResource;
        }

        public void setImageResource(int imageResource) {
            this.imageResource = imageResource;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public ShopData(int layout,String nm, int res, String desc, int cst, String date, int count)
        {
            this.id = layout;
            name  = nm;
            imageResource = res;
            description = desc;
            cost = cst;
            purchased = false;
            this.date = date;
            this.count = count;
        }

        public static Comparator<ShopData> ShpNameComparator = new Comparator<ShopData>() {

            @Override
            public int compare(ShopData s1, ShopData s2) {
                String ShpName1 = s1.getName().toUpperCase();
                String ShpName2 = s2.getName().toUpperCase();

                //ascending order
                return ShpName1.compareTo(ShpName2);

                //descending order
                //return ShpName2.compareTo(ShpName1);
            }};

        public static Comparator<ShopData> ShpPriceComparator = new Comparator<ShopData>() {

            @Override
            public int compare(ShopData s1, ShopData s2) {
                int ShpName1 = s1.getCost();
                int ShpName2 = s2.getCost();

                //ascending order
                return ShpName1-ShpName2;

                //descending order
                //return ShpName2-ShpName1;
            }};

        public static Comparator<ShopData> ShpDateComparator = new Comparator<ShopData>() {
            @Override
            public int compare(ShopData o1, ShopData o2) {
                Date date = null, date1 = null;
                try {
                    date=new SimpleDateFormat("MM/dd/yyyy").parse(o1.getDate());
                    date1=new SimpleDateFormat("MM/dd/yyyy").parse(o2.getDate());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return date.compareTo(date1);
            }
        };




    }

    class ShopItemsApdapter extends ArrayAdapter<ShopData> {

        private ArrayList<ScrollingActivity.ShopData> shopDataArrayList;
        private ArrayList<ScrollingActivity.ShopData> filteredList;
        private ItemFilter itemFilter;
        Context context;

        public ShopItemsApdapter(Activity context, ArrayList<ShopData> data) {
            super(context, 0, data);
            this.shopDataArrayList = data;
            this.filteredList = data;
            this.context = context;
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            final ShopData currentShopdata = getItem(position);
            View itemView = convertView;

            if (itemView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                //itemView = inflater.inflate(R.layout.shop_item, parent, false);
                itemView = inflater.inflate(R.layout.shop_item, parent, false);

            }

            Button itemCostButton = (Button) itemView.findViewById(R.id.costButton);
            itemCostButton.setText("$"+currentShopdata.getCost());

            Button itemNameButton = (Button) itemView.findViewById(R.id.nameButton);
            itemNameButton.setText(currentShopdata.getName());

            ImageButton itemImageButton = (ImageButton) itemView.findViewById(R.id.imageButton);
            itemImageButton.setImageResource(currentShopdata.getImageResource());

            TextView itemDate = (TextView) itemView.findViewById(R.id.message);
            itemDate.setText(currentShopdata.getDate());

            TextView itemCount = (TextView) itemView.findViewById(R.id.item_count);
            itemCount.setText(((Integer)(currentShopdata.getCount())).toString());

            itemCostButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //ShopClicked(currentShopdata.getId());
                    ShopClicked(position);
                }
            });

            return itemView;
        }

        /**
         * Get size of user list
         * @return userList size
         */
        @Override
        public int getCount() {
            return filteredList.size();
        }

        /**
         * Get specific item from user list
         * @param i item index
         * @return list item
         */
        @Override
        public ScrollingActivity.ShopData getItem(int i) {
            return filteredList.get(i);
        }


        /**
         * Get custom filter
         * @return filter
         */
        @Override
        public Filter getFilter() {
            if (itemFilter == null) {
                itemFilter = new ItemFilter();
            }
            return itemFilter;
        }


        /**
         * Custom filter for friend list
         * Filter content in friend list according to the search text
         */
        private class ItemFilter extends Filter {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint!=null && constraint.length()>0) {
                    ArrayList<ScrollingActivity.ShopData> tempList = new ArrayList<>();

                    // search content in friend list
                    for (ScrollingActivity.ShopData data : shopDataArrayList) {
                        if (data.getName().toLowerCase().contains(constraint.toString().toLowerCase())
                                || data.getDescription().toLowerCase().contains(constraint.toString().toLowerCase())) {

                            Log.d("!@#$", "123");
                            tempList.add(data);
                        }
                        Log.d("!@#$", "456");
                    }

                    filterResults.count = tempList.size();
                    filterResults.values = tempList;
                } else {
                    filterResults.count = shopDataArrayList.size();
                    filterResults.values = shopDataArrayList;
                }

                Log.d("!@#$", filterResults.toString());

                return filterResults;
            }

            /**
             * Notify about filtered list to ui
             * @param constraint text
             * @param results filtered result
             */
            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredList = (ArrayList<ScrollingActivity.ShopData>) results.values;
                notifyDataSetChanged();
            }
        }



    }


    public static CustomDialogClass customDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);

        data.add(new ShopData(0,"Bone", R.drawable.bone, "Good chew toy.", 1, "09/30/2019",3));
        data.add(new ShopData(1,"Carrot", R.drawable.carrot, "Good chew.", 1,"08/30/2019",3));
        data.add(new ShopData(2,"Dog", R.drawable.dog, "Chews toy.", 2,"08/20/2019",3));
        data.add(new ShopData(3,"Flame", R.drawable.flame, "It burns.", 1,"07/30/2029",3));
        data.add(new ShopData(4,"Grapes", R.drawable.grapes, "Your eat them.", 1,"09/30/2020",3));
        data.add(new ShopData(5,"House", R.drawable.house, "As opposed to home.", 100,"09/15/2019",3));
        data.add(new ShopData(6,"Lamp", R.drawable.lamp, "It lights.", 2,"09/30/2021",3));
        data.add(new ShopData(7,"Mouse", R.drawable.mouse, "Not a rat.", 1,"02/30/2019",3));
        data.add(new ShopData(8,"Nail", R.drawable.nail, "Hammer required.", 1,"12/30/2019",3));
        data.add(new ShopData(9,"Penguin", R.drawable.penguin, "Find Batman.", 10,"09/09/2019",3));
        data.add(new ShopData(10,"Rocks", R.drawable.rocks, "Rolls.", 1,"09/30/2019",3));
        data.add(new ShopData(11,"Star", R.drawable.star, "Like the sun but farther away.", 25,"01/30/2019",3));
        data.add(new ShopData(12,"Toad", R.drawable.toad, "Like a frog.", 1,"02/30/2019",3));
        data.add(new ShopData(13,"Van", R.drawable.van, "Has four wheels.", 10,"03/30/2019",3));
        data.add(new ShopData(14,"Wheat", R.drawable.wheat, "Some breads have it.", 1,"04/30/2019",3));
        data.add(new ShopData(15,"Yak", R.drawable.yak, "Yakity Yak Yak.", 15,"05/30/2019",3));

        itemsAdapter = new ShopItemsApdapter(this, data);
        itemListView = (ListView) findViewById(R.id.lv_items);
        itemListView.setAdapter(itemsAdapter);

        customDialog = new CustomDialogClass(this);
        itemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("IDDD", ((Long) id).toString());
                Toast.makeText(ScrollingActivity.this, "Check check", Toast.LENGTH_SHORT).show();
                ShopClicked((int) id);
            }
        });


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        toolBarLayout.setTitle(getTitle());
        try {
            //Creating a retrofit object
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(ShoppingService.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create()) //Here we are using the GsonConverterFactory to directly convert json data to object
                    .build();

            //creating the api interface
            ShoppingService api = retrofit.create(ShoppingService.class);

            Call<JsonObject> call = api.getItems();
            Log.i("autolog", "Call<List<ShopData>> call = call.getUserData();");
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.code() == 200) {
                        Log.d("API Result:", "done");
                        //JSONObject reader = new JSONObject(response);
                        JsonObject reader = response.body();
                        JsonArray lst = reader.getAsJsonArray("list");
                        ThreadLocalRandom random = ThreadLocalRandom.current();
                        int rand;
                        for (int i = 0; i < lst.size(); i++) {
                            JsonObject obj = lst.get(i).getAsJsonObject();
                            String name = obj.get("name").toString().replaceAll("\"", "");
                            String image = obj.get("image").toString().replaceAll("\"", "");
                            String desc = obj.get("description").toString().replaceAll("\"", "");
                            int cost = Integer.valueOf(String.valueOf(obj.get("cost")));
                            String date = obj.get("date").toString().replaceAll("\"", "");
                            rand = random.nextInt(16, 100);

                            int ir = 0;
                            try {
                                Field idField = R.drawable.class.getDeclaredField(image);
                                ir = idField.getInt(idField);
                            } catch (Exception e) {
                            }
                            data.add(new ShopData(rand, name, ir, desc, cost, date, 3));

                        }
                        itemsAdapter.notifyDataSetChanged();
                        }else {
                        Log.d("API Result:", "Error:" + response.message());
                        Toast.makeText(ScrollingActivity.this, "Error(Flask Server)!!!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.d("API Result:", "Error:" + t.getMessage());
                }
            });
        }catch (Exception e) {Log.i("autolog", "Exception");}

        handleIntent(getIntent());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


    }

    private void handleIntent(Intent queryIntent){
        if (Intent.ACTION_SEARCH.equals(queryIntent.getAction())) {
            String query = queryIntent.getStringExtra(SearchManager.QUERY);
            Log.d("!@#$", "NEED TO IMPLEMENT"+query);
            doSearch(query);
        }
    }

    public static void ShopClicked(int id)
    {
        customDialog.show();
        customDialog.SetDetails(id);
       //InitShopItems();
    }
    public class Cash {
        public int amount;
        Cash cash = new Cash(20);
        public Cash(int value) {
            amount = value;
        }
        public int addCash(int value) {
            return amount + value;
        };
        public boolean buybutton(int cost) {
            if (amount >= cost) {
                amount = amount / cost;
                return true;
            }
            else {
                return false;
            }
        }
    }


    private void doSearch(String query) {
       itemsAdapter.getFilter().filter(query);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.menu_scrolling, menu);


        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // hello
                Log.d("!@#$", query);
                Filter filter = itemsAdapter.getFilter();
                Log.d("!@#$Search", query + " filter: " + filter);
                filter.filter(query);
                itemsAdapter.notifyDataSetChanged();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d("!@#$Search", newText);
                Filter filter = itemsAdapter.getFilter();
                Log.d("!@#$Search", newText + " filter: " + filter);
                filter.filter(newText);
                itemsAdapter.notifyDataSetChanged();
                return true;
            }
        });


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        else if (id == R.id.action_logout) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        else if (id == R.id.action_wallet) {
            setInput(getInput() + 100);
        }else if (id == R.id.action_sort_by_name){
            sortByName();
            itemsAdapter.notifyDataSetChanged();
            Intent myIntent = new Intent(this, ScrollingActivity.class);
            startActivity(myIntent);
            return true;
        }else if (id == R.id.action_sort_by_price){
            sortByPrice();
            itemsAdapter.notifyDataSetChanged();
            Intent myIntent = new Intent(this, ScrollingActivity.class);
            startActivity(myIntent);
            return true;
        }else if ( id == R.id.action_sort_by_date){
            sortByDate();
            itemsAdapter.notifyDataSetChanged();
            Intent myIntent = new Intent(this, ScrollingActivity.class);
            startActivity(myIntent);
            return true;
        }else if( id == R.id.action_transaction_settings){
            Intent myIntent = new Intent(this, TransactionHistory.class);
            myIntent.putStringArrayListExtra("transactionHistory",transactionHistory);
            startActivity(myIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }


    private void sortByName(){
        Collections.sort(data,ScrollingActivity.ShopData.ShpNameComparator);
    }

    private void sortByPrice(){
        Collections.sort(data,ScrollingActivity.ShopData.ShpPriceComparator);
    }

    private void sortByDate(){
        Collections.sort(data, ShopData.ShpDateComparator);
    }
}
