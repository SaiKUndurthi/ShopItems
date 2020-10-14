//package com.example.myapplication;
//
//import android.content.Context;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.Filter;
//import android.widget.ImageButton;
//import android.widget.ImageView;
//import android.widget.Toast;
//
//import java.util.ArrayList;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//
//public class ShopItemsApdapter extends ArrayAdapter<ScrollingActivity.ShopData> {
//
//    private ArrayList<ScrollingActivity.ShopData> shopDataArrayList;
//    private ArrayList<ScrollingActivity.ShopData> filteredList;
//    private ItemFilter itemFilter;
//    Context context;
//
//    public ShopItemsApdapter(ArrayList<ScrollingActivity.ShopData> data, Context context) {
//        super(context, 0, data);
//        this.shopDataArrayList = data;
//        this.filteredList = data;
//        this.context = context;
//    }
//
//    @NonNull
//    @Override
//    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        View itemView = convertView;
//
//        if (itemView == null) {
//            LayoutInflater inflater = LayoutInflater.from(getContext());
//            itemView = inflater.inflate(R.layout.shop_item, parent, false);
//        }
//        ScrollingActivity.ShopData data = getItem(position);
//
//        Button itemCostButton = (Button) itemView.findViewById(R.id.costButton);
//        itemCostButton.setText("$"+data.cost);
//
//        Button itemNameButton = (Button) itemView.findViewById(R.id.nameButton);
//        itemNameButton.setText(data.name);
//
//        ImageButton itemImageButton = (ImageButton) itemView.findViewById(R.id.imageButton);
//        itemImageButton.setImageResource(data.imageResource);
//
//        itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(context, "Check check", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        return itemView;
//    }
//
//    /**
//     * Get size of user list
//     * @return userList size
//     */
//    @Override
//    public int getCount() {
//        return filteredList.size();
//    }
//
//    /**
//     * Get specific item from user list
//     * @param i item index
//     * @return list item
//     */
//    @Override
//    public ScrollingActivity.ShopData getItem(int i) {
//        return filteredList.get(i);
//    }
//
//
//    /**
//     * Get custom filter
//     * @return filter
//     */
//    @Override
//    public Filter getFilter() {
//        if (itemFilter == null) {
//            itemFilter = new ItemFilter();
//        }
//        return itemFilter;
//    }
//
//
//    /**
//     * Custom filter for friend list
//     * Filter content in friend list according to the search text
//     */
//    public class ItemFilter extends Filter {
//
//        @Override
//        protected FilterResults performFiltering(CharSequence constraint) {
//            FilterResults filterResults = new FilterResults();
//            if (constraint!=null && constraint.length()>0) {
//                ArrayList<ScrollingActivity.ShopData> tempList = new ArrayList<>();
//
//                // search content in friend list
//                for (ScrollingActivity.ShopData data : shopDataArrayList) {
//                    if (data.getName().toLowerCase().contains(constraint.toString().toLowerCase())
//                            || data.getDescription().toLowerCase().contains(constraint.toString().toLowerCase())) {
//
//                        Log.d("!@#$", "123");
//                        tempList.add(data);
//                    }
//                    Log.d("!@#$", "456");
//                }
//
//                filterResults.count = tempList.size();
//                filterResults.values = tempList;
//            } else {
//                filterResults.count = shopDataArrayList.size();
//                filterResults.values = shopDataArrayList;
//            }
//
//            Log.d("!@#$", filterResults.toString());
//
//            return filterResults;
//        }
//
//        /**
//         * Notify about filtered list to ui
//         * @param constraint text
//         * @param results filtered result
//         */
//        @SuppressWarnings("unchecked")
//        @Override
//        protected void publishResults(CharSequence constraint, FilterResults results) {
//            filteredList = (ArrayList<ScrollingActivity.ShopData>) results.values;
//            notifyDataSetChanged();
//        }
//    }
//
//
//
//}
